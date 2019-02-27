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
package org.onap.logging_analytics.pomba.pomba_aai_context_builder.test;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.pomba.contextbuilder.aai.Application;
import org.onap.pomba.contextbuilder.aai.service.rs.RestService;
import org.onap.pomba.contextbuilder.aai.util.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.onap.pomba.common.datatypes.ModelContext;
import org.onap.pomba.common.datatypes.VFModule;
import org.onap.pomba.common.datatypes.VM;
import org.onap.pomba.common.datatypes.VNF;
import org.onap.pomba.common.datatypes.VNFC;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@WebAppConfiguration
@SpringBootTest (classes = Application.class)
@TestPropertySource(properties = { "aai.serviceName=localhost",
        "aai.servicePort=9806", "aai.httpProtocol=http",
        "http.userId=admin", "http.password=OBF:1u2a1toa1w8v1tok1u30"})
public class RestServiceTest {
    @Autowired
    private String aaiPathToSearchNodeQuery;

    @Autowired
    private String httpBasicAuthorization;

    @Autowired
    private RestService dummyRestSvc;

    private String testRestHeaders = "aai-context-builder";

    HttpHeaders mockHttpHeaders = mock(HttpHeaders.class);

    private static final String DEPTH = "?depth=2";

    @Rule
    public WireMockRule aaiEnricherRule = new WireMockRule(wireMockConfig().port(9806));

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

    private static MultivaluedMap<String, String> buildHeaders(
            String partnerName, String transactionId, String authorization) {

        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.put(RestUtil.FROM_APP_ID, Collections.singletonList(partnerName));
        headers.put(RestUtil.TRANSACTION_ID, Collections.singletonList(transactionId));
        if (null != authorization) {
            headers.put(RestUtil.AUTHORIZATION, Collections.singletonList(authorization));
        }
        return headers;
    }

    ///Verify the relationship serviceInstanceId -> vnf -> vserver -> pserver
    @Test
    public void testGetContext_VSERVER_PSERVER() throws Exception {

        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;

        // Test with No Partner Name
        final MultivaluedMap<String, String> multivaluedMapImpl = buildHeaders(
                transactionId, testRestHeaders, httpBasicAuthorization);

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
                        + "/b49b830686654191bb1e952a74b014ad/vservers/vserver/b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a" + DEPTH,
                "junit/aai-vserver.json", aaiEnricherRule);

        // 5. simulate the rsp of pserver
        // note: match pserver hostname to the path of "pserver" in (4)
        addResponse(
                "/aai/v13/cloud-infrastructure/pservers/pserver/mtn96compute.cci.att.com" + DEPTH,
                "junit/pserverInput_set2.json", aaiEnricherRule);

        when(mockHttpHeaders.getRequestHeaders()).thenReturn(multivaluedMapImpl);

        Response response = this.dummyRestSvc.getContext(mockHttpHeaders, httpBasicAuthorization, testRestHeaders, transactionId,
                serviceInstanceId);

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Gson gson = new Gson();
        ModelContext modelCtx = gson.fromJson((String) response.getEntity(), ModelContext.class);
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

    ///Verify the relationship serviceInstanceId -> vnf (containing L-Interface) -> vserver (containing - LInterfaceList)

