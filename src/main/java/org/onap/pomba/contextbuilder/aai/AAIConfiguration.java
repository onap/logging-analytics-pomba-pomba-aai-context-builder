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
package org.onap.pomba.contextbuilder.aai;


import java.util.Base64;
import javax.ws.rs.ApplicationPath;
import org.eclipse.jetty.util.security.Password;
import org.onap.aai.restclient.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;


@Component
//@Configuration
@ApplicationPath("/")
public class AAIConfiguration {
    @Autowired
    @Value("${aai.serviceName}")
    private String host;
    @Autowired
    @Value("${aai.servicePort}")
    private String port;
    @Autowired
    @Value("${aai.username}")
    private String username;
    @Autowired
    @Value("${aai.password}")
    private String password;
    @Autowired
    @Value("${aai.httpProtocol}")
    private String httpProtocol;
    @Autowired
    @Value("${aai.authentication}")
    private String authenticationMode;
    @Autowired
    @Value("${aai.trustStorePath}")
    private String trustStorePath;
    @Autowired
    @Value("${aai.keyStorePath}")
    private String keyStorePath;
    @Autowired
    @Value("${aai.keyStorePassword}")
    private String keyStorePassword;
    @Autowired
    @Value("${aai.connectionTimeout}")
    private Integer connectionTimeout;
    @Autowired
    @Value("${aai.readTimeout}")
    private Integer readTimeout;

    @Autowired
    @Value("${http.userId}")
    private String httpUserId;

    @Autowired
    @Value("${http.password}")
    private String httpPassword;


    @Bean(name="httpBasicAuthorization")
    public String getHttpBasicAuth() {
        String auth = new String(this.httpUserId + ":" + Password.deobfuscate(this.httpPassword));

        String encodedAuth =  Base64.getEncoder().encodeToString(auth.getBytes());
        return ("Basic " + encodedAuth);
    }

    @Bean(name="aaiBasicAuthorization")
    public String getAAIBasicAuth() {
        String auth = new String(this.username + ":" + Password.deobfuscate(this.password));
        String encodedAuth =  Base64.getEncoder().encodeToString(auth.getBytes());
        return ("Basic " + encodedAuth);
    }

    @Conditional(AAIBasicAuthCondition.class)
    @Bean(name="aaiClient")
    public RestClient restClientWithBasicAuth() {
        System.out.println("in basic auth");
        RestClient restClient = new RestClient();
        restClient.validateServerHostname(false).validateServerCertChain(false).connectTimeoutMs(connectionTimeout).readTimeoutMs(readTimeout);
        restClient.basicAuthUsername(username);
        restClient.basicAuthPassword(Password.deobfuscate(password));
        return restClient;
    }

    @Conditional(AAIClientCertCondition.class)
    @Bean(name="aaiClient")
    public RestClient restClientWithClientCert() {
        RestClient restClient = new RestClient();
        System.out.println("in client cert");
        if (httpProtocol.equals("https"))
            restClient.validateServerHostname(false).validateServerCertChain(false).trustStore(trustStorePath).clientCertFile(keyStorePath).clientCertPassword(keyStorePassword).connectTimeoutMs(connectionTimeout).readTimeoutMs(readTimeout);
        else
            restClient.validateServerHostname(false).validateServerCertChain(false).connectTimeoutMs(connectionTimeout).readTimeoutMs(readTimeout);
        return restClient;
    }

    @Bean(name="aaiBaseUrl")
    public String getURL() {
        return httpProtocol + "://" + host + ":" + port;

    }


    @Value("${aai.searchNodeQuery}")
    private String searchNodeQuery;

    @Bean(name="aaiPathToSearchNodeQuery")
    public String getAaiPathToSearchNodeQuery() {
        return searchNodeQuery.trim();
    }

}
