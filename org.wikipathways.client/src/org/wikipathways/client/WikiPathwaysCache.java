// WikiPathways Java library,
// Copyright 2014-2015 WikiPathways
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package org.wikipathways.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.pathvisio.core.debug.Logger;
import org.pathvisio.core.model.ConverterException;
import org.pathvisio.core.util.FileUtils;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;

/**
 * This class can be used to maintain a local cache of all WikiPathways pathways,
 * that can be automatically updated through the WikiPathways client.
 */
public class WikiPathwaysCache
{
	static final String PW_EXT = "gpml";
	static final String PW_EXT_DOT = "." + PW_EXT;
	static final String INFO_EXT = "info";

	private File cacheDirectory;
	private Map<String, File> id2File;
	private List<File> files;
	private URL url;
	
	public WikiPathwaysCache(File cacheDirectory) throws MalformedURLException {
		this(new URL("http://webservice.wikipathways.org"), cacheDirectory);
	}

	public WikiPathwaysCache(URL url, File cacheDirectory) {
		this.url = url;

		if (!(cacheDirectory.exists() && cacheDirectory.isDirectory())) {
			throw new IllegalArgumentException ("Illegal cache directory " + cacheDirectory);
		}
		this.cacheDirectory = cacheDirectory;
		files = FileUtils.getFiles(cacheDirectory, PW_EXT, true);
		updateId2FileMap();
	}

	public List<File> getFiles() {
		return files;
	}

	/**
	 * Check for missing / outdated pathways
	 * and download them.
	 * Does nothing if there was no way to download.
	 * @param keeper: an optional ProgressKeeper, may be null
	 * @return A list of files that were updated (either modified, added or deleted)
	 * 	the returned list can be partial if the task was cancelled
	 * @throws IOException
	 * @throws ConverterException
	 */
	public Collection<File> update() throws ConverterException, IOException {
		Set<File> changedFiles = new HashSet<File>();

		long localdate = dateLastModified(files);
		Date d = new Date(localdate);
		DateFormat df = DateFormat.getDateTimeInstance();
		Logger.log.info("Date last modified: " + df.format(d));

		Logger.log.info("---[Updating new and removed pathways]---");
		WikiPathwaysClient wpClient = new WikiPathwaysClient(url);
		List<WSPathwayInfo> pathways = Arrays.asList(wpClient.listPathways());
			
		// remove deleted pathways
		changedFiles.addAll(purgeRemoved(pathways));
		// download new pathways
		changedFiles.addAll(downloadNew(pathways));
		
		Logger.log.info("---[Get Recently Changed pathways]---");
		
		// download recently changed pathways
		changedFiles.addAll(processRecentChanges(pathways));
		
		Logger.log.info("---[Ready]---");
		Logger.log.info("Updated pathways: " + changedFiles);
		
		files = FileUtils.getFiles(cacheDirectory, "gpml", true);
		updateId2FileMap();
		return changedFiles;
	}
	
	private void updateId2FileMap() {
		id2File = new HashMap<String, File>();
		for(File f : files) {
			try {
				WSPathwayInfo i = getPathwayInfo(f);
				id2File.put(i.getId(), f);
			} catch (IOException e) {
				Logger.log.warn("No info file available for " + f.getName());
			}
		}
	}

	/**
	 * checks if a pathway is already in the cache or not
	 * then downloads all new pathways
	 */
	private List<File> downloadNew(Collection<WSPathwayInfo> pathways) throws ConverterException, IOException {
		Set<WSPathwayInfo> newPathways = new HashSet<WSPathwayInfo>();

		for(WSPathwayInfo p : pathways) {
			File f = pathwayToFile(p);
			if(!f.exists()) {
				newPathways.add(p);
			}

		}
		return downloadFiles(newPathways);
	}

	/**
	 * In this method it is possible to download only the pathways that are recently changed.
	 * @return a list of files in the cache that has been updated (partial if interrupted)
	 * @throws ConverterException
	 * @throws IOException
	 */
	private List<File> processRecentChanges(List<WSPathwayInfo> pathways) throws ConverterException, IOException {
		Set<WSPathwayInfo> changeAndExist = new HashSet<WSPathwayInfo>();
		for(WSPathwayInfo i : pathways) {
			File pFile = pathwayToFile(i);
			if(pFile.exists()) {
				WSPathwayInfo old = getPathwayInfo(pFile);
				if(Integer.parseInt(i.getRevision()) > Integer.parseInt(old.getRevision())) {
					changeAndExist.add(i);
				}
			}
		}
		return downloadFiles(changeAndExist);
	}

