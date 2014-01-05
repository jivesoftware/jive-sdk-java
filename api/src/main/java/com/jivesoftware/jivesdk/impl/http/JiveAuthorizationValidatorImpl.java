package com.jivesoftware.jivesdk.impl.http;

import com.google.common.collect.Maps;
import com.jivesoftware.jivesdk.api.InstanceRegistrationHandler;
import com.jivesoftware.jivesdk.api.JiveAuthorizationValidator;
import com.jivesoftware.jivesdk.api.JiveSDKManager;
import com.jivesoftware.jivesdk.api.RegisteredInstance;
import com.jivesoftware.jivesdk.impl.utils.JiveSDKUtils;
import com.jivesoftware.jivesdk.server.AuthenticationResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 6/8/13
 * Time: 6:33 PM
 */
public class JiveAuthorizationValidatorImpl implements JiveAuthorizationValidator {
    private static final Logger log = LoggerFactory.getLogger(JiveAuthorizationValidatorImpl.class);

    public static final String QUERY_PARAM_SIGNATURE = "&" + PARAM_SIGNATURE + "=";

    @Nonnull
    @Override
    public Map<String, String> getParamsFromAuthz(@Nonnull String authHeader) {
        if (!authHeader.startsWith(JIVE_EXTN)) {
            return Maps.newHashMap();
        }

        authHeader = authHeader.substring(JIVE_EXTN.length());
        String[] params = authHeader.split("[?|&]");
        Map<String, String> paramMap = Maps.newHashMap();
        for (String param : params) {
            String[] tokens = param.split("=");
            if (tokens.length != 2) {
                return Maps.newHashMap();
            }

            paramMap.put(JiveSDKUtils.decodeUrl(tokens[0]), JiveSDKUtils.decodeUrl(tokens[1]));
        }

        return paramMap;
    }

    @Override
    public AuthenticationResponse authenticate(@Nonnull String authorization) {
		InstanceRegistrationHandler jiveInstanceHandler = JiveSDKManager.getInstance().getInstanceRegistrationHandler();

        try {
            if (!authorization.startsWith(JIVE_EXTN) || !authorization.contains(QUERY_PARAM_SIGNATURE)) {
                log.error("Jive authorization isn't properly formatted: " + authorization);
                return badRequest();
            }

            Map<String, String> paramMap = getParamsFromAuthz(authorization);
            String signature = paramMap.remove(PARAM_SIGNATURE);
            String algorithm = paramMap.get(PARAM_ALGORITHM);
            String clientId = paramMap.get(PARAM_CLIENT_ID);
            String jiveUrl = paramMap.get(PARAM_JIVE_URL);
            String tenantId = paramMap.get(PARAM_TENANT_ID);
            String timeStampStr = paramMap.get(PARAM_TIMESTAMP);
            if (!JiveSDKUtils.isAllExist(algorithm, clientId, jiveUrl, tenantId, timeStampStr)) {
                log.error("Jive authorization is partial: " + paramMap);
                return badRequest();
            }

            long timeStamp = Long.parseLong(timeStampStr);
            long millisPassed = System.currentTimeMillis() - timeStamp;
            if (millisPassed < 0 || millisPassed > TimeUnit.MINUTES.toMillis(5)) {
                log.error("Jive authorization is rejected since it's " + millisPassed + "ms old (max. allowed is 5 minutes): " + paramMap);
                return unAuthorized();
            }

            RegisteredInstance instance = jiveInstanceHandler.getByTenantId(tenantId);
            if (instance == null) {
                log.error("Jive authorization failed due to invalid tenant ID: " + tenantId);
                return unAuthorized();
            }

            String expectedClientId = instance.getClientId();
            if (!expectedClientId.equals(clientId)) {
                String msg = String.format("Jive authorization failed due to missing Client ID: Actual [%s], Expected [%s]", clientId, expectedClientId);
                log.error(msg);
                return unAuthorized();
            }

            String clientSecret = instance.getClientSecret();
            String paramStrWithoutSignature = authorization.substring(JIVE_EXTN.length(), authorization.indexOf(QUERY_PARAM_SIGNATURE));
            String expectedSignature = sign(paramStrWithoutSignature, clientSecret, algorithm);
            if (expectedSignature.equals(signature)) {
                return new AuthenticationResponse(clientId, clientSecret, instance);
            } else {
                log.error("Jive authorization failed due to tampered signature! Original authz: " + authorization);
                return unAuthorized();
            }
        } catch (Exception e) {
            log.error("Failed validating Jive auth. scheme", e);
            return unAuthorized();
        }
    }

    @Nonnull
    private String sign(@Nonnull String str, @Nonnull String clientSecret, @Nonnull String algorithm) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        byte[] secret = Base64.decodeBase64(clientSecret);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret, algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        mac.update(str.getBytes(JiveSDKUtils.UTF_8));
        return Base64.encodeBase64String(mac.doFinal()).replaceAll("\\s+", "");
    }

    @Nonnull
    private AuthenticationResponse badRequest() {
        return new AuthenticationResponse(HttpStatus.SC_BAD_REQUEST);
    }

    @Nonnull
    protected AuthenticationResponse unAuthorized() {
        return new AuthenticationResponse(HttpStatus.SC_UNAUTHORIZED);
    }

	@Override
	@Nonnull
	public AuthenticationResponse authenticate(String authHeader, @Nullable String jiveUrl,
											   @Nullable String tenantId) {
		if(authHeader == null) {
			return new AuthenticationResponse(HttpStatus.SC_UNAUTHORIZED);
		}

		if (JiveSDKUtils.isAllExist(jiveUrl, tenantId)) {
			Map<String, String> params = getParamsFromAuthz(authHeader);
			String paramJiveUrl = params.get(PARAM_JIVE_URL);
			String paramTenantId = params.get(PARAM_TENANT_ID);
			//noinspection ConstantConditions
			if (!jiveUrl.equals(paramJiveUrl) || !tenantId.equals(paramTenantId)) {
				String msg = String.format("Failed authenticating request. Jive URL: [%s] vs. [%s], Tenant ID: [%s] vs [%s]",
						jiveUrl, paramJiveUrl, tenantId, paramTenantId);
				log.error(msg);
				return new AuthenticationResponse(HttpStatus.SC_UNAUTHORIZED);
			}
		}

		return authenticate(authHeader);
	}
}
