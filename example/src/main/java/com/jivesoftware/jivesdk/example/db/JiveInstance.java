package com.jivesoftware.jivesdk.example.db;

import com.google.common.base.Objects;
import com.jivesoftware.jivesdk.api.RegisteredInstance;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 21/7/13
 * Time: 1:15 PM
 */
public class JiveInstance implements RegisteredInstance {
    protected long id;
    @Nonnull
    private String tenantId;
    @Nonnull
    private String url;
    @Nonnull
    private String clientId;
    @Nonnull
    private String clientSecret;

    public JiveInstance() {
    }

    public JiveInstance(@Nonnull String tenantId, @Nonnull String url, @Nonnull String clientId, @Nonnull String clientSecret) {
        this(-1, tenantId, url, clientId, clientSecret);
    }

    public JiveInstance(long id, @Nonnull String tenantId, @Nonnull String url, @Nonnull String clientId, @Nonnull String clientSecret) {
        this.id = id;
        this.tenantId = tenantId;
        this.url = url;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Nonnull
    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Nonnull
    @Override
    public String getUrl() {
        return url;
    }

    @Nonnull
    @Override
    public String getClientId() {
        return clientId;
    }

    @Nonnull
    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public void setTenantId(@Nonnull String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public void setUrl(@Nonnull String url) {
        this.url = url;
    }

    @Override
    public void setClientId(@Nonnull String clientId) {
        this.clientId = clientId;
    }

    @Override
    public void setClientSecret(@Nonnull String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Nonnull
    @Override
    public String toSimpleString() {
        return "Tenant: " + tenantId + ", URL: " + url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JiveInstance that = (JiveInstance) o;

        return clientId.equals(that.clientId) && clientSecret.equals(that.clientSecret) && tenantId.equals(that.tenantId) && url.equals(that.url);
    }

    @Override
    public int hashCode() {
        int result = tenantId.hashCode();
        result = 31 * result + (url.hashCode());
        result = 31 * result + (clientId.hashCode());
        result = 31 * result + (clientSecret.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("tenantId", tenantId)
                .add("url", url)
                .add("clientId", clientId)
                .toString();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return this.getId() == -1;
    }
}
