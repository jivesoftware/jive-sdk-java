package com.jivesoftware.jivesdk.impl.http.get;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 9/1/13
 * Time: 6:29 PM
 */
public interface RestGetRequestBuilder extends RestGetRequest {
    @Nonnull
    RestGetRequestBuilder withQueryParams(Map<String, String> params);

    @Nonnull
    RestGetRequestBuilder withGzipCompression();

    @Nonnull
    @Override
    RestGetRequestBuilder withOAuth(@Nonnull String token);

    @Nonnull
    @Override
    RestGetRequestBuilder withBasicAuth(@Nonnull String username, @Nonnull String password);

    @Nonnull
    @Override
    RestGetRequestBuilder withHeaders(@Nonnull Map<String, String> headers);
}
