package com.jivesoftware.jivesdk.example.db;

import java.util.List;

/**
 * <p>Response to a request to introspect an access token's current validity.</p>
 */
public class IntrospectionResponse {

    private List<String> scope;
    private Boolean valid;

    public List<String> getScope() {
        return scope;
    }

    public void setScope(List<String> scope) {
        this.scope = scope;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

}
