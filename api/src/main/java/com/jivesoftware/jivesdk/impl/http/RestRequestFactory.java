package com.jivesoftware.jivesdk.impl.http;

import com.jivesoftware.jivesdk.impl.http.get.RestGetRequestBuilder;
import com.jivesoftware.jivesdk.impl.http.get.RestGetRequestImpl;
import com.jivesoftware.jivesdk.impl.http.post.RestPostRequestBuilder;
import com.jivesoftware.jivesdk.impl.http.post.RestPostRequestImpl;
import com.jivesoftware.jivesdk.impl.http.put.RestPutRequestBuilder;
import com.jivesoftware.jivesdk.impl.http.put.RestPutRequestImpl;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 9/1/13
 * Time: 6:32 PM
 */
public class RestRequestFactory {
    @Nonnull
    public static RestGetRequestBuilder createGetRequestBuilder(@Nonnull String url) {
        return new RestGetRequestImpl(url);
    }

    @Nonnull
    public static RestPostRequestBuilder createPostRequestBuilder(@Nonnull String url) {
        return new RestPostRequestImpl(url);
    }

    @Nonnull
    public static RestPutRequestBuilder createPutRequestBuilder(@Nonnull String url) {
        return new RestPutRequestImpl(url);
    }
}
