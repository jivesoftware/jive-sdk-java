package com.jivesoftware.jivesdk.impl.http;


import com.jivesoftware.jivesdk.api.InstanceRegistrationHandler;
import com.jivesoftware.jivesdk.api.JiveAuthorizationValidator;
import com.jivesoftware.jivesdk.api.JiveSDKManager;
import com.jivesoftware.jivesdk.api.RegisteredInstance;
import com.jivesoftware.jivesdk.impl.utils.JiveSDKUtils;
import com.jivesoftware.jivesdk.server.AuthenticationResponse;
import org.apache.commons.lang.StringUtils;
import org.fest.reflect.core.Reflection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;

import static com.jivesoftware.jivesdk.api.JiveAuthorizationValidator.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 7/8/13
 * Time: 8:02 PM
 */
public class JiveAuthorizationValidatorImplTest {

	private JiveAuthorizationValidator jiveAuthorizationValidator;

	@Before
	public void setUp() throws Exception {
		jiveAuthorizationValidator = new JiveAuthorizationValidatorImpl();
	}

	@Test
	public void getParamsFromAuthz() {
		Assert.assertTrue("An empty map is expected when the " + JIVE_EXTN + " prefix is missing",
				jiveAuthorizationValidator.getParamsFromAuthz("not so random text....").isEmpty());
		String algorithm = UUID.randomUUID().toString();
		String clientId = UUID.randomUUID().toString();
		String jiveUrl = UUID.randomUUID().toString();
		String tenantId = UUID.randomUUID().toString();
		String timestamp = String.valueOf(System.currentTimeMillis());
		String signature = UUID.randomUUID().toString();
		String authz = createAuthzString(true, algorithm, clientId, jiveUrl, tenantId, timestamp, signature);

		Map<String, String> map = jiveAuthorizationValidator.getParamsFromAuthz(authz);
		Assert.assertEquals("Value parsed incorrectly", algorithm, map.get(PARAM_ALGORITHM));
		Assert.assertEquals("Value parsed incorrectly", clientId, map.get(PARAM_CLIENT_ID));
		Assert.assertEquals("Value parsed incorrectly", jiveUrl, map.get(PARAM_JIVE_URL));
		Assert.assertEquals("Value parsed incorrectly", tenantId, map.get(PARAM_TENANT_ID));
		Assert.assertEquals("Value parsed incorrectly", timestamp, map.get(PARAM_TIMESTAMP));
		Assert.assertEquals("Value parsed incorrectly", signature, map.get(PARAM_SIGNATURE));
	}

	@Test
	public void authenticate() {
		String algorithm = "HmacSHA256";
		String clientId = "abdef";
		String url = "http://foo.bar";
		String tenantId = "1235-2342342-abc";
		String clientSecret = "abcdefg";
		long id = 5;
		InstanceRegistrationHandler instanceHandler = mock(InstanceRegistrationHandler.class);
		RegisteredInstance registeredInstance = mock(RegisteredInstance.class);

		when(instanceHandler.getByTenantId(anyString())).thenReturn(registeredInstance);
		when(registeredInstance.getClientId()).thenReturn(clientId);
		when(registeredInstance.getClientSecret()).thenReturn(clientSecret);
		when(registeredInstance.getTenantId()).thenReturn(tenantId);
		when(registeredInstance.getUrl()).thenReturn(url);
		when(registeredInstance.getId()).thenReturn(id);

		JiveSDKManager.getInstance().setInstanceRegistrationHandler(instanceHandler);
		String timestamp = String.valueOf(System.currentTimeMillis());
		String authzNoSignature = createAuthzString(false, algorithm, clientId, url, tenantId, timestamp);
		String signature = Reflection.
				method("sign").
				withReturnType(String.class).
				withParameterTypes(String.class, String.class, String.class).
				in(jiveAuthorizationValidator).
				invoke(authzNoSignature, clientSecret, algorithm);
		String signatureUrlEncoded = JiveSDKUtils.encodeUrl(signature);
		String signedAuthz = createAuthzString(true, algorithm, clientId, url, tenantId, timestamp, signatureUrlEncoded);
		AuthenticationResponse response = jiveAuthorizationValidator.authenticate(signedAuthz);
		Assert.assertTrue("Respone should be authenticated!", response.isAuthenticated());
		Assert.assertTrue("Response should include the appropriate valid Client ID", StringUtils.isNotEmpty(response.getClientId()));
		Assert.assertTrue("Response should include the appropriate valid Client Secret", StringUtils.isNotEmpty(response.getClientSecret()));
		Assert.assertEquals("Client ID should match the one stored in the DB", clientId, response.getClientId());
		Assert.assertEquals("Client Secret should match the one stored in the DB", clientSecret, response.getClientSecret());
	}

	private String createAuthzString(boolean includeScheme, String algorithm, String clientId, String jiveUrl, String tenantId, String timestamp) {
		StringBuilder sb = new StringBuilder();
		if (includeScheme) {
			sb.append(JIVE_EXTN);
		}

		sb.append(PARAM_ALGORITHM).append('=').append(algorithm).append("&").
				append(PARAM_CLIENT_ID).append('=').append(clientId).append("&").
				append(PARAM_JIVE_URL).append('=').append(jiveUrl).append("&").
				append(PARAM_TENANT_ID).append('=').append(tenantId).append("&").
				append(PARAM_TIMESTAMP).append('=').append(timestamp);
		return sb.toString();
	}

	private String createAuthzString(boolean includeScheme, String algorithm, String clientId, String jiveUrl, String tenantId, String timestamp, String signature) {
		return createAuthzString(includeScheme, algorithm, clientId, jiveUrl, tenantId, timestamp) + '&' + PARAM_SIGNATURE + '=' + signature;
	}
}
