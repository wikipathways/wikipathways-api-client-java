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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bridgedb.DataSource;
import org.bridgedb.Xref;
import org.bridgedb.bio.Organism;
import org.pathvisio.core.model.ConverterException;
import org.pathvisio.core.model.GpmlFormat;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.core.view.MIMShapes;
import org.pathvisio.wikipathways.webservice.WSAuth;
import org.pathvisio.wikipathways.webservice.WSCurationTag;
import org.pathvisio.wikipathways.webservice.WSCurationTagHistory;
import org.pathvisio.wikipathways.webservice.WSOntologyTerm;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.pathvisio.wikipathways.webservice.WSPathwayHistory;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;
import org.pathvisio.wikipathways.webservice.WSSearchResult;
import org.pathvisio.wikipathways.webservice.WikiPathwaysPortType;
import org.pathvisio.wikipathways.webservice.WikiPathwaysRESTBindingStub;

/**
 * A client API that provides access to the WikiPathways REST webservice.
 * For more documentation on the webservice, see:
 * http://wikipathways.org/index.php/Help:WikiPathways_Webservice/API
 * @author thomas
 * @author msk
 *
 */
public class WikiPathwaysClient {
	private WikiPathwaysPortType port;

	private WSAuth auth;
	
	/**
	 * Create an instance of this class.
	 * @param portAddress The url that points to the WikiPathways webservice.
	 * @throws ServiceException
	 */
	public WikiPathwaysClient(URL portAddress) {
		MIMShapes.registerShapes();
		
		HttpClient httpclient = HttpClients.createDefault();
		port = new WikiPathwaysRESTBindingStub(httpclient, portAddress.toString());
	}

	/**
	 * Get a info about the pathway (without getting the actual
	 * GPML code).
	 */
	public WSPathwayInfo getPathwayInfo(String id) throws RemoteException {
		return port.getPathwayInfo(id);
	}
	
	/**
	 * Get a list of pathways tagged with any ontology term that is the 
	 * child of the given Ontology term.
	 */
	public WSPathwayInfo [] getPathwayByParentOntologyTerm(String term) throws RemoteException {
		return port.getPathwaysByParentOntologyTerm(term);
	}
	
	/**
	 * Get a list of pathways tagged with a given ontology term.
	 */
	public WSPathwayInfo [] getPathwayByOntologyTerm(String term) throws RemoteException {
		return port.getPathwaysByOntologyTerm(term);
	}
	
	/**
	 * get a list of all ontology terms for one pathway
	 */
	public WSOntologyTerm [] getOntologyTermsByPathway(String pwId) throws RemoteException {
		return port.getOntologyTermsByPathway(pwId);
	}

	/**
	 * Get a pathway from WikiPathways.
	 * @see #toPathway(WSPathway)
	 */
	public WSPathway getPathway(String id) throws RemoteException, ConverterException {
		return getPathway(id, 0);
	}

	/**
	 * List all pathways on WikiPathways
	 */
	public WSPathwayInfo[] listPathways() throws RemoteException {
		WSPathwayInfo[] r = port.listPathways(null);
		if(r == null) r = new WSPathwayInfo[0];
		return r;
	}

	/**
	 * List all pathways on WikiPathways for the given organism
	 * @param organism The organism to filter by.
	 * @return
	 * @throws RemoteException
	 */
	public WSPathwayInfo[] listPathways(Organism organism) throws RemoteException {
		WSPathwayInfo[] r = port.listPathways(organism.latinName());
		if(r == null) r = new WSPathwayInfo[0];
		return r;
	}

	/**
	 * Lists all available organisms on WikiPathways
	 * @throws RemoteException
	 */
	public String[] listOrganisms() throws RemoteException {
		String[] r = port.listOrganisms();
		if(r == null) r = new String[0];
		return r;
	}

	/**
	 * Get a specific revision of a pathway from WikiPathways
	 * @see #toPathway(WSPathway)
	 */
	public WSPathway getPathway(String id, int revision) throws RemoteException, ConverterException {
		WSPathway wsp = port.getPathway(id, revision);
		return wsp;
	}

	/**
	 * returns the history of a specified pathway
	 */
	public WSPathwayHistory getPathwayHistory(String id, Date start) throws RemoteException {
		String timestamp = dateToTimestamp(start);
		WSPathwayHistory hist = port.getPathwayHistory(id, timestamp);
		return hist;
	}

	/**
	 * 
	 * @param fileType
	 * @param id
	 * @param revision
	 * @return
	 * @throws RemoteException
	 */
	public byte[] getPathwayAs(String fileType, String id, int revision) throws RemoteException {
		return port.getPathwayAs(fileType, id, revision);
	}

