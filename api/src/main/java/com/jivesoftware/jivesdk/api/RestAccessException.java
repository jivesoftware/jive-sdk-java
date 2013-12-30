package com.jivesoftware.jivesdk.api;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 16/1/13
 * Time: 6:27 PM
 */
public class RestAccessException extends Exception {
    private String destination;

    public RestAccessException(String message, String destination) {
        super(message);
        this.destination = destination;
    }

    public RestAccessException(String message, String destination, Throwable cause) {
        super(message, cause);
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }
}
