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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pathvisio.wikipathways.webservice.WSCurationTag;
import org.wikipathways.client.test.utils.ConnectionSettings;
import org.wikipathways.client.test.utils.UserProperties;

public class TestSaveCurationTags {
	
	WikiPathwaysClient client;

	// define username and password in properties file:
	// resources/user.properties
	UserProperties props;
	
	
	@Before
	public void setUp() throws Exception {
		client = ConnectionSettings.createClient();
		props = new UserProperties();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		client.login(props.getProperty("username"), props.getProperty("password"));
		
		String pwId = "WP4";
		String tagName = "Curation:Tutorial";
		assertTrue(client.removeCurationTag(pwId, tagName));
		{
			WSCurationTag [] tags = client.getCurationTags("WP4");
			boolean found = false;
			for(WSCurationTag t : tags) {
				if(t.getName().equals(tagName)) {
					found = true;
				}
			}
			assertFalse(found);
		}
		
		String text = "This is the WP sandbox pathway!!";
		
		assertTrue(client.saveCurationTag(pwId, tagName, text));
		{
			WSCurationTag [] tags = client.getCurationTags("WP4");
			boolean found = false;
			for(WSCurationTag t : tags) {
				if(t.getName().equals(tagName)) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

}
