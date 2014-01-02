package com.jivesoftware.jivesdk.impl.http;

import com.google.common.base.Optional;
import com.google.common.net.HttpHeaders;
import com.jivesoftware.jivesdk.impl.utils.JiveSDKUtils;
import org.apache.http.client.methods.HttpRequestBase;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 11/1/13
 * Time: 9:05 AM
 */
public abstract class AbstractRestRequest<T extends HttpRequestBase> implements RestRequest<T> {
    protected static final String AUTH_PREFIX_OAUTH_BEARER = "Bearer ";
    protected static final String AUTH_PREFIX_BASIC_AUTH = "Basic ";

    @Nonnull
    private String url;
    @Nonnull
    private Optional<String> oAuthToken = Optional.absent();
    @Nonnull
    private Optional<String> basicUser = Optional.absent();
    @Nonnull
    private Optional<String> basicPass = Optional.absent();
    @Nonnull
    private Optional<Map<String, String>> headers = Optional.absent();

    protected AbstractRestRequest(@Nonnull String url) {
        this.url = url;
    }

    @Nonnull
    @Override
    public String getUrl() {
        return url;
    }

    @Nonnull
    @Override
    public RestRequest<T> withOAuth(@Nonnull String token) {
        this.oAuthToken = Optional.of(token);
        return this;
    }

    @Nonnull
    @Override
    public RestRequest<T> withBasicAuth(@Nonnull String username, @Nonnull String password) {
        this.basicUser = Optional.of(username);
        this.basicPass = Optional.of(password);
        return this;
    }

    @Nonnull
    @Override
    public RestRequest<T> withHeaders(@Nonnull Map<String, String> headers) {
        this.headers = Optional.of(headers);
        return this;
    }

    protected void addHeaders(@Nonnull HttpRequestBase method) {
        if (headers.isPresent()) {
            Map<String, String> map = headers.get();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                method.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    protected boolean isContentTypeIncludedInHeaders() {
        return headers.isPresent() && headers.get().containsKey(HttpHeaders.CONTENT_TYPE);
    }

    protected void addAuthorizationHeader(@Nonnull HttpRequestBase method) {
        if (isOauth()) {
            addOauthHeader(method);
        } else if (isBasicAuth()) {
            addBasicAuthHeader(method);
        }
    }

    private boolean isOauth() {
        return oAuthToken.isPresent();
    }

    private void addOauthHeader(@Nonnull HttpRequestBase method) {
        method.addHeader(HttpHeaders.AUTHORIZATION, AUTH_PREFIX_OAUTH_BEARER + oAuthToken.get());
    }

    private boolean isBasicAuth() {
        return basicUser.isPresent() && basicPass.isPresent();
    }

    private void addBasicAuthHeader(@Nonnull HttpRequestBase method) {
        String userAndPass = basicUser.get() + ':' + basicPass.get();
        String base64UserAndPass = JiveSDKUtils.encodeBase64(userAndPass);
        method.addHeader(HttpHeaders.AUTHORIZATION, AUTH_PREFIX_BASIC_AUTH + base64UserAndPass);
    }
}
