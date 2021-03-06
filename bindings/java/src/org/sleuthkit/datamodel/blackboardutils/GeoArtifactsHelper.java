/*
 * Sleuth Kit Data Model
 *
 * Copyright 2020 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	 http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.datamodel.blackboardutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.sleuthkit.datamodel.Blackboard.BlackboardException;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.Content;
import org.sleuthkit.datamodel.SleuthkitCase;
import org.sleuthkit.datamodel.TskCoreException;

/**
 * Class to help ingest modules create Geolocation artifacts. 
 *
 */
public final class GeoArtifactsHelper extends ArtifactHelperBase {

	/**
	 * Constructs a geolocation artifact helper for the given source file.
	 *
	 * @param caseDb			  Sleuthkit case db.
	 * @param moduleName	Name of module using the helper.
	 * @param srcFile			 Source file being processed by the module.
	 */
	public GeoArtifactsHelper(SleuthkitCase caseDb, String moduleName, Content srcFile) {
		super(caseDb, moduleName, srcFile);
	}
	
	/**
	 * Adds a TSK_GPS_TRACKPOINT artifact.
	 *
	 * @param latitude    Location latitude, required.
	 * @param longitude   Location longitude, required.
	 * @param timeStamp   Date/time trackpoint was recorded, can be 0 if not
	 *                    available.
	 * @param name        Trackpoint name, can be empty/null if not available.
	 * @param programName Name of program that recorded the trackpoint, can be
	 *                    empty or null if not available.
	 *
	 * @return GPS trackpoint artifact added
	 *
	 * @throws TskCoreException		If there is an error creating the artifact.
	 * @throws BlackboardException	If there is a problem posting the artifact.
	 */
	public BlackboardArtifact addGPSTrackpoint(double latitude, double longitude,
			long timeStamp, String name, String programName) throws TskCoreException, BlackboardException {

		return addGPSTrackpoint(latitude, longitude, timeStamp, name, programName,
				Collections.emptyList());
	}

	/**
	 * Adds a TSK_GPS_TRACKPOINT artifact.
	 *
	 * @param latitude            Location latitude, required.
	 * @param longitude           Location longitude, required.
	 * @param timeStamp           Date/time the trackpoint was recorded, can be
	 *                            0 if not available.
	 * @param name                Trackpoint name, can be empty/null if not
	 *                            available.
	 * @param programName         Name of program that recorded the trackpoint,
	 *                            can be empty or null if not available.
	 * @param otherAttributesList Other attributes, can be an empty list of no
	 *                            additional attributes.
	 *
	 * @return GPS trackpoint artifact added
	 *
	 * @throws TskCoreException		If there is an error creating the artifact.
	 * @throws BlackboardException	If there is a problem posting the artifact.
	 */
	public BlackboardArtifact addGPSTrackpoint(double latitude, double longitude, long timeStamp, String name, String programName,
			Collection<BlackboardAttribute> otherAttributesList) throws TskCoreException, BlackboardException {

		BlackboardArtifact gpsTrackpointArtifact;
		Collection<BlackboardAttribute> attributes = new ArrayList<>();

		// create artifact
		gpsTrackpointArtifact = getContent().newArtifact(BlackboardArtifact.ARTIFACT_TYPE.TSK_GPS_TRACKPOINT);

		// construct attributes 
		attributes.add(new BlackboardAttribute(BlackboardAttribute.ATTRIBUTE_TYPE.TSK_GEO_LATITUDE, getModuleName(), latitude));
		attributes.add(new BlackboardAttribute(BlackboardAttribute.ATTRIBUTE_TYPE.TSK_GEO_LONGITUDE, getModuleName(), longitude));

		addAttributeIfNotZero(BlackboardAttribute.ATTRIBUTE_TYPE.TSK_DATETIME, timeStamp, attributes);

		addAttributeIfNotNull(BlackboardAttribute.ATTRIBUTE_TYPE.TSK_NAME, name, attributes);
		addAttributeIfNotNull(BlackboardAttribute.ATTRIBUTE_TYPE.TSK_PROG_NAME, programName, attributes);

		// add the attributes 
		attributes.addAll(otherAttributesList);
		gpsTrackpointArtifact.addAttributes(attributes);

		// post artifact 
		getSleuthkitCase().getBlackboard().postArtifact(gpsTrackpointArtifact, getModuleName());

		// return the artifact
		return gpsTrackpointArtifact;
	}
	
}
