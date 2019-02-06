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
import org.onap.pomba.contextbuilder.aai.datatype.PInterfaceInstance;

public class PInterfaceInstanceTest {

	   @Test
	    public void testPInterfaceInstanceEquals() {
	       PInterfaceInstance  pInterfaceInstance1 = new PInterfaceInstance
	               ( "interfaceName1", "speedValue1", "speedUnits1",
	                 "portDescription1", "equipmentIdentifier1", "interfaceRole1", "interfaceType1",
	                 "provStatus1", "resourceVersion1",  "inMaint1",  "invStatus1" );

	       PInterfaceInstance  pInterfaceInstance2 = new PInterfaceInstance ();
	        pInterfaceInstance2.setInterfaceName("interfaceName1");
	        pInterfaceInstance2.setSpeedValue("speedValue1");
	        pInterfaceInstance2.setSpeedUnits("speedUnits1");
	        pInterfaceInstance2.setPortDescription("portDescription1");
	        pInterfaceInstance2.setEquipmentIdentifier("equipmentIdentifier1");
	        pInterfaceInstance2.setInterfaceRole("interfaceRole1");
	        pInterfaceInstance2.setInterfaceType("interfaceType1");
	        pInterfaceInstance2.setProvStatus("provStatus1");
	        pInterfaceInstance2.setResourceVersion("resourceVersion1");
	        pInterfaceInstance2.setInMaint("inMaint1");
	        pInterfaceInstance2.setInvStatus("invStatus1");

	        pInterfaceInstance1.toJson();
	        pInterfaceInstance1.toString();
	        assertEquals("interfaceName1", pInterfaceInstance2.getInterfaceName());
	        assertTrue(pInterfaceInstance1.equals(pInterfaceInstance1));
	        assertTrue(pInterfaceInstance1.equals(pInterfaceInstance2));
	    }
}
