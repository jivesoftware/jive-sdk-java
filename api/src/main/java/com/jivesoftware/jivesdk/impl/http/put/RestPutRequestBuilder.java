package com.jivesoftware.jivesdk.impl.http.put;

import org.apache.http.HttpEntity;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 27/1/13
 * Time: 7:03 PM
 */
public interface RestPutRequestBuilder extends RestPutRequest {
    @Nonnull
    RestPutRequestBuilder withEntity(@Nonnull HttpEntity entity);

    @Nonnull
    @Override
    RestPutRequestBuilder withOAuth(@Nonnull String token);

    @Nonnull
    @Override
    RestPutRequestBuilder withBasicAuth(@Nonnull String username, @Nonnull String password);

    @Nonnull
    @Override
    RestPutRequestBuilder withHeaders(@Nonnull Map<String, String> headers);
}
