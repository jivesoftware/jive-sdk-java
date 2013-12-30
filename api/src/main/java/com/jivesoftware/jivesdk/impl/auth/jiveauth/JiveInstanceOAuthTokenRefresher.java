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
import com.jivesoftware.externalclient.JiveOAuthResponse;
import com.jivesoftware.jivesdk.api.*;
import com.jivesoftware.jivesdk.impl.http.AuthTokenException;
import com.jivesoftware.jivesdk.impl.http.HttpResponse;
import com.jivesoftware.jivesdk.impl.http.RefreshTokenRequest;
import com.jivesoftware.jivesdk.impl.http.RestRequestFactory;
import com.jivesoftware.jivesdk.impl.http.post.RestPostRequestBuilder;
import com.jivesoftware.jivesdk.impl.utils.DealRoomUtils;
import com.jivesoftware.jivesdk.impl.utils.JsonUtils;
import com.jivesoftware.jivesdk.server.ServerConstants;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 24/7/13
 * Time: 5:44 PM
 */
public class JiveInstanceOAuthTokenRefresher implements JiveTokenRefresher {
    private final static Logger log = LoggerFactory.getLogger(JiveInstanceOAuthTokenRefresher.class);
    private RestDriver restDriver;

    @Nullable
    @Override
    public JiveOAuthResponse refreshToken(@Nonnull TileInstance item) throws
			InvalidRequestException {
        try {
            RefreshTokenRequest request = new RefreshTokenRequest(item.getCredentials().getRefreshToken());
            Map<String, String> requestParams = request.getFormParams();
            String tokenUrl = DealRoomUtils.createUrl(item.getJiveInstanceUrl(), ServerConstants.OAUTH_SUFFIX);
            Map<String, String> params = Maps.newHashMap();
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                params.put(entry.getKey(), entry.getValue());
            }
			RegisteredInstance registeredInstance = JiveSDKManager.getInstance().getInstanceRegistrationHandler().getByTenantId(
					item.getTenantId());
			RestPostRequestBuilder postRequestBuilder = RestRequestFactory.createPostRequestBuilder(tokenUrl).
                    withBodyParams(params).
                    withBasicAuth(registeredInstance.getClientId(), registeredInstance.getClientSecret());
            HttpResponse response = restDriver.execute(postRequestBuilder);
            int statusCode = response.getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Optional<String> optionalBody = response.getResponseBody();
                String msg = optionalBody.isPresent() ?
                        String.format("Received status code [%d]. Response: %s", statusCode, optionalBody.get()) :
                        String.format("Received status code [%d]", statusCode);
                log.error(msg);
                return null;
            }

            JiveOAuthResponse oAuthResponse = JsonUtils.createOAuthResponseFromHttpResponse(response);
			item.getCredentials().setAuthToken(oAuthResponse.getAccessToken());
			JiveSDKManager.getInstance().getInstanceRegistrationHandler().update(item);
			log.info("Refreshed oauth token.");
            log.debug("returning oAuthResponse " + oAuthResponse);
            return oAuthResponse;
        } catch (AuthTokenException e) {
            throw new InvalidRequestException();
        } catch (Exception e) {
            log.error("Access exception, therefore Couldn't get a new Auth from JiveId", e);
            return null;
        }
    }

    public void setRestDriver(RestDriver restDriver) {
        this.restDriver = restDriver;
    }
}
