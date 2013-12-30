package com.jivesoftware.jivesdk.api;

import com.jivesoftware.jivesdk.impl.http.AuthTokenException;
import com.jivesoftware.jivesdk.impl.http.HttpResponse;
import com.jivesoftware.jivesdk.impl.http.RestRequest;
import org.apache.http.client.HttpClient;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 9/1/13
 * Time: 11:11 AM
 */
public interface RestDriver {
    /**
     * Executes the given request and returns its response.
     *
     * @param request The request to execute
     * @return The response
     * @throws com.jivesoftware.jivesdk.impl.http.AuthTokenException  If response returns with 401 status code.
     * @throws RestAccessException If any other I/O error or network problem was identified
     */
    @Nonnull
    HttpResponse execute(@Nonnull RestRequest request) throws AuthTokenException, RestAccessException;

    /**
     * Executes the given request and returns its response.
     *
     * @param request The request to execute
     * @param timeout Timeout in milliseconds
     * @return The response
     * @throws AuthTokenException  If response returns with 401 status code.
     * @throws RestAccessException If any other I/O error or network problem was identified
     */
    @Nonnull
    HttpResponse execute(@Nonnull RestRequest request, int timeout) throws AuthTokenException, RestAccessException;

    @Nonnull
    HttpResponse executeWithClient(@Nonnull RestRequest request, @Nonnull HttpClient client) throws AuthTokenException, RestAccessException;

    @Nonnull
    HttpResponse executeWithClient(@Nonnull RestRequest request, @Nonnull HttpClient client,
                                           int numberOfRetries) throws AuthTokenException, RestAccessException;

    @Nonnull
    HttpResponse executeForResponseBody(@Nonnull RestRequest request) throws AuthTokenException, RestAccessException;
}
