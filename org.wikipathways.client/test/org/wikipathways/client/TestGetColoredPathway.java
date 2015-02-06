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

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wikipathways.client.test.utils.ConnectionSettings;

public class TestGetColoredPathway {

	private WikiPathwaysClient client;
	
	@Before
	public void setUp() throws Exception {
		client = ConnectionSettings.createClient();
	}

	@After
	public void tearDown() throws Exception {
	}

	private String pwId = "WP1";
	private String revision = "0";
	private String [] graphId = new String [] {"e04", "e6e"};
	private String [] color = new String [] {"0101DF", "FF0000"};
	
	@Test
	public void testPNG() throws IOException {
		String fileType = "png";
		File output = new File("resources/test/colored-test.png");
		client.getColoredPathway(pwId, revision, graphId, color, fileType, output);
	}
	
	@Test
	public void testPDF() throws IOException {
		String fileType = "pdf";
		File output = new File("resources/test/colored-test.pdf");
		client.getColoredPathway(pwId, revision, graphId, color, fileType, output);
	}
	
	@Test
	public void testSVG() throws IOException {
		String fileType = "svg";
		File output = new File("resources/test/colored-test.svg");
		client.getColoredPathway(pwId, revision, graphId, color, fileType, output);
	}

}
