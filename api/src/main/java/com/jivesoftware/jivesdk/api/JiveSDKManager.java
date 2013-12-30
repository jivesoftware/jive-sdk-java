package com.jivesoftware.jivesdk.api;

import com.jivesoftware.externalclient.JiveOAuthClient;
import com.jivesoftware.jivesdk.impl.auth.jiveauth.JiveInstanceOAuthTokenRefresher;
import com.jivesoftware.jivesdk.impl.http.*;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 */
@Singleton
public class JiveSDKManager {
	public static JiveSDKManager instance;
	private final RestDriverImpl restDriver;
	private final HttpClientFactoryImpl httpClientFactory;
	private final JiveInstanceOAuthClient jiveInstanceOAuthClient;
	private JiveClientImpl jiveClient;
	private JiveInstanceOAuthTokenRefresher jiveTokenRefresher;

	@Inject
	private InstanceRegistrationHandler instanceRegistrationHandler;

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
		instance = this;

	}

	public static JiveSDKManager getInstance() {
		return instance;
	}

	public InstanceRegistrationHandler getInstanceRegistrationHandler() {
		return instanceRegistrationHandler;
	}

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

}
