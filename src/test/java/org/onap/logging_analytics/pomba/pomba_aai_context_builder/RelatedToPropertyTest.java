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
import org.onap.pomba.contextbuilder.aai.datatype.RelatedToProperty;

public class RelatedToPropertyTest {

	@Test
	public void testRelatedToPropertyWithParameters() {
		RelatedToProperty relatedToProperty = new RelatedToProperty("propertyKey", "propertyValue");
		assertEquals("propertyKey", relatedToProperty.getPropertyKey());
		assertEquals("propertyValue", relatedToProperty.getPropertyValue());
	}

	@Test
	public void testRelatedToProperty() {
		RelatedToProperty relatedToProperty = new RelatedToProperty();
		relatedToProperty.setPropertyKey("propertyKey");
		relatedToProperty.setPropertyValue("propertyValue");
		assertEquals("propertyKey", relatedToProperty.getPropertyKey());
		assertEquals("propertyValue", relatedToProperty.getPropertyValue());
		String relatedToPropertyString = relatedToProperty.toString();
		assertTrue(relatedToPropertyString.contains("[propertyKey=propertyKey,propertyValue=propertyValue]"));
	}

	@Test
	public void testRelatedToPropertyIsEqual() {
		RelatedToProperty relatedToProperty1 = new RelatedToProperty("propertyKey1", "propertyValue1");
		RelatedToProperty relatedToProperty2 = new RelatedToProperty("propertyKey2", "propertyValue2");
		RelatedToProperty relatedToProperty3 = new RelatedToProperty("propertyKey1", "propertyValue1");
		assertTrue(relatedToProperty1.equals(relatedToProperty1));
		assertTrue(!relatedToProperty1.equals(relatedToProperty2));
		assertTrue(relatedToProperty1.equals(relatedToProperty3));
	}
}
