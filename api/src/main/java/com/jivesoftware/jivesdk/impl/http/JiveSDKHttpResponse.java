package com.jivesoftware.jivesdk.impl.http;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.jivesoftware.jivesdk.impl.utils.JiveSDKUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 9/1/13
 * Time: 4:00 PM
 */
public class JiveSDKHttpResponse implements HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(JiveSDKHttpResponse.class);

    public static final String ENCODING_GZIP = "gzip";
    public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";

    private int statusCode;
    private Map<String, String> responseHeaders;
    private InputStream inputStream;
    private Optional<String> responseBody;

    public JiveSDKHttpResponse(int statusCode, @Nonnull Map<String, String> responseHeaders, InputStream inputStream) {
        this(statusCode, responseHeaders, (String) null);
        this.inputStream = getInputStream(inputStream);
        this.responseBody = Optional.of(JiveSDKUtils.getStringFromStream(this.inputStream));
    }

    public JiveSDKHttpResponse(int statusCode, @Nonnull Map<String, String> responseHeaders, String responseBody) {
        this.statusCode = statusCode;
        this.responseHeaders = responseHeaders;
        this.responseBody = Optional.fromNullable(responseBody);
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Nonnull
    @Override
    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    @Override
    public InputStream getInputStream() {
        return getInputStream(this.inputStream);
    }

    private InputStream getInputStream(@Nullable InputStream inputStream) {
        InputStream result = inputStream;
        try {
            String encoding = responseHeaders.get(HEADER_CONTENT_ENCODING);
            if (inputStream != null && encoding != null && ENCODING_GZIP.equalsIgnoreCase(encoding)) {
                result = new GZIPInputStream(inputStream);
            }
        } catch (IOException e) {
            log.error("Failed wrapping response input stream with GZIP stream", e);
        }

        return result;
    }

    @Nonnull
    @Override
    public Optional<String> getResponseBody() {
        return responseBody;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("statusCode", statusCode)
                .add("responseHeaders", responseHeaders)
                .add("responseBody", responseBody)
                .toString();
    }
}
