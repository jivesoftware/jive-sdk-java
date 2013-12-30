package com.jivesoftware.jivesdk.api;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 21/7/13
 * Time: 1:08 PM
 */
public interface RegisteredInstance extends EntityBase {
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
}
