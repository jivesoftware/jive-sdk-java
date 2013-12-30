package com.jivesoftware.jivesdk.example.db;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonGetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.node.TextNode;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class TileDefinition extends AbstractEntity {

    // the various URLs
    private String action;
    private String config;
    private String register;
    private String client_id;     // client_id URL
    private Icons icons;

    // example data
    private JsonNode sampleData;

    private String displayName;
    private String id;
    private String name;
    private String style;
    private String description;
    private String state;

    private Map<Locale, Properties> localesToI18nProps = new HashMap<Locale,Properties>();

    // todo - missing: icons, updated
    // see https://brewspace.jiveland.com/docs/DOC-104380 section 2.2 Tile Definition for delta

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    @JsonIgnore
    public JsonNode getSampleData() {
        return sampleData;
    }

    public void setSampleData(JsonNode sampleData) {
        this.sampleData = sampleData;
    }

    @JsonGetter
    public String getSampleDataString() {
        return sampleData.toString();
    }

    public void setSampleDataString(String dataString) {
        sampleData = new TextNode(dataString);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Icons getIcons() {
        return icons;
    }

    public void setIcons(Icons icons) {
        this.icons = icons;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonIgnore
    public Map<Locale, Properties> getLocalesToI18nProps() {
        return localesToI18nProps;
    }

    @JsonIgnore
    public void putI18nProp(Locale l, String key, String value) {
        Properties props = localesToI18nProps.get(l);
        if (props == null) {
            props = new Properties();
            localesToI18nProps.put(l, props);
        }
        props.put(key, value);
    }

}
