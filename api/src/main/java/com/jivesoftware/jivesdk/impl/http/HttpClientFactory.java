package com.jivesoftware.jivesdk.impl.http;

import org.apache.http.client.HttpClient;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 9/1/13
 * Time: 3:48 PM
 */
public interface HttpClientFactory {
    /**
     * Acquires an HTTP client with the default timeout setting ()
     *
     * @return An HTTP client
     */
    @Nonnull
    HttpClient getClient();

    /**
     * Acquires an HTTP client with the given timeout applied for socket and connection.
     *
     * @param timeout Timeout in milliseconds
     * @return An HTTP client
     */
    @Nonnull
    HttpClient getClient(int timeout);
}
