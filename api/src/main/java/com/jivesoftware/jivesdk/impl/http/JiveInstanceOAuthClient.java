package com.jivesoftware.jivesdk.impl.http;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.jivesoftware.externalclient.JiveOAuthClient;
import com.jivesoftware.externalclient.JiveOAuthRequest;
import com.jivesoftware.externalclient.JiveOAuthResponse;
import com.jivesoftware.jivesdk.api.*;
import com.jivesoftware.jivesdk.impl.CredentialsImpl;
import com.jivesoftware.jivesdk.impl.http.post.RestPostRequestBuilder;
import com.jivesoftware.jivesdk.impl.utils.JiveSDKUtils;
import com.jivesoftware.jivesdk.impl.utils.JsonUtils;
import com.jivesoftware.jivesdk.server.ServerConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 18/7/13
 * Time: 10:49 AM
 */
public class JiveInstanceOAuthClient implements JiveCredentialsAcquirer, JiveOAuthClient {
    private static final Logger log = LoggerFactory.getLogger(JiveInstanceOAuthClient.class);

    private RestDriver restDriver;

	@Nullable
    @Override
    public Credentials acquireCredentials(@Nonnull TileRegistrationRequest tileRegistrationRequest) {
        String jiveInstanceUrl = tileRegistrationRequest.getJiveUrl();
        String clientId = tileRegistrationRequest.getClientId();
        String clientSecret = tileRegistrationRequest.getClientSecret();
        String code = tileRegistrationRequest.getCode();
        if (!JiveSDKUtils.isAllExist(jiveInstanceUrl, clientId, clientSecret, code)) {
            log.error("Can't get access token due to invalid request: " + tileRegistrationRequest);
            return null;
        }

        JiveOAuthRequest request = new JiveOAuthRequest(jiveInstanceUrl, clientId, clientSecret, code);
        JiveOAuthResponse jiveOAuthResponse = getAccessToken(request);
        if (jiveOAuthResponse == null) {
            return null;
        }

        String refreshToken = jiveOAuthResponse.getRefreshToken();
        if (StringUtils.isEmpty(refreshToken)) {
            refreshToken = ServerConstants.NO_REFRESH_TOKEN;
        }

        return new CredentialsImpl(tileRegistrationRequest.getJivePushUrl(), jiveOAuthResponse.getAccessToken(), refreshToken);
    }

    @Override
    @Nullable
    public JiveOAuthResponse getAccessToken(@Nonnull JiveOAuthRequest request) {
        String jiveInstanceUrl = request.getJiveInstanceUrl();
        String msg = "Exchange access token with Jive instance: " + jiveInstanceUrl;
        String tokenUrl = JiveSDKUtils.createUrl(jiveInstanceUrl, ServerConstants.OAUTH_SUFFIX);

        Map<String, String> params = Maps.newHashMap();
        Map<String, String> bodyParams = request.getBodyParams();
        for (Map.Entry<String, String> entry : bodyParams.entrySet()) {
            params.put(entry.getKey(), entry.getValue());
        }

        RestPostRequestBuilder requestBuilder = RestRequestFactory.
                createPostRequestBuilder(tokenUrl).
                withBodyParams(params).
                withBasicAuth(request.getClientId(), request.getClientSecret());

        try {
            // Using default timeout (in case we're reaching out to an on-prem Jive instance...)
            HttpResponse response = restDriver.execute(requestBuilder);
            if (response.getStatusCode() == HttpStatus.SC_OK) {
                JiveOAuthResponse oAuthResponse = JsonUtils.createOAuthResponseFromHttpResponse(response);
                log.debug("success " + msg);
                return oAuthResponse;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Request failed. Received: ").
                        append(response.getStatusCode());
                Optional<String> bodyOptional = response.getResponseBody();
                if (bodyOptional.isPresent()) {
                    sb.append(bodyOptional.get());
                }
                log.error(sb.toString());
            }
        } catch (AuthTokenException e) {
            log.error("Failed due to auth. token exception: " + e.getLocalizedMessage(), e);
        } catch (RestAccessException e) {
            log.error("Network error");
        } catch (Throwable t) {
            log.error("Failed registering with Jive", t);
        }

        return null;
    }

    public void setRestDriver(RestDriver restDriver) {
        this.restDriver = restDriver;
    }
}
