// WikiPathways Java library examples,
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
package org.wikipathways.webservice;

import java.io.File;
import java.net.URL;

import org.bridgedb.DataSource;
import org.bridgedb.Xref;
import org.pathvisio.core.model.ObjectType;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.core.model.PathwayElement;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;
import org.pathvisio.wikipathways.webservice.WSSearchResult;
import org.wikipathways.client.WikiPathwaysClient;

/**
 * 
 * Example code for using the new WP REST webservice
 * with the Java library
 * @author msk
 *
 */
public class Example1 {

	public static void main(String[] args) throws Exception {
		//Create a client to the WikiPathways web service
		URL wsURL = new URL("http://webservice.wikipathways.org");
		WikiPathwaysClient client = new WikiPathwaysClient(wsURL);
			
		//////////////////////////
		// EXAMPLE (FIND BY XREF)
		// Find a pathway by affymetrix probeset
		Xref affy = new Xref("201746_at", DataSource.getBySystemCode("X"));
			
		System.out.println("Searching for pathways with Affymetrix probeset " + affy);

		WSSearchResult[] result = client.findPathwaysByXref(affy);
		for(WSSearchResult r : result) {
			System.out.println("Found pathway: " + r.getName() + " (" + r.getSpecies() + ")");
		}
			
		////////////////////////////////////
		// EXAMPLE (GET PATHWAY - SAVE GPML)
		//Download a pathway from WikiPathways
		WSPathway wsPathway = client.getPathway("WP274");
			
		System.out.println("Downloaded pathway " + wsPathway.getName() + ", revision " + wsPathway.getRevision());
			
		//Create a pathway object
		Pathway pathway = WikiPathwaysClient.toPathway(wsPathway);
			
		//Get all genes, proteins and metabolites for a pathway
		for(PathwayElement pwElm : pathway.getDataObjects()) {
			//Only take elements with type DATANODE (genes, proteins, metabolites)
			if(pwElm.getObjectType() == ObjectType.DATANODE) {
				//Print information to the screen
				System.out.println(pwElm.getTextLabel());
				System.out.println("\t" + pwElm.getXref());
				System.out.println("\t" + pwElm.getDataNodeType());
			}
		}
			
		//Save the pathway locally
		pathway.writeToXml(new File(wsPathway.getName() + ".gpml"), true);
			
		///////////////////////////////
		// EXAMPLE (LIST ALL PATHWAYS)
		//Print info for all WikiPathways pathways
		WSPathwayInfo[] pathwayList = client.listPathways();
			
		for(WSPathwayInfo pathwayInfo : pathwayList) {
			System.out.println("Pathway:");
			System.out.println("\tIdentifier:\t" + pathwayInfo.getId());
			System.out.println("\tName:\t" + pathwayInfo.getName());
			System.out.println("\tOrganism:\t" + pathwayInfo.getSpecies());
		}
	}
}
