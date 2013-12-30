package com.jivesoftware.jivesdk.server;

import com.google.common.collect.Maps;
import com.jivesoftware.jivesdk.api.InstanceRegistrationHandler;
import com.jivesoftware.jivesdk.api.RegisteredInstance;
import com.jivesoftware.jivesdk.impl.utils.DealRoomUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
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
    public Map<String, String> getParamsFromAuthz(@Nonnull String authz) {
        if (!authz.startsWith(JIVE_EXTN)) {
            return Maps.newHashMap();
        }

        authz = authz.substring(JIVE_EXTN.length());
        String[] params = authz.split("[?|&]");
        Map<String, String> paramMap = Maps.newHashMap();
        for (String param : params) {
            String[] tokens = param.split("=");
            if (tokens.length != 2) {
                return Maps.newHashMap();
            }

            paramMap.put(DealRoomUtils.decodeUrl(tokens[0]), DealRoomUtils.decodeUrl(tokens[1]));
        }

        return paramMap;
    }

    @Override
    public AuthenticationResponse authenticate(@Nonnull String authz, InstanceRegistrationHandler jiveInstanceHandler) {
        try {
            if (!authz.startsWith(JIVE_EXTN) || !authz.contains(QUERY_PARAM_SIGNATURE)) {
                log.error("Jive authorization isn't properly formatted: " + authz);
                return badRequest();
            }

            Map<String, String> paramMap = getParamsFromAuthz(authz);
            String signature = paramMap.remove(PARAM_SIGNATURE);
            String algorithm = paramMap.get(PARAM_ALGORITHM);
            String clientId = paramMap.get(PARAM_CLIENT_ID);
            String jiveUrl = paramMap.get(PARAM_JIVE_URL);
            String tenantId = paramMap.get(PARAM_TENANT_ID);
            String timeStampStr = paramMap.get(PARAM_TIMESTAMP);
            if (!DealRoomUtils.isAllExist(algorithm, clientId, jiveUrl, tenantId, timeStampStr)) {
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
            String paramStrWithoutSignature = authz.substring(JIVE_EXTN.length(), authz.indexOf(QUERY_PARAM_SIGNATURE));
            String expectedSignature = sign(paramStrWithoutSignature, clientSecret, algorithm);
            if (expectedSignature.equals(signature)) {
                return new AuthenticationResponse(clientId, clientSecret, instance);
            } else {
                log.error("Jive authorization failed due to tampered signature! Original authz: " + authz);
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
        mac.update(str.getBytes(DealRoomUtils.UTF_8));
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
}
