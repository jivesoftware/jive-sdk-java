package com.jivesoftware.jivesdk.server;

import com.jivesoftware.jivesdk.api.RegisteredInstance;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 22/7/13
 * Time: 2:33 PM
 */
public class AuthenticationResponse {
    private int statusCode = -1;
    private String clientId = null;
    private String clientSecret = null;
    private RegisteredInstance instance = null;

    public AuthenticationResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public AuthenticationResponse(@Nonnull String clientId, @Nonnull String clientSecret, @Nonnull RegisteredInstance instance) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.instance = instance;
    }

    public boolean isAuthenticated() {
        return statusCode == -1 && clientId != null && clientSecret != null;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public RegisteredInstance getInstance() {
        return instance;
    }
}
