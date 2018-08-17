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
package org.onap.pomba.contextbuilder.aai.service.rs;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.onap.pomba.common.datatypes.ModelContext;
import org.onap.pomba.contextbuilder.aai.common.LogMessages;
import org.onap.pomba.contextbuilder.aai.exception.AuditException;
import org.onap.pomba.contextbuilder.aai.service.SpringService;
import org.onap.pomba.contextbuilder.aai.util.RestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RestServiceImpl implements RestService {
    private static Logger log = LoggerFactory.getLogger(RestService.class);
    private static final String EMPTY_JSON_STRING = "{}";


    @Autowired
    private SpringService service;

    @Override
    public Response getContext(HttpHeaders headers, String serviceInstanceId, String modelVersionId, String modelInvariantId, String serviceType, String customerId) {

        String url = "serviceInstanceId=" + serviceInstanceId + " modelVersion="+modelVersionId +
                " modelInvariantId="+ modelInvariantId + " serviceType="+serviceType + " customerId="+ customerId;
        if(log.isDebugEnabled()) {
            log.debug(LogMessages.AAI_CONTEXT_BUILDER_URL + url);
        }


        Response response = null;
        String transactionId = null;
        ModelContext aaiContext= null;

        Gson gson = new GsonBuilder().create();

        try {
            // Do some validation on Http headers and URL parameters
            RestUtil.validateHeader(headers);
            RestUtil.validateURL(serviceInstanceId, modelVersionId, modelInvariantId, serviceType, customerId);

            // Keep the same transaction id for logging purpose
            transactionId= RestUtil.extractTranIdHeader(headers);

            aaiContext = service.getContext(serviceInstanceId, modelVersionId, modelInvariantId, serviceType, customerId, transactionId);

            if (aaiContext==null) {
                // Return empty JSON
                response = Response.ok().entity(EMPTY_JSON_STRING).build();
            }else {
                response = Response.ok().entity(gson.toJson(aaiContext)).build();
            }
        } catch (AuditException ce) {
            if (ce.getHttpStatus() !=null) {
                response = Response.status(ce.getHttpStatus()).entity(ce.getMessage()).build();
            }else {
                // No response received, could be the cases of network issue: i.e. unreachable host
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(ce.getMessage()).build();
            }
        } catch (Exception e) {
            response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

        return response;
    }

}