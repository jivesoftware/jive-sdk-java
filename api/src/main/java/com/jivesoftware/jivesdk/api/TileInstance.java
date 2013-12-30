package com.jivesoftware.jivesdk.api;

import java.util.HashMap;
import java.util.Map;

public class TileInstance  {
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
    private Credentials credentials;

    public TileInstance() {
    }

    public TileInstance(TileRegistrationRequest request) {
        this.tempToken = request.getTempToken();
        this.config = request.getConfig();
        this.jivePushUrl = request.getJivePushUrl();
        this.jiveInstanceUrl = request.getJiveInstanceUrl();
        this.tenantId = request.getTenantId();
        this.itemType = request.getItemType();
        this.tileDefName = request.getTileDefName();
        this.guid = request.getGuid();
    }


    public String getTempToken() {
        return tempToken;
    }

    public void setTempToken(String tempToken) {
        this.tempToken = tempToken;
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

    public String getJiveInstanceUrl() {
        return jiveInstanceUrl;
    }

    public void setJiveInstanceUrl(String jiveInstanceUrl) {
        this.jiveInstanceUrl = jiveInstanceUrl;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TileInstance{");
        sb.append("tempToken='").append(tempToken).append('\'');
        sb.append(", config=").append(config);
        sb.append(", jivePushUrl='").append(jivePushUrl).append('\'');
        sb.append(", jiveInstanceUrl='").append(jiveInstanceUrl).append('\'');
        sb.append(", tenantId='").append(tenantId).append('\'');
        sb.append(", itemType='").append(itemType).append('\'');
        sb.append(", tileDefName='").append(tileDefName).append('\'');
        sb.append(", guid='").append(guid).append('\'');
        sb.append(", clientId='").append(clientId).append('\'');
        sb.append(", clientSecret='").append(clientSecret).append('\'');
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
