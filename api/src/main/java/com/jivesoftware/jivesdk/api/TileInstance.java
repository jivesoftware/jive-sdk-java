package com.jivesoftware.jivesdk.api;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a tile that has been created on a specific jive instance.
 */
public class TileInstance  {
    private String code;
	/**
	 * Configuration from the tile config screen.  This is filled in from
	 * jive.tile.close(config) in the tile configuration app.
	 */
    private Map<String, String> config = new HashMap<String, String>();

	/**
	 * The url to push tile data.
	 */
    private String jivePushUrl;

	/**
	 * The jiveURL for this jive instance.
	 */
    private String jiveUrl;

	/**
	 * The unique identifier for this jive instance.
	 */
    private String tenantID;
    private String itemType;
    private String tileDefName;

	/**
	 * A globally unique identifier for this tile instance
	 */
	private String globalTileInstanceId;

	/**
	 * Additional credentials if needed.
	 */
    private Credentials credentials;

    public TileInstance() {
    }

    public TileInstance(TileRegistrationRequest request) {
        this.code = request.getCode();
        this.config = request.getConfig();
        this.jivePushUrl = request.getJivePushUrl();
        this.jiveUrl = request.getJiveUrl();
        this.tenantID = request.getTenantID();
        this.itemType = request.getItemType();
        this.tileDefName = request.getTileDefName();
		this.globalTileInstanceId = request.getGlobalTileInstanceID();
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public String getJivePushUrl() {
        return jivePushUrl;
    }

    public void setJivePushUrl(String jivePushUrl) {
        this.jivePushUrl = jivePushUrl;
    }

    public String getJiveUrl() {
        return jiveUrl;
    }

    public void setJiveUrl(String jiveUrl) {
        this.jiveUrl = jiveUrl;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getTileDefName() {
        return tileDefName;
    }

    public void setTileDefName(String tileDefName) {
        this.tileDefName = tileDefName;
    }

	public String getGlobalTileInstanceId() {
		return globalTileInstanceId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TileInstance that = (TileInstance) o;

		if (config != null ? !config.equals(that.config) : that.config != null) {
			return false;
		}
		if (credentials != null ? !credentials.equals(that.credentials) : that.credentials != null) {
			return false;
		}
		if (globalTileInstanceId != null ? !globalTileInstanceId.equals(
				that.globalTileInstanceId) : that.globalTileInstanceId != null) {
			return false;
		}
		if (itemType != null ? !itemType.equals(that.itemType) : that.itemType != null) {
			return false;
		}
		if (jiveUrl != null ? !jiveUrl.equals(that.jiveUrl) : that.jiveUrl != null) {
			return false;
		}
		if (jivePushUrl != null ? !jivePushUrl.equals(that.jivePushUrl) : that.jivePushUrl != null) {
			return false;
		}
		if (code != null ? !code.equals(that.code) : that.code != null) {
			return false;
		}
		if (tenantID != null ? !tenantID.equals(that.tenantID) : that.tenantID != null) {
			return false;
		}
		if (tileDefName != null ? !tileDefName.equals(that.tileDefName) : that.tileDefName != null) {
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
		result = 31 * result + (globalTileInstanceId != null ? globalTileInstanceId.hashCode() : 0);
		result = 31 * result + (credentials != null ? credentials.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("TileInstance{");
		sb.append("code='").append(code).append('\'');
		sb.append(", config=").append(config);
		sb.append(", jivePushUrl='").append(jivePushUrl).append('\'');
		sb.append(", jiveUrl='").append(jiveUrl).append('\'');
		sb.append(", tenantID='").append(tenantID).append('\'');
		sb.append(", itemType='").append(itemType).append('\'');
		sb.append(", tileDefName='").append(tileDefName).append('\'');
		sb.append(", globalTileInstanceId='").append(globalTileInstanceId).append('\'');
		sb.append(", credentials=").append(credentials);
		sb.append('}');
		return sb.toString();
	}

	public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Credentials getCredentials() {
        return credentials;
    }
}
