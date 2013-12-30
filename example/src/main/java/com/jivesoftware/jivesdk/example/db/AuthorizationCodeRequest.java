package com.jivesoftware.jivesdk.example.db;

/**
 * <p>Parameters for requesting an authorization code.</p>
 */
public class AuthorizationCodeRequest {

    private String clientId;
    private String responseType;
    private String scope;
    private String userMembershipKey;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUserMembershipKey() {
        return userMembershipKey;
    }

    public void setUserMembershipKey(String userMembershipKey) {
        this.userMembershipKey = userMembershipKey;
    }
}
