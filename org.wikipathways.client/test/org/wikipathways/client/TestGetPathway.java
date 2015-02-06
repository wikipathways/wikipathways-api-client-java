// WikiPathways Java library,
// Copyright 2014 WikiPathways
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;
import org.pathvisio.core.model.ConverterException;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.wikipathways.client.test.utils.ConnectionSettings;

/**
 * JUnit Test for webservice function: getPathway
 * @author mkutmon
 */
public class TestGetPathway {

	private WikiPathwaysClient client;
	
	@Before
	public void setUp() throws Exception {
		client = ConnectionSettings.createClient();
	}


	@Test
	public void testId() throws RemoteException, ConverterException {
		String id = "WP1";
		WSPathway p = client.getPathway(id);
		assertEquals("", "Mus musculus", p.getSpecies());
		assertEquals("", "Statin Pathway", p.getName());
	}
	
	@Test
	public void testRevision() throws RemoteException, ConverterException {
		String id = "WP1";
		int revision = 53530;
		WSPathway p = client.getPathway(id, revision);
		assertEquals("", "Mus musculus", p.getSpecies());
		assertEquals("", "Statin Pathway", p.getName());
		assertTrue(p.getGpml().contains("http://genmapp.org/GPML/2010a"));
	}

}
