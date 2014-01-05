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
        IGNORE_HTTP_SIGNATURE_URLS_IN_INSTANCE_REGISTRATION("false"),
        SIGNATURE_URL_CERTIFICATE_SUBJECT_NAME("Jive Software, Inc.");

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
