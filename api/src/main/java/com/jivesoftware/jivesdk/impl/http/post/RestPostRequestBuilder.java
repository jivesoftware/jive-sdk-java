package com.jivesoftware.jivesdk.impl.http.post;

import org.apache.http.HttpEntity;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 9/1/13
 * Time: 6:33 PM
 */
public interface RestPostRequestBuilder extends RestPostRequest {
    @Nonnull
    RestPostRequestBuilder withBodyParams(@Nonnull Map<String, String> params);

    @Nonnull
    RestPostRequestBuilder withEntity(@Nonnull HttpEntity entity);

    @Nonnull
    @Override
    RestPostRequestBuilder withOAuth(@Nonnull String token);

    @Nonnull
    @Override
    RestPostRequestBuilder withBasicAuth(@Nonnull String username, @Nonnull String password);

    @Nonnull
    @Override
    RestPostRequestBuilder withHeaders(@Nonnull Map<String, String> headers);
}
