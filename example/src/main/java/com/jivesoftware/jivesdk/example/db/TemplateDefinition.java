package com.jivesoftware.jivesdk.example.db;

import java.util.ArrayList;
import java.util.List;

public class TemplateDefinition extends AbstractEntity {

    private String id;
    private String name;
    private String displayName;
    private String description;
    private String subtitle;
    private String blurb;
    private String groupType = "OPEN";
    private String backgroundImageURL;
    private boolean visibleToExternalContributors;
    private boolean global;
    private List<TileDefinition> tiles = new ArrayList<TileDefinition>();

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<TileDefinition> getTiles() {
        return tiles;
    }

    public void setTiles(List<TileDefinition> tiles) {
        this.tiles = tiles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBackgroundImageURL() {
        return backgroundImageURL;
    }

    public void setBackgroundImageURL(String backgroundImageURL) {
        this.backgroundImageURL = backgroundImageURL;
    }

    public boolean getVisibleToExternalContributors() {
        return visibleToExternalContributors;
    }

    public void setVisibleToExternalContributors(boolean visibleToExternalContributors) {
        this.visibleToExternalContributors = visibleToExternalContributors;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean getGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }
}
