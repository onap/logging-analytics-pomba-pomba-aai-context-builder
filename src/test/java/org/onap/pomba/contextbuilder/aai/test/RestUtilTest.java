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
package org.onap.pomba.contextbuilder.aai.test;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.aai.restclient.client.RestClient;
import org.onap.pomba.common.datatypes.ModelContext;
import org.onap.pomba.common.datatypes.Network;
import org.onap.pomba.common.datatypes.VFModule;
import org.onap.pomba.common.datatypes.VM;
import org.onap.pomba.common.datatypes.VNF;
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

    HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);

    private String partnerName = "RestUtilTest";

    private static final String DEPTH = "?depth=2";

    @Rule
    public WireMockRule aaiEnricherRule = new WireMockRule(wireMockConfig().port(9808));

    @Test
    public void testValidateServiceInstanceId() {
        // Missing ServiceInstanceId or it is null
        try {
            RestUtil.validateServiceInstanceId("");
        } catch (AuditException e) {
            assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: serviceInstanceId"));
        }

        try {
            RestUtil.validateServiceInstanceId(null);
        } catch (AuditException e) {
            assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: serviceInstanceId"));
        }

    }

    @Test
    public void testIsEmptyJson() {
        assertTrue(RestUtil.isEmptyJson("{}"));
        assertTrue(!RestUtil.isEmptyJson("{Not Empty}"));
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

    private static String generateGetCustomerInfoUrl (String baseURL, String aaiPathToSearchNodeQuery,String serviceInstanceId) {
        return baseURL + MessageFormat.format(aaiPathToSearchNodeQuery, serviceInstanceId);
    }

    ////
    @Test
    public void testretrieveAaiModelDataFromAaiPnf() throws Exception {

        String requestId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json

        String queryNodeUrl = generateGetCustomerInfoUrl("", aaiPathToSearchNodeQuery, serviceInstanceId);

        // 2. simulate the response of AAI (1 vnf and 1 pnf)
        addResponse( queryNodeUrl,
        "junit/aai-service-instance.json", aaiEnricherRule);

        // 3. simulate the rsp of VNF
        addResponse( "/aai/v13/network/generic-vnfs/generic-vnf/8a9ddb25-2e79-449c-a40d-5011bac0da39" + DEPTH,
        "junit/genericVnfInput.json", aaiEnricherRule);

        // 4. simulate the response of PNF based on the resourceLink in (2)
        //note: match pnf_id in junit/aai-service-instance.json
        addResponse( "/aai/v13/network/pnfs/pnf/amdocsPnfName" + DEPTH,
        "junit/pnfSampleInput.json", aaiEnricherRule);

        when(mockHttpServletRequest.getRemoteAddr()).thenReturn("testretrieveAaiModelDataFromAaiPnf");

        ModelContext modelCtx = RestUtil.retrieveAAIModelData(aaiClient,
                                                              aaiBaseUrl,
                                                              aaiPathToSearchNodeQuery,
                                                              mockHttpServletRequest,
                                                              requestId,
                                                              partnerName,
                                                              serviceInstanceId,
                                                              aaiBasicAuthorization);
        assertEquals(modelCtx.getVnfs().size(), 1);
        assertEquals(modelCtx.getPnfs().size(), 1);

    }
    ///Verify the relationship serviceInstanceId -> vnf -> vserver
    @Test
    public void testretrieveAaiModelDataFromAaiVserverPserver() throws Exception {

        String requestId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json

        String queryNodeUrl = generateGetCustomerInfoUrl("", aaiPathToSearchNodeQuery, serviceInstanceId);

        // 2. simulate the response of AAI (1 vnf)
        addResponse( queryNodeUrl,
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

        when(mockHttpServletRequest.getRemoteAddr()).thenReturn("testretrieveAaiModelDataFromAaiVserverPserver");

        ModelContext modelCtx = RestUtil.retrieveAAIModelData(aaiClient,
                                                              aaiBaseUrl,
                                                              aaiPathToSearchNodeQuery,
                                                              mockHttpServletRequest,
                                                              requestId,
                                                              partnerName,
                                                              serviceInstanceId,
                                                              aaiBasicAuthorization);
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
    public void testretrieveAaiModelDataFromAaiPinterfaceLinterfaceWithPnf() throws Exception {

        String requestId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = generateGetCustomerInfoUrl("", aaiPathToSearchNodeQuery, serviceInstanceId);
        // 2. simulate the response of AAI (1 vnf and 1 pnf)
        addResponse( queryNodeUrl,
        "junit/aai-service-instance.json", aaiEnricherRule);

        // 3. simulate the rsp of VNF
        addResponse( "/aai/v13/network/generic-vnfs/generic-vnf/8a9ddb25-2e79-449c-a40d-5011bac0da39" + DEPTH,
        "junit/genericVnfInput.json", aaiEnricherRule);

        // 4. simulate the response of PNF based on the resourceLink in (2)
        //note: match pnf_id in junit/aai-service-instance.json
        addResponse( "/aai/v13/network/pnfs/pnf/amdocsPnfName" + DEPTH,
        "junit/pnfInput_w_pInterface_LInterface.json", aaiEnricherRule);

        when(mockHttpServletRequest
                .getRemoteAddr())
                .thenReturn("testretrieveAaiModelDataFromAaiPinterfaceLinterfaceWithPnf");

        ModelContext modelCtx = RestUtil.retrieveAAIModelData(aaiClient,
                aaiBaseUrl,
                aaiPathToSearchNodeQuery,
                mockHttpServletRequest,
                requestId,
                partnerName,
                serviceInstanceId,
                aaiBasicAuthorization);

        assertEquals(modelCtx.getVnfs().size(), 1);
        assertEquals(modelCtx.getPnfs().size(), 1);
        assertEquals(modelCtx.getPnfs().get(0).getPInterfaceList().size(), 1);
        assertEquals(modelCtx.getPnfs().get(0).getPInterfaceList().get(0).getLInterfaceList().size(), 2);
        assertEquals(modelCtx.getPnfs().get(0).getPInterfaceList().get(0).getLInterfaceList().get(0).getName(), "junit-l-interface-name5"); //l-interface-name
        assertEquals(modelCtx.getPnfs().get(0).getPInterfaceList().get(0).getLInterfaceList().get(1).getName(), "junit-l-interface-name6"); //l-interface-name
    }

    @Test
    public void testretrieveAaiModelDataFromAaiPinterfaceLinterfaceWithPserver() throws Exception {

        String requestId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = generateGetCustomerInfoUrl("", aaiPathToSearchNodeQuery, serviceInstanceId);
        // 2. simulate the response of AAI (1 vnf)
        addResponse( queryNodeUrl,
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
                "junit/pserverInput_with_pInterface_LInterface.json", aaiEnricherRule);

        when(mockHttpServletRequest
                .getRemoteAddr())
                .thenReturn("testretrieveAaiModelDataFromAaiPinterfaceLinterfaceWithPserver");

        ModelContext modelCtx = RestUtil.retrieveAAIModelData(aaiClient,
                aaiBaseUrl,
                aaiPathToSearchNodeQuery,
                mockHttpServletRequest,
                requestId,
                partnerName,
                serviceInstanceId,
                aaiBasicAuthorization);

        // verify results
        List<VNF> vnfList = modelCtx.getVnfs();
        assertEquals(vnfList.size(), 1);
        List<VFModule>  vfModuleList = vnfList.get(0).getVfModules();
        assertEquals(vfModuleList.size(), 1);
        List<VM> vmList = vfModuleList.get(0).getVms();
        assertEquals(vmList.size(), 1);
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().size(), 1);
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getName(), "bdc3cc2a-c73e-414f-7ddb-367de92801cb"); //interface-name
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getLInterfaceList().size(), 2);
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getLInterfaceList().get(0).getName(), "junit-l-interface-name7"); //l-interface-name
        assertEquals(vmList.get(0).getPServer().getPInterfaceList().get(0).getLInterfaceList().get(1).getName(), "junit-l-interface-name8"); //l-interface-name
    }

    ///Verify the relationship serviceInstanceId -> l3network
    @Test
    public void testretrieveAaiModelDataFromAaiL3NetworkInServiceLevel () throws Exception {

        String requestId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = generateGetCustomerInfoUrl("", aaiPathToSearchNodeQuery, serviceInstanceId);
        // 2. simulate the response of AAI (1 vnf)
        addResponse( queryNodeUrl,
        "junit/aai-service-instance_set3.json", aaiEnricherRule);

        // 3. simulate the rsp of l3-network
        // note: match to network-id to the path of "l3network" in (2:  aai-service-instance_set3)
        addResponse(
                "/aai/v13/network/l3-networks/l3-network/01e8d84a-l3-network-1" + DEPTH,
                "junit/l3-network-1.json", aaiEnricherRule);
        addResponse(
                "/aai/v13/network/l3-networks/l3-network/01e8d84a-l3-network-2" + DEPTH,
                "junit/l3-network-2.json", aaiEnricherRule);


        when(mockHttpServletRequest
                .getRemoteAddr())
                .thenReturn("testretrieveAaiModelDataFromAaiL3NetworkInServiceLevel");

        ModelContext modelCtx = RestUtil.retrieveAAIModelData(aaiClient,
                aaiBaseUrl,
                aaiPathToSearchNodeQuery,
                mockHttpServletRequest,
                requestId,
                partnerName,
                serviceInstanceId,
                aaiBasicAuthorization);

        // verify results
        List<Network> networkList = modelCtx.getNetworkList();
        assertEquals(networkList.size(), 2);
        assertEquals(networkList.get(0).getUuid(), "01e8d84a-l3-network-1");
        assertEquals(networkList.get(1).getUuid(), "01e8d84a-l3-network-2");
    }

    ///Verify the relationship serviceInstanceId -> vnf -> l3network
    @Test
    public void testretrieveAaiModelDataFromAaiL3NetworkInVnfLevel() throws Exception {

        String requestId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = generateGetCustomerInfoUrl("", aaiPathToSearchNodeQuery, serviceInstanceId);
        // 2. simulate the response of AAI (1 vnf)
        addResponse( queryNodeUrl,
        "junit/aai-service-instance_set2.json", aaiEnricherRule);

        // 3. simulate the rsp of VNF (with 1 vserver)
        // note: match vnf_id in (2)
        addResponse( "/aai/v13/network/generic-vnfs/generic-vnf/8a9ddb25-2e79-449c-a40d-5011bac0da39" + DEPTH,
        "junit/genericVnfInput_set3.json", aaiEnricherRule);

        // 4. simulate the rsp of vserer
        // note: match to vserver-id to the path of "vserver" in (3)
        addResponse(
                "/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/CloudOwner/RegionOne/tenants/tenant"
                        + "/b49b830686654191bb1e952a74b014ad/vservers/vserver/b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a" + DEPTH,
                "junit/aai-vserver-set2.json", aaiEnricherRule);

        // 5. simulate the rsp of l3-network
        // note: match to network-id to the path of "l3network" in (3:  genericVnfInput_set3)
        addResponse(
                "/aai/v13/network/l3-networks/l3-network/01e8d84a-l3-network-1" + DEPTH,
                "junit/l3-network-1.json", aaiEnricherRule);

        when(mockHttpServletRequest
                .getRemoteAddr())
                .thenReturn("testretrieveAaiModelDataFromAaiL3NetworkInVnfLevel");

        ModelContext modelCtx = RestUtil.retrieveAAIModelData(aaiClient,
                aaiBaseUrl,
                aaiPathToSearchNodeQuery,
                mockHttpServletRequest,
                requestId,
                partnerName,
                serviceInstanceId,
                aaiBasicAuthorization);

        // verify results
        List<VNF> vnfList = modelCtx.getVnfs();
        assertEquals(vnfList.size(), 1);
        List<Network> networkList = vnfList.get(0).getNetworks();
        assertEquals(networkList.size(), 1);
        assertEquals(networkList.get(0).getUuid(), "01e8d84a-l3-network-1");
    }

    @Test
    public void testretrieveAaiModelDataFromAaiL3NetworkInvModuleLevel() throws Exception {

        String requestId = UUID.randomUUID().toString();
        String serviceInstanceId = "adc3cc2a-c73e-414f-8ddb-367de81300cb"; //match to the test data in junit/queryNodeData-1.json
        String queryNodeUrl = generateGetCustomerInfoUrl("", aaiPathToSearchNodeQuery, serviceInstanceId);
        // 2. simulate the response of AAI (1 vnf)
        addResponse( queryNodeUrl,
        "junit/aai-service-instance_set2.json", aaiEnricherRule);

        // 3. simulate the rsp of VNF (with 1 vserver)
        // note: match vnf_id in (2)
        addResponse( "/aai/v13/network/generic-vnfs/generic-vnf/8a9ddb25-2e79-449c-a40d-5011bac0da39" + DEPTH,
        "junit/genericVnfInput_set4.json", aaiEnricherRule);

        // 4. simulate the rsp of vserer
        // note: match to vserver-id to the path of "vserver" in (3)
        addResponse(
                "/aai/v13/cloud-infrastructure/cloud-regions/cloud-region/CloudOwner/RegionOne/tenants/tenant"
                        + "/b49b830686654191bb1e952a74b014ad/vservers/vserver/b494cd6e-b9f3-45e0-afe7-e1d1a5f5d74a" + DEPTH,
                "junit/aai-vserver-set2.json", aaiEnricherRule);

        // 5. simulate the rsp of l3-network
        // note: match to network-id to the path of "l3network" in (3:  genericVnfInput_set4)
        addResponse(
                "/aai/v13/network/l3-networks/l3-network/01e8d84a-l3-network-1" + DEPTH,
                "junit/l3-network-1.json", aaiEnricherRule);
        addResponse(
                "/aai/v13/network/l3-networks/l3-network/01e8d84a-l3-network-2" + DEPTH,
                "junit/l3-network-2.json", aaiEnricherRule);


        when(mockHttpServletRequest
                .getRemoteAddr())
                .thenReturn("testretrieveAaiModelDataFromAaiL3NetworkInvModuleLevel");

        ModelContext modelCtx = RestUtil.retrieveAAIModelData(aaiClient,
                aaiBaseUrl,
                aaiPathToSearchNodeQuery,
                mockHttpServletRequest,
                requestId,
                partnerName,
                serviceInstanceId,
                aaiBasicAuthorization);
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
