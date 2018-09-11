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

    @Rule
    public WireMockRule aaiEnricherRule = new WireMockRule(wireMockConfig().port(9808));

    @Test
    public void testValidateURL() {
        // Missing ServiceInstanceId or it is null
        try {
            RestUtil.validateURL("", "modelVersionId", "modelInvariantId");
        } catch (AuditException e) {
            assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: serviceInstanceId"));
        }

        try {
            RestUtil.validateURL(null, "modelVersionId", "modelInvariantId");
        } catch (AuditException e) {
            assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: serviceInstanceId"));
        }

        // Missing ModelVersionId or it is null
        try {
            RestUtil.validateURL("serviceInstanceId", "", "modelInvariantId");
        } catch (AuditException e) {
            assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: modelVersionId"));
        }

        try {
            RestUtil.validateURL("serviceInstanceId", null, "modelInvariantId");
        } catch (AuditException e) {
            assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: modelVersionId"));
        }

        // Missing ModelInvariantId or it is null
        try {
            RestUtil.validateURL("serviceInstanceId", "modelVersionId", "");
        } catch (AuditException e) {
            assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: modelInvariantId"));
        }

        try {
            RestUtil.validateURL("serviceInstanceId", "modelVersionId", null);
        } catch (AuditException e) {
            assertTrue(e.getMessage().contains("Invalid request URL, missing parameter: modelInvariantId"));
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
}
