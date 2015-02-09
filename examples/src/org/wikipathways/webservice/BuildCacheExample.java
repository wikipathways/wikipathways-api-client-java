package org.wikipathways.webservice;

import java.io.File;
import java.io.IOException;

import org.pathvisio.core.model.ConverterException;
import org.wikipathways.client.WikiPathwaysCache;

public class BuildCacheExample {

	public static void main(String[] args) throws ConverterException, IOException {
		WikiPathwaysCache cache = new WikiPathwaysCache(new File("/home/martina/Downloads/cache/"));
		cache.update();
	}

}