    @Test
    public void testGetContext_VSERVER_L_interface_And_Logical_link() throws Exception {

        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;

        // Test with No Partner Name
        final MultivaluedMap<String, String> multivaluedMapImpl = buildHeaders(
                transactionId, testRestHeaders, httpBasicAuthorization);

        // 1. simulate the response to obtainResourceLink based on ServiceInstanceId
        addResponse(queryNodeUrl, "junit/queryNodeData-1.json", aaiEnricherRule);
        // 2. simulate the response of AAI (1 vnf)
        // note: match serviceInstanceId in (1)
        addResponse( "/aai/v13/business/customers/customer/DemoCust_651800ed-2a3c-45f5-b920-85c1ed155fc2/service-subscriptions/service-subscription/vFW/service-instances/service-instance/adc3cc2a-c73e-414f-8ddb-367de81300cb",
        "junit/aai-service-instance_set2.json", aaiEnricherRule);

        // 3. simulate the rsp of VNF (with 1 vserver)
        // note: match vnf_id in (2)
        addResponse( "/aai/v13/network/generic-vnfs/generic-vnf/8a9ddb25-2e79-449c-a40d-5011bac0da39" + DEPTH,
        "junit/genericVnfInput_set6.json", aaiEnricherRule);

        // 4. simulate the rsp of vserer
        // note: match to vserver-id to the path of "vserver" in (3)
        addResponse(
                "/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/CloudOwner/RegionOne/tenants/tenant"
                        + "/b49b830686654191bb1e952a74b014ad/vservers/vserver/b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a" + DEPTH,
                "junit/aai-vserver-set3.json", aaiEnricherRule);
        // 5. simulate the rsp of logical-link
        // note: match to link-name to the path of "relationship-list" in (3:  genericVnfInput_set6)
        // and match the link-name to (4 aai-vserver-set3.json)
        addResponse(
                "/aai/v13/network/logical-links/logical-link/01e8d84a-logical-link-1",
                "junit/logical-link-input1.json", aaiEnricherRule);
        addResponse(
                "/aai/v13/network/logical-links/logical-link/01e8d84a-logical-link-2",
                "junit/logical-link-input2.json", aaiEnricherRule);
        addResponse(
                "/aai/v13/network/logical-links/logical-link/01e8d84a-logical-link-3",
                "junit/logical-link-input3.json", aaiEnricherRule);
        addResponse(
                "/aai/v13/network/logical-links/logical-link/01e8d84a-logical-link-4",
                "junit/logical-link-input4.json", aaiEnricherRule);


        when(mockHttpHeaders.getRequestHeaders()).thenReturn(multivaluedMapImpl);

        Response response = this.dummyRestSvc.getContext(mockHttpHeaders, httpBasicAuthorization, testRestHeaders, transactionId,
                serviceInstanceId);

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Gson gson = new Gson();
        ModelContext modelCtx = gson.fromJson((String) response.getEntity(), ModelContext.class);
        // verify results
        List<VNF> vnfList = modelCtx.getVnfs();
        assertEquals(vnfList.size(), 1);
        assertEquals(vnfList.get(0).getLInterfaceList().size(), 2);
        assertEquals(vnfList.get(0).getLInterfaceList().get(0).getName(), "junit-l-interface-name3"); //l-interface-name
        assertEquals(vnfList.get(0).getLInterfaceList().get(1).getName(), "junit-l-interface-name4"); //l-interface-name
        assertEquals(vnfList.get(0).getLInterfaceList().get(0).getLogicalLinkList().size(), 1);
        assertEquals(vnfList.get(0).getLInterfaceList().get(0).getLogicalLinkList().get(0).getName(), "01e8d84a-logical-link-3"); //Logical-link at vnf->l-interface -> Logical-link
        assertEquals(vnfList.get(0).getLInterfaceList().get(1).getLogicalLinkList().size(), 1);
        assertEquals(vnfList.get(0).getLInterfaceList().get(1).getLogicalLinkList().get(0).getName(), "01e8d84a-logical-link-4");
        List<VFModule>  vfModuleList = vnfList.get(0).getVfModules();
        assertEquals(vfModuleList.size(), 1);
        List<VM> vmList = vfModuleList.get(0).getVms();
        assertEquals(vmList.size(), 1);
        assertEquals(vmList.get(0).getUuid(), "b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a"); //vserver-id
        assertEquals(vmList.get(0).getLInterfaceList().size(), 2);
        assertEquals(vmList.get(0).getLInterfaceList().get(0).getName(), "junit-l-interface-name1"); //l-interface-name
        assertEquals(vmList.get(0).getLInterfaceList().get(1).getName(), "junit-l-interface-name2"); //l-interface-name
        assertEquals(vmList.get(0).getLInterfaceList().get(0).getLogicalLinkList().size(), 1);
        assertEquals(vmList.get(0).getLInterfaceList().get(0).getLogicalLinkList().get(0).getName(), "01e8d84a-logical-link-1"); //Logical-link at vserver->l-interface -> Logical-link
        assertEquals(vmList.get(0).getLInterfaceList().get(1).getLogicalLinkList().size(), 1);
        assertEquals(vmList.get(0).getLInterfaceList().get(1).getLogicalLinkList().get(0).getName(), "01e8d84a-logical-link-2");

    }

