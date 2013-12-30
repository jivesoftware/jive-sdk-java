package com.jivesoftware.jivesdk.server;

import com.jivesoftware.jivesdk.api.InstanceRegistrationHandler;

import javax.annotation.Nonnull;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 6/8/13
 * Time: 6:33 PM
 */

public interface JiveAuthorizationValidator {
    public static final String PARAM_ALGORITHM = "algorithm";
    public static final String PARAM_CLIENT_ID = "client_id";
    public static final String PARAM_JIVE_URL = "jive_url";
    public static final String PARAM_TENANT_ID = "tenant_id";
    public static final String PARAM_TIMESTAMP = "timestamp";
    public static final String PARAM_SIGNATURE = "signature";
    String JIVE_EXTN = "JiveEXTN ";

    @Nonnull
    Map<String, String> getParamsFromAuthz(@Nonnull String authz);

    AuthenticationResponse authenticate(@Nonnull String authz, InstanceRegistrationHandler jiveInstanceHandler);
}
