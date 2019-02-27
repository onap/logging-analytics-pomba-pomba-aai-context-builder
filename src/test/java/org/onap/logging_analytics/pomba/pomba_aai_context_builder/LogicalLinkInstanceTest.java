/*
 * ============LICENSE_START===================================================
 * Copyright (c) 2018 Amdocs
 * ============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=====================================================
 */
package org.onap.logging_analytics.pomba.pomba_aai_context_builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.onap.pomba.contextbuilder.aai.datatype.LogicalLinkInstance;

public class LogicalLinkInstanceTest {
	   @Test
	    public void testLogicalLinkInstanceEquals() {
	       LogicalLinkInstance  logicalLinkInstance1 = new LogicalLinkInstance
	               ( "linkName1",  "linkId1",  "modelVersionId1",
                    "modelInvariantId1",  "linkType1", "routingProtocol1",
                    "speedValue1", "speedUnits1", "provStatus1",
                    "inMaint1",  "linkRole1",  "ipVersion1",
                    "linkName21","circuitId1","purpose1");

	       LogicalLinkInstance  logicalLinkInstance2 = new LogicalLinkInstance ();
	        logicalLinkInstance2.setLinkName("linkName1");
	        logicalLinkInstance2.setLinkId("linkId1");
	        logicalLinkInstance2.setModelVersionId("modelVersionId1");
	        logicalLinkInstance2.setModelInvariantId("modelInvariantId1");
	        logicalLinkInstance2.setLinkType("linkType1");
	        logicalLinkInstance2.setRoutingProtocol("routingProtocol1");
	        logicalLinkInstance2.setSpeedValue("speedValue1");
	        logicalLinkInstance2.setSpeedUnits("speedUnits1");
	        logicalLinkInstance2.setProvStatus("provStatus1");
	        logicalLinkInstance2.setInMaint("inMaint1");
	        logicalLinkInstance2.setLinkRole("linkRole1");
	        logicalLinkInstance2.setIpVersion("ipVersion1");
	        logicalLinkInstance2.setLinkName2("linkName21");
	        logicalLinkInstance2.setCircuitId("circuitId1");
	        logicalLinkInstance2.setPurpose("purpose1");

	        logicalLinkInstance1.toJson();
	        logicalLinkInstance1.toString();
	        assertEquals("linkName1", logicalLinkInstance2.getLinkName());
	        assertTrue(logicalLinkInstance1.equals(logicalLinkInstance1));
	        assertTrue(logicalLinkInstance1.equals(logicalLinkInstance2));
	    }
}
