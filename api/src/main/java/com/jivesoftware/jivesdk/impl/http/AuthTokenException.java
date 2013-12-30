package com.jivesoftware.jivesdk.impl.http;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 11/1/13
 * Time: 8:51 AM
 */
public class AuthTokenException extends Exception {
    @Nonnull
    private String destination;
    private String response;

    public AuthTokenException(@Nonnull String destination, String response) {
        this.destination = destination;
        this.response = response;
    }

    @Nonnull
    public String getDestination() {
        return destination;
    }

    public String getResponse() {
        return response;
    }
}
