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
package org.onap.pomba.contextbuilder.aai.common;

public class LogMessages {

    private LogMessages() {}

    public static final String AAI_CONTEXT_BUILDER_URL = "AAI Context Builder URL ";
    public static final String HEADER_MESSAGE = "Header {} not present in request, generating new value: {}";
    public static final String NOT_FOUND = "{} {} is not found from AAI";
    public static final String NUMBER_OF_API_CALLS = "The number of API calls for {} is {}";
    public static final String API_CALL_LIST = "API call list for {} is: \n {}";
    public static final String NUMBER_OF_RELATIONSHIPS_FOUND = "Number of relationships found for: {} are: {}";
    public static final String RESOURCE_NOT_FOUND = "Resource for {} is not found: {}";

}
