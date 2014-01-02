package com.jivesoftware.jivesdk.impl.utils;

import com.google.common.base.Optional;
import com.jivesoftware.jivesdk.api.RegisteredInstance;
import com.jivesoftware.jivesdk.impl.http.HttpResponse;
import com.jivesoftware.jivesdk.server.ServerConstants;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Internal utility methods. These should not be used by client apps.
 *
 * There is no guarentee on backward compatiblity of these methods.
 */
public class JiveSDKUtils {
    private static final Logger log = LoggerFactory.getLogger(JiveSDKUtils.class);
    private static final Logger criticalLogger = LoggerFactory.getLogger("dealroomCriticalLogger");
    public static final String UTF_8 = "UTF-8";
    public static final Pattern PATTERN_NO_SUCH_COLUMN = Pattern.compile(".*No such column '(\\w*)' on entity '(\\w*)'.*");
    public static final String ACTIVITIES = "/activities";

    @Nonnull
    public static byte[] getBytes(@Nonnull String s) {
        try {
            return s.getBytes(UTF_8);
        } catch (UnsupportedEncodingException e) {
            log.warn("Failed reading UTF-8 bytes from string", e);
            return s.getBytes();
        }
    }

    @Nonnull
    public static String encodeUrl(@Nonnull String url) {
        try {
            return URLEncoder.encode(url, UTF_8);
        } catch (UnsupportedEncodingException e) {
            log.warn("Failed encoding URL using UTF-8 charset", e);
			//noinspection deprecation
			return URLEncoder.encode(url);
        }
    }

    public static String decodeUrl(@Nonnull String url) {
        try {
            return URLDecoder.decode(url, UTF_8);
        } catch (UnsupportedEncodingException e) {
            log.warn("Failed decoding URL using UTF-8 charset", e);
			//noinspection deprecation
			return URLDecoder.decode(url);
        }
    }

    @Nonnull
    public static String encodeBase64(@Nonnull String s) {
        byte[] stateBytes = getBytes(s);
        return Base64.encodeBase64String(stateBytes).replaceAll("\n|\r", "");
    }

    @Nonnull
    public static String decodeBase64(@Nonnull String s) {
        byte[] decoded = Base64.decodeBase64(s);
        return new String(decoded);
    }

    @Nonnull
    public static String createUrl(@Nonnull String baseUrl, @Nonnull String path) {
        String result = baseUrl;
        if (!result.endsWith(ServerConstants.Endpoints.ROOT)) {
            result += ServerConstants.Endpoints.ROOT;
        }

        if (path.startsWith(ServerConstants.Endpoints.ROOT)) {
            path = path.substring(1);
        }

        return result + path;
    }

    /**
     * Workaround to external stream url - Trimming /activities suffix
     *
     * @param url The input URL
     * @return A valid push URL either for tiles / ext. streams
     */
    public static String normalizeItemUrl(@Nullable String url) {
        return url != null && url.endsWith(ACTIVITIES) ?
                url.substring(0, url.lastIndexOf(ACTIVITIES)) :
                url;
    }

    /**
     * Normalize a URL to not include a trailing '/'
     *
     * @param url The URL
     * @return The given URL without a trailing '/' (if url included one)
     */
    @Nonnull
    public static String normalizeUrl(@Nonnull String url) {
        return url.endsWith(ServerConstants.Endpoints.ROOT) ?
                url.substring(0, url.length() - 1) :
                url;
    }


    /**
     * Returns a URL pattern (starting with %s to be replaced later with the instance URL) for the given item URL. <br/>
     * This instance URL <b>MUST BE NORMALIZED</b> (Lower-cased and no trailing '/') to avoid cases where instance URL changes and then '/' is included <br/>
     * This is handled by: com.jivesoftware.dealroom.masters.RegisteredInstanceMasterImpl#saveOrUpdate
     *
     * @param itemUrl  The item URL
     * @param instance The instance
     * @return A URL pattern or null if instance URL doesn't match item URL
     */
    @Nullable
    public static String getItemUrlPattern(@Nonnull String itemUrl, @Nonnull RegisteredInstance instance) {
        String jiveBaseUrl = instance.getUrl();
        if (itemUrl.startsWith(jiveBaseUrl)) {
            return itemUrl.replace(jiveBaseUrl, "%s");
        } else {
            return null;
        }
    }

    public static boolean validateBasicAuthentication(@Nonnull Optional<String> authHeader, @Nonnull String username, @Nonnull String password) {
        if (!authHeader.isPresent()) {
            return false;
        }

        String authorization = authHeader.get();
        if (!authorization.startsWith("Basic ") || authorization.length() <= 6) {
            return false;
        }

        String base64UserAndPass = authorization.substring(6);
        String userAndPass = JiveSDKUtils.decodeBase64(base64UserAndPass);
        int separator = userAndPass.indexOf(":");
        if (separator == -1) {
            return false;
        }

        String basicUser = userAndPass.substring(0, separator);
        String basicPass = userAndPass.substring(separator + 1);

        return !StringUtils.isEmpty(basicUser) && !StringUtils.isEmpty(basicPass) &&
                basicUser.equals(username) && basicPass.equals(password);
    }

