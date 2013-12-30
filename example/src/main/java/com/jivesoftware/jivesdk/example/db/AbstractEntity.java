package com.jivesoftware.jivesdk.example.db;


import java.util.Date;
import java.util.Map;

/**
 * Convenience base class for entities
 */
public class AbstractEntity implements Comparable<AbstractEntity> {

    private Date published = new Date();
    //private Map<String,Resource> resources = new HashMap<String,Resource>();
    private Date updated = new Date();

    private Map<String, Resource> resources;

    public AbstractEntity() {

    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public Map<String, Resource> getResources() {
        return resources;
    }

    public void setResources(Map<String, Resource> resources) {
        this.resources = resources;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }



    @Override
    public int compareTo(AbstractEntity o) {
        return published.compareTo(o.published);
    }
}
