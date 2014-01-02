package com.jivesoftware.jivesdk.server.endpoints;

import com.jivesoftware.jivesdk.api.InstanceRegistrationHandler;
import com.jivesoftware.jivesdk.api.InstanceRegistrationRequest;
import com.jivesoftware.jivesdk.api.JiveSignatureValidator;
import com.jivesoftware.jivesdk.api.RegisteredInstance;
import com.jivesoftware.jivesdk.impl.auth.jiveauth.JiveSignatureValidatorImpl;
import com.jivesoftware.jivesdk.impl.utils.JiveSDKUtils;
import com.jivesoftware.jivesdk.server.ServerConstants;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 17/7/13
 * Time: 11:00 AM
 */
@Path(ServerConstants.Endpoints.INSTANCE)
@Consumes(MediaType.APPLICATION_JSON)
public class InstancesEndpoints extends AbstractEndpoint {
    private final static Logger log = LoggerFactory.getLogger(InstancesEndpoints.class);

    @Inject
    private InstanceRegistrationHandler instanceRegistrationHandler;

    private JiveSignatureValidator jiveSignatureValidator = new JiveSignatureValidatorImpl();


    /**
     * * {
     * "tenantId": "e2e4edde-dab8-4e27-9a4d-3014d9d08059-dev",
     * "jiveSignatureURL": "https://market.apps.jivesoftware.com/appsmarket/services/rest/jive/instance/validation/123549ef-8592-4002-af11-00954823d1de",
     * "timestamp": "Wed Jul 17 15:28:27 IDT 2013",
     * "jiveUrl": "http://localhost:8080",
     * "jiveSignature": "WOgYuo1nb14v4JITWf5ed4/PXYNxLURiuCdK0gaihCI=",
     * "scope": "uri:/api",
     * "code": "7ttbqy5k6vv4muizzk3sbdmeht9a0pji.c",
     * "clientSecret": "ar684jxdzq2odfzbuurbqlrdd6kqodaf.s",
     * "clientId": "mk2r0bp2ym43tmxehswpv8q3mma0e9xd.i"
     * }
     *
     * @param request The instance registration request
     * @return 204 response if succeeded
     */
    @POST
    @Path(ServerConstants.Endpoints.REGISTER)
    public Response register(@HeaderParam(HttpHeaders.USER_AGENT) String userAgent,
                             InstanceRegistrationRequest request) {
        try {
            if (request == null) {
                log.error("Invalid request");
                return unAuthorized();
            }

            String signature = request.getJiveSignature();
            String signatureURL = request.getJiveSignatureURL();
            if (!JiveSDKUtils.isAllExist(signature, signatureURL)) {
                String msg = String.format("Invalid signature [%s] / signature URL [%s]", signature, signatureURL);
                log.error(msg);
                return unAuthorized();
            }

            if (!jiveSignatureValidator.validate(request)) {
                log.error("Invalid signature");
                return unAuthorized();
            }

            RegisteredInstance registeredInstance = instanceRegistrationHandler.register(request);
            if (registeredInstance == null) {
                log.error("Failed registering instance");
                return serverError();
            }


            log.debug("register success.");
            return Response.noContent().build();
        } catch (Throwable t) {
            t.printStackTrace();
            log.error("Caught exception", t);
            ObjectNode errorNode = logErrorAndCreateErrorResponse(t.getLocalizedMessage());
            return Response.serverError().entity(errorNode).build();
        }
    }

    @Nonnull
    private Response unAuthorized() {
        return Response.status(HttpStatus.SC_UNAUTHORIZED).build();
    }

    @Nonnull
    private Response serverError() {
        return Response.serverError().build();
    }

    public void setInstanceRegistrationHandler(InstanceRegistrationHandler instanceRegistrationHandler) {
        this.instanceRegistrationHandler = instanceRegistrationHandler;
    }

    public void setJiveSignatureValidator(JiveSignatureValidator jiveSignatureValidator) {
        this.jiveSignatureValidator = jiveSignatureValidator;
    }
}