    @Nonnull
    public static String readFile(@Nonnull String filename) throws Exception {
        String html = "";
        try {
            Optional<InputStream> optional = getFileInputStream(filename);
            if (optional.isPresent()) {
                InputStream stream = optional.get();
                html = getStringFromStream(stream);
            }
        } catch (Exception e) {
            log.error("Failed reading file: " + filename, e);
        }

        return html;
    }

    public static String readStringFromResponse(@Nullable HttpResponse response) {
        String result = "";
        try {
            if (response != null) {
                // First try to consume the string response
                Optional<String> optional = response.getResponseBody();
                if (optional.isPresent()) {
                    result = optional.get();
                }

                if (result == null) {
                    InputStream inputStream = response.getInputStream();
                    // If no string response - try to consume the stream response
                    if (inputStream != null) {
                        result = getStringFromStream(inputStream);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed reading input stream", e);
        }

        return result;
    }

    @Nonnull
    public static Optional<InputStream> getFileInputStream(@Nonnull String filename) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(filename);
            return Optional.fromNullable(inputStream);
        } catch (Exception e) {
            log.error("Failed reading input stream for file: " + filename, e);
            return Optional.absent();
        }
    }

    @Nonnull
    public static String getStringFromStream(@Nullable InputStream stream) {
        String s = "";
        if (stream == null) {
            return s;
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        try {
            while ((nRead = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            byte[] bytes = buffer.toByteArray();
            s = new String(bytes); // TODO: Removed the UTF-8... is that ok?
        } catch (Exception e) {
            log.error("Failed reading string from stream", e);
        } finally {
            try {
                stream.close();
            } catch (Exception ignored) {
            }
        }

        return s;
    }

    @Nonnull
    public static String trim(@Nullable String s, int length) {
        if (s != null && s.length() > length) {
            s = s.substring(0, length - 3) + "...";
        }

        return s != null ? s : "";
    }

    public static String trimValue(@Nullable String numberString, int length, boolean trimNumberWithoutDot) {
        String originalValue = numberString;

        try {
            // check not empty
            if (numberString == null || (numberString.length() <= 0))
                return trim(numberString, length);

            if (!NumberUtils.isNumber(numberString.substring(1))) {
                return trim(numberString, length);
            }

            // Get the sight if presented
            String beforeNumber = "";
            if (!NumberUtils.isNumber(numberString.substring(0, 1))) {
                beforeNumber = numberString.substring(0, 1);
                numberString = numberString.substring(1);
            }

            // check is number with dot and we are not allowing to trim in this case
            int indexOfDot = numberString.indexOf(".");
            if (indexOfDot <= 0 && !trimNumberWithoutDot) {
                return trim(numberString, length);
            }
            Number parsedNumber = DecimalFormat.getNumberInstance().parse(numberString);
            String newValue = beforeNumber + new DecimalFormat("#,##0.00").format(parsedNumber);

            return trim(newValue, length);
        } catch (Exception ex) {
            return originalValue;
        }
    }


    /**
     * Check if all the items are presented and not empty
     *
     * @param items : Checked items
     * @return :
     *         true -> if all the items are presented.
     *         false -> if at least one is absent or empty.
     */
    public static boolean isAllExist(@Nullable Object... items) {
        if (items == null) {
            return false;
        }

        for (Object item : items) {
            if (item == null || item.toString().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if all the items are presented and not empty
     *
     * @param items : Checked items
     * @return :
     *         true -> if all the items are presented.
     *         false -> if at least one is absent or empty.
     */
    public static boolean isAllExistO(@Nonnull Optional... items) {
        for (Optional item : items) {
            if (!item.isPresent() || item.get().toString().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    // get a specific value from a row
    public static <T> T getValueFromMap(Map<String, Object> row, String fieldName) {
        Object obj = row.get(fieldName);
        T objValue = null;
        if (obj != null) {
            try {
                //noinspection unchecked
                objValue = (T) obj;
            } catch (ClassCastException e) {
                log.error(String.format("Failed getting value %s from row: %s", fieldName, row));
            }
        } else {
            log.debug("cant get the value of " + fieldName);
        }

        return objValue;
    }

    @Nonnull
    public static String createHtmlMessage(@Nonnull String msg) {
        return String.format("<html><body>%s</body></html>", msg);
    }

    public static void sleepBeforeRetrying() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error("Thread interrupted while sleeping before retrying", e);
        }
    }

    public static Logger getCriticalLogger() {
        return criticalLogger;
    }
}
