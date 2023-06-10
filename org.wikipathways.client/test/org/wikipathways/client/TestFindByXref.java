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

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.bridgedb.DataSource;
import org.bridgedb.Xref;
import org.bridgedb.bio.DataSourceTxt;
import org.junit.Before;
import org.junit.Test;
import org.pathvisio.wikipathways.webservice.WSSearchResult;
import org.wikipathways.client.test.utils.ConnectionSettings;

/**
 * JUnit Test for webservice function: findPathwaysByXref
 * @author mkutmon
 */
public class TestFindByXref {

	private WikiPathwaysClient client;
	
	@Before
	public void setUp() throws Exception {
		client = ConnectionSettings.createClient();
		if (!DataSource.systemCodeExists("S")) DataSourceTxt.init();
	}

	@Test
	public void testSingleXref() throws RemoteException {
		Xref x = new Xref("1234", DataSource.getExistingBySystemCode("L"));
		WSSearchResult [] result = client.findPathwaysByXref(x);
		assertTrue(result.length > 0);
		
		Xref x2 = new Xref("ABC", DataSource.getExistingBySystemCode("L"));
		WSSearchResult [] result2 = client.findPathwaysByXref(x2);
		assertTrue(result2.length == 0);
	}
	
	@Test
	public void testMultiXref() throws RemoteException {
		Xref x = new Xref("1234", DataSource.getExistingBySystemCode("L"));
		Xref x2 = new Xref("ENSG00000130164", DataSource.getExistingBySystemCode("En"));
		WSSearchResult [] result = client.findPathwaysByXref(x, x2);
		assertTrue(result.length > 0);
	}
}
