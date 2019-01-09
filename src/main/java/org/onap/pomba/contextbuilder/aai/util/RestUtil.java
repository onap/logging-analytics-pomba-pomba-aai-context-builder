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
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.onap.aai.restclient.client.OperationResult;
import org.onap.aai.restclient.client.RestClient;
import org.onap.pomba.common.datatypes.Attribute;
import org.onap.pomba.common.datatypes.DataQuality;
import org.onap.pomba.common.datatypes.ModelContext;
import org.onap.pomba.common.datatypes.Service;
import org.onap.pomba.common.datatypes.VNF;
import org.onap.pomba.common.datatypes.VFModule;
import org.onap.pomba.common.datatypes.VM;
import org.onap.pomba.common.datatypes.VNFC;
import org.onap.pomba.contextbuilder.aai.common.LogMessages;
import org.onap.pomba.contextbuilder.aai.datatype.Relationship;
import org.onap.pomba.contextbuilder.aai.datatype.RelationshipList;
import org.onap.pomba.contextbuilder.aai.datatype.ServiceInstance;
import org.onap.pomba.contextbuilder.aai.datatype.VfModule;
import org.onap.pomba.contextbuilder.aai.datatype.VnfInstance;
import org.onap.pomba.contextbuilder.aai.datatype.VnfcInstance;
import org.onap.pomba.contextbuilder.aai.datatype.Vserver;
import org.onap.pomba.contextbuilder.aai.exception.AuditError;
import org.onap.pomba.contextbuilder.aai.exception.AuditException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.onap.pomba.contextbuilder.aai.datatype.PnfInstance;
import org.onap.pomba.common.datatypes.PNF;
import com.bazaarvoice.jolt.JsonUtils;

public class RestUtil {

    private RestUtil() {}

    private static Logger log = LoggerFactory.getLogger(RestUtil.class);
    // Parameters for Query AAI Model Data API
    private static final String SERVICE_INSTANCE_ID = "serviceInstanceId";

    // HTTP headers
    private static final String TRANSACTION_ID = "X-TransactionId";
    private static final String FROM_APP_ID = "X-FromAppId";
    private static final String AUTHORIZATION = "Authorization";

    private static final String APP_NAME = "aaiCtxBuilder";

    // Service Catalog -  "related-to"
    private static final String CATALOG_GENERIC_VNF = "generic-vnf";
    private static final String CATALOG_VNFC = "vnfc";
    private static final String CATALOG_SERVICE_INSTANCE = "service-instance";
    private static final String CATALOG_VSERVER = "vserver";
    private static final String CATALOG_IMAGE = "image";
    private static final String CATALOG_PSERVER = "pserver";
    private static final String VF_MODULES = "vf-modules";
    private static final String VF_MODULE = "vf-module";

    private static final String CATALOG_PNF = "pnf";

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

    //Attribute Name
    private static final String LOCKEDBOOLEAN = "lockedBoolean";
    private static final String HOSTNAME = "hostName";
    private static final String IMAGEID = "imageId";

    //Attribute Names for PNF
    private static final String PNF_NETWORK_FUNCTION = "networkFunction";
    private static final String PNF_NETWORK_ROLE = "networkRole";
    private static final String PNF_RESOURCE_VERSION = "resourceVersion";
    private static final String PNF_NAME2 = "name2";
    private static final String PNF_NAME2_SOURCE = "name2Source";
    private static final String PNF_EQUIPMENT_TYPE = "equipType";
    private static final String PNF_EQUIPMENT_VENDOR = "equipVendor";
    private static final String PNF_EQUIPMENT_MODEL = "equipModel";
    private static final String PNF_MANAGEMENT_OPTIONS = "managementOptions";
    private static final String PNF_SW_VERSION = "swVersion";
    private static final String PNF_FRAME_ID = "frameId";
    private static final String PNF_SERIAL_NUMBER = "serialNumber";

