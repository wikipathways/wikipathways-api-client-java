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

import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.bridgedb.bio.Organism;
import org.junit.Before;
import org.junit.Test;
import org.pathvisio.wikipathways.webservice.WSSearchResult;
import org.wikipathways.client.test.utils.ConnectionSettings;

/**
 * JUnit Test for webservice function: findPathwaysByText
 * @author mkutmon
 */
public class TestFindPathwayByText {

	private WikiPathwaysClient client;
	
	@Before
	public void setUp() throws Exception {
		client = ConnectionSettings.createClient();
	}

	@Test
	public void test() throws RemoteException {
		String query = "apoptosis";
		WSSearchResult [] results = client.findPathwaysByText(query);
		boolean found = false;
		for(WSSearchResult res : results) {
			// original human apoptosis pathway
			if(res.getId().equals("WP254")) {
				found = true;
			}
		}
		assertTrue(found);
	}

	@Test
	public void testOrg() throws RemoteException {
		String query = "apoptosis";
		WSSearchResult [] results = client.findPathwaysByText(query, Organism.HomoSapiens);
		boolean found = false;
		for(WSSearchResult res : results) {
			// original human apoptosis pathway
			if(res.getId().equals("WP254")) {
				found = true;
			}
		}
		assertTrue(found);
		
		// human pathway should not show up in mouse query
		WSSearchResult [] results2 = client.findPathwaysByText(query, Organism.MusMusculus);
		boolean found2 = false;
		for(WSSearchResult res : results2) {
			// original human apoptosis pathway
			if(res.getId().equals("WP254")) {
				found2 = true;
			}
		}
		assertTrue(!found2);
	}
}
