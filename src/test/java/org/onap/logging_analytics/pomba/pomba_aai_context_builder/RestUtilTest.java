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

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.aai.restclient.client.RestClient;
import org.onap.pomba.contextbuilder.aai.Application;
import org.onap.pomba.contextbuilder.aai.exception.AuditException;
import org.onap.pomba.contextbuilder.aai.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.onap.pomba.common.datatypes.ModelContext;
import org.onap.pomba.common.datatypes.VNF;
import org.onap.pomba.common.datatypes.VFModule;
import org.onap.pomba.common.datatypes.VM;
import org.onap.pomba.common.datatypes.Network;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
@TestPropertySource(properties = { "aai.serviceName=localhost","aai.servicePort=9808", "aai.httpProtocol=http" })
public class RestUtilTest {
    @Autowired
    private RestClient aaiClient;
    @Autowired
    private String aaiBaseUrl;
    @Autowired
    private String aaiPathToSearchNodeQuery;
    @Autowired
    private String aaiBasicAuthorization;

    private static final String DEPTH = "?depth=2";

    @Rule
    public WireMockRule aaiEnricherRule = new WireMockRule(wireMockConfig().port(9808));

    @Test
    public void testValidateURL() {
        // Missing ServiceInstanceId or it is null
        try {
            RestUtil.validateURL("");
        } catch (AuditException e) {
            assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: serviceInstanceId"));
        }

        try {
            RestUtil.validateURL(null);
        } catch (AuditException e) {
            assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: serviceInstanceId"));
        }

    }

    @Test
    public void testIsEmptyJson() {
        assertTrue(RestUtil.isEmptyJson("{}"));
        assertTrue(!RestUtil.isEmptyJson("{Not Empty}"));
    }

    @Test
    public void testObtainResouceLinkBasedOnServiceInstanceFromAAI() throws Exception {
        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;
        addResponse(queryNodeUrl, "junit/queryNodeData-1.json", aaiEnricherRule);

        String resourceLinkUlr = RestUtil.obtainResouceLinkBasedOnServiceInstanceFromAAI(aaiClient, aaiBaseUrl, aaiPathToSearchNodeQuery, serviceInstanceId, transactionId, aaiBasicAuthorization);

        String returnedInstanceId = resourceLinkUlr.substring(resourceLinkUlr.lastIndexOf("/")+1).trim();
        assertEquals(serviceInstanceId, returnedInstanceId);
    }

    @Test
    public void testObtainResouceLinkBasedOnServiceInstanceFromAAI_nullResourceLink() throws Exception {
        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;
        addResponse(queryNodeUrl, "junit/queryNodeData-nullResourceLink.json", aaiEnricherRule);

        try {
            RestUtil.obtainResouceLinkBasedOnServiceInstanceFromAAI(aaiClient, aaiBaseUrl, aaiPathToSearchNodeQuery, serviceInstanceId, transactionId, aaiBasicAuthorization);
        } catch (AuditException e) {
            assertTrue(e.getMessage().contains("JSONObject[\"resource-link\"] not found"));
        }
    }

    private void addResponse(String path, String classpathResource, WireMockRule thisMock) throws IOException {
        String payload = readFully(ClassLoader.getSystemResourceAsStream(classpathResource));
        thisMock.stubFor(get(path).willReturn(okJson(payload)));
    }

