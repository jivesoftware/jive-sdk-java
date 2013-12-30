package com.jivesoftware.jivesdk.impl.http;

import com.google.common.base.Optional;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 9/1/13
 * Time: 3:51 PM
 */
public interface HttpResponse {
    int getStatusCode();

    @Nonnull
    Map<String, String> getResponseHeaders();

    InputStream getInputStream();

    @Nonnull
    Optional<String> getResponseBody();
}
