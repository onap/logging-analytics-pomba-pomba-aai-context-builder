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
package org.onap.pomba.contextbuilder.aai.service;

import org.onap.aai.restclient.client.RestClient;
import org.onap.pomba.common.datatypes.ModelContext;
import org.onap.pomba.contextbuilder.aai.common.LogMessages;
import org.onap.pomba.contextbuilder.aai.exception.AuditException;
import org.onap.pomba.contextbuilder.aai.util.RestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpringServiceImpl implements SpringService {
    private static Logger log = LoggerFactory.getLogger(SpringServiceImpl.class);

    @Autowired
    private RestClient aaiClient;
    @Autowired
    private String aaiBaseUrl;
    @Autowired
    private String aaiServiceInstancePath;
    @Autowired
    private String aaiBasicAuthorization;
    @Autowired
    private String aaiPathToSearchNodeQuery;

    public SpringServiceImpl() {
        // needed for instantiation
    }

    @Override
    public ModelContext getContext(String serviceInstanceId, String modelVersionId, String modelInvariantId, String serviceType, String customerId, String tranId) throws AuditException {

        String url = "serviceInstanceId=" + serviceInstanceId + " modelVersion="+modelVersionId +
                " modelInvariantId="+ modelInvariantId + " serviceType="+serviceType + " customerId="+ customerId;
        log.info(LogMessages.AAI_CONTEXT_BUILDER_URL, url);

        ModelContext context = null;

        // Retrieve the service instance information from AAI
        try {
            context= RestUtil.retrieveAAIModelData(aaiClient, aaiBaseUrl, aaiPathToSearchNodeQuery, aaiServiceInstancePath, tranId, serviceInstanceId, modelVersionId, modelInvariantId, serviceType, customerId,aaiBasicAuthorization);
        } catch (AuditException ae) {
            throw ae;
        } catch (Exception e) {
            throw new AuditException(e.getLocalizedMessage());
        }
        return context;
    }

}
