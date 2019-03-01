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

package org.onap.pomba.contextbuilder.aai.test.datatype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.onap.pomba.contextbuilder.aai.datatype.PserverInstance;

public class PserverInstanceTest {

    @Test
    public void testPserverInstanceEquals() {
        PserverInstance  pserverInstance1 = new PserverInstance("pserverId1",  "hostname1",  "pserverName2a",
                "ptniiEquipName1",  "equipType1", "equipVendor1", "equipModel1", "fqdn1", "internetTopology1",
                "inMaint1",  "resourceVersion1",  "serialNumber1",
                "purpose1",null);

        PserverInstance  pserverInstance2 = new PserverInstance();
        pserverInstance2.setPserverId("pserverId1");
        pserverInstance2.setHostname("hostname1");
        pserverInstance2.setPserverName2("pserverName2a");
        pserverInstance2.setPtniiEquipName("ptniiEquipName1");
        pserverInstance2.setEquipType("equipType1");
        pserverInstance2.setEquipVendor("equipVendor1");
        pserverInstance2.setEquipModel("equipModel1");
        pserverInstance2.setFqdn("fqdn1");
        pserverInstance2.setInternetTopology("internetTopology1");
        pserverInstance2.setInMaint("inMaint1");
        pserverInstance2.setResourceVersion("resourceVersion1");
        pserverInstance2.setSerialNumber("serialNumber1");
        pserverInstance2.setPurpose("purpose1");
        pserverInstance2.setPInterfaceInstanceList(null);

        pserverInstance1.toJson();
        pserverInstance1.toString();
        assertEquals("hostname1", pserverInstance2.getHostname());
        assertTrue(pserverInstance1.equals(pserverInstance1));
        assertTrue(pserverInstance1.equals(pserverInstance2));
    }
}
