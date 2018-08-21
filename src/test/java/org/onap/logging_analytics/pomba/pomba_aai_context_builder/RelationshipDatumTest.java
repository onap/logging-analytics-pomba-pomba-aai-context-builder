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
import org.onap.pomba.contextbuilder.aai.datatype.RelationshipDatum;

public class RelationshipDatumTest {
	@Test
	public void testRelationshipDatum() {
		RelationshipDatum relationshipDatum = new RelationshipDatum();

		relationshipDatum.setRelationshipKey("relationshipKey");
		relationshipDatum.setRelationshipValue("relationshipValue");

		assertEquals("relationshipKey", relationshipDatum.getRelationshipKey());
		assertEquals("relationshipValue", relationshipDatum.getRelationshipValue());
	}

	@Test
	public void testRelationshipDatumWithParameters() {
		RelationshipDatum relationshipDatum = new RelationshipDatum("relationshipKey", "relationshipValue");

		assertEquals("relationshipKey", relationshipDatum.getRelationshipKey());
		assertEquals("relationshipValue", relationshipDatum.getRelationshipValue());
		String relationshipDatumString = relationshipDatum.toString();
		assertTrue(relationshipDatumString.contains("[relationshipKey=relationshipKey,relationshipValue=relationshipValue]"));
	}

	@Test
	public void testRelationshipDatumEqual() {
		RelationshipDatum relationshipDatum1 = new RelationshipDatum("relationshipKey1", "relationshipValue1");
		RelationshipDatum relationshipDatum2 = new RelationshipDatum("relationshipKey2", "relationshipValue2");
		RelationshipDatum relationshipDatum3 = new RelationshipDatum("relationshipKey1", "relationshipValue1");

		assertTrue(relationshipDatum1.equals(relationshipDatum1));
		assertTrue(!relationshipDatum1.equals(relationshipDatum2));
		assertTrue(relationshipDatum1.equals(relationshipDatum3));
	}
}
