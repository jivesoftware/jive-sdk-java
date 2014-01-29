/*
 * Copyright (c) 2014. Jive Software
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

package com.jivesoftware.jivesdk.api.oauth;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 18/7/13
 * Time: 7:36 PM
 */
public class JiveOAuthResponse {
    public final static String SCOPE = "scope";
    public final static String TOKEN_TYPE = "token_type";
    public final static String EXPIRES_IN = "expires_in";
    public final static String ACCESS_TOKEN = "access_token";
    public final static String REFRESH_TOKEN = "refresh_token";

    private String scope;
    private String token_type;
    private long expires_in;
    private String access_token;
    private String refresh_token;

    public JiveOAuthResponse(String scope, String tokenType, long expiresIn, String accessToken, String refreshToken) {
        this.scope = scope;
        this.token_type = tokenType;
        this.expires_in = expiresIn;
        this.access_token = accessToken;
        this.refresh_token = refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenType() {
        return token_type;
    }

    public long getExpiresin() {
        return expires_in;
    }

    public String getAccessToken() {
        return access_token;
    }

    public String getRefreshToken() {
        return refresh_token;
    }
}
