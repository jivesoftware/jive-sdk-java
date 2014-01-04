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

package com.jivesoftware.jivesdk.impl.http;

import com.jivesoftware.jivesdk.api.*;
import org.apache.http.client.HttpClient;
import org.junit.Before;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 */
public class JiveSDKTestBase {
	protected JiveAuthorizationValidator jiveAuthorizationValidator;
	protected InstanceRegistrationHandler instanceHandler;
	protected RegisteredInstance registeredInstance;
	protected RestDriver restDriver;
	protected HttpResponse response;
	protected String algorithm = "HmacSHA256";
	protected String clientId = "abdef";
	protected String url = "http://foo.bar";
	protected String tenantId = "1235-2342342-abc";
	protected String clientSecret = "abcdefg";
	protected long id = 5;


	@Before
	public void setUp() throws Exception {
		jiveAuthorizationValidator = new JiveAuthorizationValidatorImpl();
		instanceHandler = mock(InstanceRegistrationHandler.class);
		registeredInstance = mock(RegisteredInstance.class);
		JiveSDKManager.getInstance().setInstanceRegistrationHandler(instanceHandler);
		restDriver = mock(RestDriver.class);
		response = mock(HttpResponse.class);
		when(restDriver.execute(any(RestRequest.class))).thenReturn(response);
		when(restDriver.execute(any(RestRequest.class), anyInt())).thenReturn(response);
		when(restDriver.executeWithClient(any(RestRequest.class), any(HttpClient.class))).thenReturn(response);
		when(restDriver.executeWithClient(any(RestRequest.class), any(HttpClient.class), anyInt())).thenReturn(response);
		when(restDriver.executeForResponseBody(any(RestRequest.class))).thenReturn(response);
		when(instanceHandler.getByTenantId(anyString())).thenReturn(registeredInstance);
		when(registeredInstance.getClientId()).thenReturn(clientId);
		when(registeredInstance.getClientSecret()).thenReturn(clientSecret);
		when(registeredInstance.getTenantId()).thenReturn(tenantId);
		when(registeredInstance.getUrl()).thenReturn(url);
		when(registeredInstance.getId()).thenReturn(id);


	}
}
