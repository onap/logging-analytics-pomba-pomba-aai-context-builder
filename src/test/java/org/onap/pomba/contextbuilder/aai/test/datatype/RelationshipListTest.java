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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.onap.pomba.contextbuilder.aai.datatype.RelatedToProperty;
import org.onap.pomba.contextbuilder.aai.datatype.Relationship;
import org.onap.pomba.contextbuilder.aai.datatype.RelationshipDatum;
import org.onap.pomba.contextbuilder.aai.datatype.RelationshipList;

public class RelationshipListTest {
    @Test
    public void testRelationshipList() {
        RelationshipList relationshipList = new RelationshipList();
        Relationship relationship = new Relationship();
        List<Relationship> list = new ArrayList<Relationship>();
        list.add(relationship);
        relationshipList.setRelationship(list);

        assertTrue(relationshipList.getRelationship().size() == 1);

        relationship.setRelatedLink("relatedLink");
        relationship.setRelatedTo("relatedTo");

        RelatedToProperty relatedToProperty = new RelatedToProperty();
        List<RelatedToProperty> relatedToPropertyList = new ArrayList<RelatedToProperty>();
        relatedToPropertyList.add(relatedToProperty);
        relationship.setRelatedToProperty(relatedToPropertyList);
        list.add(relationship);
        relationshipList.setRelationship(list);

        String relationshipListString = relationshipList.toString();
        assertTrue(relationshipListString.contains("[relationship="));

    }

    @Test
    public void testRelationshipListWithParameters() {
        Relationship relationship = new Relationship("relatedTo",
                "relatedLink",
                new ArrayList<RelationshipDatum>(),
                new ArrayList<RelatedToProperty>());
        List<Relationship> listOfRelationship = new ArrayList<Relationship>();
        listOfRelationship.add(relationship);
        RelationshipList relationshipList = new RelationshipList(listOfRelationship);

        assertTrue(relationshipList.getRelationship().size() == 1);
    }

    @Test
    public void testRelationshipListEquals() {

        Relationship relationship1 = new Relationship("relatedTo1",
                "relatedLink1",
                new ArrayList<RelationshipDatum>(),
                new ArrayList<RelatedToProperty>());
        List<Relationship> listOfRelationship = new ArrayList<Relationship>();
        listOfRelationship.add(relationship1);
        RelationshipList relationshipList1 = new RelationshipList(listOfRelationship);

        Relationship relationship2 = new Relationship("relatedTo2",
                "relatedLink2",
                new ArrayList<RelationshipDatum>(),
                new ArrayList<RelatedToProperty>());
        List<Relationship> listOfRelationship2 = new ArrayList<Relationship>();
        listOfRelationship.add(relationship2);
        RelationshipList relationshipList2 = new RelationshipList(listOfRelationship2);

        Relationship relationship3 = new Relationship("relatedTo1",
                "relatedLink1",
                new ArrayList<RelationshipDatum>(),
                new ArrayList<RelatedToProperty>());
        List<Relationship> listOfRelationship3 = new ArrayList<Relationship>();
        listOfRelationship.add(relationship3);
        RelationshipList relationshipList3 = new RelationshipList(listOfRelationship3);

        assertTrue(relationshipList1.equals(relationshipList1));
        assertTrue(!relationshipList1.equals(relationshipList2));
        assertTrue(!relationshipList1.equals(relationshipList3));
    }
}
