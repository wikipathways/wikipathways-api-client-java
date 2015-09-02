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

import org.junit.Before;
import org.junit.Test;
import org.pathvisio.wikipathways.webservice.WSIndexField;
import org.pathvisio.wikipathways.webservice.WSSearchResult;
import org.wikipathways.client.test.utils.ConnectionSettings;

/**
 * JUnit Test for webservice function: findInteractions
 * @author mkutmon
 */
public class TestFindInteractions {

	private WikiPathwaysClient client;
	
	@Before
	public void setUp() throws Exception {
		client = ConnectionSettings.createClient();
	}

	@Test
	public void test() throws RemoteException {
		// find interaction of DFFA and DFFB in pathway WP254
		String query = "DFFA";
		WSSearchResult [] res = client.findInteractions(query);
		String partner = "DFFB";
		
		System.out.println(res.length + " interactions found for " + query);
		
		boolean found = false;
		for(WSSearchResult r : res) {
			if(r.getId().equals("WP254")) {
				for(WSIndexField f : r.getFields()) {
					if(f.getName().equals("right")) {
						String [] values = f.getValues();
						for(String s : values) {
							if(s.equals(partner)) {
								found = true;
							}
						}
					}
				}
			}
		}
		
		assertTrue(found);
	}
	
	@Test
	public void test2() throws RemoteException {
		// find interaction of DFFA and DFFB in pathway WP254
		String query = "p53";
		WSSearchResult [] res = client.findInteractions(query);
		String partner = "p73";
		
		System.out.println(res.length + " interactions found for " + query);
		
		boolean found = false;
		for(WSSearchResult r : res) {
			if(r.getId().equals("WP655")) {
				for(WSIndexField f : r.getFields()) {
					if(f.getName().equals("left")) {
						String [] values = f.getValues();
						for(String s : values) {
							if(s.equals(partner)) {
								found = true;
							}
						}
					}
				}
			}
		}
		
		assertTrue(found);
	}
}
