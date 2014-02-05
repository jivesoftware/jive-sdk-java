package com.jivesoftware.jivesdk.api;

import javax.annotation.Nonnull;

/**
 * The connection details for a jive instance.
 */
public interface RegisteredInstance {
    @Nonnull
    String getTenantId();

    @Nonnull
    String getUrl();

    @Nonnull
    String getClientId();

    @Nonnull
    String getClientSecret();

    void setTenantId(@Nonnull String tenantId);

    void setUrl(@Nonnull String url);

    void setClientId(@Nonnull String clientId);

    void setClientSecret(@Nonnull String clientSecret);

    @Nonnull
    String toSimpleString();

	long getId();

	void setId(long id);
}
