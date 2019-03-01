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
import org.onap.pomba.contextbuilder.aai.datatype.RelationshipList;
import org.onap.pomba.contextbuilder.aai.datatype.VnfcInstance;

public class VnfcInstanceTest {
    @Test
    public void testVnfcInstance() {
        VnfcInstance vnfcInstance = new VnfcInstance();
        vnfcInstance.setVnfcName("vnfcName");
        vnfcInstance.setNfcNamingCode("nfcNamingCode");
        vnfcInstance.setNfcFunction("nfcFunction");
        vnfcInstance.setProvisionStatus("provisionStatus");
        vnfcInstance.setOrchestrationStatus("orchestrationStatus");
        vnfcInstance.setInMaintenance(true);
        vnfcInstance.setIsClosedLoopDisabled(true);
        vnfcInstance.setResourceVersion("resourceVersion");
        vnfcInstance.setModelInvariantId("modelInvariantId");
        vnfcInstance.setModelVersionId("modelVersionId");
        vnfcInstance.setRelationshipList(new RelationshipList());

        assertEquals("vnfcName", vnfcInstance.getVnfcName());
        assertEquals("nfcNamingCode", vnfcInstance.getNfcNamingCode());
        assertEquals("nfcFunction", vnfcInstance.getNfcFunction());
        assertEquals("provisionStatus", vnfcInstance.getProvisionStatus());
        assertEquals("orchestrationStatus", vnfcInstance.getOrchestrationStatus());
        assertTrue(vnfcInstance.getInMaintenance());
        assertTrue(vnfcInstance.getIsClosedLoopDisabled());
        assertEquals("resourceVersion", vnfcInstance.getResourceVersion());
        assertEquals("modelInvariantId", vnfcInstance.getModelInvariantId());
        assertEquals("modelVersionId", vnfcInstance.getModelVersionId());
        assertTrue(vnfcInstance.getRelationshipList() instanceof RelationshipList);

    }

    @Test
    public void testVnfcInstanceWithParameters() {
        VnfcInstance vnfcInstance = new VnfcInstance("vnfcName",
                                                     "nfcNamingCode",
                                                     "nfcFunction",
                                                     "provisionStatus",
                                                     "orchestrationStatus",
                                                     true,
                                                     true,
                                                     "resourceVersion",
                                                     "modelInvariantId",
                                                     "modelVersionId",
                                                     new RelationshipList());

        assertEquals("vnfcName", vnfcInstance.getVnfcName());
        assertEquals("nfcNamingCode", vnfcInstance.getNfcNamingCode());
        assertEquals("nfcFunction", vnfcInstance.getNfcFunction());
        assertEquals("provisionStatus", vnfcInstance.getProvisionStatus());
        assertEquals("orchestrationStatus", vnfcInstance.getOrchestrationStatus());
        assertTrue(vnfcInstance.getInMaintenance());
        assertTrue(vnfcInstance.getIsClosedLoopDisabled());
        assertEquals("resourceVersion", vnfcInstance.getResourceVersion());
        assertEquals("modelInvariantId", vnfcInstance.getModelInvariantId());
        assertEquals("modelVersionId", vnfcInstance.getModelVersionId());
        assertTrue(vnfcInstance.getRelationshipList() instanceof RelationshipList);

        String vnfcInstanceString = vnfcInstance.toString();
        assertTrue(vnfcInstanceString.contains("[vnfcName=vnfcName,nfcNamingCode=nfcNamingCode,"
                                             + "nfcFunction=nfcFunction,provisionStatus=provisionStatus,"
                                             + "orchestration_status=orchestrationStatus,inMaintenance=true,"
                                             + "isClosedLoopDisabled=true,resourceVersion=resourceVersion,"
                                             + "modelInvariantId=modelInvariantId,modelVersionId=modelVersionId,"));
    }

    @Test
    public void testVnfcInstanceEquals() {
        VnfcInstance vnfcInstance1 = new VnfcInstance("vnfcName1",
                                                      "nfcNamingCode1",
                                                      "nfcFunction1",
                                                      "provisionStatus1",
                                                      "orchestrationStatus1",
                                                      true,
                                                      true,
                                                      "resourceVersion1",
                                                      "modelInvariantId1",
                                                      "modelVersionId1",
                                                      new RelationshipList());
        VnfcInstance vnfcInstance2 = new VnfcInstance("vnfcName2",
                                                      "nfcNamingCode2",
                                                      "nfcFunction2",
                                                      "provisionStatus2",
                                                      "orchestrationStatus2",
                                                      true,
                                                      true,
                                                      "resourceVersion2",
                                                      "modelInvariantId2",
                                                      "modelVersionId2",
                                                      new RelationshipList());
        VnfcInstance vnfcInstance3 = new VnfcInstance("vnfcName1",
                                                      "nfcNamingCode1",
                                                      "nfcFunction1",
                                                      "provisionStatus1",
                                                      "orchestrationStatus1",
                                                      true,
                                                      true,
                                                      "resourceVersion1",
                                                      "modelInvariantId1",
                                                      "modelVersionId1",
                                                      new RelationshipList());

        assertTrue(vnfcInstance1.equals(vnfcInstance1));
        assertTrue(!vnfcInstance1.equals(vnfcInstance2));
        assertTrue(vnfcInstance1.equals(vnfcInstance3));
    }
}
