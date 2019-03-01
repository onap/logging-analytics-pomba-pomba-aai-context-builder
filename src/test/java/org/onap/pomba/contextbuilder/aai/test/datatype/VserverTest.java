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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.onap.pomba.contextbuilder.aai.datatype.LInterfaceInstanceList;
import org.onap.pomba.contextbuilder.aai.datatype.Relationship;
import org.onap.pomba.contextbuilder.aai.datatype.RelationshipList;
import org.onap.pomba.contextbuilder.aai.datatype.Vserver;

public class VserverTest {

    @Test
    public void testVserver() {
        Vserver aVServer = new Vserver();
        aVServer.setVserverId("vserverId");
        aVServer.setVserverName("vserverName");
        aVServer.setVserverName2("vserverName2");
        aVServer.setVserverSelflink("vserverSelflink");
        aVServer.setInMaint(true);
        aVServer.setIsClosedLoopDisabled(true);
        aVServer.setProvStatus("provStatus");
        Relationship relationship = new Relationship();
        List<Relationship> list = new ArrayList<Relationship>();
        list.add(relationship);
        RelationshipList relationshipList = new RelationshipList(list);
        aVServer.setRelationshipList(relationshipList);
        aVServer.setResourceVersion("resourceVersion");

        assertEquals("vserverId", aVServer.getVserverId());
        assertEquals("vserverName", aVServer.getVserverName());
        assertEquals("vserverName2", aVServer.getVserverName2());
        assertEquals("vserverSelflink", aVServer.getVserverSelflink());
        assertEquals(true, aVServer.getInMaint());
        assertEquals(true, aVServer.getIsClosedLoopDisabled());
        assertEquals("provStatus", aVServer.getProvStatus());
        assertTrue(aVServer.getRelationshipList().getRelationship().size() == 1);
        assertEquals("resourceVersion", aVServer.getResourceVersion());

        String vserverToString = aVServer.toString();
        assertTrue(vserverToString.contains("[vserverId=vserverId,vserverName=vserverName,vserverName2=vserverName2"
                                          + ",provStatus=provStatus,vserverSelflink=vserverSelflink,inMaint=true,"
                                          + "isClosedLoopDisabled=true,resourceVersion=resourceVersion"));
    }

    @Test
    public void testVserverWithParameters() {
        Vserver aVServer = new Vserver("vserverId",
                                       "vserverName",
                                       "vserverName2",
                                       "provStatus",
                                       "vserverSelflink",
                                       true,
                                       true,
                                       "resourceVersion",
                                       new RelationshipList(),
                                       new LInterfaceInstanceList());

        assertEquals("vserverId", aVServer.getVserverId());
        assertEquals("vserverName", aVServer.getVserverName());
        assertEquals("vserverName2", aVServer.getVserverName2());
        assertEquals("vserverSelflink", aVServer.getVserverSelflink());
        assertEquals(true, aVServer.getInMaint());
        assertEquals(true, aVServer.getIsClosedLoopDisabled());
        assertEquals("provStatus", aVServer.getProvStatus());
        assertEquals("resourceVersion", aVServer.getResourceVersion());
    }

    @Test
    public void testVserverEquals() {
        Vserver aVServer1 = new Vserver("vserverId1",
                                        "vserverName1",
                                        "vserverName2",
                                        "provStatus1",
                                        "vserverSelflink1",
                                        true,
                                        true,
                                        "resourceVersion1",
                                        new RelationshipList(),
                                        new LInterfaceInstanceList());
        Vserver aVServer2 = new Vserver("vserverId2",
                                        "vserverName2",
                                        "vserverName2",
                                        "provStatus2",
                                        "vserverSelflink2",
                                        false,
                                        false,
                                        "resourceVersion2",
                                        new RelationshipList(),
                                        new LInterfaceInstanceList());
        Vserver aVServer3 = new Vserver("vserverId1",
                                        "vserverName1",
                                        "vserverName2",
                                        "provStatus1",
                                        "vserverSelflink1",
                                        true,
                                        true,
                                        "resourceVersion1",
                                        new RelationshipList(),
                                        new LInterfaceInstanceList());

        assertTrue(aVServer1.equals(aVServer1));
        assertFalse(aVServer1.equals(aVServer2));
        assertTrue(aVServer1.equals(aVServer3));
    }

}
