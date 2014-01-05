package com.jivesoftware.jivesdk.server;

import com.google.common.collect.Sets;
import org.apache.http.HttpStatus;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Static context, mainly for reaching out to spring beans (tests)
 * <p/>
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 15/1/13
 * Time: 4:03 PM
 */

public class ServerConstants {
    public static final Set<Integer> RETRIABLE_RESPONSE_CODES = Sets.newHashSet(
            HttpStatus.SC_REQUEST_TIMEOUT,
            HttpStatus.SC_BAD_GATEWAY,
            HttpStatus.SC_SERVICE_UNAVAILABLE,
            HttpStatus.SC_GATEWAY_TIMEOUT
    );

    public static final String OAUTH_SUFFIX = "/oauth2/token";

    public class Endpoints {

        public static final String INSTANCE = "/instance";
        public static final String REGISTER = "/register";
        public static final String UNREGISTER = "/unregister";


        public static final String TILE = "/tile";

        public static final String TEMPLATE_PREFIX = "{{{";
        public static final String TEMPLATE_SUFFIX = "}}}";
        public static final String HOST_TEMPLATE = TEMPLATE_PREFIX + "host" + TEMPLATE_SUFFIX;
        public static final String TIME_TEMPLATE = TEMPLATE_PREFIX + "time" + TEMPLATE_SUFFIX;
        public static final String HOST_SUFFIX = TEMPLATE_PREFIX + "host_suffix" + TEMPLATE_SUFFIX;

        public static final String ROOT = "/";
        public static final String WEB = "/web";
        public static final String ROOT_BY_NAME = "/{name:.*}";
        public static final String IMAGE_BY_NAME = "/image/{name}";
        public static final String IMAGE_GIF_BY_NAME = "/image/gif/{name}";
    }

    public static final String MEDIA_TYPE_TEXT_JAVASCRIPT = "text/javascript";
    public static final String MEDIA_TYPE_TEXT_CSS = "text/css";
    public static final String MEDIA_TYPE_IMAGE_PNG = "image/png";
    public static final String MEDIA_TYPE_IMAGE_GIF = "image/gif";

    public static final String HTTPS = "https";
    public static final String HTTPS_PREFIX = HTTPS + "://";

    public static final String NO_REFRESH_TOKEN = "<no_refresh_token>";

    public static final AtomicBoolean shouldDumpRequest = new AtomicBoolean(false);
    public static final AtomicBoolean shouldDumpResponse = new AtomicBoolean(false);
}