    ///Verify the relationship serviceInstanceId -> vnf + vnfc
    @Test
    public void testGetContext_VNFC() throws Exception {

        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;

        // Test with No Partner Name
        final MultivaluedMap<String, String> multivaluedMapImpl = buildHeaders(
                transactionId, testRestHeaders, httpBasicAuthorization);

        // 1. simulate the response to obtainResourceLink based on ServiceInstanceId
        addResponse(queryNodeUrl, "junit/queryNodeData-1.json", aaiEnricherRule);
        // 2. simulate the response of AAI (1 vnf)
        // note: match serviceInstanceId in (1)
        addResponse( "/aai/v13/business/customers/customer/DemoCust_651800ed-2a3c-45f5-b920-85c1ed155fc2/service-subscriptions/service-subscription/vFW/service-instances/service-instance/adc3cc2a-c73e-414f-8ddb-367de81300cb",
        "junit/aai-service-instance_set2.json", aaiEnricherRule);

        // 3. simulate the rsp of VNF (with 1 vserver)
        // note: match vnf_id in (2)
        addResponse( "/aai/v13/network/generic-vnfs/generic-vnf/8a9ddb25-2e79-449c-a40d-5011bac0da39" + DEPTH,
        "junit/genericVnfInput_set5.json", aaiEnricherRule);

        // 4. simulate the rsp of vserer
        // note: match to vserver-id to the path of "vserver" in (3)
        addResponse(
                "/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/CloudOwner/RegionOne/tenants/tenant"
                        + "/b49b830686654191bb1e952a74b014ad/vservers/vserver/b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a" + DEPTH,
                "junit/aai-vserver-set2.json", aaiEnricherRule);

        // 5. simulate the rsp of vnfc
        // note: match to vnfc-name to the path of "vnfc" in (3)
        addResponse(
                "/aai/v13/network/vnfcs/vnfc/junit-vnfc-name1212",
                "junit/vnfc-input1.json", aaiEnricherRule);

        when(mockHttpHeaders.getRequestHeaders()).thenReturn(multivaluedMapImpl);

        Response response = this.dummyRestSvc.getContext(mockHttpHeaders, httpBasicAuthorization, testRestHeaders, transactionId,
                serviceInstanceId);

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Gson gson = new Gson();
        ModelContext modelCtx = gson.fromJson((String) response.getEntity(), ModelContext.class);
        // verify results
        List<VNF> vnfList = modelCtx.getVnfs();
        assertEquals(vnfList.size(), 1);
        List<VFModule>  vfModuleList = vnfList.get(0).getVfModules();
        assertEquals(vfModuleList.size(), 1);
        List<VM> vmList = vfModuleList.get(0).getVms();
        assertEquals(vmList.size(), 1);
        assertEquals(vmList.get(0).getUuid(), "b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a"); //vserver-id

        List<VNFC> vnfcList = vnfList.get(0).getVnfcs();
        assertEquals(vnfcList.size(), 1);
        assertEquals(vnfcList.get(0).getName(), "junit-vnfc-name1212"); //vnfc-name
    }

