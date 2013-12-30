package com.jivesoftware.jivesdk.impl.http;

import org.apache.http.client.methods.HttpRequestBase;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 9/1/13
 * Time: 6:24 PM
 */
public interface RestRequest<T extends HttpRequestBase> {
    @Nonnull
    String getUrl();

    @Nonnull
    RestRequest<T> withOAuth(@Nonnull String token);

    @Nonnull
    RestRequest<T> withBasicAuth(@Nonnull String username, @Nonnull String password);

    @Nonnull
    RestRequest<T> withHeaders(@Nonnull Map<String, String> headers);

    @Nonnull
    T create();
}
