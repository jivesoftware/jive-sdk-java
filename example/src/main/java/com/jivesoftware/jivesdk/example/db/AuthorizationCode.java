package com.jivesoftware.jivesdk.example.db;

/**
 * Entity representation of an authorization code that has been created.
 */
public class AuthorizationCode extends AbstractEntity {

    private String code;
    private String scope;
    private String state;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