    /**
     * Validates the URL parameter.
     *
     * @throws AuditException if there is missing parameter
     */
    public static void validateURL(String serviceInstanceId)
            throws AuditException {

        if (serviceInstanceId == null || serviceInstanceId.isEmpty()) {
            throw new AuditException(AuditError.INVALID_REQUEST_URL_MISSING_PARAMETER + SERVICE_INSTANCE_ID,
                    Status.BAD_REQUEST);
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
     * Trigger external API call to AAI to collect the data in order to transform to common model
     *
     */
    public static ModelContext retrieveAAIModelData(RestClient aaiClient, String baseURL, String aaiPathToSearchNodeQuery,
            String transactionId, String serviceInstanceId, String aaiBasicAuthorization) throws AuditException {
        String serviceInstancePayload = null;
        String genericVNFPayload = null;

        List<VnfInstance> vnfLst = new ArrayList<VnfInstance>(); // List of the VNF POJO object
        //Map to track multiple vnfc under the Gerneric VNF id. The key = vnf-id. The value = list of vnfc instance
        Map<String, List<VnfcInstance>> vnfcMap = new HashMap<String, List<VnfcInstance>>();

        //Map to track the relationship between vnf->vfmodule->verver
        Map<String, Map<String, List<Vserver>>> vnf_vfmodule_vserver_Map = new HashMap<String, Map<String, List<Vserver>>>();

        // Obtain resource-link based on resource-type = service-Instance
        String resourceLink = obtainResouceLinkBasedOnServiceInstanceFromAAI(aaiClient, baseURL, aaiPathToSearchNodeQuery, serviceInstanceId, transactionId, aaiBasicAuthorization);

        // Handle the case if the service instance is not found in AAI
        if (resourceLink==null) {
            // return the empty Json on the root level. i.e service instance
            return null;
        }

        log.info("ResourceLink from AAI:" + resourceLink);

        // Build URl to get ServiceInstance Payload
        String url = baseURL + resourceLink;

        // Response from service instance API call
        serviceInstancePayload =
                getResource(aaiClient, url, aaiBasicAuthorization, transactionId, MediaType.valueOf(MediaType.APPLICATION_JSON));

        // Handle the case if the service instance is not found in AAI
        if (isEmptyJson(serviceInstancePayload)) {
            log.info(LogMessages.NOT_FOUND, "Service Instance" , serviceInstanceId);
            // Only return the empty Json on the root level. i.e service instance
            return null;
        }
        log.info("Message from AAI:" + JsonUtils.toPrettyJsonString(JsonUtils.jsonToObject(serviceInstancePayload)) );

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
                log.info("Message from AAI for VNF " + genericVNFURL + ",message body:" + JsonUtils.toPrettyJsonString(JsonUtils.jsonToObject(genericVNFPayload)) );

                // Logic to Create the Generic VNF Instance POJO object
                VnfInstance vnfInstance = VnfInstance.fromJson(genericVNFPayload);
                vnfLst.add(vnfInstance);

                // Build the vnf_vnfc relationship map
                vnfcMap = buildVnfcMap(genericVNFPayload, aaiClient, baseURL, transactionId, aaiBasicAuthorization );

                // Build vnf_vfmodule_vserver relationship map
                vnf_vfmodule_vserver_Map= buildVfmoduleVserverMap(genericVNFPayload, aaiClient, baseURL, transactionId, aaiBasicAuthorization);

            }
        }

        //Obtain PNF (Physical Network Function)
        List<String> genericPNFLinkLst = extractRelatedLink(serviceInstancePayload, CATALOG_PNF);
        log.info(LogMessages.NUMBER_OF_API_CALLS, "PNF", genericPNFLinkLst.size());
        log.info(LogMessages.API_CALL_LIST, "PNF", printOutAPIList(genericPNFLinkLst));

        String genericPNFPayload = null;
        List<PnfInstance> pnfLst = new ArrayList<PnfInstance>(); // List of the PNF POJO object

        for (String genericPNFLink : genericPNFLinkLst) {
            // With latest AAI development, in order to retrieve the both generic PNF
            String genericPNFURL = baseURL + genericPNFLink;
            // Response from generic PNF API call
            genericPNFPayload =
                    getResource(aaiClient, genericPNFURL, aaiBasicAuthorization, transactionId, MediaType.valueOf(MediaType.APPLICATION_JSON));

            if (isEmptyJson(genericPNFPayload)) {
                log.info(LogMessages.NOT_FOUND, "GenericPNF with url ", genericPNFLink);
            } else {
                log.info("Message from AAI for PNF " + genericPNFLink + ",message body:" + JsonUtils.toPrettyJsonString(JsonUtils.jsonToObject(genericPNFPayload)) );

                // Logic to Create the Generic VNF Instance POJO object
                PnfInstance pnfInstance = PnfInstance.fromJson(genericPNFPayload);
                pnfLst.add(pnfInstance);
            }
        }

        // Transform to common model and return
        return transform(ServiceInstance.fromJson(serviceInstancePayload), vnfLst, vnfcMap, vnf_vfmodule_vserver_Map, pnfLst);
    }

