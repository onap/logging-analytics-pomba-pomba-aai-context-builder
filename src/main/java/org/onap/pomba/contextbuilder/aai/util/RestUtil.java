/*
 * ============LICENSE_START=================================================== Copyright (c) 2018
 * Amdocs ============================================================================ Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. ============LICENSE_END=====================================================
 */
package org.onap.pomba.contextbuilder.aai.util;



import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.onap.aai.restclient.client.OperationResult;
import org.onap.aai.restclient.client.RestClient;
import org.onap.pomba.common.datatypes.ModelContext;
import org.onap.pomba.common.datatypes.Service;
import org.onap.pomba.common.datatypes.VF;
import org.onap.pomba.common.datatypes.VFModule;
import org.onap.pomba.common.datatypes.VNFC;
import org.onap.pomba.contextbuilder.aai.common.LogMessages;
import org.onap.pomba.contextbuilder.aai.datatype.ServiceInstance;
import org.onap.pomba.contextbuilder.aai.datatype.VfModule;
import org.onap.pomba.contextbuilder.aai.datatype.VnfInstance;
import org.onap.pomba.contextbuilder.aai.datatype.VnfcInstance;
import org.onap.pomba.contextbuilder.aai.exception.AuditError;
import org.onap.pomba.contextbuilder.aai.exception.AuditException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RestUtil {

    private RestUtil() {}

    private static Logger log = LoggerFactory.getLogger(RestUtil.class);
    // Parameters for Query AAI Model Data API
    private static final String SERVICE_INSTANCE_ID = "serviceInstanceId";
    private static final String MODEL_VERSION_ID = "modelVersionId";
    private static final String MODEL_INVARIANT_ID = "modelInvariantId";
    private static final String CUSTOMER_ID = "customerId";
    private static final String SERVICE_TYPE = "serviceType";

    // HTTP headers
    private static final String TRANSACTION_ID = "X-TransactionId";
    private static final String FROM_APP_ID = "X-FromAppId";
    private static final String AUTHORIZATION = "Authorization";

    private static final String APP_NAME = "aaiCtxBuilder";

    // Service Catalog
    private static final String CATALOG_GENERIC_VNF = "generic-vnf";
    private static final String CATALOG_VNFC = "vnfc";
    private static final String CATALOG_SERVICE_INSTANCE = "service-instance";

    // Relationship Json Path
    private static final String RELATIONSHIP_LIST = "relationship-list";
    private static final String RELATIONSHIP = "relationship";
    private static final String RESULT_DATA = "result-data";

    private static final String JSON_ATT_RELATED_TO = "related-to";
    private static final String JSON_ATT_RELATED_LINK = "related-link";
    private static final String JSON_ATT_RESOURCE_TYPE = "resource-type";
    private static final String JSON_ATT_RESOURCE_LINK = "resource-link";

    private static final String EMPTY_JSON_STRING = "{}";
    private static final String DELIMITER = "$";
    private static final String DEPTH = "?depth=2";


    /**
     * Validates the URL parameter.
     *
     * @throws AuditException if there is missing parameter
     */
    public static void validateURL(String serviceInstanceId, String modelVersionId, String modelInvariantId,
            String serviceType, String customerId) throws AuditException {

        if (serviceInstanceId == null || serviceInstanceId.isEmpty()) {
            throw new AuditException(AuditError.INVALID_REQUEST_URL_MISSING_PARAMETER + SERVICE_INSTANCE_ID,
                    Status.BAD_REQUEST);
        }
        // modelVersionId
        if (modelVersionId == null || modelVersionId.isEmpty()) {
            throw new AuditException(AuditError.INVALID_REQUEST_URL_MISSING_PARAMETER + MODEL_VERSION_ID, Status.BAD_REQUEST);
        }
        // modelInvariantId
        if (modelInvariantId == null || modelInvariantId.isEmpty()) {
            throw new AuditException(AuditError.INVALID_REQUEST_URL_MISSING_PARAMETER + MODEL_INVARIANT_ID,
                    Status.BAD_REQUEST);
        }

        // serviceType
        if (serviceType == null || serviceType.isEmpty()) {
            throw new AuditException(AuditError.INVALID_REQUEST_URL_MISSING_PARAMETER + SERVICE_TYPE, Status.BAD_REQUEST);
        }
        // customerId
        if (customerId == null || customerId.isEmpty()) {
            throw new AuditException(AuditError.INVALID_REQUEST_URL_MISSING_PARAMETER + CUSTOMER_ID, Status.BAD_REQUEST);
        }
    }

    public static void validateBasicAuthorization(HttpHeaders headers, String basicAuthorization) throws AuditException {
    	String authorization = null;

        // validation on HTTP Authorization Header
        authorization = headers.getRequestHeaders().getFirst(AUTHORIZATION);
        if (authorization != null && !authorization.trim().isEmpty() && authorization.startsWith("Basic")) {
            if (!authorization.equals(basicAuthorization)) {
                throw new AuditException(AuditError.MISMATCH, Status.UNAUTHORIZED);
            };
        } else {
            throw new AuditException(AuditError.MISSING_AUTHORIZATION_HEADER, Status.UNAUTHORIZED);
        }
    }

    public static void validateHeader(HttpHeaders headers) throws AuditException {
        String fromAppId = null;
        String transactionId = null;

        fromAppId = headers.getRequestHeaders().getFirst(FROM_APP_ID);
        if ((fromAppId == null) || fromAppId.trim().isEmpty()) {
            throw new AuditException(AuditError.MISSING_HEADER_PARAMETER + FROM_APP_ID, Status.BAD_REQUEST);
        }

        transactionId = headers.getRequestHeaders().getFirst(TRANSACTION_ID);
        if ((transactionId == null) || transactionId.trim().isEmpty()) {
            transactionId = UUID.randomUUID().toString();
            log.info(LogMessages.HEADER_MESSAGE, TRANSACTION_ID, transactionId);
        }
    }

    /*
     * The purpose is to keep same transaction Id from north bound interface to south bound interface
     */
    public static String extractTranIdHeader(HttpHeaders headers) {
        String transactionId = null;
        transactionId = headers.getRequestHeaders().getFirst(TRANSACTION_ID);
        if ((transactionId == null) || transactionId.trim().isEmpty()) {
            transactionId = UUID.randomUUID().toString();
            log.info(LogMessages.HEADER_MESSAGE, TRANSACTION_ID, transactionId);
        }
        return transactionId;
    }

    /*
     * Trigger external API call to AAI to retrieve Service Instance data (i.e. genericVNF and VNFC)
     */
    public static ModelContext retrieveAAIModelData(RestClient aaiClient, String baseURL, String aaiPathToSearchNodeQuery, String aaiServiceInstancePath, 
            String transactionId, String serviceInstanceId, String modelVersionId, String modelInvariantId,
            String serviceType, String customerId, String aaiBasicAuthorization) throws AuditException {
        String serviceInstancePayload = null;
        String genericVNFPayload = null;
        String vnfcPayload = null;

        // Follow two variables for transform purpose
        List<VnfInstance> vnfLst = new ArrayList<VnfInstance>(); // List of the VNF POJO object
        Map<String, List<VnfcInstance>> vnfMap = new HashMap<String, List<VnfcInstance>>(); // MAP the vnf-id as the
                                                                                            // key, and list of the vNFC
                                                                                            // pojo object
        // Obtain resource-link based on resource-type = service-Instance
        String resourceLink = obtainResouceLinkBasedOnServiceInstanceFromAAI(aaiClient, baseURL, aaiPathToSearchNodeQuery, serviceInstanceId, transactionId, aaiBasicAuthorization);

        String url = baseURL
                + generateServiceInstanceURL(aaiServiceInstancePath, customerId, serviceType, serviceInstanceId);
        // Response from service instance API call
        serviceInstancePayload =
                getResource(aaiClient, url, aaiBasicAuthorization, transactionId, MediaType.valueOf(MediaType.APPLICATION_JSON));

        // Handle the case if the service instance is not found in AAI
        if (isEmptyJson(serviceInstancePayload)) {
            log.info(LogMessages.NOT_FOUND, "Service Instance" , serviceInstanceId);
            // Only return the empty Json on the root level. i.e service instance
            return null;
        }

        List<String> genericVNFLinkLst = extractRelatedLink(serviceInstancePayload, CATALOG_GENERIC_VNF);
        log.info(LogMessages.NUMBER_OF_API_CALLS, "genericVNF", genericVNFLinkLst.size());
        log.info(LogMessages.API_CALL_LIST, "genericVNF", printOutAPIList(genericVNFLinkLst));


        for (String genericVNFLink : genericVNFLinkLst) {
            // With latest AAI development, in order to retrieve the both generic VNF + vf_module, we can use
            // one API call but with depth=2
            String genericVNFURL = baseURL + genericVNFLink + DEPTH;
            // Response from generic VNF API call
            genericVNFPayload =
                    getResource(aaiClient, genericVNFURL, aaiBasicAuthorization, transactionId, MediaType.valueOf(MediaType.APPLICATION_JSON));

            if (isEmptyJson(genericVNFPayload)) {
                log.info(LogMessages.NOT_FOUND, "GenericVNF with url ", genericVNFLink);
            } else {

                // Logic to Create the Generic VNF Instance POJO object
                VnfInstance vnfInstance = VnfInstance.fromJson(genericVNFPayload);
                vnfLst.add(vnfInstance);

                List<String> vnfcLinkLst = extractRelatedLink(genericVNFPayload, CATALOG_VNFC);
                log.info(LogMessages.NUMBER_OF_API_CALLS, "vnfc", vnfcLinkLst.size());
                log.info(LogMessages.API_CALL_LIST, "vnfc", printOutAPIList(vnfcLinkLst));

                List<VnfcInstance> vnfcLst = new ArrayList<VnfcInstance>();
                for (String vnfcLink : vnfcLinkLst) {
                    String vnfcURL = baseURL + vnfcLink;
                    vnfcPayload = getResource(aaiClient, vnfcURL, aaiBasicAuthorization,  transactionId,
                            MediaType.valueOf(MediaType.APPLICATION_XML));

                    if (isEmptyJson(vnfcPayload)) {
                        log.info(LogMessages.NOT_FOUND, "VNFC with url", vnfcLink);
                    } else {
                        // Logic to Create the VNFC POJO object
                        VnfcInstance vnfcInstance = VnfcInstance.fromJson(vnfcPayload);
                        vnfcLst.add(vnfcInstance);
                    }
                }

                // Assume the vnf-id is unique as a key
                vnfMap.put(vnfInstance.getVnfId(), vnfcLst);
            }
        }
        // Transform to common model and return
        return transform(ServiceInstance.fromJson(serviceInstancePayload), vnfLst, vnfMap);
    }


    /*
     * Transform AAI Representation to Common Model
     */
    private static ModelContext transform(ServiceInstance svcInstance, List<VnfInstance> vnfLst,
            Map<String, List<VnfcInstance>> vnfMap) {
        ModelContext context = new ModelContext();
        Service service = new Service();
        service.setInvariantUuid(svcInstance.getModelInvariantId());
        service.setName(svcInstance.getServiceInstanceName());
        service.setUuid(svcInstance.getModelVersionId());

        List<VF> vfLst = new ArrayList<VF>();

        for (VnfInstance vnf : vnfLst) {
            VF vf = new VF();
            vf.setInvariantUuid(vnf.getModelInvariantId());
            vf.setName(vnf.getVnfName());
            vf.setUuid(vnf.getModelVersionId());
            vf.setType(vnf.getVnfType());
            vf.setNfNamingCode(vnf.getNfNamingCode());

            String key = vnf.getVnfId();
            List<VNFC> vnfcLst = new ArrayList<VNFC>();

            for (Map.Entry<String, List<VnfcInstance>> entry : vnfMap.entrySet()) {

                if (key.equals(entry.getKey())) {
                    List<VnfcInstance> vnfcInstanceLst = entry.getValue();

                    for (VnfcInstance vnfc : vnfcInstanceLst) {
                        VNFC vnfcModel = new VNFC();
                        vnfcModel.setInvariantUuid(vnfc.getModelInvariantId());
                        vnfcModel.setName(vnfc.getVnfcName());
                        vnfcModel.setNfcNamingCode(vnfc.getNfcNamingCode());
                        vnfcModel.setUuid(vnfc.getModelVersionId());
                        vnfcLst.add(vnfcModel);
                    }
                }
            }

            vf.setVnfc(vnfcLst);
            // Add the vfModule
            List<VFModule> vfModuleLst = new ArrayList<VFModule>();

            if (vnf.getVfModules() != null) {
                ConcurrentMap<String, AtomicInteger> vnfModulemap =
                        buildMaxInstanceMap(vnf.getVfModules().getVfModule());

                for (Map.Entry<String, AtomicInteger> entry : vnfModulemap.entrySet()) {

                    String[] s = entry.getKey().split("\\" + DELIMITER);

                    String modelVersionId = s[0];
                    String modelInvariantId = s[1];
                    VFModule vfModule = new VFModule();
                    vfModule.setUuid(modelVersionId);
                    vfModule.setInvariantUuid(modelInvariantId);
                    vfModule.setMaxInstances(entry.getValue().intValue());
                    vfModuleLst.add(vfModule);
                }
            }
            vf.setVfModules(vfModuleLst);

            vfLst.add(vf);

        } // done the vnfInstance

        context.setService(service);
        context.setVf(vfLst);

        return context;
    }



    /*
     * Build the map with key (model_version_id and model_invariant_id), and with the max occurrences of
     * the value in the map
     *
     * @param vfModuleList
     *
     * @return
     */
    private static ConcurrentMap<String, AtomicInteger> buildMaxInstanceMap(List<VfModule> vfModuleList) {

        ConcurrentMap<String, AtomicInteger> map = new ConcurrentHashMap<>();

        for (VfModule vfModule : vfModuleList) {
            // group the key by model_version_id and model_invariant_id
            String key = new StringBuilder().append(vfModule.getModelVersionId()).append(DELIMITER)
                    .append(vfModule.getModelInvariantId()).toString();

            if (key.length() > 0) {
                map.putIfAbsent(key, new AtomicInteger(0));
                map.get(key).incrementAndGet();
            }

        }

        return map;

    }



    public static boolean isEmptyJson(String serviceInstancePayload) {

        return (serviceInstancePayload.equals(EMPTY_JSON_STRING));
    }


    private static String printOutAPIList(List<String> relatedLinkLst) {
        StringBuilder builder = new StringBuilder();

        // Loop and append values.
        for (String s : relatedLinkLst) {
            builder.append(s).append("\n");
        }
        return builder.toString();

    }


    /*
     * Extract the related-Link from Json payload. For example
     * {
     *      "related-to": "vnfc",
     *      "related-link": "/aai/v11/network/vnfcs/vnfc/zrdm5aepdg01vmg003",
     *      "relationship-data": [
     *          { "relationship-key": "vnfc.vnfc-name",
     *          "relationship-value": "zrdm5aepdg01vmg003" }
     *       ]
     * },
     */
    private static List<String> extractRelatedLink(String payload, String catalog) throws AuditException {
        JSONObject jsonPayload = new JSONObject(payload);
        JSONArray relationships = null;
        List<String> relatedLinkList = new ArrayList<String>();
        log.info("Fetching the vnfc related link");

        try {
            JSONObject relationshipList = jsonPayload.getJSONObject(RELATIONSHIP_LIST);
            if (relationshipList != null) {
                relationships = relationshipList.getJSONArray(RELATIONSHIP);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AuditException(AuditError.JSON_READER_PARSE_ERROR + " " + e.getMessage());
        }

        if (relationships != null && relationships.length() > 0) {
            for (int i = 0; i < relationships.length(); i++) {
                Object relatedToObj = null;
                Object relatedLinkObj = null;

                JSONObject obj = relationships.optJSONObject(i);
                relatedToObj = obj.get(JSON_ATT_RELATED_TO);

                if (relatedToObj.toString().equals(catalog)) {
                    relatedLinkObj = obj.get(JSON_ATT_RELATED_LINK);
                    if (relatedLinkObj != null) {
                        relatedLinkList.add(relatedLinkObj.toString());
                    }
                }
            }
        }

        log.info(LogMessages.NUMBER_OF_RELATIONSHIPS_FOUND, catalog, relatedLinkList.size());

        return relatedLinkList;
    }



    @SuppressWarnings("unchecked")
    private static Map<String, List<String>> buildHeaders(String aaiBasicAuthorization, String transactionId) {
        MultivaluedMap<String, String> headers = new MultivaluedMapImpl();
        headers.put(TRANSACTION_ID, Collections.singletonList(transactionId));
        headers.put(FROM_APP_ID, Collections.singletonList(APP_NAME));
        headers.put(AUTHORIZATION, Collections.singletonList(aaiBasicAuthorization));
        return headers;
    }


    private static String getResource(RestClient client, String url, String aaiBasicAuthorization, String transId, MediaType mediaType)
            throws AuditException {
        OperationResult result = client.get(url, buildHeaders(aaiBasicAuthorization, transId), MediaType.valueOf(MediaType.APPLICATION_JSON));

        if (result.getResultCode() == 200) {
            return result.getResult();
        } else if (result.getResultCode() == 404) {
            // Resource not found, generate empty JSON format
            log.info(LogMessages.RESOURCE_NOT_FOUND, url, "return empty Json format");
            return new JSONObject().toString();

        } else {
            throw new AuditException(AuditError.INTERNAL_SERVER_ERROR + " with " + result.getFailureCause());
        }
    }


    private static String generateServiceInstanceURL(String siPath, String customerId, String serviceType,
            String serviceInstanceId) {
        return MessageFormat.format(siPath, customerId, serviceType, serviceInstanceId);
    }

    public static String obtainResouceLinkBasedOnServiceInstanceFromAAI(RestClient aaiClient, String baseURL, String aaiPathToSearchNodeQuery, String serviceInstanceId,
            String transactionId, String aaiBasicAuthorization) throws AuditException {

        String url = generateGetCustomerInfoUrl(baseURL, aaiPathToSearchNodeQuery, serviceInstanceId);
        String customerInfoString  = getResource(aaiClient, url, aaiBasicAuthorization, transactionId, MediaType.valueOf(MediaType.APPLICATION_JSON));

        // Handle the case if the service instance is not found in AAI
        if (isEmptyJson(customerInfoString)) {
            log.info(LogMessages.NOT_FOUND, "Service Instance" , serviceInstanceId);
            // Only return the empty Json on the root level. i.e service instance
            return null;
        }

        return extractResourceLinkBasedOnResourceType(customerInfoString, CATALOG_SERVICE_INSTANCE);
    }

    private static String generateGetCustomerInfoUrl (String baseURL, String aaiPathToSearchNodeQuery ,String serviceInstanceId) {
        return baseURL + aaiPathToSearchNodeQuery + serviceInstanceId;
    }

    /*
     * Extract the resource-Link from Json payload. For example
     * {
     *     "result-data": [
     *         {
     *             "resource-type": "service-instance",
     *             "resource-link": "/aai/v11/business/customers/customer/DemoCust_651800ed-2a3c-45f5-b920-85c1ed155fc2/service-subscriptions/service-subscription/vFW/service-instances/service-instance/adc3cc2a-c73e-414f-8ddb-367de81300cb"
     *         }
     *     ]
     * }
     */
    private static String extractResourceLinkBasedOnResourceType(String payload, String catalog) throws AuditException {
        String resourceLinkList = null;
        log.info("Fetching the resource-link based on resource-type=" + catalog);

        try {
            JSONArray result_data_list = new JSONObject(payload).getJSONArray(RESULT_DATA);
            if (result_data_list != null) {
                for (int i = 0; i < result_data_list.length(); i++) {
                    JSONObject obj = result_data_list.optJSONObject(i);
                    if (obj.has(JSON_ATT_RESOURCE_TYPE) && (obj.getString(JSON_ATT_RESOURCE_TYPE).equals(catalog) ))  {
                        resourceLinkList = obj.getString(JSON_ATT_RESOURCE_LINK);
                        log.info(resourceLinkList);
                        return resourceLinkList;
                    }
                }
            }
        } catch (JSONException e) {
            log.error(e.getMessage());
            throw new AuditException(AuditError.JSON_READER_PARSE_ERROR + " " + e.getMessage());
        }

        log.error("resource-link CANNOT be found: ", payload );

        return resourceLinkList;
    }
}
