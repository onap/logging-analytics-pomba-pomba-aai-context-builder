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
import org.onap.pomba.contextbuilder.aai.datatype.LInterfaceInstance;

public class LInterfaceInstanceTest {
	   @Test
	    public void testLInterfaceInstanceEquals() {
	       LInterfaceInstance  lInterfaceInstance1 = new LInterfaceInstance
	               ( "interfaceId1",  "interfaceName1",  "interfaceRole1",
                    "isPortMirrored1",  "adminStatus1",  "networkName1",
                    "macAddr1", "inMaint1", null);

	       LInterfaceInstance  lInterfaceInstance2 = new LInterfaceInstance ();
	        lInterfaceInstance2.setInterfaceId("interfaceId1");
	        lInterfaceInstance2.setInterfaceName("interfaceName1");
	        lInterfaceInstance2.setInterfaceRole("interfaceRole1");
	        lInterfaceInstance2.setIsPortMirrored("isPortMirrored1");
	        lInterfaceInstance2.setAdminStatus("adminStatus1");
	        lInterfaceInstance2.setNetworkName("networkName1");
	        lInterfaceInstance2.setMacAddr("macAddr1");
	        lInterfaceInstance2.setInMaint("inMaint1");

	        lInterfaceInstance1.toJson();
	        lInterfaceInstance1.toString();
	        assertEquals("interfaceName1", lInterfaceInstance2.getInterfaceName());
	        assertTrue(lInterfaceInstance1.equals(lInterfaceInstance1));
	        assertTrue(lInterfaceInstance1.equals(lInterfaceInstance2));
	    }

}