    /*
     *  The map is to track the relationship of vnf-id with multiple vnfc relationship
     */
    private static Map<String, List<VnfcInstance>> buildVnfcMap(String genericVNFPayload, RestClient aaiClient, String baseURL,
            String transactionId, String aaiBasicAuthorization) throws AuditException {

        String vnfcPayload = null;
        Map<String, List<VnfcInstance>> vnfcMap = new HashMap<String, List<VnfcInstance>>();

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
                log.info("Message from AAI for VNFC with url " + vnfcLink + ",message body:" + JsonUtils.toPrettyJsonString(JsonUtils.jsonToObject(vnfcPayload)) );
                // Logic to Create the VNFC POJO object
                VnfcInstance vnfcInstance = VnfcInstance.fromJson(vnfcPayload);
                vnfcLst.add(vnfcInstance);
            }
        }
        // Assume the vnf-id is unique as a key
        vnfcMap.put(getVnfId(genericVNFPayload), vnfcLst);

        return vnfcMap;
    }

    /*
     * This is a two layer map to track the relationship between  vnf->vfmodule->verver
     *
     * The Map<String, List<Vserver>  is to track multiple vserver under the vfModule.
     *              Key: combination of the model_version_id and model_invariant_id.
     *              Value:  list of Vserver
     *
     * The Map<String, Map<String, List<Vserver>>> key: vnf-id
     */
    private static Map<String, Map<String, List<Vserver>>> buildVfmoduleVserverMap(String genericVNFPayload, RestClient aaiClient, String baseURL, String transactionId, String aaiBasicAuthorization) throws AuditException {

        Map<String, Map<String, List<Vserver>>> vnf_vfmodule_vserver_Map = new HashMap<String, Map<String, List<Vserver>>>();

        Map<String, List<Vserver>> vServerMap = new HashMap<String, List<Vserver>>();

        Map<String, List<String>> vServerRelatedLinkMap = extractRelatedLinkFromVfmodule(genericVNFPayload, CATALOG_VSERVER);
        String vnfId= getVnfId(genericVNFPayload);
        String vserverPayload = null;


        if (vServerRelatedLinkMap !=null && vServerRelatedLinkMap.size() >0) {
            for(Map.Entry<String, List<String>>  entry : vServerRelatedLinkMap.entrySet()) {

                List<String>   vserverLinkLst = entry.getValue();
                log.info(LogMessages.NUMBER_OF_API_CALLS, "vserver", vserverLinkLst.size());
                log.info(LogMessages.API_CALL_LIST, "vserver", printOutAPIList(vserverLinkLst));

                List<Vserver> vserverLst = new ArrayList<Vserver>();
                for (String vserverLink : vserverLinkLst) {
                    String vserverURL = baseURL + vserverLink;
                    vserverPayload = getResource(aaiClient, vserverURL, aaiBasicAuthorization,  transactionId,
                            MediaType.valueOf(MediaType.APPLICATION_XML));

                    if (isEmptyJson(vserverPayload)) {
                        log.info(LogMessages.NOT_FOUND, "VSERVER with url", vserverURL);
                    } else {
                        // Logic to Create the Vserver POJO object
                        Vserver vserver = Vserver.fromJson(vserverPayload);
                        vserverLst.add(vserver);
                    }
                }

                vServerMap.put(entry.getKey(), vserverLst);
            }
        }
        vnf_vfmodule_vserver_Map.put(vnfId, vServerMap);

        return vnf_vfmodule_vserver_Map;
    }


    private static String getVnfId(String genericVNFPayload) throws AuditException {

        VnfInstance vnfInstance = VnfInstance.fromJson(genericVNFPayload);
        return vnfInstance.getVnfId();
    }

    /*
     * Transform AAI Representation to Common Model
     */
    private static ModelContext transform(ServiceInstance svcInstance, List<VnfInstance> vnfLst,
            Map<String, List<VnfcInstance>> vnfcMap,  Map<String, Map<String, List<Vserver>>> vnf_vfmodule_vserver_Map, List<PnfInstance> pnfLst_fromAAi) {
        ModelContext context = new ModelContext();
        Service service = new Service();
        service.setModelInvariantUUID(svcInstance.getModelInvariantId());
        service.setName(svcInstance.getServiceInstanceName());
        service.setUuid(svcInstance.getServiceInstanceId());
        service.setDataQuality(DataQuality.ok());
        List<VNF> vfLst = new ArrayList<VNF>();

        for (VnfInstance vnf : vnfLst) {
            VNF vf = new VNF();
            vf.setModelInvariantUUID(vnf.getModelInvariantId());
            vf.setName(vnf.getVnfName());
            vf.setUuid(vnf.getModelVersionId());
            vf.setType(vnf.getVnfType());
            vf.setDataQuality(DataQuality.ok());

            String key = vnf.getVnfId();   // generic vnf-id (top level of the key)

            // ---------------- Handle VNFC data
            List<VNFC> vnfcLst = new ArrayList<VNFC>();
            for (Map.Entry<String, List<VnfcInstance>> entry : vnfcMap.entrySet()) {

                if (key.equals(entry.getKey())) {
                    List<VnfcInstance> vnfcInstanceLst = entry.getValue();

                    for (VnfcInstance vnfc : vnfcInstanceLst) {
                        VNFC vnfcModel = new VNFC();
                        vnfcModel.setModelInvariantUUID(vnfc.getModelInvariantId());
                        vnfcModel.setName(vnfc.getVnfcName());
                        vnfcModel.setUuid(vnfc.getModelVersionId());
                        vnfcLst.add(vnfcModel);
                    }
                }
            }
            vf.setVnfcs(vnfcLst);


            // --------------- Handle the vfModule
            List<VFModule> vfModuleLst = new ArrayList<VFModule>();
            //Map to calculate the Vf Module MaxInstance.
            if (vnf.getVfModules() != null) {
                ConcurrentMap<String, AtomicInteger> maxInstanceMap =
                        buildMaxInstanceMap(vnf.getVfModules().getVfModule());

                for ( Map.Entry<String, Map<String, List<Vserver>>> entry:    vnf_vfmodule_vserver_Map.entrySet() ) {
                    // find the vnf-id
                    if (key.equals(entry.getKey())) {

                        Map<String, List<Vserver>> vfmodule_vserver_map= entry.getValue();

                        for ( Map.Entry<String, List<Vserver>> vfmoduleEntry:  vfmodule_vserver_map.entrySet() ){
                            // The key is modelversionId$modelInvariantid
                            String[] s = vfmoduleEntry.getKey().split("\\" + DELIMITER);
                            String modelVersionId = s[0];
                            String modelInvariantId = s[1];

                            VFModule vfModule = new VFModule();
                            vfModule.setUuid(modelVersionId);
                            vfModule.setModelInvariantUUID(modelInvariantId);
                            vfModule.setMaxInstances(getMaxInstance(vfmoduleEntry.getKey(), maxInstanceMap));
                            vfModule.setDataQuality(DataQuality.ok());

                            List<Vserver>  vserverList = vfmoduleEntry.getValue();

                            // Handle VM
                            List<VM>   vmList = new ArrayList<VM>();
                            for (Vserver vserver: vserverList) {

                                List<Attribute>  attributeList = new ArrayList<Attribute>();

                                // Iterate through the ENUM Attribute list
                                for (Attribute.Name  name: Attribute.Name.values()) {
                                    if (name.toString().equals(LOCKEDBOOLEAN)) {
                                        Attribute att = new Attribute();
                                        att.setDataQuality(DataQuality.ok());
                                        att.setName(Attribute.Name.lockedBoolean);
                                        att.setValue(String.valueOf(vserver.getInMaint()));
                                        attributeList.add(att);
                                    }

                                    if (name.toString().equals(HOSTNAME)) {
                                        Attribute att = new Attribute();
                                        att.setDataQuality(DataQuality.ok());
                                        att.setName(Attribute.Name.hostName);
                                        att.setValue(getVserverAttribute(vserver, CATALOG_PSERVER));
                                        attributeList.add(att);
                                    }

                                    if (name.toString().equals(IMAGEID)) {
                                        Attribute att = new Attribute();
                                        att.setDataQuality(DataQuality.ok());
                                        att.setName(Attribute.Name.imageId);
                                        att.setValue(getVserverAttribute(vserver, CATALOG_IMAGE));
                                        attributeList.add(att);
                                    }
                                }
                                VM vm = new VM();
                                vm.setUuid(vserver.getVserverId());
                                vm.setName(vserver.getVserverName());
                                vm.setAttributes(attributeList);
                                vmList.add(vm);
                            }
                            vfModule.setVms(vmList);
                            vfModuleLst.add(vfModule);
                        }

                    }
                }
            }  // done the vfmodule

            vf.setVfModules(vfModuleLst);
            vfLst.add(vf);


        } // done the vnfInstance

        context.setService(service);
        context.setVnfs(vfLst);
        //Add PNF info
        context.setPnfs(transformPNF(pnfLst_fromAAi));

        return context;
    }

    /*
     * Transform AAI Representation to Common Model
     */
    public static List<PNF> transformPNF(List<PnfInstance> pnfLst_from_AAI) {
        if (pnfLst_from_AAI.isEmpty()) {
            log.info(LogMessages.API_CALL_LIST, "Nill PNF list");
           return null;
        }
        List<PNF> pnfLst = new ArrayList<PNF>();

        for (PnfInstance pnf_from_aai : pnfLst_from_AAI) {
            PNF pnf = new PNF();
            pnf.setModelInvariantUUID(pnf_from_aai.getPnfId());
            pnf.setName(pnf_from_aai.getPnfName());
            pnf.setModelVersionID(pnf_from_aai.getModelVersionId());
            pnf.setModelInvariantUUID(pnf_from_aai.getModelInvariantId());
            pnf.setDataQuality(DataQuality.ok());
            List<Attribute>  attributeList = new ArrayList<Attribute>();
            pnf.setAttributes(attributeList);

            // Iterate through the ENUM Attribute list
            for (Attribute.Name  name: Attribute.Name.values()) {
                if (name.toString().equals(PNF_NETWORK_FUNCTION )) {
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.networkFunction);
                    att.setValue(String.valueOf( pnf_from_aai.getNfFunction()));
                    attributeList.add(att);
                }

                if (name.toString().equals(PNF_NETWORK_ROLE )) {
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.networkRole);
                    att.setValue(String.valueOf( pnf_from_aai.getNfRole()));
                    attributeList.add(att);
                }

                if (name.toString().equals(PNF_RESOURCE_VERSION)) {
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.resourceVersion);
                    att.setValue(String.valueOf( pnf_from_aai.getResourceVersion()));
                    attributeList.add(att);
                }

                if (name.toString().equals(PNF_NAME2)) {
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.name2);
                    att.setValue(String.valueOf( pnf_from_aai.getPnfName2()));
                    attributeList.add(att);
                }

                if (name.toString().equals(PNF_NAME2_SOURCE )) {
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.name2Source);
                    att.setValue(String.valueOf( pnf_from_aai.getPnfName2Source()));
                    attributeList.add(att);
                }

                if (name.toString().equals(PNF_EQUIPMENT_TYPE )) {
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.equipType);
                    att.setValue(String.valueOf( pnf_from_aai.getEquipmentType()));
                    attributeList.add(att);
                }

                if (name.toString().equals(PNF_EQUIPMENT_VENDOR )) {
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.equipVendor);
                    att.setValue(String.valueOf( pnf_from_aai.getEquipmentVendor()));
                    attributeList.add(att);
                }

                if (name.toString().equals(PNF_EQUIPMENT_MODEL)) {
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.equipModel);
                    att.setValue(String.valueOf( pnf_from_aai.getEquipmentModel()));
                    attributeList.add(att);
                }

                if (name.toString().equals(PNF_MANAGEMENT_OPTIONS)) {
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.managementOptions);
                    att.setValue(String.valueOf( pnf_from_aai.getManagementOptions()));
                    attributeList.add(att);
                }

                if (name.toString().equals(PNF_SW_VERSION)) {
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.swVersion);
                    att.setValue(String.valueOf( pnf_from_aai.getManagementOptions()));
                    attributeList.add(att);
                }

                if (name.toString().equals(PNF_FRAME_ID)) {
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.frameId);
                    att.setValue(String.valueOf( pnf_from_aai.getFrameId()));
                    attributeList.add(att);
                }

                if (name.toString().equals(PNF_SERIAL_NUMBER)) {
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.serialNumber);
                    att.setValue(String.valueOf( pnf_from_aai.getSerialNumber()));
                    attributeList.add(att);
                }

            }

            pnf.setAttributes(attributeList);
            pnfLst.add(pnf);

        } // done the vnfInstance


        return pnfLst;
    }


    /*
     * Return the Vserver Attribute value by looking through the relationship. i.e. if "related-to" is "pserver", we will get
     * the value of the attribute "hostname" from the last substring of the "related-link"
     * {
     *    "related-to": "pserver",
     *    "related-link": "/aai/v11/cloud-infrastructure/pservers/pserver/rdm5r10c008.rdm5a.cci.att.com",
     *    "relationship-data": [         {
     *       "relationship-key": "pserver.hostname",
     *       "relationship-value": "rdm5r10c008.rdm5a.cci.att.com"
     *    }],
     *    "related-to-property": [{"property-key": "pserver.pserver-name2"}]
     * },
     *
     */
    private static String getVserverAttribute(Vserver vserver, String key) {
        RelationshipList lst = vserver.getRelationshipList();
        if (lst != null) {
            List<Relationship> relations =  lst.getRelationship();
            for (Relationship re: relations)  {
                if (re.getRelatedTo().equals(key)) {
                    return extractAttValue(re.getRelatedLink());
                }
            }
        }
        return null;
    }



    /*
     * Get the last substring from the related-link
     * For example the value of the attribute "hostname" will be "rdm5r10c008.rdm5a.cci.att.com"
     *    "related-to": "pserver",
     *    "related-link": "/aai/v11/cloud-infrastructure/pservers/pserver/rdm5r10c008.rdm5a.cci.att.com",
     */
    private static String extractAttValue(String relatedLink) {
        return relatedLink.substring(relatedLink.lastIndexOf("/")+1);
    }

    /*
     * Build the map with key (model_version_id and model_invariant_id), and with the max occurrences of
     * the value in the map
     *
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

    /*
     * Return the occurrence of the VfModule complex key: (model-version-id)+(model-invariant-id)
     */
    private static int getMaxInstance(String key, ConcurrentMap<String, AtomicInteger> maxInstanceMap) {

        for (Map.Entry<String, AtomicInteger> entry : maxInstanceMap.entrySet()) {

            if (entry.getKey().equals(key)) {
                return entry.getValue().intValue();
            }

        }
        return 0;
    }


    public static boolean isEmptyJson(String serviceInstancePayload) {

        return (serviceInstancePayload.equals(EMPTY_JSON_STRING));
    }

    /*
     * Debug purpose - Display the related-link list
     */
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
    private static List<String> extractRelatedLink(String payload, String catalog)  {
        JSONObject jsonPayload = new JSONObject(payload);
        JSONArray relationships = null;
        List<String> relatedLinkList = new ArrayList<String>();
        log.debug("Fetching the related link");

        try {
            JSONObject relationshipList = jsonPayload.getJSONObject(RELATIONSHIP_LIST);
            if (relationshipList != null) {
                relationships = relationshipList.getJSONArray(RELATIONSHIP);
            }
        } catch (JSONException e) {
            log.error(e.getMessage());
            // Return empty map if the json payload missing relationship-list or relationship
            return relatedLinkList;
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

    /*
     * Return the Map with key:   Vfmodule->model-version-id + Vfmoduel->model-invariant-id
     *                with value: list of the related-link based on the catalog
     * The catalog can be "vserver" or "l3-network" based on common model requirement.
     */
    private static Map<String, List<String>> extractRelatedLinkFromVfmodule(String payload, String catalog) {

        Map<String, List<String>> vServerRelatedLinkMap = new HashMap<String, List<String>>();

        JSONObject jsonPayload = new JSONObject(payload);
        JSONArray vfmoduleArray = null;


        try {
            log.debug("Fetching the Vf-module");
            JSONObject vfmodules = jsonPayload.getJSONObject(VF_MODULES);
            if (vfmodules != null) {
                vfmoduleArray = vfmodules.getJSONArray(VF_MODULE);
            }

        } catch (JSONException e) {
            log.error(e.getMessage());

        }

        if (vfmoduleArray != null && vfmoduleArray.length() > 0) {
            vServerRelatedLinkMap = handleRelationship(vfmoduleArray, catalog);
        }

        return vServerRelatedLinkMap;
    }


    private static Map<String, List<String>> handleRelationship(JSONArray vfmoduleArray, String catalog) {
        Map<String, List<String>> vServerRelatedLinkMap = new HashMap<String, List<String>>();
        JSONArray relationships = null;
        // If there are multiple vf-module, but one of vf-module missing relationship, we should log the exception and keep loop
        for (int i = 0; i < vfmoduleArray.length(); i++) {
            List<String> relatedLinkList = new ArrayList<String>();
            JSONObject obj = vfmoduleArray.optJSONObject(i);
            String key = (String)obj.get("model-version-id") + DELIMITER + (String)obj.get("model-invariant-id");

            log.debug("Fetching the relationship");

            try {
                JSONObject relationshipList = obj.getJSONObject(RELATIONSHIP_LIST);
                if (relationshipList != null) {
                    relationships = relationshipList.getJSONArray(RELATIONSHIP);
                }
            } catch (JSONException e) {
                log.error(e.getMessage());
                // There is case: vf-module missing relationship, build empty value with the key
             }

            if (relationships != null && relationships.length() > 0) {
                for (int j = 0; j < relationships.length(); j++) {
                    Object relatedToObj = null;
                    Object relatedLinkObj = null;

                    JSONObject obj2 = relationships.optJSONObject(j);
                    relatedToObj = obj2.get(JSON_ATT_RELATED_TO);

                    if (relatedToObj.toString().equals(catalog)) {
                        relatedLinkObj = obj2.get(JSON_ATT_RELATED_LINK);
                        if (relatedLinkObj != null) {
                            relatedLinkList.add(relatedLinkObj.toString());
                        }
                    }
                }  //relationship
            }

            vServerRelatedLinkMap.put(key, relatedLinkList);
        } //vf-module

        return vServerRelatedLinkMap;
    }

    private static Map<String, List<String>> buildHeaders(String aaiBasicAuthorization, String transactionId) {
        MultivaluedHashMap<String, String> headers = new MultivaluedHashMap<String, String>();
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

    private static String generateGetCustomerInfoUrl (String baseURL, String aaiPathToSearchNodeQuery,String serviceInstanceId) {
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
        String resourceLink = null;
        log.info("Fetching the resource-link based on resource-type=" + catalog);

        try {
            JSONArray result_data_list = new JSONObject(payload).getJSONArray(RESULT_DATA);
            if (result_data_list != null) {
                for (int i = 0; i < result_data_list.length(); i++) {
                    JSONObject obj = result_data_list.optJSONObject(i);
                    if (obj.has(JSON_ATT_RESOURCE_TYPE) && (obj.getString(JSON_ATT_RESOURCE_TYPE).equals(catalog) ))  {
                        resourceLink = obj.getString(JSON_ATT_RESOURCE_LINK);
                        log.info(resourceLink);
                        return resourceLink;
                    }
                }
            }
        } catch (JSONException e) {
            log.error(e.getMessage());
            throw new AuditException(AuditError.JSON_READER_PARSE_ERROR + " " + e.getMessage());
        }

        log.warn("resource-link CANNOT be found: ", payload );

        return resourceLink;
    }
}
