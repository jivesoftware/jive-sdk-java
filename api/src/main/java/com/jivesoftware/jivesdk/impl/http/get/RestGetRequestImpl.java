package com.jivesoftware.jivesdk.impl.http.get;

import com.jivesoftware.jivesdk.impl.http.AbstractRestRequest;
import com.jivesoftware.jivesdk.impl.utils.JiveSDKUtils;
import org.apache.http.client.methods.HttpGet;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 9/1/13
 * Time: 6:30 PM
 */
public class RestGetRequestImpl extends AbstractRestRequest<HttpGet> implements RestGetRequestBuilder {
    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String HEADER_VALUE_GZIP = "gzip";
    private Map<String, String> queryParams;
    private boolean gzip;

    @Override
    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public RestGetRequestImpl(@Nonnull String url) {
        super(url);
        gzip = false;
    }

    @Nonnull
    @Override
    public RestGetRequestBuilder withQueryParams(Map<String, String> params) {
        this.queryParams = params;
        return this;
    }

    @Nonnull
    @Override
    public RestGetRequestBuilder withGzipCompression() {
        this.gzip = true;
        return this;
    }

    @Nonnull
    @Override
    public RestGetRequestBuilder withOAuth(@Nonnull String token) {
        return (RestGetRequestBuilder) super.withOAuth(token);
    }

    @Nonnull
    @Override
    public RestGetRequestBuilder withBasicAuth(@Nonnull String username, @Nonnull String password) {
        return (RestGetRequestBuilder) super.withBasicAuth(username, password);
    }

    @Nonnull
    @Override
    public RestGetRequestBuilder withHeaders(@Nonnull Map<String, String> headers) {
        return (RestGetRequestBuilder) super.withHeaders(headers);
    }

    @Nonnull
    @Override
    public HttpGet create() {
        StringBuilder paramsStr = new StringBuilder();
        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                if (paramsStr.length() > 0) {
                    paramsStr.append('&');
                } else {
                    paramsStr.append('?');
                }

                paramsStr.append(JiveSDKUtils.encodeUrl(entry.getKey())).
                        append('=').
                        append(JiveSDKUtils.encodeUrl(entry.getValue()));
            }
        }

        HttpGet method = new HttpGet(getUrl() + paramsStr.toString());

        addAuthorizationHeader(method);
        addHeaders(method);

        if (gzip) {
            method.addHeader(HEADER_ACCEPT_ENCODING, HEADER_VALUE_GZIP);
        }

        return method;
    }
}
