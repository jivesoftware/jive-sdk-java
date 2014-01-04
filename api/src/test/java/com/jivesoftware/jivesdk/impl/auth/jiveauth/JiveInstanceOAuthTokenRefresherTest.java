/*
 * Copyright (c) 2014. Jive Software
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 */

package com.jivesoftware.jivesdk.impl.auth.jiveauth;

import com.google.common.base.Optional;
import com.jivesoftware.externalclient.JiveOAuthResponse;
import com.jivesoftware.jivesdk.api.InvalidRequestException;
import com.jivesoftware.jivesdk.api.RestAccessException;
import com.jivesoftware.jivesdk.api.TileInstance;
import com.jivesoftware.jivesdk.impl.CredentialsImpl;
import com.jivesoftware.jivesdk.impl.http.AuthTokenException;
import com.jivesoftware.jivesdk.impl.http.JiveSDKTestBase;
import com.jivesoftware.jivesdk.impl.http.RestRequest;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 */
public class JiveInstanceOAuthTokenRefresherTest extends JiveSDKTestBase {
	private JiveInstanceOAuthTokenRefresher refresher;

	@Before
	public void setup() throws Exception {
		super.setUp();
		refresher = new JiveInstanceOAuthTokenRefresher();
		refresher.setRestDriver(restDriver);
	}

	@Test
	public void refreshTokenHandleNewAccessToken() throws InvalidRequestException {
		TileInstance tileInstance = new TileInstance();
		tileInstance.setJiveUrl(url);
		tileInstance.setJivePushUrl(url + "/tiles/1000");
		tileInstance.setCredentials(new CredentialsImpl());
		Optional<String> responseBody = Optional.of("{\"" + JiveOAuthResponse.ACCESS_TOKEN + "\": \"foo\"}");
		when(response.getStatusCode()).thenReturn(HttpStatus.SC_OK);
		when(response.getResponseBody()).thenReturn(responseBody);
		JiveOAuthResponse response = refresher.refreshToken(tileInstance);
		assertNotNull("Should successfully refresh token", response);
		assertEquals("Should successfully refresh token", response.getAccessToken(), "foo");
	}

	@Test
	public void refreshTokenHandleError() throws InvalidRequestException {
		TileInstance tileInstance = new TileInstance();
		tileInstance.setJiveUrl(url);
		tileInstance.setJivePushUrl(url + "/tiles/1000");
		tileInstance.setCredentials(new CredentialsImpl());
		Optional<String> responseBody = Optional.of("{\"" + JiveOAuthResponse.ACCESS_TOKEN + "\": \"foo\"}");
		when(response.getStatusCode()).thenReturn(HttpStatus.SC_FORBIDDEN);
		when(response.getResponseBody()).thenReturn(responseBody);
		JiveOAuthResponse response = refresher.refreshToken(tileInstance);
		assertNull("Should return null on error", response);
	}

	@Test(expected = InvalidRequestException.class)
	public void refreshTokenHandleAuthTokenException() throws InvalidRequestException, AuthTokenException, RestAccessException {
		TileInstance tileInstance = new TileInstance();
		tileInstance.setJiveUrl(url);
		tileInstance.setJivePushUrl(url + "/tiles/1000");
		tileInstance.setCredentials(new CredentialsImpl());
		Optional<String> responseBody = Optional.of("{\"" + JiveOAuthResponse.ACCESS_TOKEN + "\": \"foo\"}");
		when(response.getStatusCode()).thenReturn(HttpStatus.SC_UNAUTHORIZED);
		when(response.getResponseBody()).thenReturn(responseBody);
		when(restDriver.execute(any(RestRequest.class))).thenThrow(AuthTokenException.class);
		JiveOAuthResponse response = refresher.refreshToken(tileInstance);
		fail("refresh token should have thrown an error.");
	}

	@Test
	public void refreshTokenHandleException() throws InvalidRequestException, AuthTokenException, RestAccessException {
		// I am not positive this is the right behavoir, but it is how it is working now.
		TileInstance tileInstance = new TileInstance();
		tileInstance.setJiveUrl(url);
		tileInstance.setJivePushUrl(url + "/tiles/1000");
		tileInstance.setCredentials(new CredentialsImpl());
		Optional<String> responseBody = Optional.of("{\"" + JiveOAuthResponse.ACCESS_TOKEN + "\": \"foo\"}");
		when(response.getStatusCode()).thenReturn(HttpStatus.SC_UNAUTHORIZED);
		when(response.getResponseBody()).thenReturn(responseBody);
		when(restDriver.execute(any(RestRequest.class))).thenThrow(IllegalArgumentException.class);
		JiveOAuthResponse response = refresher.refreshToken(tileInstance);

		assertNull("Should return null on error", response);
	}

}
