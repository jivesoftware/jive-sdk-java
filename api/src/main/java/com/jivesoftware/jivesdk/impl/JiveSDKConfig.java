package com.jivesoftware.jivesdk.impl;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 5/2/13
 * Time: 1:19 PM
 */
public class JiveSDKConfig {
    public enum CommonConfiguration {
        MAX_HTTP_CONNECTIONS_TOTAL("100"),
        MAX_HTTP_CONNECTIONS_PER_HOST("16"),
        HTTP_CONNECTION_TIMEOUT("10000"),
        HTTP_SOCKET_TIMEOUT("10000");

        private String defaultValue;

        private CommonConfiguration(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public String getKey() {
            return this.toString();
        }

        public String getDefault() {
            return defaultValue;
        }
    }



    public enum ServiceConfiguration {
        SFDC_REDIRECT_URL(""),
        LONG_RUNNING_JOB_THRESHOLD_IN_SECONDS("900"),
        COMMENT_SYNCER_BASE_URL("http://dealroom-qa-app1.jca.eng.jiveland.com/36000"),
        DATA_POLLER_BASE_URL("http://dealroom-qa-app1.jca.eng.jiveland.com/38000"),
        IGNORE_HTTP_SIGNATURE_URLS_IN_INSTANCE_REGISTRATION("false"),
        SIGNATURE_URL_CERTIFICATE_SUBJECT_NAME("Jive Software, Inc."),
        ENABLE_TITAN_MIGRATION("true");
        private String defaultValue;

        private ServiceConfiguration(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public String getKey() {
            return this.toString();
        }

        public String getDefault() {
            return defaultValue;
        }
    }
}