	/**
	 * downloads and saves a pathway in different file formats
	 */
	public void downloadPathwayAs(File output, String fileType, String id, int revision) throws IOException {
		byte[] data = getPathwayAs(fileType, id, revision);
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(output));
		out.write(data);
		out.close();
	}

	/**
	 * Get a list of external references on the pathway (gene, protein or metabolite ids),
	 * translated to the given database system.
	 * @param id The pathway id
	 * @param dataSource The data source to translate to (e.g. BioDataSource.ENTREZ_GENE)
	 * @return The identifiers of the external references.
	 * @throws RemoteException
	 */
	public String[] getXrefList(String id, DataSource dataSource) throws RemoteException {
		String[] xrefs = port.getXrefList(id, dataSource.getSystemCode());
		if(xrefs == null) xrefs = new String[0];
		return xrefs;
	}
	
	/**
	 * get a user name by orcid id
	 */
	public String getUserByOrcid(String orcid) throws RemoteException {
		return port.getUserByOrcid(orcid);
	}

	/**
	 * Utility method to create a pathway model from the webservice class
	 * WSPathway.
	 * @param wsp The WSPathway object returned by the webservice.
	 * @return The org.pathvisio.model.Pathway model representation of the GPML code.
	 * @throws ConverterException
	 */
	public static Pathway toPathway(WSPathway wsp) throws ConverterException {
		Pathway p = new Pathway();
		p.readFromXml(new StringReader(wsp.getGpml()), true);
		return p;
	}

	/**
	 * Update a pathway on WikiPathways.
	 * Note: you need to login first, see: {@link #login(String, String)}.
	 * @param id The pathway identifier
	 * @param pathway The updated pathway data
	 * @param description A description of the changes
	 * @param revision The revision these changes were based on (to prevent conflicts)
	 * @return returns new revision of pathway
	 */
	public String updatePathway(String id, Pathway pathway, String description, int revision) throws ConverterException, RemoteException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GpmlFormat.writeToXml(pathway, out, true);
		String gpml = out.toString();
