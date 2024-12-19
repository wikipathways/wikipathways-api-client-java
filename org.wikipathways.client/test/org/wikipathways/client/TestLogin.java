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

import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;
import org.wikipathways.client.test.utils.ConnectionSettings;
import org.wikipathways.client.test.utils.UserProperties;

/**
 * 
 * JUnit Test for webservice function: login
 * @author mkutmon
 *
 */
public class TestLogin {

	private WikiPathwaysClient client;
	
	// define username and password in properties file:
	// resources/user.properties
	UserProperties props;
	
	@Before
	public void setUp() throws Exception {
		client = ConnectionSettings.createClient();
		props = new UserProperties();
	}

	@Test
	public void test() {
		try {
			String username = props.getProperty("username");
			System.out.println(username);
			String password = props.getProperty("password");
			System.out.println(password);
			client.login(username, password);
		} catch (RemoteException e) {
			fail("Login failed: " + e.getMessage());
		}
	}

}
