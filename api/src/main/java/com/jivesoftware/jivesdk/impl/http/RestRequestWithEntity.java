package com.jivesoftware.jivesdk.impl.http;

import com.google.common.base.Optional;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpRequestBase;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 11/7/13
 * Time: 4:01 PM
 */
public interface RestRequestWithEntity<T extends HttpRequestBase> extends RestRequest<T> {
    @Nonnull
    Optional<HttpEntity> getEntity();
}
