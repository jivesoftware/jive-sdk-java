package com.jivesoftware.jivesdk.example.db;

import java.util.ArrayList;
import java.util.List;

/**
 * An individual resource entry for an entity.
 */
public class Resource {

    private List<String> allowed;
    private String ref;

    public Resource() {}

    public Resource(String ref, String... allowed) {
        this.allowed = new ArrayList<String>(allowed.length);
        for (String value : allowed) {
            this.allowed.add(value);
        }
        this.ref = ref;
    }

    public List<String> getAllowed() {
        return allowed;
    }

    public void setAllowed(List<String> allowed) {
        this.allowed = allowed;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

}
