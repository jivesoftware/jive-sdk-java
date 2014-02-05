package com.jivesoftware.jivesdk.api;

import javax.annotation.Nonnull;

/**
 * Interface representing oauth credentials.
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