    private String readFully(InputStream in) throws IOException {
        char[] cbuf = new char[1024];
        StringBuilder content = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(in, "UTF-8")) {
            int count;
            while ((count = reader.read(cbuf)) >= 0) {
                content.append(cbuf, 0, count);
            }
        }
        return content.toString();
    }


    ////
    @Test
    public void testretrieveAAIModelDataFromAAI_PNF() throws Exception {

        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;
        // 1. simulate the response to obtainResourceLink based on ServiceInstanceId
        addResponse(queryNodeUrl, "junit/queryNodeData-1.json", aaiEnricherRule);
        // 2. simulate the response of AAI (1 vnf and 1 pnf)
        addResponse( "/aai/v13/business/customers/customer/DemoCust_651800ed-2a3c-45f5-b920-85c1ed155fc2/service-subscriptions/service-subscription/vFW/service-instances/service-instance/adc3cc2a-c73e-414f-8ddb-367de81300cb",
        "junit/aai-service-instance.json", aaiEnricherRule);

        // 3. simulate the rsp of VNF
        addResponse( "/aai/v13/network/generic-vnfs/generic-vnf/8a9ddb25-2e79-449c-a40d-5011bac0da39" + DEPTH,
        "junit/genericVnfInput.json", aaiEnricherRule);

        // 4. simulate the response of PNF based on the resourceLink in (2)
        //note: match pnf_id in junit/aai-service-instance.json
        addResponse( "/aai/v13/network/pnfs/pnf/amdocsPnfName" + DEPTH,
        "junit/pnfSampleInput.json", aaiEnricherRule);

        ModelContext modelCtx = RestUtil.retrieveAAIModelData(aaiClient, aaiBaseUrl, aaiPathToSearchNodeQuery, transactionId , serviceInstanceId, aaiBasicAuthorization);

        assertEquals(modelCtx.getVnfs().size(), 1);
        assertEquals(modelCtx.getPnfs().size(), 1);

    }
    ///Verify the relationship serviceInstanceId -> vnf -> vserver
    @Test
    public void testretrieveAAIModelDataFromAAI_VSERVER_PSERVER() throws Exception {

        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;
        // 1. simulate the response to obtainResourceLink based on ServiceInstanceId
        addResponse(queryNodeUrl, "junit/queryNodeData-1.json", aaiEnricherRule);
        // 2. simulate the response of AAI (1 vnf)
        // note: match serviceInstanceId in (1)
        addResponse( "/aai/v13/business/customers/customer/DemoCust_651800ed-2a3c-45f5-b920-85c1ed155fc2/service-subscriptions/service-subscription/vFW/service-instances/service-instance/adc3cc2a-c73e-414f-8ddb-367de81300cb",
        "junit/aai-service-instance_set2.json", aaiEnricherRule);

        // 3. simulate the rsp of VNF (with 1 vserver)
        // note: match vnf_id in (2)
        addResponse( "/aai/v13/network/generic-vnfs/generic-vnf/8a9ddb25-2e79-449c-a40d-5011bac0da39" + DEPTH,
        "junit/genericVnfInput_set2.json", aaiEnricherRule);

        // 4. simulate the rsp of vserer
        // note: match to vserver-id to the path of "vserver" in (3)
        addResponse(
                "/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/CloudOwner/RegionOne/tenants/tenant"
                        + "/b49b830686654191bb1e952a74b014ad/vservers/vserver/b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a",
                "junit/aai-vserver.json", aaiEnricherRule);

        // 5. simulate the rsp of pserver
        // note: match pserver hostname to the path of "pserver" in (4)
        addResponse(
                "/aai/v13/cloud-infrastructure/pservers/pserver/mtn96compute.cci.att.com" + DEPTH,
                "junit/pserverInput_set2.json", aaiEnricherRule);

        ModelContext modelCtx = RestUtil.retrieveAAIModelData(aaiClient, aaiBaseUrl, aaiPathToSearchNodeQuery, transactionId , serviceInstanceId, aaiBasicAuthorization);

        // verify results
        List<VNF> vnfList = modelCtx.getVnfs();
        assertEquals(vnfList.size(), 1);
        List<VFModule>  vfModuleList = vnfList.get(0).getVfModules();
        assertEquals(vfModuleList.size(), 1);
        List<VM> vmList = vfModuleList.get(0).getVms();
        assertEquals(vmList.size(), 1);
        assertEquals(vmList.get(0).getUuid(), "b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a"); //vserver-id
        assertEquals(vmList.get(0).getPServer().getName(), "mtn96compute.cci.att.com"); //pserver-name
    }

    ////
    @Test
    public void testretrieveAAIModelDataFromAAI_PInterface_with_PNF() throws Exception {

        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;
        // 1. simulate the response to obtainResourceLink based on ServiceInstanceId
        addResponse(queryNodeUrl, "junit/queryNodeData-1.json", aaiEnricherRule);
        // 2. simulate the response of AAI (1 vnf and 1 pnf)
        addResponse( "/aai/v13/business/customers/customer/DemoCust_651800ed-2a3c-45f5-b920-85c1ed155fc2/service-subscriptions/service-subscription/vFW/service-instances/service-instance/adc3cc2a-c73e-414f-8ddb-367de81300cb",
        "junit/aai-service-instance.json", aaiEnricherRule);

        // 3. simulate the rsp of VNF
        addResponse( "/aai/v13/network/generic-vnfs/generic-vnf/8a9ddb25-2e79-449c-a40d-5011bac0da39" + DEPTH,
        "junit/genericVnfInput.json", aaiEnricherRule);

        // 4. simulate the response of PNF based on the resourceLink in (2)
        //note: match pnf_id in junit/aai-service-instance.json
        addResponse( "/aai/v13/network/pnfs/pnf/amdocsPnfName" + DEPTH,
        "junit/pnfInput_w_pInterface.json", aaiEnricherRule);

        ModelContext modelCtx = RestUtil.retrieveAAIModelData(aaiClient, aaiBaseUrl, aaiPathToSearchNodeQuery, transactionId , serviceInstanceId, aaiBasicAuthorization);

        assertEquals(modelCtx.getVnfs().size(), 1);
        assertEquals(modelCtx.getPnfs().size(), 1);

    }

    @Test
    public void testretrieveAAIModelDataFromAAI_P_Interface_with_PSERVER() throws Exception {

        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;
        // 1. simulate the response to obtainResourceLink based on ServiceInstanceId
        addResponse(queryNodeUrl, "junit/queryNodeData-1.json", aaiEnricherRule);
        // 2. simulate the response of AAI (1 vnf)
        // note: match serviceInstanceId in (1)
        addResponse( "/aai/v13/business/customers/customer/DemoCust_651800ed-2a3c-45f5-b920-85c1ed155fc2/service-subscriptions/service-subscription/vFW/service-instances/service-instance/adc3cc2a-c73e-414f-8ddb-367de81300cb",
        "junit/aai-service-instance_set2.json", aaiEnricherRule);

        // 3. simulate the rsp of VNF (with 1 vserver)
        // note: match vnf_id in (2)
        addResponse( "/aai/v13/network/generic-vnfs/generic-vnf/8a9ddb25-2e79-449c-a40d-5011bac0da39" + DEPTH,
        "junit/genericVnfInput_set2.json", aaiEnricherRule);

        // 4. simulate the rsp of vserer
        // note: match to vserver-id to the path of "vserver" in (3)
        addResponse(
                "/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/CloudOwner/RegionOne/tenants/tenant"
                        + "/b49b830686654191bb1e952a74b014ad/vservers/vserver/b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a",
                "junit/aai-vserver.json", aaiEnricherRule);

        // 5. simulate the rsp of pserver
        // note: match pserver hostname to the path of "pserver" in (4)
        addResponse(
                "/aai/v13/cloud-infrastructure/pservers/pserver/mtn96compute.cci.att.com" + DEPTH,
                "junit/pserverInput_with_pInterface.json", aaiEnricherRule);

        ModelContext modelCtx = RestUtil.retrieveAAIModelData(aaiClient, aaiBaseUrl, aaiPathToSearchNodeQuery, transactionId , serviceInstanceId, aaiBasicAuthorization);

        // verify results
        List<VNF> vnfList = modelCtx.getVnfs();
        assertEquals(vnfList.size(), 1);
        List<VFModule>  vfModuleList = vnfList.get(0).getVfModules();
        assertEquals(vfModuleList.size(), 1);
        List<VM> vmList = vfModuleList.get(0).getVms();
        assertEquals(vmList.size(), 1);
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().size(), 1);
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getName(), "bdc3cc2a-c73e-414f-7ddb-367de92801cb"); //interface-name
    }

    ///Verify the relationship serviceInstanceId -> l3network
    @Test
    public void testretrieveAAIModelDataFromAAI_L3_network_in_service_level () throws Exception {

        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;
        // 1. simulate the response to obtainResourceLink based on ServiceInstanceId
        addResponse(queryNodeUrl, "junit/queryNodeData-1.json", aaiEnricherRule);
        // 2. simulate the response of AAI (1 vnf)
        // note: match serviceInstanceId in (1)
        addResponse( "/aai/v13/business/customers/customer/DemoCust_651800ed-2a3c-45f5-b920-85c1ed155fc2/service-subscriptions/service-subscription/vFW/service-instances/service-instance/adc3cc2a-c73e-414f-8ddb-367de81300cb",
        "junit/aai-service-instance_set3.json", aaiEnricherRule);

        // 3. simulate the rsp of l3-network
        // note: match to network-id to the path of "l3network" in (2:  aai-service-instance_set3)
        addResponse(
                "/aai/v13/network/l3-networks/l3-network/01e8d84a-l3-network-1" + DEPTH,
                "junit/l3-network-1.json", aaiEnricherRule);
        addResponse(
                "/aai/v13/network/l3-networks/l3-network/01e8d84a-l3-network-2" + DEPTH,
                "junit/l3-network-2.json", aaiEnricherRule);


        ModelContext modelCtx = RestUtil.retrieveAAIModelData(aaiClient, aaiBaseUrl, aaiPathToSearchNodeQuery, transactionId , serviceInstanceId, aaiBasicAuthorization);

        // verify results
        List<Network> networkList = modelCtx.getNetworkList();
        assertEquals(networkList.size(), 2);
        assertEquals(networkList.get(0).getUuid(), "01e8d84a-l3-network-1");
        assertEquals(networkList.get(1).getUuid(), "01e8d84a-l3-network-2");
    }

    ///Verify the relationship serviceInstanceId -> vnf -> l3network
    @Test
    public void testretrieveAAIModelDataFromAAI_L3_network_in_VNF_level() throws Exception {

        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;
        // 1. simulate the response to obtainResourceLink based on ServiceInstanceId
        addResponse(queryNodeUrl, "junit/queryNodeData-1.json", aaiEnricherRule);
        // 2. simulate the response of AAI (1 vnf)
        // note: match serviceInstanceId in (1)
        addResponse( "/aai/v13/business/customers/customer/DemoCust_651800ed-2a3c-45f5-b920-85c1ed155fc2/service-subscriptions/service-subscription/vFW/service-instances/service-instance/adc3cc2a-c73e-414f-8ddb-367de81300cb",
        "junit/aai-service-instance_set2.json", aaiEnricherRule);

        // 3. simulate the rsp of VNF (with 1 vserver)
        // note: match vnf_id in (2)
        addResponse( "/aai/v13/network/generic-vnfs/generic-vnf/8a9ddb25-2e79-449c-a40d-5011bac0da39" + DEPTH,
        "junit/genericVnfInput_set3.json", aaiEnricherRule);

        // 4. simulate the rsp of vserer
        // note: match to vserver-id to the path of "vserver" in (3)
        addResponse(
                "/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/CloudOwner/RegionOne/tenants/tenant"
                        + "/b49b830686654191bb1e952a74b014ad/vservers/vserver/b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a",
                "junit/aai-vserver-set2.json", aaiEnricherRule);

        // 5. simulate the rsp of l3-network
        // note: match to network-id to the path of "l3network" in (3:  genericVnfInput_set3)
        addResponse(
                "/aai/v13/network/l3-networks/l3-network/01e8d84a-l3-network-1" + DEPTH,
                "junit/l3-network-1.json", aaiEnricherRule);

        ModelContext modelCtx = RestUtil.retrieveAAIModelData(aaiClient, aaiBaseUrl, aaiPathToSearchNodeQuery, transactionId , serviceInstanceId, aaiBasicAuthorization);

        // verify results
        List<VNF> vnfList = modelCtx.getVnfs();
        assertEquals(vnfList.size(), 1);
        List<Network> networkList = vnfList.get(0).getNetworks();
        assertEquals(networkList.size(), 1);
        assertEquals(networkList.get(0).getUuid(), "01e8d84a-l3-network-1");
    }

    @Test
    public void testretrieveAAIModelDataFromAAI_L3_network_in_vModule_level() throws Exception {

        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;
        // 1. simulate the response to obtainResourceLink based on ServiceInstanceId
        addResponse(queryNodeUrl, "junit/queryNodeData-1.json", aaiEnricherRule);
        // 2. simulate the response of AAI (1 vnf)
        // note: match serviceInstanceId in (1)
        addResponse( "/aai/v13/business/customers/customer/DemoCust_651800ed-2a3c-45f5-b920-85c1ed155fc2/service-subscriptions/service-subscription/vFW/service-instances/service-instance/adc3cc2a-c73e-414f-8ddb-367de81300cb",
        "junit/aai-service-instance_set2.json", aaiEnricherRule);

        // 3. simulate the rsp of VNF (with 1 vserver)
        // note: match vnf_id in (2)
        addResponse( "/aai/v13/network/generic-vnfs/generic-vnf/8a9ddb25-2e79-449c-a40d-5011bac0da39" + DEPTH,
        "junit/genericVnfInput_set4.json", aaiEnricherRule);

        // 4. simulate the rsp of vserer
        // note: match to vserver-id to the path of "vserver" in (3)
        addResponse(
                "/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/CloudOwner/RegionOne/tenants/tenant"
                        + "/b49b830686654191bb1e952a74b014ad/vservers/vserver/b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a",
                "junit/aai-vserver-set2.json", aaiEnricherRule);

        // 5. simulate the rsp of l3-network
        // note: match to network-id to the path of "l3network" in (3:  genericVnfInput_set4)
        addResponse(
                "/aai/v13/network/l3-networks/l3-network/01e8d84a-l3-network-1" + DEPTH,
                "junit/l3-network-1.json", aaiEnricherRule);
        addResponse(
                "/aai/v13/network/l3-networks/l3-network/01e8d84a-l3-network-2" + DEPTH,
                "junit/l3-network-2.json", aaiEnricherRule);


        ModelContext modelCtx = RestUtil.retrieveAAIModelData(aaiClient, aaiBaseUrl, aaiPathToSearchNodeQuery, transactionId , serviceInstanceId, aaiBasicAuthorization);

        // verify results
        List<VNF> vnfList = modelCtx.getVnfs();
        assertEquals(vnfList.size(), 1);
        List<VFModule>  vfModuleList = vnfList.get(0).getVfModules();
        assertEquals(vfModuleList.size(), 1);

        List<Network> networkList = vfModuleList.get(0).getNetworks();
        assertEquals(networkList.size(), 2);
        assertEquals(networkList.get(0).getUuid(), "01e8d84a-l3-network-1");
        assertEquals(networkList.get(1).getUuid(), "01e8d84a-l3-network-2");
    }

}
