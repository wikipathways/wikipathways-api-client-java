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

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.wikipathways.client.test.utils.ConnectionSettings;

/**
 * JUnit Test for webservice function: savePathwayAs
 * @author mkutmon
 */
public class TestDownloadPathwayAs {

	WikiPathwaysClient client;
	
	@Before
	public void setUp() throws Exception {
		client = ConnectionSettings.createClient();
		File file = new File("resources/test/");
		file.mkdirs();
	}

	@Test
	public void testPng() throws IOException {
		client.downloadPathwayAs(new File("resources/test/output.png"), "png", "WP4", 0);
	}
	
	@Test
	public void testGpml() throws IOException {
		client.downloadPathwayAs(new File("resources/test/output.gpml"), "gpml", "WP4", 0);
	}
	
	@Test
	public void testSvg() throws IOException {
		client.downloadPathwayAs(new File("resources/test/output.svg"), "svg", "WP4", 0);
	}
	
	@Test
	public void testPdf() throws IOException {
		client.downloadPathwayAs(new File("resources/test/output.pdf"), "pdf", "WP4", 0);
	}
	
	@Test
	public void testTxt() throws IOException {
		client.downloadPathwayAs(new File("resources/test/output.txt"), "txt", "WP4", 0);
	}
	
	@Test
	public void testPwf() throws IOException {
		client.downloadPathwayAs(new File("resources/test/output.pwf"), "pwf", "WP4", 0);
	}
	
	@Test
	public void testOwl() throws IOException {
		client.downloadPathwayAs(new File("resources/test/output.owl"), "owl", "WP4", 0);
	}
}
