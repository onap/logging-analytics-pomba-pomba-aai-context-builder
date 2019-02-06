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
import org.onap.pomba.contextbuilder.aai.datatype.PnfInstance;

public class PnfInstanceTest {
	   @Test
	    public void testPnfInstanceEquals() {
	       PnfInstance  pnfInstance1 = new PnfInstance
	               ( "uuid1",  "name1",  "networkRole1",
                    "name21",  "name2Source1", "equipmentType1",
                    "equipmentVendor1", "equipmentModel1", "managementOptions1",
                    "swVersion1",  "frameId1",  "serialNumber1",
                    "modelInvariantId1",  "modelVersionId1",  null);

	       PnfInstance  pnfInstance2 = new PnfInstance ();
	        pnfInstance2.setPnfId("uuid1");
	        pnfInstance2.setPnfName("name1");
	        pnfInstance2.setNfRole("networkRole1");
	        pnfInstance2.setPnfName2("name21");
	        pnfInstance2.setPnfName2Source("name2Source1");
	        pnfInstance2.setEquipmentType("equipmentType1");
	        pnfInstance2.setEquipmentVendor("equipmentVendor1");
	        pnfInstance2.setEquipmentModel("equipmentModel1");
	        pnfInstance2.setManagementOptions("managementOptions1");
	        pnfInstance2.setSwVersion("swVersion1");
	        pnfInstance2.setFrameId("frameId1");
	        pnfInstance2.setSerialNumber("serialNumber1");
	        pnfInstance2.setModelInvariantId("modelInvariantId1");
	        pnfInstance2.setModelVersionId("modelVersionId1");
	        pnfInstance2.setPInterfaceInstanceList(null);

	        pnfInstance1.toJson();
	        pnfInstance1.toString();
	        assertEquals("name1", pnfInstance2.getPnfName());
	        assertTrue(pnfInstance1.equals(pnfInstance1));
	        assertTrue(pnfInstance1.equals(pnfInstance2));
	    }
}
