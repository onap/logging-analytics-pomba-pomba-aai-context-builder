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
import org.onap.pomba.common.datatypes.Network;
import org.onap.pomba.contextbuilder.aai.common.LogMessages;
import org.onap.pomba.contextbuilder.aai.datatype.Relationship;
import org.onap.pomba.contextbuilder.aai.datatype.RelationshipList;
import org.onap.pomba.contextbuilder.aai.datatype.ServiceInstance;
import org.onap.pomba.contextbuilder.aai.datatype.VfModule;
import org.onap.pomba.contextbuilder.aai.datatype.VnfInstance;
import org.onap.pomba.contextbuilder.aai.datatype.VnfcInstance;
import org.onap.pomba.contextbuilder.aai.datatype.Vserver;
import org.onap.pomba.contextbuilder.aai.datatype.PserverInstance;
import org.onap.pomba.contextbuilder.aai.datatype.PInterfaceInstance;
import org.onap.pomba.contextbuilder.aai.datatype.L3networkInstance;
import org.onap.pomba.contextbuilder.aai.datatype.LInterfaceInstance;
import org.onap.pomba.contextbuilder.aai.exception.AuditError;
import org.onap.pomba.contextbuilder.aai.exception.AuditException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.onap.pomba.contextbuilder.aai.datatype.PnfInstance;
import org.onap.pomba.contextbuilder.aai.datatype.PInterfaceInstanceList;
import org.onap.pomba.common.datatypes.PNF;
import org.onap.pomba.common.datatypes.PInterface;
import com.bazaarvoice.jolt.JsonUtils;
import org.onap.pomba.common.datatypes.Pserver;
import org.onap.pomba.common.datatypes.LInterface;


public class RestUtil {

    private RestUtil() {}

    private static Logger log = LoggerFactory.getLogger(RestUtil.class);
    // Parameters for Query AAI Model Data API
    private static final String SERVICE_INSTANCE_ID = "serviceInstanceId";

    // HTTP headers
    public static final String TRANSACTION_ID = "X-TransactionId";
    public static final String FROM_APP_ID = "X-FromAppId";
    public static final String AUTHORIZATION = "Authorization";

    private static final String APP_NAME = "aaiCtxBuilder";

    // Service Catalog -  "related-to"
    private static final String CATALOG_GENERIC_VNF = "generic-vnf";
    private static final String CATALOG_VNFC = "vnfc";
    private static final String CATALOG_SERVICE_INSTANCE = "service-instance";
    private static final String CATALOG_VSERVER = "vserver";
    private static final String CATALOG_IMAGE = "image";
    private static final String CATALOG_PSERVER = "pserver";
    private static final String CATALOG_L3_NETWORK = "l3-network";
    private static final String CATALOG_PNF = "pnf";
    private static final String CATALOG_L_INTERFACE = "l-interface";

    private static final String VF_MODULES = "vf-modules";
    private static final String VF_MODULE = "vf-module";



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
    private static final String ATTRIBUTE_LOCKEDBOOLEAN = "lockedBoolean";
    private static final String ATTRIBUTE_HOSTNAME = "hostName";
    private static final String ATTRIBUTE_IMAGEID = "imageId";
    private static final String ATTRIBUTE_NF_ROLE = "nfRole";
    private static final String ATTRIBUTE_NF_TYPE = "nfType";
    private static final String ATTRIBUTE_NF_FUNCTION = "nfFunction";
    private static final String ATTRIBUTE_RESOURCE_VERSION = "resourceVersion";
    private static final String ATTRIBUTE_NAME2 = "name2";
    private static final String ATTRIBUTE_NAME2_SOURCE = "name2Source";
    private static final String ATTRIBUTE_EQUIPMENT_TYPE = "equipType";
    private static final String ATTRIBUTE_EQUIPMENT_VENDOR = "equipVendor";
    private static final String ATTRIBUTE_EQUIPMENT_MODEL = "equipModel";
    private static final String ATTRIBUTE_MANAGEMENT_OPTIONS = "managementOptions";
    private static final String ATTRIBUTE_SW_VERSION = "swVersion";
    private static final String ATTRIBUTE_FRAME_ID = "frameId";
    private static final String ATTRIBUTE_SERIAL_NUMBER = "serialNumber";
    private static final String ATTRIBUTE_PTNII_NAME = "ptniiName";
    private static final String ATTRIBUTE_FQDN = "fqdn";
    private static final String ATTRIBUTE_TOPOLOGY = "topology";
    private static final String ATTRIBUTE_PURPOSE = "purpose";
    private static final String ATTRIBUTE_SPEED_VALUE = "speedValue";
    private static final String ATTRIBUTE_SPEED_UNITS = "speedUnits";
    private static final String ATTRIBUTE_PORT_DESCRIPTION = "portDescription";
    private static final String ATTRIBUTE_EQUIPTMENT_ID = "equipmentID";
    private static final String ATTRIBUTE_INTERFACE_ROLE = "interfaceRole";
    private static final String ATTRIBUTE_INTERFACE_TYPE = "interfaceType";
    private static final String ATTRIBUTE_NETWORK_TYPE = "networkType";
    private static final String ATTRIBUTE_NETWORK_ROLE = "networkRole";
    private static final String ATTRIBUTE_NETWORK_TECHNOLOGY = "networkTechnology";
    private static final String ATTRIBUTE_PHYSICAL_NETWORK_NAME = "physicalNetworkName";
    private static final String ATTRIBUTE_SHARED_NETWORK_BOOLEAN = "sharedNetworkBoolean";
    private static final String ATTRIBUTE_IS_PORT_MIRRORED = "isPortMirrored";
    private static final String ATTRIBUTE_NETWORK_NAME = "networkName";
    private static final String ATTRIBUTE_MAC_ADDR = "macAddr";
    private static final String ATTRIBUTE_ADMIN_STATUS = "adminStatus";
    private static final String ATTRIBUTE_NFC_NAMING_CODE = "nfcNamingCode";
    private static final String ATTRIBUTE_NF_NAMING_CODE = "nfNamingCode";


    /**
     * Validates the URL parameter.
     *
     * @throws AuditException if there is missing parameter
     */
    public static void validateServiceInstanceId(String serviceInstanceId)
            throws AuditException {

        if (serviceInstanceId == null || serviceInstanceId.isEmpty()) {
            log.error("Null {}", SERVICE_INSTANCE_ID);
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
            log.error("Null {}", FROM_APP_ID);
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

        List<VnfInstance> vnfLst = new ArrayList<>(); // List of the VNF POJO object
        //Map to track multiple vnfc under the Gerneric VNF id. The key = vnf-id. The value = list of vnfc instance
        Map<String, List<VnfcInstance>> vnfcMap = new HashMap<>();

        //Map to track the relationship between vnf->vfmodule->verver
        Map<String, Map<String, List<Vserver>>> vnfVfmoduleVserverMap = new HashMap<>();

        //Map to track multiple l3-network under the Gerneric VNF id. The key = vnf-id. The value = list of l3-network instance
        Map<String, List<L3networkInstance>> l3networkMapInVnf = new HashMap<>();

        // Obtain resource-link based on resource-type = service-Instance
        String resourceLink = obtainResouceLinkBasedOnServiceInstanceFromAAI(aaiClient, baseURL, aaiPathToSearchNodeQuery, serviceInstanceId, transactionId, aaiBasicAuthorization);

        // Handle the case if the service instance is not found in AAI
        if (resourceLink==null) {
            // return the empty Json on the root level. i.e service instance
            return null;
        }

        log.info(String.format("ResourceLink from AAI: %s", resourceLink));
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

        log.info("Message from AAI:%s", JsonUtils.toPrettyJsonString(JsonUtils.jsonToObject(serviceInstancePayload)));

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
                log.info("Message from AAI for VNF %s,message body: %s", genericVNFURL, JsonUtils.toPrettyJsonString(JsonUtils.jsonToObject(genericVNFPayload)));
                // Logic to Create the Generic VNF Instance POJO object
                VnfInstance vnfInstance = VnfInstance.fromJson(genericVNFPayload);
                vnfLst.add(vnfInstance);

                // Update VModule with l3-network list from aai, if any.
                buildVModuleWithL3NetworkInfo (vnfInstance, aaiClient, baseURL, transactionId,  aaiBasicAuthorization);

                // Build the vnf_vnfc relationship map
                buildVnfcMap(vnfcMap, genericVNFPayload, aaiClient, baseURL, transactionId, aaiBasicAuthorization );

                // Build vnf_vfmodule_vserver relationship map
                buildVfmoduleVserverMap(vnfVfmoduleVserverMap,  genericVNFPayload, aaiClient, baseURL, transactionId, aaiBasicAuthorization);

                // Build the vnf_l3_network relationship map
                buildVnfWithL3networkInfo(l3networkMapInVnf, genericVNFPayload, aaiClient, baseURL, transactionId, aaiBasicAuthorization );
            }
        }

