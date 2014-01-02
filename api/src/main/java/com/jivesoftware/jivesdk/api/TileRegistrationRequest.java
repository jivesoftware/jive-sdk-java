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
    public static final String PROPERTY_NAME_TILE_INSTANCE_ID = "id";

    private String code;
    private Map<String, String> config = new HashMap<String, String>();
    private String jivePushUrl;
    private String jiveUrl;
    private String tenantID;
    private String itemType;
    private String tileDefName;
    private String tileInstanceID;

    private String clientId;
    private String clientSecret;
    private RegisteredInstance instance;

    @JsonCreator
    public TileRegistrationRequest(@JsonProperty(PROPERTY_NAME_TEMP_TOKEN) String code,
								   @JsonProperty(PROPERTY_NAME_CONFIG_JSON) Map<String, String> config,
								   @JsonProperty(PROPERTY_NAME_JIVE_PUSH_URL) String jivePushUrl,
								   @JsonProperty(PROPERTY_NAME_JIVE_INSTANCE_URL) String jiveInstanceUrl,
								   @JsonProperty(PROPERTY_NAME_TENANT_ID) String tenantID,
								   @JsonProperty(PROPERTY_NAME_TILE_INSTANCE_ID) String tileInstanceID,
								   @JsonProperty(PROPERTY_NAME_ITEM_TYPE) String itemType) {
        this.code = code;
        this.config = config;
        this.tileInstanceID = tileInstanceID;

        // Workaround to external stream url
        this.jivePushUrl = JiveSDKUtils.normalizeItemUrl(jivePushUrl);
        this.jiveUrl = jiveInstanceUrl;
        this.tenantID = tenantID;

        this.tileDefName = itemType;
        this.itemType = itemType;
    }

    public String getCode() {
        return code;
    }

    public Map<String, String>  getConfig() {
        return config;
    }

    public String getJivePushUrl() {
        return jivePushUrl != null ? jivePushUrl.trim().toLowerCase() : null;
    }

    public String getJiveUrl() {
        return jiveUrl != null ? jiveUrl.trim().toLowerCase() : null;
    }

    public String getTenantID() {
        return tenantID;
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


	/**
	 * Get a globally unique tile instance id.  This combines the tile id and tenant id.
	 * @return
	 */
	public String getGlobalTileInstanceID() {
		return tenantID + "_" + this.tileInstanceID;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TileRegistrationRequest that = (TileRegistrationRequest) o;

		if (clientId != null ? !clientId.equals(that.clientId) : that.clientId != null) {
			return false;
		}
		if (clientSecret != null ? !clientSecret.equals(that.clientSecret) : that.clientSecret != null) {
			return false;
		}
		if (code != null ? !code.equals(that.code) : that.code != null) {
			return false;
		}
		if (config != null ? !config.equals(that.config) : that.config != null) {
			return false;
		}
		if (instance != null ? !instance.equals(that.instance) : that.instance != null) {
			return false;
		}
		if (itemType != null ? !itemType.equals(that.itemType) : that.itemType != null) {
			return false;
		}
		if (jivePushUrl != null ? !jivePushUrl.equals(that.jivePushUrl) : that.jivePushUrl != null) {
			return false;
		}
		if (jiveUrl != null ? !jiveUrl.equals(that.jiveUrl) : that.jiveUrl != null) {
			return false;
		}
		if (tenantID != null ? !tenantID.equals(that.tenantID) : that.tenantID != null) {
			return false;
		}
		if (tileDefName != null ? !tileDefName.equals(that.tileDefName) : that.tileDefName != null) {
			return false;
		}
		if (tileInstanceID != null ? !tileInstanceID.equals(that.tileInstanceID) : that.tileInstanceID != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = code != null ? code.hashCode() : 0;
		result = 31 * result + (config != null ? config.hashCode() : 0);
		result = 31 * result + (jivePushUrl != null ? jivePushUrl.hashCode() : 0);
		result = 31 * result + (jiveUrl != null ? jiveUrl.hashCode() : 0);
		result = 31 * result + (tenantID != null ? tenantID.hashCode() : 0);
		result = 31 * result + (itemType != null ? itemType.hashCode() : 0);
		result = 31 * result + (tileDefName != null ? tileDefName.hashCode() : 0);
		result = 31 * result + (tileInstanceID != null ? tileInstanceID.hashCode() : 0);
		result = 31 * result + (clientId != null ? clientId.hashCode() : 0);
		result = 31 * result + (clientSecret != null ? clientSecret.hashCode() : 0);
		result = 31 * result + (instance != null ? instance.hashCode() : 0);
		return result;
	}

	@Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("code", code)
                .add("config", config)
                .add("jivePushUrl", jivePushUrl)
                .add("jiveUrl", jiveUrl)
                .add("tenantID", tenantID)
                .add("itemType", itemType)
                .add("tileDefName", tileDefName)
                .add("tileInstanceID", tileInstanceID)
                .add("clientId", clientId)
                .toString();
    }
}
