package com.jivesoftware.jivesdk.api;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: sharon
 * Date: 29/1/13
 * Time: 10:14 AM
 */
public interface JiveCredentialsAcquirer {
    /**
     * Responsible for acquiring credentials to push data to Jive.
     *
     *
     * @param tileRegistrationRequest The request
     * @return Optional credentials, in case the request completed successfully, or Optional.absent() if not.
     */
    @Nonnull
    Credentials acquireCredentials(@Nonnull TileRegistrationRequest tileRegistrationRequest);
}
