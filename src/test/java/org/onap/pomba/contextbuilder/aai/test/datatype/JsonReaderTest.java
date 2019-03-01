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

import static org.junit.Assert.fail;

import org.junit.Test;
import org.onap.pomba.contextbuilder.aai.exception.AuditException;
import org.onap.pomba.contextbuilder.aai.reader.JsonReader;

public class JsonReaderTest {

    @Test
    public void testJsonReader() {
        JsonReader reader = new JsonReader();
        try {
            reader.parse("{test:test}");
        } catch (AuditException e) {
            fail("This should not throw an exception.");
        }
    }

    @Test
    public void testJsonReaderWithException() {
        JsonReader reader = new JsonReader();
        try {
            reader.parse("{test}");
            fail("This should throw an exception.");
        } catch (AuditException e) {
            //Just a test, no need to log the exception here.
        }
    }
}
