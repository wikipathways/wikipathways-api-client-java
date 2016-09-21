package org.wikipathways.cache;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.pathvisio.core.model.ConverterException;
import org.pathvisio.core.model.Pathway;
import org.wikipathways.client.WikiPathwaysCache;

public class TestCache {

	@Test
	public void test() throws ConverterException, IOException {
		// setting up a new cache will take quite some time!!
		WikiPathwaysCache cache = new WikiPathwaysCache(new File("cache"));
		cache.update();
		
		File f = cache.getPathwayGpml("WP4");
		if(f != null) {
			Pathway p = new Pathway();
			p.readFromXml(f, false);
			assertTrue(p.getMappInfo().getMapInfoName().equals("Sandbox Pathway"));
		}
	}

}