        //Obtain PNF (Physical Network Function)
        List<PnfInstance> pnfLst = retrieveAAIModelData_PNF (aaiClient, baseURL, transactionId, serviceInstanceId, aaiBasicAuthorization, serviceInstancePayload) ;

        //Obtain l3-network on service level
        List<L3networkInstance> l3networkLstInService = retrieveAaiModelDataL3NetworkInServiceLevel (aaiClient, baseURL, transactionId, aaiBasicAuthorization, serviceInstancePayload) ;

        // Transform to common model and return
        return transform(ServiceInstance.fromJson(serviceInstancePayload), vnfLst, vnfcMap, l3networkMapInVnf, vnfVfmoduleVserverMap, pnfLst, l3networkLstInService);
    }

    private static void buildVModuleWithL3NetworkInfo (VnfInstance vnfInstance,
            RestClient aaiClient, String baseURL,
            String transactionId,  String aaiBasicAuthorization
            ) throws AuditException {

        if ((vnfInstance != null ) && (vnfInstance.getVfModules()) != null) {
            List<VfModule> vfModuleList_from_aai = vnfInstance.getVfModules().getVfModule();
            for (VfModule t_vfModule : vfModuleList_from_aai ) {
                RelationshipList vfModuleRelateionShipList = t_vfModule.getRelationshipList();
                if ( vfModuleRelateionShipList != null) {
                    List<Relationship> vfModuleRelateionShipLocalList = vfModuleRelateionShipList.getRelationship();
                    if ( vfModuleRelateionShipLocalList != null) {
                        List<String> relatedLinkList = new ArrayList<>();

                        for ( Relationship vfModuleRelationShip : vfModuleRelateionShipLocalList ) {
                            if (vfModuleRelationShip.getRelatedTo().equals(CATALOG_L3_NETWORK)) {
                                String vfModule_L3_network_RelatedLink = vfModuleRelationShip.getRelatedLink();
                                relatedLinkList.add(vfModule_L3_network_RelatedLink);
                            }
                        }

                        if (!(relatedLinkList.isEmpty())) {
                            List<L3networkInstance> l3nwInsLst = queryAaiForL3networkInfo (aaiClient,baseURL,transactionId,aaiBasicAuthorization,relatedLinkList);

                            if ((l3nwInsLst != null) && (!l3nwInsLst.isEmpty())) {
                                t_vfModule.setL3NetworkList(l3nwInsLst);
                            }
                        }
                    }
                }
            }
        }
    }

    private static List<PnfInstance> retrieveAAIModelData_PNF(RestClient aaiClient, String baseURL,
            String transactionId, String serviceInstanceId, String aaiBasicAuthorization, String serviceInstancePayload) throws AuditException {

        List<String> genericPNFLinkLst = extractRelatedLink(serviceInstancePayload, CATALOG_PNF);
        log.info(LogMessages.NUMBER_OF_API_CALLS, "PNF", genericPNFLinkLst.size());
        log.info(LogMessages.API_CALL_LIST, "PNF", printOutAPIList(genericPNFLinkLst));

        if ( genericPNFLinkLst.isEmpty()) {
           return null;
        }

        String genericPNFPayload = null;
        List<PnfInstance> pnfLst = new ArrayList<>(); // List of the PNF POJO object

        for (String genericPNFLink : genericPNFLinkLst) {
            // With latest AAI development, in order to retrieve the both generic PNF
            String genericPNFURL = baseURL + genericPNFLink + DEPTH;
            // Response from generic PNF API call
            genericPNFPayload =
                    getResource(aaiClient, genericPNFURL, aaiBasicAuthorization, transactionId, MediaType.valueOf(MediaType.APPLICATION_JSON));

            if (isEmptyJson(genericPNFPayload)) {
                log.info(LogMessages.NOT_FOUND, "GenericPNF with url ", genericPNFLink);
            } else {
                log.info(String.format("Message from AAI for PNF %s ,message body: %s", genericPNFLink, JsonUtils.toPrettyJsonString(JsonUtils.jsonToObject(genericPNFPayload))));
                // Logic to Create the Generic VNF Instance POJO object
                PnfInstance pnfInstance = PnfInstance.fromJson(genericPNFPayload);
                pnfLst.add(pnfInstance);
            }
        }

        return pnfLst;
    }

    private static List<L3networkInstance> queryAaiForL3networkInfo(RestClient aaiClient, String baseURL,
            String transactionId, String aaiBasicAuthorization, List<String> genericL3networkLinkLst) throws AuditException {
        if ( genericL3networkLinkLst.isEmpty()) {
            return null;
         }
        log.info(LogMessages.NUMBER_OF_API_CALLS, "L3Network", genericL3networkLinkLst.size());
        log.info(LogMessages.API_CALL_LIST, "L3Network", printOutAPIList(genericL3networkLinkLst));

         String genericL3networkPayload = null;
         List<L3networkInstance> l3nwLst = new ArrayList<>(); // List of the L3-Network POJO object

         for (String genericNetworkLink : genericL3networkLinkLst) {
             // With latest AAI development, in order to retrieve the both generic l3-network
             String genericL3NetworkURL = baseURL + genericNetworkLink + DEPTH;
             // Response from generic l3-network API call
             genericL3networkPayload =
                     getResource(aaiClient, genericL3NetworkURL, aaiBasicAuthorization, transactionId, MediaType.valueOf(MediaType.APPLICATION_JSON));

             if (isEmptyJson(genericL3networkPayload)) {
                 log.info(LogMessages.NOT_FOUND, "GenericPNF with url ", genericNetworkLink);
             } else {
                 log.info("Message from AAI for l3-network %s ,message body: %s", genericNetworkLink, JsonUtils.toPrettyJsonString(JsonUtils.jsonToObject(genericL3networkPayload)));
                 // Logic to Create the Generic VNF Instance POJO object
                 L3networkInstance l3NetworkInstance = L3networkInstance.fromJson(genericL3networkPayload);
                 l3nwLst.add(l3NetworkInstance);
             }
         }

         return l3nwLst;
    }

    private static List<L3networkInstance> retrieveAaiModelDataL3NetworkInServiceLevel(RestClient aaiClient, String baseURL,
            String transactionId, String aaiBasicAuthorization, String relationShipPayload) throws AuditException {

        List<String> genericL3networkLinkLst = extractRelatedLink(relationShipPayload, CATALOG_L3_NETWORK);

        return (queryAaiForL3networkInfo (aaiClient,baseURL,transactionId,aaiBasicAuthorization,genericL3networkLinkLst) ) ;
    }

    /*
     *  The map is to track the relationship of vnf-id with multiple vnfc relationship
     */
    private static Map<String, List<VnfcInstance>> buildVnfcMap(Map<String, List<VnfcInstance>> vnfcMap, String genericVNFPayload, RestClient aaiClient, String baseURL,
            String transactionId, String aaiBasicAuthorization) throws AuditException {

        String vnfcPayload = null;

        List<String> vnfcLinkLst = extractRelatedLink(genericVNFPayload, CATALOG_VNFC);
        log.info(LogMessages.NUMBER_OF_API_CALLS, "vnfc", vnfcLinkLst.size());
        log.info(LogMessages.API_CALL_LIST, "vnfc", printOutAPIList(vnfcLinkLst));

        List<VnfcInstance> vnfcLst = new ArrayList<>();
        for (String vnfcLink : vnfcLinkLst) {
            String vnfcURL = baseURL + vnfcLink;
            vnfcPayload = getResource(aaiClient, vnfcURL, aaiBasicAuthorization,  transactionId,
                    MediaType.valueOf(MediaType.APPLICATION_JSON));

            if (isEmptyJson(vnfcPayload)) {
                log.info(LogMessages.NOT_FOUND, "VNFC with url", vnfcLink);
            } else {
                log.info(String.format("Message from AAI for VNFC with url %s, ,message body: %s", vnfcLink,  JsonUtils.toPrettyJsonString(JsonUtils.jsonToObject(vnfcPayload))));
                // Logic to Create the VNFC POJO object
                VnfcInstance vnfcInstance = VnfcInstance.fromJson(vnfcPayload);
                vnfcLst.add(vnfcInstance);
            }
        }

        if (!vnfcLst.isEmpty()) {
            // Assume the vnf-id is unique as a key
            vnfcMap.put(getVnfId(genericVNFPayload), vnfcLst);
        }

        return vnfcMap;
    }

    /*
     *  The map is to track the relationship of vnf-id with multiple l3-network relationship at vnf level
     */
    private static Map<String, List<L3networkInstance>> buildVnfWithL3networkInfo(Map<String, List<L3networkInstance>> l3networkMap_in_vnf, String genericVNFPayload, RestClient aaiClient, String baseURL,
            String transactionId, String aaiBasicAuthorization) throws AuditException {

        List<L3networkInstance> l3NwLst = retrieveAaiModelDataL3NetworkInServiceLevel (aaiClient, baseURL, transactionId, aaiBasicAuthorization, genericVNFPayload) ;

        if ((l3NwLst != null ) && (!l3NwLst.isEmpty())) {
            // Assume the vnf-id is unique as a key
            l3networkMap_in_vnf.put(getVnfId(genericVNFPayload), l3NwLst);
        }
        return l3networkMap_in_vnf;
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
    private static Map<String, Map<String, List<Vserver>>> buildVfmoduleVserverMap(Map<String, Map<String, List<Vserver>>> vnf_vfmodule_vserver_Map, String genericVNFPayload, RestClient aaiClient, String baseURL, String transactionId, String aaiBasicAuthorization) throws AuditException {

        Map<String, List<Vserver>> vServerMap = new HashMap<>();

        Map<String, List<String>> vServerRelatedLinkMap = extractRelatedLinkFromVfmodule(genericVNFPayload, CATALOG_VSERVER);
        String vnfId= getVnfId(genericVNFPayload);
        String vserverPayload = null;


        if (vServerRelatedLinkMap !=null && (!vServerRelatedLinkMap.isEmpty())) {
            for(Map.Entry<String, List<String>>  entry : vServerRelatedLinkMap.entrySet()) {

                List<String>   vserverLinkLst = entry.getValue();
                log.info(LogMessages.NUMBER_OF_API_CALLS, CATALOG_VSERVER, vserverLinkLst.size());
                log.info(LogMessages.API_CALL_LIST, CATALOG_VSERVER, printOutAPIList(vserverLinkLst));

                List<Vserver> vserverLst = new ArrayList<>();
                for (String vserverLink : vserverLinkLst) {
                    String vserverURL = baseURL + vserverLink;
                    vserverPayload = getResource(aaiClient, vserverURL, aaiBasicAuthorization,  transactionId,
                            MediaType.valueOf(MediaType.APPLICATION_XML));

                    if (isEmptyJson(vserverPayload)) {
                        log.info(LogMessages.NOT_FOUND, "VSERVER with url", vserverURL);
                    } else {
                        // Logic to Create the Vserver POJO object
                        Vserver vserver = Vserver.fromJson(vserverPayload);

                        // add pserver if any
                        List<PserverInstance> pserverInstanceLst = getPserverInfoFromAai(vserverPayload, aaiClient, baseURL, transactionId, aaiBasicAuthorization);
                        if ((pserverInstanceLst != null) && (!pserverInstanceLst.isEmpty())) {
                            vserver.setPserverInstanceList(pserverInstanceLst);
                        }

                        // add L-interface List if any
                        List<LInterfaceInstance> lInterfaceInstanceLst = getLInterfaceInstanceInfoFromAai(vserverPayload, aaiClient, baseURL, transactionId, aaiBasicAuthorization);
                        if ((lInterfaceInstanceLst != null) && (!lInterfaceInstanceLst.isEmpty())) {
                            vserver.setlInterfaceInstanceList(lInterfaceInstanceLst);
                        }

                        vserverLst.add(vserver);
                    }
                }

                if (!vserverLst.isEmpty()) {
                    vServerMap.put(entry.getKey(), vserverLst);
                }
            }
            if (!vServerMap.isEmpty()) {
                vnf_vfmodule_vserver_Map.put(vnfId, vServerMap);
            }
        }

        return vnf_vfmodule_vserver_Map;
    }

    private static List<PserverInstance> getPserverInfoFromAai (String vserverPayload, RestClient aaiClient, String baseURL, String transactionId, String aaiBasicAuthorization) throws AuditException {
        if (vserverPayload == null) {
            //already reported.
            return null;
        }

        //Obtain related Pserver info
        List<String> pserverRelatedLinkList = handleRelationshipGeneral (vserverPayload,CATALOG_PSERVER );
        List<PserverInstance> pserverLst = null;
        if ((pserverRelatedLinkList == null) || (pserverRelatedLinkList.isEmpty())){
            // already reported
            return null;
        }
        pserverLst = new ArrayList<>();
        for (String pserverRelatedLink : pserverRelatedLinkList) {
            String pserverURL = baseURL + pserverRelatedLink + DEPTH;;
            String pserverPayload = getResource(aaiClient, pserverURL, aaiBasicAuthorization,  transactionId,
                    MediaType.valueOf(MediaType.APPLICATION_XML));

            if (isEmptyJson(pserverPayload)) {
                log.info(LogMessages.NOT_FOUND, "PSERVER with url", pserverURL);
            } else {
                log.info("Message from AAI for pserver %s ,message body: %s", pserverURL,pserverPayload);
                // Logic to Create the Pserver POJO object
                PserverInstance pserver = PserverInstance.fromJson(pserverPayload);

                //update P-Interface if any.
                pserverLst.add(pserver);
            }
        }
        return pserverLst;
    }

    private static List<LInterfaceInstance> getLInterfaceInstanceInfoFromAai (String vserverPayload, RestClient aaiClient, String baseURL, String transactionId, String aaiBasicAuthorization) throws AuditException {
        if (vserverPayload == null) {
            //already reported.
            return null;
        }

        //Obtain related L-Interface instance info
        List<String> lInterfaceRelatedLinkList = handleRelationshipGeneral (vserverPayload,CATALOG_L_INTERFACE );
        List<LInterfaceInstance> lInterfaceLst = null;
        if ((lInterfaceRelatedLinkList == null) || (lInterfaceRelatedLinkList.isEmpty())){
            // already reported
            return null;
        }
        lInterfaceLst = new ArrayList<>();
        for (String lInterfaceRelatedLink : lInterfaceRelatedLinkList) {
            String lInterfaceURL = baseURL + lInterfaceRelatedLink + DEPTH;;
            String lInterfacePayload = getResource(aaiClient, lInterfaceURL, aaiBasicAuthorization,  transactionId,
                    MediaType.valueOf(MediaType.APPLICATION_XML));

            if (isEmptyJson(lInterfacePayload)) {
                log.info(LogMessages.NOT_FOUND, "L-INTERFACE with url", lInterfaceURL);
            } else {
                log.info("Message from AAI for L-INTERFACE %s ,message body: %s", lInterfaceURL,lInterfacePayload);
                // Logic to Create the Pserver POJO object
                LInterfaceInstance lInterfaceInstance = LInterfaceInstance.fromJson(lInterfacePayload);

                //update P-Interface if any.
                lInterfaceLst.add(lInterfaceInstance);
            }
        }
        return lInterfaceLst;
    }

    private static String getVnfId(String genericVNFPayload) throws AuditException {

        VnfInstance vnfInstance = VnfInstance.fromJson(genericVNFPayload);
        return vnfInstance.getVnfId();
    }

    /*
     * Transform AAI Representation to Common Model
     */
    private static ModelContext transform(ServiceInstance svcInstance, List<VnfInstance> vnfLst,
            Map<String, List<VnfcInstance>> vnfcMap,
            Map<String, List<L3networkInstance>> l3networkMap_in_vnf,
            Map<String, Map<String, List<Vserver>>> vnf_vfmodule_vserver_Map, List<PnfInstance> pnfLst_fromAAi,
            List<L3networkInstance> l3networkLst_in_service) {
        ModelContext context = new ModelContext();
        Service service = new Service();
        service.setModelInvariantUUID(svcInstance.getModelInvariantId());
        service.setName(svcInstance.getServiceInstanceName());
        service.setModelVersionID(svcInstance.getModelVersionId());
        service.setUuid(svcInstance.getServiceInstanceId());
        service.setDataQuality(DataQuality.ok());
        List<VNF> vfLst = new ArrayList<>();

        for (VnfInstance vnf : vnfLst) {
            VNF vf = new VNF();
            vf.setModelInvariantUUID(vnf.getModelInvariantId());
            vf.setName(vnf.getVnfName());
            vf.setUuid(vnf.getVnfId());
            vf.setType(vnf.getVnfType());
            vf.setModelVersionID(vnf.getModelVersionId());
            vf.setDataQuality(DataQuality.ok());
            vf.setAttributes(populateVnfAttributeList(vnf));
            String key = vnf.getVnfId();   // generic vnf-id (top level of the key)

            // ---------------- Handle VNFC data
            List<VNFC> vnfcLst = new ArrayList<>();
            for (Map.Entry<String, List<VnfcInstance>> entry : vnfcMap.entrySet()) {

                if (key.equals(entry.getKey())) {
                    List<VnfcInstance> vnfcInstanceLst = entry.getValue();

                    for (VnfcInstance vnfc : vnfcInstanceLst) {
                        VNFC vnfcModel = new VNFC();
                        vnfcModel.setModelInvariantUUID(vnfc.getModelInvariantId());
                        vnfcModel.setName(vnfc.getVnfcName());
                        vnfcModel.setModelVersionID(vnfc.getModelVersionId());
                        vnfcModel.setUuid(vnfc.getModelVersionId());
                        vnfcModel.setAttributes(populateVnfcAttributeList(vnfc));
                        vnfcLst.add(vnfcModel);
                    }
                }
            }
            vf.setVnfcs(vnfcLst);

            // add vnf level l3-network
            List<Network> nwLst_in_vnf = null;
            for (Map.Entry<String, List<L3networkInstance>> entry : l3networkMap_in_vnf.entrySet()) {
                if (key.equals(entry.getKey())) {
                    List<L3networkInstance> l3NwInstanceLst_in_vnf = entry.getValue();
                    nwLst_in_vnf = transformL3Network (l3NwInstanceLst_in_vnf) ;
                }
            }

            if ((nwLst_in_vnf != null) && (!nwLst_in_vnf.isEmpty())) {
                vf.setNetworks(nwLst_in_vnf);
            }

            // --------------- Handle the vfModule
            List<VFModule> vfModuleLst = new ArrayList<>();
            //Map to calculate the Vf Module MaxInstance.
            if (vnf.getVfModules() != null) {
                List<VfModule> vfModuleList_from_aai = vnf.getVfModules().getVfModule();
                ConcurrentMap<String, AtomicInteger> maxInstanceMap =
                        buildMaxInstanceMap(vfModuleList_from_aai);

                for (VfModule t_vfModule : vfModuleList_from_aai ) {
                    VFModule vfModule = new VFModule();
                    vfModule.setUuid(t_vfModule.getVfModuleId());
                    vfModule.setModelInvariantUUID(t_vfModule.getModelInvariantId());
                    vfModule.setName(t_vfModule.getVfModuleName());
                    vfModule.setModelVersionID(t_vfModule.getModelVersionId());
                    vfModule.setModelCustomizationUUID(t_vfModule.getModelCustomizationId());
                    vfModule.setMaxInstances(maxInstanceMap.size());
                    vfModule.setDataQuality(DataQuality.ok());
                    List<Network> l3networkInVfModule = transformL3Network(t_vfModule.getL3NetworkList());
                    if ((l3networkInVfModule != null) && (!l3networkInVfModule.isEmpty()) ){
                        vfModule.setNetworks(l3networkInVfModule);
                    }

                    for ( Map.Entry<String, Map<String, List<Vserver>>> entry:    vnf_vfmodule_vserver_Map.entrySet() ) {
                        // find the vnf-id
                        if (key.equals(entry.getKey())) {

                            Map<String, List<Vserver>> vfmodule_vserver_map= entry.getValue();

                            for ( Map.Entry<String, List<Vserver>> vfmoduleEntry:  vfmodule_vserver_map.entrySet() ){
                                // The key is modelversionId$modelInvariantid
                                String[] s = vfmoduleEntry.getKey().split("\\" + DELIMITER);
                                String vfModuleId = s[0];
                                String modelInvariantId = s[1];

                                if ((vfModuleId.equals(t_vfModule.getVfModuleId()))
                                &&  (modelInvariantId.equals(t_vfModule.getModelInvariantId()))){

                                    List<Vserver>  vserverList = vfmoduleEntry.getValue();
                                    vfModule.setMaxInstances(getMaxInstance(vfmoduleEntry.getKey(), maxInstanceMap));

                                    // Handle VM
                                    List<VM>   vmList = new ArrayList<>();
                                    for (Vserver vserver: vserverList) {

                                        List<Attribute>  attributeList = new ArrayList<>();

                                        // Iterate through the ENUM Attribute list
                                        for (Attribute.Name  name: Attribute.Name.values()) {
                                            if (name.name().equals(ATTRIBUTE_LOCKEDBOOLEAN)) {
                                                Attribute att = new Attribute();
                                                att.setDataQuality(DataQuality.ok());
                                                att.setName(Attribute.Name.lockedBoolean);
                                                att.setValue(String.valueOf(vserver.getInMaint()));
                                                attributeList.add(att);
                                            }

                                            if (name.name().equals(ATTRIBUTE_HOSTNAME)) {
                                                Attribute att = new Attribute();
                                                att.setDataQuality(DataQuality.ok());
                                                att.setName(Attribute.Name.hostName);
                                                att.setValue(getVserverAttribute(vserver, CATALOG_PSERVER));
                                                attributeList.add(att);
                                            }

                                            if (name.name().equals(ATTRIBUTE_IMAGEID)) {
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

                                        //Update pserver here
                                        List<PserverInstance> pserverInstanceList = vserver.getPserverInstanceList();
                                        Pserver pServer = null;
                                        if (pserverInstanceList != null) {
                                            pServer = getPserverInfo (pserverInstanceList);
                                        }
                                        vm.setPServer(pServer);

                                        //Update L-Interface here
                                        List<LInterfaceInstance> lInterfaceInstanceList = vserver.getlInterfaceInstanceList();
                                        List<LInterface> lInterfacelst = null;
                                        if (lInterfaceInstanceList != null) {
                                            lInterfacelst = getLInterfaceLstInfo (lInterfaceInstanceList);
                                        }
                                        if ((lInterfacelst != null) && (!lInterfacelst.isEmpty())) {
                                            vm.setLInterfaceList(lInterfacelst);
                                        }

                                        vmList.add(vm);
                                    }

                                    if (!vmList.isEmpty()) {
                                        vfModule.setVms(vmList);
                                    }
                                }
                            }
                        }
                    }
                    vfModuleLst.add(vfModule);
                }

            }  // done the vfmodule

            vf.setVfModules(vfModuleLst);
            vfLst.add(vf);

        } // done the vnfInstance

        context.setService(service);
        context.setVnfs(vfLst);
        //Add PNF info
        context.setPnfs(transformPNF(pnfLst_fromAAi));
        //Add service-level l3-network info
        context.setNetworkList(transformL3Network (l3networkLst_in_service));
        log.info((String.format("ModelContext body: %s ", JsonUtils.toPrettyJsonString(context))));
        return context;
    }

    private static List<Attribute> populateVnfAttributeList (VnfInstance vnf) {
        if (vnf == null) {
           return null;
        }

        List<Attribute>  attributeList = new ArrayList<>();

        for (Attribute.Name  name: Attribute.Name.values()) {
            if ((name.name().equals(ATTRIBUTE_NF_NAMING_CODE ))
                    && isValid(vnf.getNfNamingCode())){
                Attribute att = new Attribute();
                att.setDataQuality(DataQuality.ok());
                att.setName(Attribute.Name.nfNamingCode);
                att.setValue(String.valueOf(vnf.getNfNamingCode()));
                attributeList.add(att);
            }

            if ((name.name().equals(ATTRIBUTE_NF_TYPE  ))
                    && isValid(vnf.getNfType())){
                Attribute att = new Attribute();
                att.setDataQuality(DataQuality.ok());
                att.setName(Attribute.Name.nfType);
                att.setValue(String.valueOf(vnf.getNfType()));
                attributeList.add(att);
            }

            if ((name.name().equals(ATTRIBUTE_NF_ROLE  ))
                    && isValid(vnf.getNfRole())){
                Attribute att = new Attribute();
                att.setDataQuality(DataQuality.ok());
                att.setName(Attribute.Name.nfRole);
                att.setValue(String.valueOf(vnf.getNfRole()));
                attributeList.add(att);
            }

            if ((name.name().equals(ATTRIBUTE_NF_FUNCTION  ))
                    && isValid(vnf.getNfFunction())){
                Attribute att = new Attribute();
                att.setDataQuality(DataQuality.ok());
                att.setName(Attribute.Name.nfFunction);
                att.setValue(String.valueOf(vnf.getNfFunction()));
                attributeList.add(att);
            }
        }

        if (attributeList.size() > 0 ) {
            return attributeList;
        }
        return null;
    }

    private static List<Attribute> populateVnfcAttributeList (VnfcInstance vnfc) {
        if (vnfc == null) {
           return null;
        }

        List<Attribute>  attributeList = new ArrayList<>();

        for (Attribute.Name  name: Attribute.Name.values()) {
            if ((name.name().equals(ATTRIBUTE_NFC_NAMING_CODE  ))
                    && isValid(vnfc.getNfcNamingCode())){
                Attribute att = new Attribute();
                att.setDataQuality(DataQuality.ok());
                att.setName(Attribute.Name.nfcNamingCode);
                att.setValue(String.valueOf(vnfc.getNfcNamingCode()));
                attributeList.add(att);
            }

            if ((name.name().equals(ATTRIBUTE_LOCKEDBOOLEAN  ))
             && (vnfc.getInMaintenance() != null) )
            {
                Attribute att = new Attribute();
                att.setDataQuality(DataQuality.ok());
                att.setName(Attribute.Name.nfcNamingCode);
                att.setValue(String.valueOf(vnfc.getInMaintenance()));
                attributeList.add(att);
            }
        }

        if (attributeList.size() > 0 ) {
            return attributeList;
        }
        return null;
    }

    private static Pserver getPserverInfo (List<PserverInstance> pserverInstanceList) {
         if (pserverInstanceList == null) {
            return null;
         }

         Pserver pserver = null;

         for (PserverInstance pserverInstance: pserverInstanceList) {
             pserver = new Pserver();
             pserver.setUuid(pserverInstance.getPserverId());
             pserver.setName(pserverInstance.getHostname());

             List<Attribute>  attributeList = new ArrayList<>();
             // Iterate through the ENUM Attribute list
             for (Attribute.Name  name: Attribute.Name.values()) {
                 if ((name.name().equals(ATTRIBUTE_NAME2 ))
                         && isValid(pserverInstance.getPserverName2())){
                     Attribute att = new Attribute();
                     att.setDataQuality(DataQuality.ok());
                     att.setName(Attribute.Name.name2);
                     att.setValue(String.valueOf(pserverInstance.getPserverName2()));
                     attributeList.add(att);
                 }
                 if ((name.name().equals(ATTRIBUTE_PTNII_NAME ))
                         && isValid(pserverInstance.getPtniiEquipName())){
                     Attribute att = new Attribute();
                     att.setDataQuality(DataQuality.ok());
                     att.setName(Attribute.Name.ptniiName);
                     att.setValue(String.valueOf(pserverInstance.getPtniiEquipName()));
                     attributeList.add(att);
                 }
                 if ((name.name().equals( ATTRIBUTE_EQUIPMENT_TYPE ))
                         && isValid(pserverInstance.getEquipType())){
                     Attribute att = new Attribute();
                     att.setDataQuality(DataQuality.ok());
                     att.setName(Attribute.Name.equipType);
                     att.setValue(String.valueOf(pserverInstance.getEquipType()));
                     attributeList.add(att);
                 }
                 if ((name.name().equals( ATTRIBUTE_EQUIPMENT_VENDOR ))
                         && isValid(pserverInstance.getEquipVendor())){
                     Attribute att = new Attribute();
                     att.setDataQuality(DataQuality.ok());
                     att.setName(Attribute.Name.equipVendor);
                     att.setValue(String.valueOf(pserverInstance.getEquipVendor()));
                     attributeList.add(att);
                 }
                 if ((name.name().equals( ATTRIBUTE_EQUIPMENT_MODEL ))
                         && isValid(pserverInstance.getEquipModel())){
                     Attribute att = new Attribute();
                     att.setDataQuality(DataQuality.ok());
                     att.setName(Attribute.Name.equipModel);
                     att.setValue(String.valueOf(pserverInstance.getEquipModel()));
                     attributeList.add(att);
                 }
                 if ((name.name().equals( ATTRIBUTE_FQDN ))
                         && isValid(pserverInstance.getFqdn())){
                     Attribute att = new Attribute();
                     att.setDataQuality(DataQuality.ok());
                     att.setName(Attribute.Name.fqdn);
                     att.setValue(String.valueOf(pserverInstance.getFqdn()));
                     attributeList.add(att);
                 }
                 if ((name.name().equals( ATTRIBUTE_SERIAL_NUMBER  ))
                         && isValid(pserverInstance.getSerialNumber())){
                     Attribute att = new Attribute();
                     att.setDataQuality(DataQuality.ok());
                     att.setName(Attribute.Name.serialNumber);
                     att.setValue(String.valueOf(pserverInstance.getSerialNumber()));
                     attributeList.add(att);
                 }
                 if ((name.name().equals( ATTRIBUTE_TOPOLOGY  ))
                         && isValid(pserverInstance.getInternetTopology())){
                     Attribute att = new Attribute();
                     att.setDataQuality(DataQuality.ok());
                     att.setName(Attribute.Name.topology);
                     att.setValue(String.valueOf(pserverInstance.getInternetTopology()));
                     attributeList.add(att);
                 }
                 if ((name.name().equals(ATTRIBUTE_LOCKEDBOOLEAN))
                         && isValid(pserverInstance.getInMaint())){
                     Attribute att = new Attribute();
                     att.setDataQuality(DataQuality.ok());
                     att.setName(Attribute.Name.lockedBoolean);
                     att.setValue(String.valueOf(pserverInstance.getInMaint()));
                     attributeList.add(att);
                 }
                 if ((name.name().equals(ATTRIBUTE_RESOURCE_VERSION))
                         && isValid(pserverInstance.getResourceVersion())){
                     Attribute att = new Attribute();
                     att.setDataQuality(DataQuality.ok());
                     att.setName(Attribute.Name.resourceVersion);
                     att.setValue(String.valueOf(pserverInstance.getResourceVersion()));
                     attributeList.add(att);
                 }
                 if ((name.name().equals(ATTRIBUTE_PURPOSE))
                         && isValid(pserverInstance.getPurpose())){
                     Attribute att = new Attribute();
                     att.setDataQuality(DataQuality.ok());
                     att.setName(Attribute.Name.purpose);
                     att.setValue(String.valueOf(pserverInstance.getPurpose()));
                     attributeList.add(att);
                 }
             }

             if (!attributeList.isEmpty()) {
                 pserver.setAttributes(attributeList);
             }

             // Update P-Interface if any,
             PInterfaceInstanceList pInterfaceInstanceList = pserverInstance.getPInterfaceInstanceList();
             if ((pInterfaceInstanceList != null) && (!(pInterfaceInstanceList.getPInterfaceList().isEmpty()))) {
                 pserver = updatePserverInfoWithPInterface (pserver, pInterfaceInstanceList.getPInterfaceList());
             }

             // NOTE: we only support one pserver per vserver hence we only add
             // the first pserver if there are multiple pservers are provided for the given vserver.
             return pserver;
         }

         return pserver;
    }

    private static List<LInterface>  getLInterfaceLstInfo (List<LInterfaceInstance> lInterfaceInstanceList) {
        if (lInterfaceInstanceList == null) {
           return null;
        }

        List<LInterface> lInterfaceLst = new ArrayList<>();

        for (LInterfaceInstance lInterfaceInstance: lInterfaceInstanceList) {
            LInterface lInterface = new LInterface();
            lInterface.setUuid(lInterfaceInstance.getInterfaceId());
            lInterface.setName(lInterfaceInstance.getInterfaceName());

            List<Attribute>  attributeList = new ArrayList<>();
            // Iterate through the ENUM Attribute list
            for (Attribute.Name  name: Attribute.Name.values()) {
                if ((name.name().equals(ATTRIBUTE_INTERFACE_ROLE))
                        && isValid(lInterfaceInstance.getInterfaceRole())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.interfaceRole);
                    att.setValue(String.valueOf(lInterfaceInstance.getInterfaceRole()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_IS_PORT_MIRRORED))
                        && isValid(lInterfaceInstance.getIsPortMirrored())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.isPortMirrored);
                    att.setValue(String.valueOf(lInterfaceInstance.getIsPortMirrored()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_ADMIN_STATUS ))
                        && isValid(lInterfaceInstance.getAdminStatus())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.adminStatus);
                    att.setValue(String.valueOf(lInterfaceInstance.getAdminStatus()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_NETWORK_NAME ))
                        && isValid(lInterfaceInstance.getNetworkName())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.networkName);
                    att.setValue(String.valueOf(lInterfaceInstance.getNetworkName()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_MAC_ADDR ))
                        && isValid(lInterfaceInstance.getMacAddr())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.macAddress);
                    att.setValue(String.valueOf(lInterfaceInstance.getMacAddr()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_LOCKEDBOOLEAN))
                        && isValid(lInterfaceInstance.getInMaint())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.lockedBoolean);
                    att.setValue(String.valueOf(lInterfaceInstance.getInMaint()));
                    attributeList.add(att);
                }

            }

            if (!attributeList.isEmpty()) {
                lInterface.setAttributes(attributeList);
            }
            lInterfaceLst.add(lInterface);
        }

        if (lInterfaceLst.isEmpty()) {
            return null;
        }
        return lInterfaceLst;
   }

    private static Pserver updatePserverInfoWithPInterface (Pserver pserver, List<PInterfaceInstance> pInterfaceInstanceList) {

        List<PInterface> pInterfaceList = new ArrayList<>();
        for (PInterfaceInstance pInterfaceInst_aai: pInterfaceInstanceList) {
            PInterface pInterface = new PInterface();
            // pInterface.setUuid( ); // there is no mapping data for UUID from AAI data.
            pInterface.setName(pInterfaceInst_aai.getInterfaceName());
            pInterface.setDataQuality(DataQuality.ok());

            List<Attribute>  pInterface_attributeList = new ArrayList<>();
            updatePInterfaceAttributeList (pInterfaceInst_aai, pInterface_attributeList) ;
            if (!pInterface_attributeList.isEmpty()) {
                pInterface.setAttributes(pInterface_attributeList);
            }

            pInterfaceList.add(pInterface);
        }

        if (!pInterfaceList.isEmpty()) {
            pserver.setPInterfaceList(pInterfaceList);;
        }

        return pserver;
    }

    /*
     * Transform AAI Representation to Common Model
     */
    public static List<PNF> transformPNF(List<PnfInstance> pnfLst_from_AAI) {
        if ((pnfLst_from_AAI == null ) || (pnfLst_from_AAI.isEmpty())) {
            log.info(LogMessages.API_CALL_LIST, "Nill PNF list");
           return null;
        }
        List<PNF> pnfLst = new ArrayList<>();

        for (PnfInstance pnfFromAai : pnfLst_from_AAI) {
            PNF pnf = new PNF();
            pnf.setUuid(pnfFromAai.getPnfId());
            pnf.setName(pnfFromAai.getPnfName());
            pnf.setModelVersionID(pnfFromAai.getModelVersionId());
            pnf.setModelInvariantUUID(pnfFromAai.getModelInvariantId());
            pnf.setDataQuality(DataQuality.ok());
            List<Attribute>  attributeList = new ArrayList<>();
            pnf.setAttributes(attributeList);

            // Iterate through the ENUM Attribute list
            for (Attribute.Name  name: Attribute.Name.values()) {
                if ((name.name().equals(ATTRIBUTE_NF_ROLE ))
                        && isValid(pnfFromAai.getNfRole())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.nfRole);
                    att.setValue(String.valueOf( pnfFromAai.getNfRole()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_RESOURCE_VERSION))
                        && isValid(pnfFromAai.getResourceVersion())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.resourceVersion);
                    att.setValue(String.valueOf( pnfFromAai.getResourceVersion()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_NAME2))
                        && isValid(pnfFromAai.getPnfName2() )){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.name2);
                    att.setValue(String.valueOf( pnfFromAai.getPnfName2()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_NAME2_SOURCE ))
                        && isValid(pnfFromAai.getPnfName2Source())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.name2Source);
                    att.setValue(String.valueOf( pnfFromAai.getPnfName2Source()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_EQUIPMENT_TYPE ))
                        && isValid(pnfFromAai.getEquipmentType())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.equipType);
                    att.setValue(String.valueOf( pnfFromAai.getEquipmentType()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_EQUIPMENT_VENDOR ))
                        && isValid(pnfFromAai.getEquipmentVendor())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.equipVendor);
                    att.setValue(String.valueOf( pnfFromAai.getEquipmentVendor()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_EQUIPMENT_MODEL))
                        && isValid(pnfFromAai.getEquipmentModel())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.equipModel);
                    att.setValue(String.valueOf( pnfFromAai.getEquipmentModel()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_MANAGEMENT_OPTIONS))
                        && isValid(pnfFromAai.getManagementOptions())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.managementOptions);
                    att.setValue(String.valueOf( pnfFromAai.getManagementOptions()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_SW_VERSION))
                        && isValid(pnfFromAai.getSwVersion())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.swVersion);
                    att.setValue(String.valueOf( pnfFromAai.getSwVersion()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_FRAME_ID))
                        && isValid(pnfFromAai.getFrameId())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.frameId);
                    att.setValue(String.valueOf( pnfFromAai.getFrameId()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_SERIAL_NUMBER))
                        && isValid(pnfFromAai.getSerialNumber())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.serialNumber);
                    att.setValue(String.valueOf( pnfFromAai.getSerialNumber()));
                    attributeList.add(att);
                }
            }

            pnf.setAttributes(attributeList);

            // Update P-Interface
            if ((pnfFromAai.getPInterfaceInstanceList() != null)
              &&(!(pnfFromAai.getPInterfaceInstanceList().getPInterfaceList().isEmpty()))) {

                List<PInterface> pInterfaceList = new ArrayList<>();
                List<PInterfaceInstance> pInterfaceInstList_aai = pnfFromAai.getPInterfaceInstanceList().getPInterfaceList();
                for (PInterfaceInstance pInterfaceInst_aai : pInterfaceInstList_aai) {
                    PInterface pInterface = new PInterface();
                    pInterface.setUuid(pInterfaceInst_aai.getEquipmentIdentifier() );
                    pInterface.setName(pInterfaceInst_aai.getInterfaceName());
                    pInterface.setDataQuality(DataQuality.ok());

                    List<Attribute>  pInterface_attributeList = new ArrayList<>();
                    updatePInterfaceAttributeList (pInterfaceInst_aai, pInterface_attributeList) ;
                    if (!pInterface_attributeList.isEmpty()) {
                        pInterface.setAttributes(pInterface_attributeList);
                    }

                    pInterfaceList.add(pInterface);
                }

                if (!pInterfaceList.isEmpty()) {
                    pnf.setPInterfaceList(pInterfaceList);
                }
            }

            pnfLst.add(pnf);

        } // done the vnfInstance

        return pnfLst;
    }

    /*
     * Transform AAI Representation to Common Model
     */
    public static List<Network> transformL3Network(List<L3networkInstance> l3networkLstFromAAI) {
        if ((l3networkLstFromAAI == null ) || (l3networkLstFromAAI.isEmpty())) {
            log.info(LogMessages.API_CALL_LIST, "Nill L3-Network list");
           return null;
        }
        List<Network> l3NetworkLst = new ArrayList<>();

        for (L3networkInstance l3networkFromAai : l3networkLstFromAAI) {
            Network l3network = new Network();
            l3network.setUuid(l3networkFromAai.getNetworkId());
            l3network.setName(l3networkFromAai.getNetworkName());
            l3network.setModelVersionID(l3networkFromAai.getModelVersionId());
            l3network.setModelInvariantUUID(l3networkFromAai.getModelInvariantId());
            l3network.setDataQuality(DataQuality.ok());
            List<Attribute>  attributeList = new ArrayList<>();

            // Iterate through the ENUM Attribute list
            for (Attribute.Name  name: Attribute.Name.values()) {
                if ((name.name().equals(ATTRIBUTE_NETWORK_TYPE  ))
                        && isValid(l3networkFromAai.getNetworkType())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.networkType);
                    att.setValue(String.valueOf( l3networkFromAai.getNetworkType()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_NETWORK_ROLE  ))
                        && isValid(l3networkFromAai.getNetworkRole())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.networkRole);
                    att.setValue(String.valueOf( l3networkFromAai.getNetworkRole()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_NETWORK_TECHNOLOGY  ))
                        && isValid(l3networkFromAai.getNetworkTechnology())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.networkTechnology);
                    att.setValue(String.valueOf( l3networkFromAai.getNetworkTechnology()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_RESOURCE_VERSION  ))
                        && isValid(l3networkFromAai.getResourceVersion())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.resourceVersion);
                    att.setValue(String.valueOf( l3networkFromAai.getResourceVersion()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_PHYSICAL_NETWORK_NAME  ))
                        && isValid(l3networkFromAai.getPhysicalNetworkName())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.physicalNetworkName);
                    att.setValue(String.valueOf( l3networkFromAai.getPhysicalNetworkName()));
                    attributeList.add(att);
                }

                if ((name.name().equals(ATTRIBUTE_SHARED_NETWORK_BOOLEAN ))
                        && isValid(l3networkFromAai.getSharedNetworkBoolean())){
                    Attribute att = new Attribute();
                    att.setDataQuality(DataQuality.ok());
                    att.setName(Attribute.Name.sharedNetworkBoolean);
                    att.setValue(String.valueOf( l3networkFromAai.getSharedNetworkBoolean()));
                    attributeList.add(att);
                }

            }

            if (!attributeList.isEmpty()) {
                l3network.setAttributes(attributeList);
            }

            l3NetworkLst.add(l3network);
        } // done the vnfInstance

        return l3NetworkLst;
    }

    private static void updatePInterfaceAttributeList(PInterfaceInstance pInterfaceInstFromAai, List<Attribute>  pInterfaceAttributeList ) {
        // Iterate through the ENUM Attribute list
        for (Attribute.Name  name: Attribute.Name.values()) {
            if ((name.name().equals(ATTRIBUTE_SPEED_VALUE ))
                    && isValid(pInterfaceInstFromAai.getSpeedValue())){
                Attribute att = new Attribute();
                att.setDataQuality(DataQuality.ok());
                att.setName(Attribute.Name.speedValue);
                att.setValue(String.valueOf( pInterfaceInstFromAai.getSpeedValue()));
                pInterfaceAttributeList.add(att);
            }

            if ((name.name().equals(ATTRIBUTE_SPEED_UNITS ))
                    && isValid(pInterfaceInstFromAai.getSpeedUnits())){
                Attribute att = new Attribute();
                att.setDataQuality(DataQuality.ok());
                att.setName(Attribute.Name.speedUnits);
                att.setValue(String.valueOf( pInterfaceInstFromAai.getSpeedUnits()));
                pInterfaceAttributeList.add(att);
            }

            if ((name.name().equals(ATTRIBUTE_PORT_DESCRIPTION ))
                    && isValid(pInterfaceInstFromAai.getPortDescription())){
                Attribute att = new Attribute();
                att.setDataQuality(DataQuality.ok());
                att.setName(Attribute.Name.portDescription);
                att.setValue(String.valueOf( pInterfaceInstFromAai.getPortDescription()));
                pInterfaceAttributeList.add(att);
            }

            if ((name.name().equals(ATTRIBUTE_EQUIPTMENT_ID ))
                    && isValid(pInterfaceInstFromAai.getEquipmentIdentifier())){
                Attribute att = new Attribute();
                att.setDataQuality(DataQuality.ok());
                att.setName(Attribute.Name.equipmentID);
                att.setValue(String.valueOf( pInterfaceInstFromAai.getEquipmentIdentifier()));
                pInterfaceAttributeList.add(att);
            }

            if ((name.name().equals(ATTRIBUTE_INTERFACE_ROLE ))
                    && isValid(pInterfaceInstFromAai.getInterfaceRole())){
                Attribute att = new Attribute();
                att.setDataQuality(DataQuality.ok());
                att.setName(Attribute.Name.interfaceRole);
                att.setValue(String.valueOf( pInterfaceInstFromAai.getInterfaceRole()));
                pInterfaceAttributeList.add(att);
            }

            if ((name.name().equals(ATTRIBUTE_INTERFACE_TYPE ))
                    && isValid(pInterfaceInstFromAai.getInterfaceType())){
                Attribute att = new Attribute();
                att.setDataQuality(DataQuality.ok());
                att.setName(Attribute.Name.interfaceType);
                att.setValue(String.valueOf( pInterfaceInstFromAai.getInterfaceType()));
                pInterfaceAttributeList.add(att);
            }

            if ((name.name().equals( ATTRIBUTE_RESOURCE_VERSION ))
                    && isValid(pInterfaceInstFromAai.getResourceVersion())){
                Attribute att = new Attribute();
                att.setDataQuality(DataQuality.ok());
                att.setName(Attribute.Name.resourceVersion);
                att.setValue(String.valueOf( pInterfaceInstFromAai.getResourceVersion()));
                pInterfaceAttributeList.add(att);
            }

            if ((name.name().equals( ATTRIBUTE_LOCKEDBOOLEAN  ))
                    && isValid(pInterfaceInstFromAai.getInMaint())){
                Attribute att = new Attribute();
                att.setDataQuality(DataQuality.ok());
                att.setName(Attribute.Name.lockedBoolean);
                att.setValue(String.valueOf( pInterfaceInstFromAai.getInMaint()));
                pInterfaceAttributeList.add(att);
            }
        }
        return;

    }

    /*
     * Return the Vserver Attribute value by looking through the relationship. i.e. if "related-to" is "pserver", we will get
     * the value of the attribute "hostname" from the last substring of the "related-link"
     * {
     *    "related-to": "pserver",
     *    "related-link": "/aai/v13/cloud-infrastructure/pservers/pserver/rdm5r10c008.rdm5a.cci.att.com",
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
     *    "related-link": "/aai/v13/cloud-infrastructure/pservers/pserver/rdm5r10c008.rdm5a.cci.att.com",
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
            // group the key by vf-module-id, model-invariant-id,vf-module-name, model-version-id and model-customization-id
            String key = new StringBuilder().append(vfModule.getVfModuleId()).append(DELIMITER)
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
     *      "related-link": "/aai/v13/network/vnfcs/vnfc/zrdm5aepdg01vmg003",
     *      "relationship-data": [
     *          { "relationship-key": "vnfc.vnfc-name",
     *          "relationship-value": "zrdm5aepdg01vmg003" }
     *       ]
     * },
     */
    private static List<String> extractRelatedLink(String payload, String catalog)  {
        JSONObject jsonPayload = new JSONObject(payload);
        JSONArray relationships = null;
        List<String> relatedLinkList = new ArrayList<>();
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

        Map<String, List<String>> vServerRelatedLinkMap = new HashMap<>();

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
            vServerRelatedLinkMap = handleRelationshipVserver(vfmoduleArray, jsonPayload, catalog);
        }

        return vServerRelatedLinkMap;
    }

    private static List<String> handleRelationshipGeneral(String payload_str, String catalog) {

        if (payload_str == null) {
          return null;
        }
        JSONObject jsonPayload = new JSONObject(payload_str);

        JSONArray relationships = null;
        List<String> relatedLinkList = null;

        try {
            JSONObject relationshipList = jsonPayload.getJSONObject(RELATIONSHIP_LIST);
            if (relationshipList != null) {
                relatedLinkList = new ArrayList<>();
                relationships = relationshipList.getJSONArray(RELATIONSHIP);
            }
        } catch (JSONException e) {
            log.error(e.getMessage());
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
        return relatedLinkList;
    }

    private static Map<String, List<String>> handleRelationshipVserver(JSONArray vfmoduleArray, JSONObject vnf_jsonPayload, String catalog) {
        Map<String, List<String>> vServerRelatedLinkMap = new HashMap<>();
        JSONArray relationships = null;
        // If there are multiple vf-module, but one of vf-module missing relationship, we should log the exception and keep loop
        for (int i = 0; i < vfmoduleArray.length(); i++) {
            List<String> relatedLinkList = new ArrayList<>();
            JSONObject obj = vfmoduleArray.optJSONObject(i);
            String key = (String)obj.get("vf-module-id") + DELIMITER
                       + (String)obj.get("model-invariant-id");

            log.debug("Fetching the relationship");

            try {
                JSONObject relationshipList = vnf_jsonPayload.getJSONObject(RELATIONSHIP_LIST);
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
        MultivaluedHashMap<String, String> headers = new MultivaluedHashMap<>();
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
     *             "resource-link": "/aai/v13/business/customers/customer/DemoCust_651800ed-2a3c-45f5-b920-85c1ed155fc2/service-subscriptions/service-subscription/vFW/service-instances/service-instance/adc3cc2a-c73e-414f-8ddb-367de81300cb"
     *         }
     *     ]
     * }
     */
    private static String extractResourceLinkBasedOnResourceType(String payload, String catalog) throws AuditException {
        String resourceLink = null;
        log.info(String.format("Fetching the resource-link based on resource-type= %s", catalog));
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

    private static boolean isValid ( String inputField) {
        if (inputField == null) {
            return false;
        }
        String localInputField = inputField.trim();

        if (localInputField.equals("")) {
            return false;
        }

        return true;
    }
}
