package com.jivesoftware.jivesdk.example.db;

import java.util.Date;

/**
 * Entity representation of an access token that has been created.
 */
public class AccessToken extends AbstractEntity {

    private String access_token;
    private Date expires;
    private String scope;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
