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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.onap.pomba.contextbuilder.aai.model.GenericResponse;

@Api
@Path("{version: v2}/service")
@Produces({MediaType.APPLICATION_JSON})
public interface RestService {

    @GET
    @Path("/context")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(
            value = "Respond AAIContext Model Data",
            notes = "Returns a JSON object which represents the AAIConetxt model data",
            response = GenericResponse.class
            )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 400, message = "Bad Request"),
                    @ApiResponse(code = 404, message = "Service not available"),
                    @ApiResponse(code = 500, message = "Unexpected Runtime error")
            })
    public Response getContext(@Context HttpHeaders headers,
            @Context HttpServletRequest req,
            @HeaderParam(HttpHeaders.AUTHORIZATION) @ApiParam(hidden=true) String authorization,
            @HeaderParam(org.onap.pomba.contextbuilder.aai.util.RestUtil.FROM_APP_ID) @ApiParam(required=true) String xPartnerName,
            @HeaderParam(org.onap.pomba.contextbuilder.aai.util.RestUtil.TRANSACTION_ID) String xRequestId,
            @QueryParam("serviceInstanceId") @ApiParam(required=true) String serviceInstanceId
            );
}