	/**
	 * Download the latest version of all given pathways to the cache directory
	 * @return The list of downloaded files (partial list if interrupted)
	 * @throws ConverterException
	 * @throws IOException
	 */
	private List<File> downloadFiles (Collection<WSPathwayInfo> pathways) throws ConverterException, IOException {
		List<File> files = new ArrayList<File>();

		WikiPathwaysClient wpClient = new WikiPathwaysClient(url);
		int i = 1;
		for(WSPathwayInfo pwi : pathways) {
			
			// to make sure that there are not too many threads in one session
			if((i % 30) == 0) {
				wpClient = new WikiPathwaysClient(url);
			}
			
			Logger.log.info("\tDownloading " + pwi.getName());
			
			File file = pathwayToFile(pwi);
			WSPathway wsp = wpClient.getPathway(pwi.getId());
						// write out GPML without any tests 
			// needed for new pathways with SBGN symbols which should 
			// just be ignored for now
			FileWriter writer = new FileWriter(file);
			writer.write(wsp.getGpml());
			writer.close();
			
			// also write a file that stores some pathway info
			writeInfoFile(pwi);
			files.add(file);
			
			Logger.log.info("Downloaded file "+(i++)+" of "+pathways.size()+ ": " +
					pwi.getName() + " (" + pwi.getSpecies() + ")");
		}
		return files;
	}

	private File pathwayToFile(WSPathwayInfo pathway) {
		String species = pathway.getSpecies();

		// construct the download path
		String pathToDownload = cacheDirectory + File.separator + species + File.separator;

		//	make a folder for a species when it doesn't exist
		new File(pathToDownload).mkdirs();

		// download the pathway and give status in console
		File pwFile = new File (pathToDownload + pathway.getId() + PW_EXT_DOT);

		return pwFile;
	}

	private void writeInfoFile(WSPathwayInfo pathway) throws IOException {
		Properties prop = new Properties();
		prop.setProperty("Name", pathway.getName());
		prop.setProperty("Species", pathway.getSpecies());
		prop.setProperty("Url", pathway.getUrl());
		prop.setProperty("Revision", pathway.getRevision());
		prop.setProperty("Id", pathway.getId());
		FileOutputStream stream = new FileOutputStream(getInfoFile(pathwayToFile(pathway)));
		prop.store(stream, "");
		stream.close();
	}

	private File getInfoFile(File pathwayFile) {
		return new File(pathwayFile.getAbsolutePath() + "." + INFO_EXT);
	}
	
	/**
	 * Get the source url (that points to the pathway
	 * on WikiPathways) for the given cache file.
	 */
	public String cacheFileToUrl(File f) throws FileNotFoundException, IOException {
		return getPathwayInfo(f).getUrl();
	}

	public WSPathwayInfo getPathwayInfo(File cacheFile) throws FileNotFoundException, IOException {
		File info = getInfoFile(cacheFile);
		Properties prop = new Properties();
		FileInputStream in = new FileInputStream(info);
		prop.load(in);
		in.close();
		WSPathwayInfo pi = new WSPathwayInfo(
				prop.getProperty("Id"),
				prop.getProperty("Url"),
				prop.getProperty("Name"),
				prop.getProperty("Species"),
				prop.getProperty("Revision")
		);
		return pi;
	}
	
	public File getPathwayGpml(String wpId) throws NullPointerException {
		if(id2File.containsKey(wpId)) {
			return id2File.get(wpId);
		} else {
			throw new NullPointerException("invalid identifier - pathway not found in cache");
		}
	}

	/**
	 * deletes pathway files if pathway has been deleted
	 * on WikiPathways
	 * @param pathways
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private List<File> purgeRemoved(Collection<WSPathwayInfo> pathways) throws FileNotFoundException, IOException {
		Set<File> remoteFiles = new HashSet<File>();
		for(WSPathwayInfo p : pathways) remoteFiles.add(pathwayToFile(p));

		List<File> cacheFiles = FileUtils.getFiles(cacheDirectory, PW_EXT, true);
		List<File> deleted = new ArrayList<File>();

		for (File file : cacheFiles) {
			if(!remoteFiles.contains(file)) {
				deleted.add(file);
				file.delete();
				//Delete info file on exit, so classes that use the cache
				//can still use the info in this session.
				getInfoFile(file).deleteOnExit();
			}
		}

		return deleted;
	}

	/**
	 * In this method the date is returned when the last change is made in a pathway. The
	 * property that has to be given is:
	 * 'pathways' (a list of pathways you want to have the most recent date from).
	 */
	private static long dateLastModified(List<File> pathways) {
		// Set initial value.
		long lastModified = 0;
		// Walk through all the pathways.
		for (File pathway : pathways)
		{
			// If pathway is more recent, use this date.
			if (lastModified < pathway.lastModified())
			{
				lastModified = pathway.lastModified();
			}
		}
		return lastModified;
	}
}