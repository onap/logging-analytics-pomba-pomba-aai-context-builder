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

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.onap.pomba.contextbuilder.aai.datatype.L3networkInstance;
import org.onap.pomba.contextbuilder.aai.datatype.RelatedToProperty;
import org.onap.pomba.contextbuilder.aai.datatype.Relationship;
import org.onap.pomba.contextbuilder.aai.datatype.RelationshipDatum;
import org.onap.pomba.contextbuilder.aai.datatype.RelationshipList;
import org.onap.pomba.contextbuilder.aai.datatype.VfModule;

public class VfModuleTest {
    @Test
    public void testVfModule() {
        VfModule vfModule = new VfModule();
        vfModule.setVfModuleId("vfModuleId");
        vfModule.setVfModuleName("vfModuleName");
        vfModule.setHeatStackId("heatStackId");
        vfModule.setOrchestrationStatus("orchestrationStatus");
        vfModule.setIsBaseVfModule(true);
        vfModule.setResourceVersion("resourceVersion");
        vfModule.setModelInvariantId("modelInvariantId");
        vfModule.setModelVersionId("modelVersionId");
        vfModule.setModelCustomizationId("modelCustomizationId");
        vfModule.setModuleIndex(1);

        RelationshipList relationshipList = new RelationshipList();
        List<Relationship> listOfRelationship = new ArrayList<Relationship>();
        Relationship relationship = new Relationship("relatedTo",
                                                     "relatedLink",
                                                     new ArrayList<RelationshipDatum>(),
                                                     new ArrayList<RelatedToProperty>());
        listOfRelationship.add(relationship);
        relationshipList.setRelationship(listOfRelationship);

        vfModule.setRelationshipList(relationshipList);

        assertEquals("vfModuleId", vfModule.getVfModuleId());
        assertEquals("vfModuleName", vfModule.getVfModuleName());
        assertEquals("heatStackId", vfModule.getHeatStackId());
        assertEquals("orchestrationStatus", vfModule.getOrchestrationStatus());
        assertTrue(vfModule.getIsBaseVfModule());
        assertEquals("resourceVersion", vfModule.getResourceVersion());
        assertEquals("modelInvariantId", vfModule.getModelInvariantId());
        assertEquals("modelVersionId", vfModule.getModelVersionId());
        assertEquals("modelCustomizationId", vfModule.getModelCustomizationId());
        assertEquals(new Integer(1), vfModule.getModuleIndex());
        assertEquals("relatedTo", vfModule.getRelationshipList().getRelationship().get(0).getRelatedTo());
    }

    @Test
    public void testVfModuleWithParameters() {
        VfModule vfModule = new VfModule("vfModuleId",
                                         "vfModuleName",
                                         "heatStackId",
                                         "orchestrationStatus",
                                         true,
                                         "resourceVersion",
                                         "modelInvariantId",
                                         "modelVersionId",
                                         "modelCustomizationId",
                                         1,
                                         new RelationshipList() ,
                                         new ArrayList<L3networkInstance>());

        assertEquals("vfModuleId", vfModule.getVfModuleId());
        assertEquals("vfModuleName", vfModule.getVfModuleName());
        assertEquals("heatStackId", vfModule.getHeatStackId());
        assertEquals("orchestrationStatus", vfModule.getOrchestrationStatus());
        assertTrue(vfModule.getIsBaseVfModule());
        assertEquals("resourceVersion", vfModule.getResourceVersion());
        assertEquals("modelInvariantId", vfModule.getModelInvariantId());
        assertEquals("modelVersionId", vfModule.getModelVersionId());
        assertEquals("modelCustomizationId", vfModule.getModelCustomizationId());
        assertEquals(new Integer(1), vfModule.getModuleIndex());

        String vfModuleString = vfModule.toString();
        assertTrue(vfModuleString.contains("[vfModuleId=vfModuleId,vfModuleName=vfModuleName,heatStackId=heatStackId,"
                                         + "orchestrationStatus=orchestrationStatus,isBaseVfModule=true,"
                                         + "resourceVersion=resourceVersion,modelInvariantId=modelInvariantId,"
                                         + "modelVersionId=modelVersionId,modelCustomizationId=modelCustomizationId,"
                                         + "moduleIndex=1"));

    }

    @Test
    public void testVfModuleEquals() {
        VfModule vfModule1 = new VfModule("vfModuleId1",
                                          "vfModuleName1",
                                          "heatStackId1",
                                          "orchestrationStatus1",
                                          true,
                                          "resourceVersion1",
                                          "modelInvariantId1",
                                          "modelVersionId1",
                                          "modelCustomizationId1",
                                          1,
                                          new RelationshipList() ,
                                          new ArrayList<L3networkInstance>());
        VfModule vfModule2 = new VfModule("vfModuleId2",
                                          "vfModuleName2",
                                          "heatStackId2",
                                          "orchestrationStatus2",
                                          true,
                                          "resourceVersion2",
                                          "modelInvariantId2",
                                          "modelVersionId2",
                                          "modelCustomizationId2",
                                          1,
                                          new RelationshipList() ,
                                          new ArrayList<L3networkInstance>());
        VfModule vfModule3 = new VfModule("vfModuleId1",
                                          "vfModuleName1",
                                          "heatStackId1",
                                          "orchestrationStatus1",
                                          true,
                                          "resourceVersion1",
                                          "modelInvariantId1",
                                          "modelVersionId1",
                                          "modelCustomizationId1",
                                          1,
                                          new RelationshipList() ,
                                          new ArrayList<L3networkInstance>());

        assertTrue(vfModule1.equals(vfModule1));
        assertTrue(!vfModule1.equals(vfModule2));
        assertTrue(vfModule1.equals(vfModule3));
    }

}
