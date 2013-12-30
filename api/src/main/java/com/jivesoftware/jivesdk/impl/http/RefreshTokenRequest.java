package com.jivesoftware.jivesdk.impl.http;

import com.google.common.collect.Maps;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

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
        this(GRANT_TYPE_DEFAULT_VALUE, refreshTokenValue);
    }

    public RefreshTokenRequest(String grantType, String refreshTokenValue) {
        bodyFields.put(GRANT_TYPE, grantType);
        bodyFields.put(REFRESH_TOKEN, refreshTokenValue);
    }

    public final JsonNode getJsonBody() {
        ObjectNode node = new ObjectMapper().createObjectNode();
        for (Map.Entry<String, String> entry : bodyFields.entrySet()) {
            node.put(entry.getKey(), entry.getValue());
        }
        return node;
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
