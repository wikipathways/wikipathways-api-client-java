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
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.pathvisio.wikipathways.webservice.WSPathwayHistory;
import org.wikipathways.client.test.utils.ConnectionSettings;

/**
 * JUnit Test for webservice function: getPathwayHistory
 * @author mkutmon
 */
public class TestGetPathwayHistory {

	private WikiPathwaysClient client;
	
	@Before
	public void setUp() throws Exception {
		client = ConnectionSettings.createClient();
	}

	@Test
	public void test() throws RemoteException {
		String id = "WP1";
		Calendar c = Calendar.getInstance();
		c.set(2011, 1, 1, 0, 0, 0);
		
		WSPathwayHistory hist = client.getPathwayHistory(id, c.getTime());
		assertTrue(hist.getHistory(0).getRevision().equals("41296"));
	}
}
