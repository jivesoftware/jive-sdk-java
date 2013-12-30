/*
 * Copyright (c) 2013. Jive Software
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 */

package com.jivesoftware.jivesdk.impl.auth.jiveauth;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.net.MediaType;
import com.jivesoftware.jivesdk.api.InstanceRegistrationRequest;
import com.jivesoftware.jivesdk.api.JiveSignatureValidator;
import com.jivesoftware.jivesdk.api.RestDriver;
import com.jivesoftware.jivesdk.impl.JiveSDKConfig;
import com.jivesoftware.jivesdk.impl.PropertyConfiguration;
import com.jivesoftware.jivesdk.impl.http.HttpResponse;
import com.jivesoftware.jivesdk.impl.http.RestDriverImpl;
import com.jivesoftware.jivesdk.impl.http.RestRequestFactory;
import com.jivesoftware.jivesdk.impl.http.post.RestPostRequest;
import com.jivesoftware.jivesdk.impl.http.ssl.ssl.SubjectVerifierSocketFactory;
import com.jivesoftware.jivesdk.server.ServerConstants;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.SortedMap;

import static com.jivesoftware.jivesdk.impl.JiveSDKConfig.ServiceConfiguration.IGNORE_HTTP_SIGNATURE_URLS_IN_INSTANCE_REGISTRATION;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 21/7/13
 * Time: 1:55 PM
 */
public class JiveSignatureValidatorImpl implements JiveSignatureValidator {
    private final static Logger log = LoggerFactory.getLogger(JiveSignatureValidatorImpl.class);
    private RestDriver restDriver = new RestDriverImpl();
    private PropertyConfiguration configuration = PropertyConfiguration.getInstance();

    @Override
    public boolean validate(@Nonnull InstanceRegistrationRequest request) {
        try {
            String signature = request.getJiveSignature();
            String signatureURL = request.getJiveSignatureURL();
            if (signature == null || signatureURL == null) {
                String msg = String.format("Invalid signature [%s] or signature URL [%s]", signature, signatureURL);
                log.error(msg);
                return false;
            }

            String valueStr = configuration.getProperty(IGNORE_HTTP_SIGNATURE_URLS_IN_INSTANCE_REGISTRATION);
            boolean shouldIgnoreHttps = Boolean.parseBoolean(valueStr);
            boolean isHttps = signatureURL.toLowerCase().startsWith(ServerConstants.HTTPS_PREFIX);
            if (!shouldIgnoreHttps && !isHttps) {
                log.error("Signature URL must be over SSL: " + signatureURL);
                return false;
            }

            RestPostRequest postRequest = createValidationPostRequest(request);
            HttpResponse response;
            if (isHttps) {
                HttpClient client = createSubjectVerifierHttpClient(signatureURL);
                response = restDriver.executeWithClient(postRequest, client);
            } else {
                response = restDriver.execute(postRequest);
            }

            int statusCode = response.getStatusCode();
            if (statusCode == HttpStatus.SC_NO_CONTENT) {
                log.debug("success");
                return true;
            } else {
                Optional<String> optional = response.getResponseBody();
                String actualBody = optional.isPresent() ? optional.get() : "<empty body>";
                String msg = String.format("Received response [%d]: %s", statusCode, actualBody);
                log.error(msg);
                return false;
            }
        } catch (Exception e) {
            log.error("Failed validating instance registration request: " + request, e);
            return false;
        }
    }

    @Nonnull
    private RestPostRequest createValidationPostRequest(@Nonnull InstanceRegistrationRequest request) throws NoSuchAlgorithmException {
        Map<String, String> headers = Maps.newHashMap();
        headers.put("X-Jive-MAC", request.getJiveSignature());
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.OCTET_STREAM.toString());

        String body = createSignatureValidationRequest(request);

        return RestRequestFactory.createPostRequestBuilder(request.getJiveSignatureURL())
                .withHeaders(headers)
                .withEntity(new StringEntity(body, ContentType.APPLICATION_OCTET_STREAM));
    }

    @Nonnull
    private String createSignatureValidationRequest(@Nonnull InstanceRegistrationRequest request) throws NoSuchAlgorithmException {
        SortedMap<String, String> sortedMap = request.toSortedMap();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null && value != null) {
                sb.append(key).append(':').append(value).append((char) 10);
            }
        }

        return sb.toString();
    }

    private HttpClient createSubjectVerifierHttpClient(@Nonnull String url) throws Exception {
        int targetPort = new URL(url).getPort();
        if (targetPort == -1) {
            if (url.startsWith(ServerConstants.HTTPS_PREFIX)) {
                targetPort = 443;
            } else {
                targetPort = 80;
            }
        }

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String subject = configuration.getProperty(JiveSDKConfig.ServiceConfiguration.SIGNATURE_URL_CERTIFICATE_SUBJECT_NAME);
        SchemeSocketFactory sslf = SubjectVerifierSocketFactory.createSocketFactory(subject);
        ClientConnectionManager connectionManager = httpClient.getConnectionManager();
        SchemeRegistry schemeRegistry = connectionManager.getSchemeRegistry();
        schemeRegistry.register(new Scheme(ServerConstants.HTTPS, targetPort, sslf));
        return httpClient;
    }

    public void setRestDriver(RestDriver restDriver) {
        this.restDriver = restDriver;
    }

    public void setConfiguration(PropertyConfiguration configuration) {
        this.configuration = configuration;
    }
}
