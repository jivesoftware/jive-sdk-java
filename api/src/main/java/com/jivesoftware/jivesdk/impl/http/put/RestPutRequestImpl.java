package com.jivesoftware.jivesdk.impl.http.put;

import com.google.common.base.Optional;
import com.jivesoftware.jivesdk.impl.http.AbstractRestRequestWithEntity;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPut;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 27/1/13
 * Time: 7:03 PM
 */
public class RestPutRequestImpl extends AbstractRestRequestWithEntity<HttpPut> implements RestPutRequestBuilder {
    public RestPutRequestImpl(@Nonnull String url) {
        super(url);
    }

    @Nonnull
    @Override
    public RestPutRequestBuilder withEntity(@Nonnull HttpEntity entity) {
        setEntity(entity);
        return this;
    }

    @Nonnull
    @Override
    public RestPutRequestBuilder withOAuth(@Nonnull String token) {
        return (RestPutRequestBuilder) super.withOAuth(token);
    }

    @Nonnull
    @Override
    public RestPutRequestBuilder withBasicAuth(@Nonnull String username, @Nonnull String password) {
        return (RestPutRequestBuilder) super.withBasicAuth(username, password);
    }

    @Nonnull
    @Override
    public RestPutRequestBuilder withHeaders(@Nonnull Map<String, String> headers) {
        return (RestPutRequestBuilder) super.withHeaders(headers);
    }

    @Nonnull
    @Override
    public HttpPut create() {
        HttpPut method = new HttpPut(getUrl());
        Optional<HttpEntity> optional = getEntity();
        if (optional.isPresent()) {
            method.setEntity(optional.get());
        }

        addAuthorizationHeader(method);
        addHeaders(method);
        return method;
    }
}
