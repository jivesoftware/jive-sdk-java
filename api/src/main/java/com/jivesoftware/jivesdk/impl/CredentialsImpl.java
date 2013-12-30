package com.jivesoftware.jivesdk.impl;

import com.google.common.base.Optional;
import com.jivesoftware.jivesdk.api.Credentials;
import com.jivesoftware.jivesdk.api.RestAccessException;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 27/1/13
 * Time: 5:25 PM
 */
public class CredentialsImpl implements Credentials {
    //<editor-fold desc="Access Token Response Fields">
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String INVALID = "invalid";
    private static final String OK = "ok";
    //</editor-fold>

    @Nonnull
    private String url;
    @Nonnull
    private String authToken;
    @Nonnull
    private String refreshToken;

	public CredentialsImpl() {
	}

	// TODO:sharon-> change to use AccessTokenResponse instead of this one
    public CredentialsImpl(@Nonnull String url, @Nonnull String authToken, @Nonnull String refreshToken) {
        this.url = url;
        this.authToken = authToken;
        this.refreshToken = refreshToken;
    }

    public CredentialsImpl(@Nonnull String url, Optional<JsonNode> jsonFromResponse) throws RestAccessException {
        if (jsonFromResponse.isPresent()) {
            JsonNode jsonNode = jsonFromResponse.get();
            JsonNode jsonAccessTokenNode = jsonNode.get(ACCESS_TOKEN);
            JsonNode jsonRefreshTokenNode = jsonNode.get(REFRESH_TOKEN);

            if (jsonAccessTokenNode != null && jsonRefreshTokenNode != null && jsonAccessTokenNode.isTextual() && jsonRefreshTokenNode.isTextual()) {
                this.url = url;
                this.authToken = jsonAccessTokenNode.getTextValue();
                this.refreshToken = jsonRefreshTokenNode.getTextValue();
            } else {
                throw new RestAccessException("Bad access response", null);
            }
        }
    }

    @Nonnull
    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setAuthToken(@Nonnull String newAuthToken) {
        authToken = newAuthToken;
    }

    @Nonnull
    @Override
    public String getAuthToken() {
        return authToken;
    }

    @Nonnull
    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

	@Override
	public void setRefreshToken(@Nonnull String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CredentialsImpl that = (CredentialsImpl) o;

        return authToken.equals(that.authToken) && refreshToken.equals(that.refreshToken) && url.equals(that.url);
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + (authToken.hashCode());
        result = 31 * result + (refreshToken.hashCode());
        return result;
    }

    public String toString() {
        return String.format("Credentials {url=%s, authToken? %s, refreshToken? %s}",
                url,
                StringUtils.isEmpty(authToken) ? INVALID : OK,
                StringUtils.isEmpty(refreshToken) ? INVALID : OK);
    }
}
