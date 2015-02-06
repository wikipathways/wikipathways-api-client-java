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

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import org.bridgedb.DataSource;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.wikipathways.client.test.utils.ConnectionSettings;

/**
 * JUnit Test for webservice function: xrefList
 * @author mkutmon
 */
public class TestXrefList {

	private WikiPathwaysClient client;
	
	@Before
	public void setUp() throws Exception {
		client = ConnectionSettings.createClient();
	}

	@Test
	public void test() throws RemoteException {
		String [] res = client.getXrefList("WP1", DataSource.getBySystemCode("L"));
		List<String> list = Arrays.asList(res);
		assertTrue(list.contains("15450"));
		assertTrue(list.contains("17777"));
		
		String [] res2 = client.getXrefList("WP1", DataSource.getBySystemCode("En"));
		List<String> list2 = Arrays.asList(res2);
		assertTrue(list2.contains("ENSMUSG00000032207"));
		assertTrue(list2.contains("ENSMUSG00000028158"));
	}
}
