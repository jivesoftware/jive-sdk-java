package com.jivesoftware.jivesdk.api;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 21/7/13
 * Time: 1:39 PM
 */
public interface InstanceRegistrationHandler {
    RegisteredInstance register(@Nonnull InstanceRegistrationRequest request);
    RegisteredInstance getByTenantId(String tenantId);
    void register(@Nonnull TileRegistrationRequest request);

	/**
	 * The object was updated and should be re-persisted.
	 *
	 * This can happen to a tileInstance if the oauth token was refreshed.
	 * @param object
	 */
	void update(Object object);
}
