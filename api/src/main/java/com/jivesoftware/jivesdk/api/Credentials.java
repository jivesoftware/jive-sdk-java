package com.jivesoftware.jivesdk.api;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 27/1/13
 * Time: 5:23 PM
 */
public interface Credentials {
    @Nonnull
    String getUrl();

    void setAuthToken(@Nonnull String newAuthToken);

    @Nonnull
    String getAuthToken();

    @Nonnull
    String getRefreshToken();

	void setRefreshToken(@Nonnull String refreshToken);
}
