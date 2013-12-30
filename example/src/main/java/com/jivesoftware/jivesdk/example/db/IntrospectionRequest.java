package com.jivesoftware.jivesdk.example.db;

/**
 * <p>Entity representing the request parameters for introspecting the current state of an
 * access token.</p>
 */
public class IntrospectionRequest {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
