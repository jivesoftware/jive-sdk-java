package com.jivesoftware.jivesdk.api;

import com.jivesoftware.jivesdk.api.TileUnregisterRequest;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;

/**
 * Handler to implement to store registration into a local datastore.
 */
public interface InstanceRegistrationHandler {
    RegisteredInstance register(@Nonnull InstanceRegistrationRequest request);
    RegisteredInstance getByTenantId(String tenantId);
    void register(@Nonnull TileRegistrationRequest request);
	void unregister(TileUnregisterRequest unRegistrationRequest);
	/**
	 * The object was updated and should be re-persisted.
	 *
	 * This can happen to a tileInstance if the oauth token was refreshed.
	 * @param object
	 */
	void update(Object object);


}
