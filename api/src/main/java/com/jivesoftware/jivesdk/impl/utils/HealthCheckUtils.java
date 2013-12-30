package com.jivesoftware.jivesdk.impl.utils;

import org.codehaus.jackson.node.ObjectNode;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 17/3/13
 * Time: 1:57 PM
 */
public class HealthCheckUtils {
    public static final String ACCESSIBLE = "accessible";
    public static final String CONNECT_ERROR = "connect.error";

    public static void markAccessible(@Nonnull ObjectNode internalNode, boolean isAccessible) {
        internalNode.put(ACCESSIBLE, isAccessible);
    }

    public static void setConnectionErrorMessage(@Nonnull ObjectNode internalNode, @Nonnull String errMsg) {
        internalNode.put(CONNECT_ERROR, errMsg);
    }
}
