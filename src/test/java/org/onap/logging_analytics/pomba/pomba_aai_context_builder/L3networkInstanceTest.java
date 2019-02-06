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
import org.onap.pomba.contextbuilder.aai.datatype.L3networkInstance;

public class L3networkInstanceTest {
	@Test
	public void testL3networkInstanceEquals() {
	    L3networkInstance  l3networkInstance1 = new L3networkInstance
	            ( "networkId1",  "networkName1",  "networkType1",
	             "networkRole1",  "networkTechnology1", "resourceVersion1",
	             "modelInvariantId1",  "modelVersionId1",  "physicalNetworkName1", "true",
	             null);

	    L3networkInstance  l3networkInstance2 = new L3networkInstance ();
	    l3networkInstance2.setNetworkId("networkId1");
        l3networkInstance2.setNetworkName("networkName1");
        l3networkInstance2.setNetworkType("networkType1");
        l3networkInstance2.setNetworkRole("networkRole1");
        l3networkInstance2.setNetworkTechnology("networkTechnology1");
        l3networkInstance2.setResourceVersion("resourceVersion1");
        l3networkInstance2.setModelInvariantId("modelInvariantId1");
        l3networkInstance2.setModelVersionId("modelVersionId1");
        l3networkInstance2.setPhysicalNetworkName("physicalNetworkName1");
        l3networkInstance2.setSharedNetworkBoolean("true");
        l3networkInstance2.setRelationshipList(null);

        l3networkInstance1.toJson();
        l3networkInstance1.toString();
        assertEquals("physicalNetworkName1", l3networkInstance2.getPhysicalNetworkName());
		assertTrue(l3networkInstance1.equals(l3networkInstance1));
		assertTrue(l3networkInstance1.equals(l3networkInstance2));
	}
}
