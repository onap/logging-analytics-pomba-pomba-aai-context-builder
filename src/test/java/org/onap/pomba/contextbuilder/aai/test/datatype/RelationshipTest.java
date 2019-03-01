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
import org.onap.pomba.contextbuilder.aai.datatype.RelatedToProperty;
import org.onap.pomba.contextbuilder.aai.datatype.Relationship;
import org.onap.pomba.contextbuilder.aai.datatype.RelationshipDatum;

public class RelationshipTest {

    @Test
    public void testRelationshipWithParameters() {
        List<RelationshipDatum> relationshipDatum = new ArrayList<RelationshipDatum>();
        List<RelatedToProperty> relatedToProperty = new ArrayList<RelatedToProperty>();
        Relationship relationship = new Relationship("relatedTo", "relatedLink", relationshipDatum, relatedToProperty);

        assertEquals("relatedTo", relationship.getRelatedTo());
        assertEquals("relatedLink", relationship.getRelatedLink());
        assertTrue(relationship.getRelationshipData().isEmpty());
        assertTrue(relationship.getRelatedToProperty().isEmpty());
    }

    @Test
    public void testRelationship() {
        Relationship relationship = new Relationship();
        List<RelationshipDatum> relationshipDatum = new ArrayList<RelationshipDatum>();
        List<RelatedToProperty> relatedToProperty = new ArrayList<RelatedToProperty>();

        relationship.setRelatedLink("relatedLink");
        relationship.setRelatedTo("relatedTo");
        relationship.setRelatedToProperty(relatedToProperty);
        relationship.setRelationshipData(relationshipDatum);

        assertEquals("relatedTo", relationship.getRelatedTo());
        assertEquals("relatedLink", relationship.getRelatedLink());
        assertTrue(relationship.getRelationshipData().isEmpty());
        assertTrue(relationship.getRelatedToProperty().isEmpty());

        String relationshipString = relationship.toString();
        assertTrue(relationshipString.contains("[relatedTo=relatedTo,"
                                             + "relatedLink=relatedLink,"
                                             + "relationshipData=[],"
                                             + "relatedToProperty=[]]"));
    }

    @Test
    public void testRelationshipEquals() {
        List<RelationshipDatum> relationshipDatum = new ArrayList<RelationshipDatum>();
        List<RelatedToProperty> relatedToProperty = new ArrayList<RelatedToProperty>();
        Relationship relationship1 = new Relationship("relatedTo1",
                                                      "relatedLink1",
                                                      relationshipDatum,
                                                      relatedToProperty);
        Relationship relationship2 = new Relationship("relatedTo2",
                                                      "relatedLink2",
                                                      relationshipDatum,
                                                      relatedToProperty);
        Relationship relationship3 = new Relationship("relatedTo1",
                                                      "relatedLink1",
                                                      relationshipDatum,
                                                      relatedToProperty);
        assertTrue(relationship1.equals(relationship1));
        assertTrue(!relationship1.equals(relationship2));
        assertTrue(relationship1.equals(relationship3));
    }

}
