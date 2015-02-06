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

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;
import org.pathvisio.wikipathways.webservice.WSCurationTag;
import org.wikipathways.client.test.utils.ConnectionSettings;

/**
 * JUnit Test for webservice function: getCurationTags
 * @author mkutmon
 */
public class TestGetCurationTags {

	private WikiPathwaysClient client;
	
	@Before
	public void setUp() throws Exception {
		client = ConnectionSettings.createClient();
	}	

	@Test
	public void test() throws RemoteException {
		WSCurationTag [] tags = client.getCurationTags("WP4");
		
		// has tutorial pathway tag?
		boolean tutorial = false;
		for(WSCurationTag tag : tags) {
			if(tag.getDisplayName().equals("Tutorial pathway")) {
				tutorial = true;
			}
		}
		
		assertTrue(tutorial);
		
		// ----------------------------------------------
		
		// human apoptosis pathway should not have a tutorial pathway
		// tag
		WSCurationTag [] tags2 = client.getCurationTags("WP254");
		
		// has tutorial pathway tag?
		boolean tutorial2 = false;
		for(WSCurationTag tag : tags2) {
			if(tag.getDisplayName().equals("Tutorial pathway")) {
				tutorial2 = true;
			}
		}
		
		assertTrue(!tutorial2);
	}
}
