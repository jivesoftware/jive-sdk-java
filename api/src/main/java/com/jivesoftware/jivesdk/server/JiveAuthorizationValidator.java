package com.jivesoftware.jivesdk.server;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

	/**
	 * validate the Authorization header.  Make sure it is a JiveEXTN style header with the correct signature.
	 * @param authHeader
	 * @return
	 */
	@Nonnull
	AuthenticationResponse authenticate(@Nonnull String authHeader );

	/**
	 * validate the Authorization header.  Make sure it is a JiveEXTN style header with the correct signature. Also
	 * make sure the jiveUrl and tenantId match the expected input.
	 * @param authHeader
	 * @return
	 */
	@Nonnull
	AuthenticationResponse authenticate(String authHeader, @Nullable String jiveUrl, @Nullable String tenantId);

	/**
	 * Parse the Authorization header and pull out the parameters from the request.
	 *
	 * NOTE this does not validate the signature nor check whether it is a JiveEXTN header.  Use authenticate validate
	 * this header.
	 *
	 * @param authHeader
	 * @return
	 */
	@Nonnull
	Map<String, String> getParamsFromAuthz(@Nonnull String authHeader);
}
