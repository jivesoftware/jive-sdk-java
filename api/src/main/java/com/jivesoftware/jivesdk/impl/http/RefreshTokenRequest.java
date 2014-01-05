package com.jivesoftware.jivesdk.impl.http;

import com.google.common.collect.Maps;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sharon
 * Date: 7/2/13
 * Time: 4:12 PM
 */
public class RefreshTokenRequest {
    public static final String GRANT_TYPE_DEFAULT_VALUE = "refresh_token";

    private static final String GRANT_TYPE = "grant_type";
    private static final String REFRESH_TOKEN = "refresh_token";

    private final Map<String, String> bodyFields = Maps.newHashMap();

    public RefreshTokenRequest(@Nonnull String refreshTokenValue) {
        bodyFields.put(GRANT_TYPE, GRANT_TYPE_DEFAULT_VALUE);
        bodyFields.put(REFRESH_TOKEN, refreshTokenValue);
    }

    public Map<String, String> getFormParams() {
        return Maps.newHashMap(bodyFields);
    }

    @Override
    public String toString() {
        return "RefreshTokenRequest{" +
                bodyFields +
                '}';
    }
}