//		gpml = gpml.replace("\n", "");
//		gpml = gpml.replace(" ", "+");
//		description = description.replace(" ", "+");
		return port.updatePathway(id, description, gpml, revision, auth);
	}

	/**
	 * Creates a new pathway on WikiPathways.
	 * Note: you need to login first, see: {@link #login(String, String)}.
	 * @param pathway The pathway to create on WikiPathways
	 * @return The WSPathwayInfo object, containing the identifier and revision of the created pathway.
	 * @throws RemoteException
	 * @throws ConverterException
	 */
	public WSPathwayInfo createPathway(Pathway pathway) throws RemoteException, ConverterException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GpmlFormat.writeToXml(pathway, out, true);
		String gpml = out.toString();
		return port.createPathway(gpml, auth);
	}

	/**
	 * Apply a curation tag to a pathway. Will overwrite existing tags with the same name.
	 * @param id The pathway identifier
	 * @param tagName The name of the tag (e.g. CurationTag:Approved)
	 * @param tagText The tag text
	 * @param revision The revision to apply the tag to
	 * @throws RemoteException
	 */
	public boolean saveCurationTag(String id, String tagName, String tagText, int revision) throws RemoteException {
		return port.saveCurationTag(id, tagName, tagText, revision, auth);
	}

	/**
	 * Apply a curation tag to a pathway. Will overwrite existing tags with the same name.
	 * @param id The pathway identifier
	 * @param tagName The name of the tag (e.g. CurationTag:Approved)
	 * @param tagText The tag text
	 * @throws RemoteException
	 */
	public boolean saveCurationTag(String id, String tagName, String text) throws RemoteException {
		return saveCurationTag(id, tagName, text, 0);
	}

	/**
	 * Remove the given curation tag from the pathway
	 * @param id The pathway identifier
	 * @param tagName The name of the tag (e.g. CurationTag:Approved)
	 * @throws RemoteException
	 */
	public boolean removeCurationTag(String id, String tagName) throws RemoteException {
		return port.removeCurationTag(id, tagName, auth);
	}
	
	/** 
	 * add ontology tag to pathway
	 */
	public boolean saveOntologyTag(String id, String term, String termId) throws RemoteException {
		return port.saveOntologyTag(id, term, termId, auth);
	}
	
	/**
	 * remove ontology tag from pathway
	 */
	public boolean removeOntologyTag(String id, String termId) throws RemoteException {
		return port.removeOntologyTag(id, termId, auth);
	}

	/**
	 * Get all curation tags for the given pathway
	 * @param id The pathway identifier
	 * @return An array with the curation tags.
	 * @throws RemoteException
	 */
	public WSCurationTag[] getCurationTags(String id) throws RemoteException {
		WSCurationTag[] tags = port.getCurationTags(id);
		if(tags == null) tags = new WSCurationTag[0];
		return tags;
	}

	/**
	 * get all pathways for a specific curation tag
	 */
	public WSCurationTag[] getCurationTagsByName(String tagName) throws RemoteException {
		WSCurationTag[] tags = port.getCurationTagsByName(tagName);
		if(tags == null) tags = new WSCurationTag[0];
		return tags;
	}

	/**
	 * Get the curation tag history for the given pathway
	 * @param id The pathway identifier
	 * @param cutoff Only get history items that occured after the given cutoff date
	 * @return An array with the history items
	 * @throws RemoteException
	 */
	public WSCurationTagHistory[] getCurationTagHistory(String id, Date cutoff) throws RemoteException {
		String timestamp = "0";
		if(cutoff != null) {
			// turn Date into expected timestamp format, in GMT:
			SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			timestamp = sdf.format(cutoff);
		}
		WSCurationTagHistory[] hist = port.getCurationTagHistory(id, timestamp);
		if(hist == null) hist = new WSCurationTagHistory[0];
		return hist;
	}

	/**
	 * Get the curation tag history for the given pathway
	 * @param id The pathway identifier
	 * @return An array with the history items
	 * @throws RemoteException
	 */
	public WSCurationTagHistory[] getCurationTagHistory(String id) throws RemoteException {
		return getCurationTagHistory(id, null);
	}
	/**
	 * Login using your WikiPathways account. You need to login in order
	 * to make changes to pathways.
	 * @param name The username of the WikiPathways account
	 * @param pass The password of the WikiPathways account
	 * @throws RemoteException
	 */
	public void login(String name, String pass) throws RemoteException {
		auth = new WSAuth(name, port.login(name, pass));
	}

	/**
	 * Get a list of recently changed pathways.
	 * @param cutoff Only return changes since this date.
	 * @throws RemoteException
	 */
	public WSPathwayInfo[] getRecentChanges(Date cutoff) throws RemoteException {
		String timestamp = dateToTimestamp(cutoff);
		WSPathwayInfo[] changes = port.getRecentChanges(timestamp);
		if(changes == null) changes = new WSPathwayInfo[0];
		return changes;
	}

	private static String dateToTimestamp(Date date) {
		// turn Date into expected timestamp format, in GMT:
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(date);
	}

	public WSSearchResult[] findPathwaysByText(String query) throws RemoteException {
		WSSearchResult[] r = port.findPathwaysByText(query, null);
		if(r == null) r = new WSSearchResult[0];
		return r;
	}

	public WSSearchResult[] findPathwaysByText(String query, Organism organism) throws RemoteException {
		String species = null;
		if(organism != null) {
			species = organism.latinName();
		}
		WSSearchResult[] r =  port.findPathwaysByText(query, species);
		if(r == null) r = new WSSearchResult[0];
		return r;
	}

	/**
	 * Search for pathways containing one of the given xrefs by taking
	 * into account cross-references to other database systems.
	 * @param xrefs
	 * @return
	 * @throws RemoteException
	 */
	public WSSearchResult[] findPathwaysByXref(Xref... xrefs) throws RemoteException {
		String[] ids = new String[xrefs.length];
		String[] codes = new String[xrefs.length];
		for(int i = 0; i < xrefs.length; i++) {
			ids[i] = xrefs[i].getId();
			DataSource ds = xrefs[i].getDataSource();
			if(ds == null) {
				codes[i] = null;
			} else {
				codes[i] = ds.getSystemCode();
			}
		}
		WSSearchResult[] r =  port.findPathwaysByXref(ids, codes);
		if(r == null) r = new WSSearchResult[0];
		return r;
	}
	
	public WSSearchResult[] findInteractions(String query) throws RemoteException {
		WSSearchResult[] r = port.findInteractions(query);
		if(r == null) r = new WSSearchResult[0];
		return r;
	}

	public File getColoredPathway(String pwId, String revision, String [] graphId, String [] color, String fileType, File output) throws IOException {
		byte [] result = port.getColoredPathway(pwId, revision, graphId, color, fileType);
		FileOutputStream fos = new FileOutputStream(output);
		fos.write(result);
		fos.close();
		return output;
	}
	
	/**
	 * search by literature reference
	 */
	public WSSearchResult[] findPathwaysByLiterature(String query) throws RemoteException {
		WSSearchResult[] r = port.findPathwaysByLiterature(query);
		if(r == null) r = new WSSearchResult[0];
		return r;        
	}

}