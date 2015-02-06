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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;
import org.pathvisio.wikipathways.webservice.WSOntologyTerm;
import org.wikipathways.client.test.utils.ConnectionSettings;

/**
 * JUnit Test for webservice function: getOntologyTermsByPathway
 * @author mkutmon
 */
public class TestGetOntologyTermsByPathway {

	private WikiPathwaysClient client;
	
	@Before
	public void setUp() throws Exception {
		client = ConnectionSettings.createClient();
	}

	@Test
	public void test() throws RemoteException {
		WSOntologyTerm [] terms = client.getOntologyTermsByPathway("WP254");
		
		boolean apoptotic = false;
		boolean syndrome = false;
		for(WSOntologyTerm t : terms) {
			if(t.getName().equals("apoptotic cell death pathway")) {
				apoptotic = true;
			} else if (t.getName().equals("syndrome")) {
				syndrome = true;
			}
		}
	
		assertTrue(apoptotic);
		assertFalse(syndrome);
	}

}
