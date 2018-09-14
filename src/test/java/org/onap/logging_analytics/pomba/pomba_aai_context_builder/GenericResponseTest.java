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
import org.onap.pomba.contextbuilder.aai.model.GenericResponse;

public class GenericResponseTest {

    @Test
    public void testGenericResponse() {
        GenericResponse response = new GenericResponse();

        response.setFailureReason("failureReason");
        response.setResponseObj(new Object());
        response.setStatus("status");

        assertEquals("failureReason", response.getFailureReason());
        assertEquals("status", response.getStatus());
        assertTrue(response.getResponseObj() != null);

    }
}
