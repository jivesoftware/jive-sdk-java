package com.jivesoftware.jivesdk.api;

import com.jivesoftware.externalclient.JiveOAuthClient;
import com.jivesoftware.jivesdk.impl.auth.jiveauth.JiveInstanceOAuthTokenRefresher;
import com.jivesoftware.jivesdk.impl.http.*;
import com.jivesoftware.jivesdk.server.JiveAuthorizationValidator;
import com.jivesoftware.jivesdk.server.JiveAuthorizationValidatorImpl;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * This is the main entry point to the jive sdk.  All object instances are created from this class
 * and can be retrieved from the appopriate methods.
 *
 */
@Singleton
public class JiveSDKManager {
	public static JiveSDKManager instance = new JiveSDKManager();
	private final RestDriverImpl restDriver;
	private final HttpClientFactoryImpl httpClientFactory;
	private final JiveInstanceOAuthClient jiveInstanceOAuthClient;
	private JiveClientImpl jiveClient;
	private JiveInstanceOAuthTokenRefresher jiveTokenRefresher;

	private InstanceRegistrationHandler instanceRegistrationHandler;
	private JiveAuthorizationValidator authorizationValidator;

	public JiveSDKManager() {
		// The order of initialization here matters.  Since this is an api we don't use
		// dependency injection, so this class is meant to provide instances for each
		// item:
		httpClientFactory = new HttpClientFactoryImpl();
		httpClientFactory.init();
		restDriver = new RestDriverImpl();
		restDriver.setHttpClientFactory(httpClientFactory);
		jiveTokenRefresher = new JiveInstanceOAuthTokenRefresher();
		jiveTokenRefresher.setRestDriver(restDriver);
		jiveInstanceOAuthClient = new JiveInstanceOAuthClient();
		jiveInstanceOAuthClient.setRestDriver(restDriver);
		jiveClient = new JiveClientImpl();
		jiveClient.setRestDriver(restDriver);
		jiveClient.setJiveTokenRefresher(jiveTokenRefresher);
		authorizationValidator = new JiveAuthorizationValidatorImpl();
		instance = this;

	}

	public static JiveSDKManager getInstance() {
		return instance;
	}

	public InstanceRegistrationHandler getInstanceRegistrationHandler() {
		return instanceRegistrationHandler;
	}

	/**
	 * Register the instanceRegistrationHandler with the SDK.  This can be used if there wasn't an injection framework used to create
	 * this class.
	 * @param instanceRegistrationHandler
	 */
	public void setInstanceRegistrationHandler(InstanceRegistrationHandler instanceRegistrationHandler) {
		this.instanceRegistrationHandler = instanceRegistrationHandler;
	}

	public RestDriver getRestDriver() {
		return restDriver;
	}

	public HttpClientFactory getHttpClientFactory() {
		return httpClientFactory;
	}

	public JiveCredentialsAcquirer getJiveCredentialsAcquirer() {
		return jiveInstanceOAuthClient;
	}

	public JiveOAuthClient getJiveOAuthClient() {
		return jiveInstanceOAuthClient;
	}

	public JiveClientImpl getJiveClient() {
		return jiveClient;
	}

	public JiveTokenRefresher getJiveTokenRefresher() {
		return jiveTokenRefresher;
	}

	public JiveAuthorizationValidator getAuthorizationValidator() {
		return authorizationValidator;
	}


}
