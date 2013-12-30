package com.jivesoftware.jivesdk.impl.utils;

import com.google.common.base.Optional;
import com.jivesoftware.externalclient.JiveOAuthResponse;
import com.jivesoftware.jivesdk.impl.http.HttpResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 29/1/13
 * Time: 10:32 AM
 */
public class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
    public static final String APPLICATION_JSON = "application/json";
    public static final String TEXT_HTML = "text/html";

    public static HttpEntity jsonToRequestEntity(JsonNode json) {
        return new StringEntity(json.toString(), ContentType.APPLICATION_JSON);
    }

    @Nonnull
    public static <T> Optional<T> parseJsonResponse(@Nonnull HttpResponse response, @Nonnull Class<T> clazz) throws
			IOException {
        return parseJsonResponse(response, clazz, false);
    }

    @Nonnull
    public static <T> Optional<T> parseJsonResponse(@Nonnull HttpResponse response, @Nonnull Class<T> clazz, boolean shouldReThrowException) throws
			IOException {
        InputStream inputStream = null;
        String responseBody = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Optional<String> optional = response.getResponseBody();
            if (optional.isPresent()) {
                responseBody = optional.get();
                return Optional.fromNullable(objectMapper.readValue(responseBody, clazz));
            } else {
                inputStream = response.getInputStream();
                T t = objectMapper.readValue(inputStream, clazz);
                return Optional.fromNullable(t);
            }
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed encoding json response to object of type: ").append(clazz.getName());
            if (responseBody != null) {
                sb.append(". Object: ").append(responseBody);
            }
            log.error(sb.toString(), e);

            if (shouldReThrowException) {
                throw e;
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }

        return Optional.absent();
    }

    public static Optional<JsonNode> getJsonFromResponse(HttpResponse response) {
        InputStream inputStream = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Optional<String> optional = response.getResponseBody();
            if (optional.isPresent()) {
                return Optional.fromNullable(objectMapper.readTree(optional.get()));
            } else {
                inputStream = response.getInputStream();
                if (inputStream != null) {
                    return Optional.fromNullable(objectMapper.readTree(inputStream));
                }
            }
        } catch (IOException e) {
            log.error("Failed reading response", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }

        return Optional.absent();
    }

    public static Optional<JsonNode> getJsonFromResponseBody(@Nonnull HttpResponse response) {
        try {
            Optional<String> optional = response.getResponseBody();
            String body = null;
            if (optional.isPresent()) {
                body = optional.get();
            }

            if (body != null) {
                // Trim the 'throw exception' prefix...
                if (body.startsWith("throw")) {
                    body = body.substring(1 + body.indexOf(";"));
                }

                ObjectMapper objectMapper = new ObjectMapper();
                return Optional.fromNullable(objectMapper.readTree(body));
            }
        } catch (IOException e) {
            log.error("Failed reading JSON from response body", e);
        }

        return Optional.absent();
    }

    @Nonnull
    public static JiveOAuthResponse createOAuthResponseFromHttpResponse(@Nonnull HttpResponse httpResponse) throws IllegalArgumentException {
        Optional<JsonNode> optionalResponse = getJsonFromResponse(httpResponse);
        if (optionalResponse.isPresent()) {
            JsonNode jsonResponse = optionalResponse.get();
            if (!jsonResponse.isNull()) {
                return createOAuthResponseFromJson(jsonResponse);
            } else {
                throw new IllegalArgumentException("Invalid JSON response");
            }
        } else {
            throw new IllegalArgumentException("No JSON response");
        }
    }

    @Nonnull
    private static JiveOAuthResponse createOAuthResponseFromJson(@Nonnull JsonNode jsonResponse) throws IllegalArgumentException {
        JsonNode node = jsonResponse.get(JiveOAuthResponse.SCOPE);
        String scope = node != null ? node.asText() : null;
        node = jsonResponse.get(JiveOAuthResponse.TOKEN_TYPE);
        String tokenType = node != null ? node.asText() : null;
        node = jsonResponse.get(JiveOAuthResponse.EXPIRES_IN);
        long expiresIn = node != null ? node.asLong() : -1;
        node = jsonResponse.get(JiveOAuthResponse.ACCESS_TOKEN);
        String accessToken = node != null ? node.asText() : null;
        node = jsonResponse.get(JiveOAuthResponse.REFRESH_TOKEN);
        String refreshToken = node != null ? node.asText() : null;

        if (StringUtils.isEmpty(accessToken)) {
            throw new IllegalArgumentException("Invalid JSON response: " + node);
        }

        return new JiveOAuthResponse(scope, tokenType, expiresIn, accessToken, refreshToken);
    }

}
