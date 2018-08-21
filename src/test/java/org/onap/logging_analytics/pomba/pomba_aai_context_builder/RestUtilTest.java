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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.onap.pomba.contextbuilder.aai.exception.AuditException;
import org.onap.pomba.contextbuilder.aai.util.RestUtil;

public class RestUtilTest {

	@Test
	public void testValidateURL() {
		// Missing ServiceInstanceId or it is null
		try {
			RestUtil.validateURL("", "modelVersionId", "modelInvariantId", "serviceType", "customerId");
		} catch (AuditException e) {
			assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: serviceInstanceId"));
		}

		try {
			RestUtil.validateURL(null, "modelVersionId", "modelInvariantId", "serviceType", "customerId");
		} catch (AuditException e) {
			assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: serviceInstanceId"));
		}

		// Missing ModelVersionId or it is null
		try {
			RestUtil.validateURL("serviceInstanceId", "", "modelInvariantId", "serviceType", "customerId");
		} catch (AuditException e) {
			assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: modelVersionId"));
		}

		try {
			RestUtil.validateURL("serviceInstanceId", null, "modelInvariantId", "serviceType", "customerId");
		} catch (AuditException e) {
			assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: modelVersionId"));
		}

		// Missing ModelInvariantId or it is null
		try {
			RestUtil.validateURL("serviceInstanceId", "modelVersionId", "", "serviceType", "customerId");
		} catch (AuditException e) {
			assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: modelInvariantId"));
		}

		try {
			RestUtil.validateURL("serviceInstanceId", "modelVersionId", null, "serviceType", "customerId");
		} catch (AuditException e) {
			assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: modelInvariantId"));
		}

		// Missing ServiceType or it is null
		try {
			RestUtil.validateURL("serviceInstanceId", "modelVersionId", "modelInvariantId", "", "customerId");
		} catch (AuditException e) {
			assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: serviceType"));
		}

		try {
			RestUtil.validateURL("serviceInstanceId", "modelVersionId", "modelInvariantId", null, "customerId");
		} catch (AuditException e) {
			assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: serviceType"));
		}

		// Missing CustomerId or it is null
		try {
			RestUtil.validateURL("serviceInstanceId", "modelVersionId", "modelInvariantId", "serviceType", "");
		} catch (AuditException e) {
			assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: customerId"));
		}


		try {
			RestUtil.validateURL("serviceInstanceId", "modelVersionId", "modelInvariantId", "serviceType", null);
		} catch (AuditException e) {
			assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: customerId"));
		}
	}

	@Test
	public void testIsEmptyJson() {
		assertTrue(RestUtil.isEmptyJson("{}"));
		assertTrue(!RestUtil.isEmptyJson("{Not Empty}"));
	}
}
