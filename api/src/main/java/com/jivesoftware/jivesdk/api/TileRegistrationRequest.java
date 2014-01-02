package com.jivesoftware.jivesdk.api;

import com.google.common.base.Objects;
import com.jivesoftware.jivesdk.impl.utils.JiveSDKUtils;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Request from jive to register a new tile instance.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TileRegistrationRequest {
    public static final String PROPERTY_NAME_TEMP_TOKEN = "code";
    public static final String PROPERTY_NAME_CONFIG_JSON = "config";
    public static final String PROPERTY_NAME_JIVE_PUSH_URL = "url";
    public static final String PROPERTY_NAME_TENANT_ID = "tenantID";
    public static final String PROPERTY_NAME_JIVE_INSTANCE_URL = "jiveUrl";
    public static final String PROPERTY_NAME_ITEM_TYPE = "name";
    public static final String PROPERTY_NAME_GUID = "guid";

    private String tempToken;
    private Map<String, String> config = new HashMap<String, String>();
    private String jivePushUrl;
    private String jiveInstanceUrl;
    private String tenantId;
    private String itemType;
    private String tileDefName;
    private String guid;

    private String clientId;
    private String clientSecret;
    private RegisteredInstance instance;

    @JsonCreator
    public TileRegistrationRequest(@JsonProperty(PROPERTY_NAME_TEMP_TOKEN) String tempToken,
								   @JsonProperty(PROPERTY_NAME_CONFIG_JSON) Map<String, String> config,
								   @JsonProperty(PROPERTY_NAME_JIVE_PUSH_URL) String jivePushUrl,
								   @JsonProperty(PROPERTY_NAME_JIVE_INSTANCE_URL) String jiveInstanceUrl,
								   @JsonProperty(PROPERTY_NAME_TENANT_ID) String tenantId,
								   @JsonProperty(PROPERTY_NAME_GUID) String guid,
								   @JsonProperty(PROPERTY_NAME_ITEM_TYPE) String itemType) {
        this.tempToken = tempToken;
        this.config = config;
        this.guid = guid;

        // Workaround to external stream url
        this.jivePushUrl = JiveSDKUtils.normalizeItemUrl(jivePushUrl);
        this.jiveInstanceUrl = jiveInstanceUrl;
        this.tenantId = tenantId;

        this.tileDefName = itemType;
        this.itemType = itemType;
    }

    public String getTempToken() {
        return tempToken;
    }

    public Map<String, String>  getConfig() {
        return config;
    }

    public String getGuid() {
        return guid;
    }

    public String getJivePushUrl() {
        return jivePushUrl != null ? jivePushUrl.trim().toLowerCase() : null;
    }

    public String getJiveInstanceUrl() {
        return jiveInstanceUrl != null ? jiveInstanceUrl.trim().toLowerCase() : null;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getItemType() {
        return itemType;
    }

    public String getTileDefName() {
        return tileDefName;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public RegisteredInstance getInstance() {
        return instance;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setInstance(RegisteredInstance instance) {
        this.instance = instance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TileRegistrationRequest that = (TileRegistrationRequest) o;

        return !(clientId != null ? !clientId.equals(that.clientId) : that.clientId != null) && !(clientSecret != null ? !clientSecret.equals(that.clientSecret) : that.clientSecret != null) && !(config != null ? !config.equals(that.config) : that.config != null) && !(guid != null ? !guid.equals(that.guid) : that.guid != null) && !(itemType != null ? !itemType.equals(that.itemType) : that.itemType != null) && !(jiveInstanceUrl != null ? !jiveInstanceUrl.equals(that.jiveInstanceUrl) : that.jiveInstanceUrl != null) && !(jivePushUrl != null ? !jivePushUrl.equals(that.jivePushUrl) : that.jivePushUrl != null) && !(tempToken != null ? !tempToken.equals(that.tempToken) : that.tempToken != null) && !(tenantId != null ? !tenantId.equals(that.tenantId) : that.tenantId != null) && !(tileDefName != null ? !tileDefName.equals(that.tileDefName) : that.tileDefName != null);
    }

    @Override
    public int hashCode() {
        int result = tempToken != null ? tempToken.hashCode() : 0;
        result = 31 * result + (config != null ? config.hashCode() : 0);
        result = 31 * result + (jivePushUrl != null ? jivePushUrl.hashCode() : 0);
        result = 31 * result + (jiveInstanceUrl != null ? jiveInstanceUrl.hashCode() : 0);
        result = 31 * result + (tenantId != null ? tenantId.hashCode() : 0);
        result = 31 * result + (itemType != null ? itemType.hashCode() : 0);
        result = 31 * result + (tileDefName != null ? tileDefName.hashCode() : 0);
        result = 31 * result + (guid != null ? guid.hashCode() : 0);
        result = 31 * result + (clientId != null ? clientId.hashCode() : 0);
        result = 31 * result + (clientSecret != null ? clientSecret.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("tempToken", tempToken)
                .add("config", config)
                .add("jivePushUrl", jivePushUrl)
                .add("jiveInstanceUrl", jiveInstanceUrl)
                .add("tenantId", tenantId)
                .add("itemType", itemType)
                .add("tileDefName", tileDefName)
                .add("guid", guid)
                .add("clientId", clientId)
                .toString();
    }
}