    ///Verify the relationship serviceInstanceId -> vnf + vnfc
    @Test
    public void testGetContext_VNFC2() throws Exception {

        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;

        // Test with No Partner Name
        final MultivaluedMap<String, String> multivaluedMapImpl = buildHeaders(
                transactionId, testRestHeaders, httpBasicAuthorization);

        // 1. simulate the response to obtainResourceLink based on ServiceInstanceId
        addResponse(queryNodeUrl, "junit/queryNodeData-1.json", aaiEnricherRule);
        // 2. simulate the response of AAI (1 vnf)
        // note: match serviceInstanceId in (1)
        addResponse( "/aai/v13/business/customers/customer/DemoCust_651800ed-2a3c-45f5-b920-85c1ed155fc2/service-subscriptions/service-subscription/vFW/service-instances/service-instance/adc3cc2a-c73e-414f-8ddb-367de81300cb",
        "junit/aai-service-instance_set2.json", aaiEnricherRule);

        // 3. simulate the rsp of VNF (with 1 vserver)
        // note: match vnf_id in (2)
        addResponse( "/aai/v13/network/generic-vnfs/generic-vnf/8a9ddb25-2e79-449c-a40d-5011bac0da39" + DEPTH,
        "junit/genericVnfInput_set5.json", aaiEnricherRule);

        // 4. simulate the rsp of vserer
        // note: match to vserver-id to the path of "vserver" in (3)
        addResponse(
                "/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/CloudOwner/RegionOne/tenants/tenant"
                        + "/b49b830686654191bb1e952a74b014ad/vservers/vserver/b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a" + DEPTH,
                "junit/aai-vserver-set2.json", aaiEnricherRule);

        // 5. simulate the rsp of vnfc
        // note: match to vnfc-name to the path of "vnfc" in (3)
        addResponse(
                "/aai/v13/network/vnfcs/vnfc/junit-vnfc-name1212",
                "junit/vnfc-input1.json", aaiEnricherRule);

        when(mockHttpHeaders.getRequestHeaders()).thenReturn(multivaluedMapImpl);

        Response response = this.dummyRestSvc.getContext(mockHttpHeaders, httpBasicAuthorization, testRestHeaders, transactionId,
                serviceInstanceId);

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Gson gson = new Gson();
        ModelContext modelCtx = gson.fromJson((String) response.getEntity(), ModelContext.class);
        // verify results
        List<VNF> vnfList = modelCtx.getVnfs();
        assertEquals(vnfList.size(), 1);
        List<VFModule>  vfModuleList = vnfList.get(0).getVfModules();
        assertEquals(vfModuleList.size(), 1);
        List<VM> vmList = vfModuleList.get(0).getVms();
        assertEquals(vmList.size(), 1);
        assertEquals(vmList.get(0).getUuid(), "b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a"); //vserver-id

        List<VNFC> vnfcList = vnfList.get(0).getVnfcs();
        assertEquals(vnfcList.size(), 1);
        assertEquals(vnfcList.get(0).getName(), "junit-vnfc-name1212"); //vnfc-name
    }

