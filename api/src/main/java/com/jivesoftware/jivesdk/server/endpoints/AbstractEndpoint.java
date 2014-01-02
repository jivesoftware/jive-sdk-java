package com.jivesoftware.jivesdk.server.endpoints;

import com.jivesoftware.jivesdk.api.JiveSDKManager;
import com.jivesoftware.jivesdk.api.JiveAuthorizationValidator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 22/7/13
 * Time: 2:28 PM
 */
public abstract class AbstractEndpoint {
    private final static Logger log = LoggerFactory.getLogger(AbstractEndpoint.class);

    public static final String MESSAGE = "message";
    public static final String STATUS = "status";
    public static final String OK = "ok";
    public static final String EXPIRED = "expired";

    JiveAuthorizationValidator jiveAuthorizationValidator = JiveSDKManager.getInstance().getAuthorizationValidator();


    @Nonnull
    public static CacheControl createNoCache() {
        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);
        cacheControl.setNoStore(true);
        return cacheControl;
    }

	@Nonnull
    protected ObjectNode logErrorAndCreateErrorResponse(@Nonnull String msg) {
        log.error(msg);
        ObjectNode errorObj = new ObjectMapper().createObjectNode();
        errorObj.put(STATUS, "error");
        errorObj.put(MESSAGE, msg);
        return errorObj;
    }

    @Nonnull
    protected Response returnResponseOnThrowable(Throwable t, @Nonnull String msg) {
        log.error(msg, t);
        ObjectNode errorObj = new ObjectMapper().createObjectNode();
        errorObj.put(STATUS, "error");
        errorObj.put(MESSAGE, msg + ". Details: " + t.getLocalizedMessage());
        return Response.serverError().entity(errorObj).build();
    }

    @SuppressWarnings("UnusedDeclaration")
    protected void printRequest(@Nonnull HttpServletRequest hsr) {
        Enumeration headerNames = hsr.getHeaderNames();
        System.out.println("Headers:");
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            String headerValue = hsr.getHeader(headerName);
            System.out.println(headerName + ": " + headerValue);
        }
        BufferedReader bufferedReader = null;
        try {
            ServletInputStream inputStream = hsr.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(streamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ignored) {
                }
            }

        }
    }
}
