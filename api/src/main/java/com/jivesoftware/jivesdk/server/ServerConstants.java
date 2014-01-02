package com.jivesoftware.jivesdk.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jivesoftware.jivesdk.impl.utils.JiveSDKUtils;
import org.apache.http.HttpStatus;

import java.util.List;
import java.util.Set;
import java.util.UUID;
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
    public static final String SERVICE_UUID = UUID.randomUUID().toString();

    public static final Set<Integer> RETRIABLE_RESPONSE_CODES = Sets.newHashSet(
            HttpStatus.SC_REQUEST_TIMEOUT,
            HttpStatus.SC_BAD_GATEWAY,
            HttpStatus.SC_SERVICE_UNAVAILABLE,
            HttpStatus.SC_GATEWAY_TIMEOUT
    );

    public static final String SPRING_CONTEXT_FILE_NAME_CORE = "spring-coreContext.xml";
    public static final String SPRING_CONTEXT_FILE_NAME_POLLER = "spring-pollerContext.xml";
    public static final String SPRING_CONTEXT_FILE_NAME_PUSHER = "spring-pusherContext.xml";
    public static final String SPRING_CONTEXT_FILE_NAME_SERVICE = "spring-serviceContext.xml";
    public static final String SPRING_CONTEXT_FILE_NAME_COMMENT_SYNCER = "spring-comment-syncer.xml";

    public static final String ENDPOINT_HEALTH_CHECK = "/health";
    public static final String CONNECTION_TEST_SUFFIX = "/connection";

    public static final String PUSHER_ENDPOINT_INCOMING_DATA = "/data";
    public static final String PUSHER_ENDPOINT_OPPORTUNITY_DATA_SUFFIX = "/opportunity";
    public static final String PUSHER_ENDPOINT_OPPORTUNITY_DATA = PUSHER_ENDPOINT_INCOMING_DATA + PUSHER_ENDPOINT_OPPORTUNITY_DATA_SUFFIX;
    public static final String PUSHER_ENDPOINT_CHATTER_DATA_SUFFIX = "/chatter";
    public static final String PUSHER_ENDPOINT_CHATTER_DATA = PUSHER_ENDPOINT_INCOMING_DATA + PUSHER_ENDPOINT_CHATTER_DATA_SUFFIX;
    public static final String PUSHER_ENDPOINT_INVALID_TOKEN_SUFFIX = "/invalidToken";
    public static final String PUSHER_ENDPOINT_INVALID_TOKEN = PUSHER_ENDPOINT_INCOMING_DATA + PUSHER_ENDPOINT_INVALID_TOKEN_SUFFIX;
    public static final String PUSHER_ENDPOINT_CONNECTION_TEST = ENDPOINT_HEALTH_CHECK + CONNECTION_TEST_SUFFIX;
    public static final String POLLER_ENDPOINT_CONNECTION_TEST = ENDPOINT_HEALTH_CHECK + CONNECTION_TEST_SUFFIX;
    public static final String COMMENT_SYNC_ENDPOINT_CONNECTION_TEST = ENDPOINT_HEALTH_CHECK + CONNECTION_TEST_SUFFIX;

    public static final String METRIC_SERVICE_CONFIGURATION_REQUEST_INCOMING = "service.configuration.incoming.count";
    public static final String METRIC_SERVICE_CONFIGURATION_REQUEST_SFDC = "service.configuration.sfdc.count";
    public static final String METRIC_SERVICE_CONFIGURATION_FAILURE_SFDC = "service.configuration.failures.sfdc";
    public static final String METRIC_SERVICE_CONFIGURATION_FAILURE_OTHER = "service.configuration.failures.other";
    public static final String METRIC_SERVICE_REGISTRATION_REQUESTS = "service.registration.count";
    public static final String METRIC_SERVICE_REGISTRATION_FAILURES_JIVE_ID = "service.registration.failures.jiveid";
    public static final String METRIC_SERVICE_REGISTRATION_FAILURES_JIVE_INSTANCE_OAUTH = "service.registration.failures.jiveinstanceoauth";
    public static final String METRIC_SERVICE_REGISTRATION_FAILURES_UNAUTHORIZED = "service.registration.failures.unauthorized";
    public static final String METRIC_SERVICE_REGISTRATION_FAILURES_BAD_REQUEST = "service.registration.failures.badrequest";
    public static final String METRIC_SERVICE_REGISTRATION_FAILURES_TOTAL = "service.registration.failures.total";
    public static final String METRIC_SERVICE_REGISTRATION_SUCCESS_TOTAL = "service.registration.success.total";
    public static final String METRIC_SERVICE_REGISTRATION_IGNORED_TOTAL = "service.registration.ignored.total";
    public static final String METRIC_SERVICE_INSTANCE_REGISTRATION_REQUESTS = "service.instance_registration.count";
    public static final String METRIC_SERVICE_INSTANCE_REGISTRATION_FAILURES_UNAUTHORIZED = "service.instance_registration.failures.unauthorized";
    public static final String METRIC_SERVICE_INSTANCE_REGISTRATION_FAILURES_BAD_REQUEST = "service.instance_registration.failures.badrequest";
    public static final String METRIC_SERVICE_INSTANCE_REGISTRATION_FAILURES_TOTAL = "service.instance_registration.failures.total";
    public static final String METRIC_SERVICE_INSTANCE_REGISTRATION_SUCCESS_TOTAL = "service.instance_registration.success.total";

    public static final List<String> METRICS_SERVICE_CONFIGURE = Lists.newArrayList(
            METRIC_SERVICE_CONFIGURATION_REQUEST_INCOMING, METRIC_SERVICE_CONFIGURATION_REQUEST_SFDC,
            METRIC_SERVICE_CONFIGURATION_FAILURE_SFDC, METRIC_SERVICE_CONFIGURATION_FAILURE_OTHER
    );

    public static final List<String> METRICS_SERVICE_REGISTER = Lists.newArrayList(
            METRIC_SERVICE_REGISTRATION_REQUESTS, METRIC_SERVICE_REGISTRATION_FAILURES_JIVE_ID,
            METRIC_SERVICE_REGISTRATION_FAILURES_JIVE_INSTANCE_OAUTH, METRIC_SERVICE_REGISTRATION_FAILURES_UNAUTHORIZED,
            METRIC_SERVICE_REGISTRATION_FAILURES_BAD_REQUEST, METRIC_SERVICE_REGISTRATION_FAILURES_TOTAL,
            METRIC_SERVICE_REGISTRATION_SUCCESS_TOTAL, METRIC_SERVICE_REGISTRATION_IGNORED_TOTAL
    );

    public static final List<String> METRICS_SERVICE_INSTANCE_REGISTER = Lists.newArrayList(
            METRIC_SERVICE_INSTANCE_REGISTRATION_REQUESTS, METRIC_SERVICE_INSTANCE_REGISTRATION_FAILURES_UNAUTHORIZED,
            METRIC_SERVICE_INSTANCE_REGISTRATION_FAILURES_BAD_REQUEST, METRIC_SERVICE_INSTANCE_REGISTRATION_FAILURES_TOTAL,
            METRIC_SERVICE_INSTANCE_REGISTRATION_SUCCESS_TOTAL
    );

    public static final String METRIC_POLLER_OPPORTUNITY_SEND_FAILURES = "poller.data.send.failures.opportunity";
    public static final String METRIC_POLLER_CHATTER_SEND_FAILURES = "poller.data.send.failures.chatter";
    public static final String METRIC_POLLER_INVALID_CREDENTIALS_SEND_FAILURES = "poller.data.send.failures.invalid.credentials";

    public static final List<String> METRICS_POLLER = Lists.newArrayList(
            METRIC_POLLER_OPPORTUNITY_SEND_FAILURES, METRIC_POLLER_CHATTER_SEND_FAILURES, METRIC_POLLER_INVALID_CREDENTIALS_SEND_FAILURES);

    /**
     * Tile for showing opportunity details
     */
    public static final String ITEM_NAME_TILE_OPPORTUNITY_DETAILS = "opportunity-details";
    /**
     * Tile for showing account details
     */
    public static final String ITEM_NAME_TILE_ACCOUNT_DETAILS = "account-details";
    /**
     * Tile for showing probability gauge
     */
    public static final String ITEM_NAME_TILE_PROBABILITY = "opportunity-probability";
    /**
     * External Chatter stream
     */
    public static final String ITEM_NAME_STREAM_CHATTER = "chatter-stream";
    /**
     * Tile (table type) for showing custom SFDC fields
     */
    public static final String ITEM_NAME_TILE_SFDC_CUSTOM = "sfdc-custom";

    public static final String[] ITEM_NAME_TILES = {
            ITEM_NAME_TILE_OPPORTUNITY_DETAILS, ITEM_NAME_TILE_ACCOUNT_DETAILS, ITEM_NAME_TILE_PROBABILITY
    };

    public static final String OAUTH_SUFFIX = "/oauth2/token";

    public static final String JIVE_TILES_MIGRATION_ENDPOINT = "/integration/tiles/migration";

    public class Endpoints {
        public static final String AUTHORIZE_URL = "/authorizeUrl";
        public static final String TICKET = "/ticket";
        public static final String QUERY = "/query";

        public static final String INSTANCE = "/instance";
        public static final String REGISTER = "/register";
        public static final String UNREGISTER = "/unregister";
        public static final String CONFIGURE = "/configure";
        public static final String INSTANCES = INSTANCE + "s";


        public static final String TILE = "/tile";
        public static final String OAUTH_CALLBACK = "/_callback";

        public static final String TEMPLATE_PREFIX = "{{{";
        public static final String TEMPLATE_SUFFIX = "}}}";
        public static final String HOST_TEMPLATE = TEMPLATE_PREFIX + "host" + TEMPLATE_SUFFIX;
        public static final String TIME_TEMPLATE = TEMPLATE_PREFIX + "time" + TEMPLATE_SUFFIX;
        public static final String HOST_SUFFIX = TEMPLATE_PREFIX + "host_suffix" + TEMPLATE_SUFFIX;

        public static final String ROOT = "/";
        public static final String WEB = "/web";
        public static final String ROOT_BY_NAME = "/{name:.*}";
        public static final String SCRIPT_BY_NAME = "/script/{name}";
        public static final String STYLE_BY_NAME = "/style/{name}";
        public static final String IMAGE_BY_NAME = "/image/{name}";
        public static final String IMAGE_GIF_BY_NAME = "/image/gif/{name}";
    }

    public static final String MEDIA_TYPE_TEXT_JAVASCRIPT = "text/javascript";
    public static final String MEDIA_TYPE_TEXT_CSS = "text/css";
    public static final String MEDIA_TYPE_IMAGE_PNG = "image/png";
    public static final String MEDIA_TYPE_IMAGE_GIF = "image/gif";

    public static final String HTTPS = "https";
    public static final String HTTPS_PREFIX = HTTPS + "://";

    public static final String SCOPE = "scope";
    public static final String SCOPE_PRE_TITAN = "pre7c4_scope";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String URL = "url";
    public static final String STATUS = "status";

    public static final String NO_REFRESH_TOKEN = "<no_refresh_token>";

    public static final int MIN_IDLE_WORKER_SLEEP_TIME = 5;
    public static final int MAX_IDLE_WORKER_SLEEP_TIME = 10;
    public static final int OFFSET_IDLE_WORKER_SLEEP_TIME = MAX_IDLE_WORKER_SLEEP_TIME - MIN_IDLE_WORKER_SLEEP_TIME + 1;

    public static final AtomicBoolean shouldDumpRequest = new AtomicBoolean(false);
    public static final AtomicBoolean shouldDumpResponse = new AtomicBoolean(false);
}