    ///Verify the relationship serviceInstanceId -> vnf + vnfc
    @Test
    public void testGetContext_LogicalLink_in_service_level() throws Exception {
        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;

        // Test with No Partner Name
        final MultivaluedMap<String, String> multivaluedMapImpl = buildHeaders(
                transactionId, testRestHeaders, httpBasicAuthorization);

        // 1. simulate the response to obtainResourceLink based on ServiceInstanceId
        addResponse(queryNodeUrl, "junit/queryNodeData-1.json", aaiEnricherRule);
        // 2. simulate the response of AAI (1 vnf)
        // note: match serviceInstanceId in (1)
        addResponse( "/aai/v13/business/customers/customer/DemoCust_651800ed-2a3c-45f5-b920-85c1ed155fc2/service-subscriptions/service-subscription/vFW/service-instances/service-instance/adc3cc2a-c73e-414f-8ddb-367de81300cb",
        "junit/aai-service-instance_set4.json", aaiEnricherRule);

        // 3. simulate the rsp of logical-link
        // note: match to link-name to the path of "relationship-list" in (2:  aai-service-instance_set4)
        addResponse(
                "/aai/v13/network/logical-links/logical-link/01e8d84a-logical-link-1",
                "junit/logical-link-input1.json", aaiEnricherRule);
        addResponse(
                "/aai/v13/network/logical-links/logical-link/01e8d84a-logical-link-2",
                "junit/logical-link-input2.json", aaiEnricherRule);

        when(mockHttpHeaders.getRequestHeaders()).thenReturn(multivaluedMapImpl);
        Response response = this.dummyRestSvc.getContext(mockHttpHeaders, httpBasicAuthorization, testRestHeaders, transactionId,
                serviceInstanceId);

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Gson gson = new Gson();
        ModelContext modelCtx = gson.fromJson((String) response.getEntity(), ModelContext.class);
        assertEquals(modelCtx.getLogicalLinkList().size(), 2);
        assertEquals(modelCtx.getLogicalLinkList().get(0).getName(), "01e8d84a-logical-link-1");
        assertEquals(modelCtx.getLogicalLinkList().get(1).getName(), "01e8d84a-logical-link-2");
    }

    ///Verify the relationship P-interface, L-interaface, Logical-link
    @Test
    public void testGetContext_LogicalLink_in_PInterface_level_with_PNF() throws Exception {
        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;

        // Test with No Partner Name
        final MultivaluedMap<String, String> multivaluedMapImpl = buildHeaders(
                transactionId, testRestHeaders, httpBasicAuthorization);

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
        "junit/pnfInput_w_pInterface_LInterface_set2.json", aaiEnricherRule);

        // 5. simulate the rsp of logical-link
        // note: match to link-name to the path of "relationship-list" in (4:  pnfInput_w_pInterface_LInterface_set2)
        addResponse(
                "/aai/v13/network/logical-links/logical-link/01e8d84a-logical-link-1",
                "junit/logical-link-input1.json", aaiEnricherRule);
        addResponse(
                "/aai/v13/network/logical-links/logical-link/01e8d84a-logical-link-2",
                "junit/logical-link-input2.json", aaiEnricherRule);

