package com.jivesoftware.jivesdk.example.db;

import java.util.Date;

/**
 * Entity representation of a refresh token that has been created.
 */
public class RefreshToken extends AbstractEntity {

    private String accessToken;
    private Date expires;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
