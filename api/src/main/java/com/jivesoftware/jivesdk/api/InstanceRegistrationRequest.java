package com.jivesoftware.jivesdk.api;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.annotation.Nonnull;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 17/7/13
 * Time: 5:55 PM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstanceRegistrationRequest {
    public final static String TENANT_ID = "tenantId";
    public final static String JIVE_SIGNATURE_URL = "jiveSignatureURL";
    public final static String TIMESTAMP = "timestamp";
    public final static String JIVE_URL = "jiveUrl";
    public final static String JIVE_SIGNATURE = "jiveSignature";
    public final static String SCOPE = "scope";
    public final static String CODE = "code";
    public final static String CLIENT_SECRET = "clientSecret";
    public final static String CLIENT_ID = "clientId";

    private String tenantId;
    private String jiveSignatureURL;
    private String timestamp;
    private String jiveUrl;
    private String jiveSignature;
    private String scope;
    private String code;
    private String clientSecret;
    private String clientId;

    @JsonCreator
    public InstanceRegistrationRequest(@JsonProperty(TENANT_ID) String tenantId,
                                       @JsonProperty(JIVE_SIGNATURE_URL) String jiveSignatureURL,
                                       @JsonProperty(TIMESTAMP) String timestamp,
                                       @JsonProperty(JIVE_URL) String jiveUrl,
                                       @JsonProperty(JIVE_SIGNATURE) String jiveSignature,
                                       @JsonProperty(SCOPE) String scope,
                                       @JsonProperty(CODE) String code,
                                       @JsonProperty(CLIENT_SECRET) String clientSecret,
                                       @JsonProperty(CLIENT_ID) String clientId) {
        this.tenantId = tenantId;
        this.jiveSignatureURL = jiveSignatureURL;
        this.timestamp = timestamp;
        this.jiveUrl = jiveUrl;
        this.jiveSignature = jiveSignature;
        this.scope = scope;
        this.code = code;
        this.clientSecret = clientSecret;
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getCode() {
        return code;
    }

    public String getScope() {
        return scope;
    }

    public String getJiveSignature() {
        return jiveSignature;
    }

    public String getJiveUrl() {
        return jiveUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getJiveSignatureURL() {
        return jiveSignatureURL;
    }

    public String getTenantId() {
        return tenantId;
    }

    @Nonnull
    public SortedMap<String, String> toSortedMap() throws NoSuchAlgorithmException {
        // Encode the client-secret
        String encodedClientSecret = DigestUtils.sha256Hex(clientSecret);
        SortedMap<String, String> sortedMap = Maps.newTreeMap();
        sortedMap.put(InstanceRegistrationRequest.CLIENT_ID, clientId);
        sortedMap.put(InstanceRegistrationRequest.CLIENT_SECRET, encodedClientSecret);
        sortedMap.put(InstanceRegistrationRequest.CODE, code);
        sortedMap.put(InstanceRegistrationRequest.JIVE_SIGNATURE_URL, jiveSignatureURL);
        sortedMap.put(InstanceRegistrationRequest.JIVE_URL, jiveUrl);
        sortedMap.put(InstanceRegistrationRequest.SCOPE, scope);
        sortedMap.put(InstanceRegistrationRequest.TENANT_ID, tenantId);
        sortedMap.put(InstanceRegistrationRequest.TIMESTAMP, timestamp);
        return sortedMap;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("tenantId", tenantId)
                .add("jiveSignatureURL", jiveSignatureURL)
                .add("timestamp", timestamp)
                .add("jiveUrl", jiveUrl)
                .add("jiveSignature", jiveSignature)
                .add("scope", scope)
                .add("code", code)
                .add("clientId", clientId)
                .toString();
    }
}