        when(mockHttpHeaders.getRequestHeaders()).thenReturn(multivaluedMapImpl);
        Response response = this.dummyRestSvc.getContext(mockHttpHeaders, httpBasicAuthorization, testRestHeaders, transactionId,
                serviceInstanceId);

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Gson gson = new Gson();
        ModelContext modelCtx = gson.fromJson((String) response.getEntity(), ModelContext.class);
        assertEquals(modelCtx.getVnfs().size(), 1);
        assertEquals(modelCtx.getPnfs().size(), 1);
        assertEquals(modelCtx.getPnfs().get(0).getPInterfaceList().size(), 1);
        assertEquals(modelCtx.getPnfs().get(0).getPInterfaceList().get(0).getLInterfaceList().size(), 2);
        assertEquals(modelCtx.getPnfs().get(0).getPInterfaceList().get(0).getLInterfaceList().get(0).getName(), "junit-l-interface-name5"); //l-interface-name
        assertEquals(modelCtx.getPnfs().get(0).getPInterfaceList().get(0).getLInterfaceList().get(1).getName(), "junit-l-interface-name6"); //l-interface-name
        assertEquals(modelCtx.getPnfs().get(0).getPInterfaceList().get(0).getLogicalLinkList().size(), 2);
        assertEquals(modelCtx.getPnfs().get(0).getPInterfaceList().get(0).getLogicalLinkList().get(0).getName(), "01e8d84a-logical-link-1");
        assertEquals(modelCtx.getPnfs().get(0).getPInterfaceList().get(0).getLogicalLinkList().get(1).getName(), "01e8d84a-logical-link-2");
    }

    ///Verify the relationship P-interface, L-interaface, Logical-link
    @Test
    public void testGetContext_LogicalLink_in_PInterface_level_with_PSERVER() throws Exception {
        String transactionId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = aaiPathToSearchNodeQuery + serviceInstanceId;

        // Test with No Partner Name
        final MultivaluedMap<String, String> multivaluedMapImpl = buildHeaders(
                transactionId, testRestHeaders, httpBasicAuthorization);

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
                        + "/b49b830686654191bb1e952a74b014ad/vservers/vserver/b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a" + DEPTH,
                "junit/aai-vserver.json", aaiEnricherRule);

        // 5. simulate the rsp of pserver with P-interface which also contains L-interface.
        // note: match pserver hostname to the path of "pserver" in (4)
        addResponse(
                "/aai/v13/cloud-infrastructure/pservers/pserver/mtn96compute.cci.att.com" + DEPTH,
                "junit/pserverInput_with_pInterface_LInterface_set2.json", aaiEnricherRule);

        // 6. simulate the rsp of logical-link
        // note: match to link-name to the path of "relationship-list" in (5:  pserverInput_with_pInterface_LInterface_set2)
        addResponse(
                "/aai/v13/network/logical-links/logical-link/01e8d84a-logical-link-1",
                "junit/logical-link-input1.json", aaiEnricherRule);
        addResponse(
                "/aai/v13/network/logical-links/logical-link/01e8d84a-logical-link-2",
                "junit/logical-link-input2.json", aaiEnricherRule);
        addResponse(
                "/aai/v13/network/logical-links/logical-link/01e8d84a-logical-link-3",
                "junit/logical-link-input3.json", aaiEnricherRule);
        addResponse(
                "/aai/v13/network/logical-links/logical-link/01e8d84a-logical-link-4",
                "junit/logical-link-input4.json", aaiEnricherRule);

        when(mockHttpHeaders.getRequestHeaders()).thenReturn(multivaluedMapImpl);
        Response response = this.dummyRestSvc.getContext(mockHttpHeaders, httpBasicAuthorization, testRestHeaders, transactionId,
                serviceInstanceId);

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        Gson gson = new Gson();
        ModelContext modelCtx = gson.fromJson((String) response.getEntity(), ModelContext.class);

        List<VNF> vnfList = modelCtx.getVnfs();
        assertEquals(vnfList.size(), 1);
        List<VFModule>  vfModuleList = vnfList.get(0).getVfModules();
        assertEquals(vfModuleList.size(), 1);
        List<VM> vmList = vfModuleList.get(0).getVms();
        assertEquals(vmList.size(), 1);
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().size(), 1);
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getName(), "bdc3cc2a-c73e-414f-7ddb-367de92801cb"); //interface-name
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getLInterfaceList().size(), 2);
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getLInterfaceList().get(0).getName(), "junit-l-interface-name7"); //Vserver-> Pserver-> P-interface -> l-interface
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getLInterfaceList().get(1).getName(), "junit-l-interface-name8");
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getLInterfaceList().get(0).getLogicalLinkList().size(), 1);
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getLInterfaceList().get(0).getLogicalLinkList().get(0).getName(), "01e8d84a-logical-link-3");  //Vserver-> Pserver-> P-interface -> l-interface -> logical-link
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getLInterfaceList().get(1).getLogicalLinkList().size(), 1);
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getLInterfaceList().get(1).getLogicalLinkList().get(0).getName(), "01e8d84a-logical-link-4");
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getLogicalLinkList().size(), 2);
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getLogicalLinkList().get(0).getName(), "01e8d84a-logical-link-1"); //Vserver-> Pserver-> P-interface -> Logical-link
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getLogicalLinkList().get(1).getName(), "01e8d84a-logical-link-2");
    }
}
