/*
 * Copyright (c) 2013. Jive Software
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

package com.jivesoftware.jivesdk.server.endpoints;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.jivesoftware.jivesdk.impl.PropertyConfiguration;
import com.jivesoftware.jivesdk.impl.utils.DealRoomUtils;
import com.jivesoftware.jivesdk.server.ServerConstants;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import static com.jivesoftware.jivesdk.server.ServerConstants.Endpoints.*;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 17/2/13
 * Time: 12:23 PM
 */
@Path(WEB)
public class WebEndpoints {
    private static final Logger log = LoggerFactory.getLogger(WebEndpoints.class);

    private PropertyConfiguration config = PropertyConfiguration.getInstance();

    @Context
    Request request;

    @Context
    UriInfo uriInfo;

    @GET
    @Path(ROOT_BY_NAME)
    @Produces(MediaType.TEXT_HTML)
    public Response getFile(@PathParam("name") String name, @QueryParam("vars") String vars) {
        try {
            if (name == null || name.trim().isEmpty()) {
                log.error("Request for invalid HTML resource");
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            Map<String, String> replacements = Maps.newHashMap();
            replacements.put(HOST_SUFFIX, "");
            if (!StringUtils.isEmpty(vars)) {
                String decodedVars = DealRoomUtils.decodeBase64(vars);
                JsonNode replaceObj = new ObjectMapper().readTree(decodedVars);
                Iterator<Map.Entry<String, JsonNode>> it = replaceObj.getFields();
                while (it.hasNext()) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    String key = entry.getKey();
                    String value = entry.getValue().asText();
                    if (key.startsWith(TEMPLATE_PREFIX) && key.endsWith(TEMPLATE_SUFFIX)) {
                        replacements.put(key, value);
                    } else {
                        log.warn("Ignoring invalid HTML replacement-var: " + key + " -> " + value);
                    }
                }
            }

            replacements.put(HOST_TEMPLATE, StringUtils.stripEnd(uriInfo.getBaseUri().toString(), "/"));
            replacements.put(TIME_TEMPLATE, String.valueOf(System.currentTimeMillis()));

            if (!verifyReplacementsAreValid(replacements)) {
                String entity = DealRoomUtils.createHtmlMessage("Invalid HTML replacement-vars");
                return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
            }

            String contents = DealRoomUtils.readFile(name);
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                contents = contents.replace(entry.getKey(), entry.getValue());
            }

            return Response.ok(contents).build();
        } catch (Throwable t) {
            log.error("Failed fetching HTML file: " + name, t);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @GET
    @Path(IMAGE_BY_NAME)
    @Produces(ServerConstants.MEDIA_TYPE_IMAGE_PNG)
    public Response getImage(@PathParam("name") String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                log.error("Request for invalid image resource");
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            return getStreamResponse("image/" + name, ServerConstants.MEDIA_TYPE_IMAGE_PNG);
        } catch (Throwable t) {
            log.error("Failed fetching PNG file: " + name, t);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path(IMAGE_GIF_BY_NAME)
    @Produces(ServerConstants.MEDIA_TYPE_IMAGE_GIF)
    public Response getGifImage(@PathParam("name") String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                log.error("Request for invalid image resource");
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            return getStreamResponse("image/" + name, ServerConstants.MEDIA_TYPE_IMAGE_GIF);
        } catch (Throwable t) {
            log.error("Failed fetching GIF file: " + name, t);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private boolean verifyReplacementsAreValid(@Nonnull Map<String, String> replacements) {
        String s = replacements.get(HOST_SUFFIX);
        return (StringUtils.isEmpty(s) || s.equals(ServerConstants.Endpoints.V2_PREFIX));
    }

    private Response getStreamResponse(@Nonnull String filename, @Nonnull String mediaType) {
        try {
            Optional<InputStream> inputStream = DealRoomUtils.getFileInputStream(filename);
            if (inputStream.isPresent()) {
                return Response.ok(inputStream.get(), mediaType).build();
            }
        } catch (Exception e) {
            log.error(String.format("Failed fetching file [%s] of type [%s]", filename, mediaType), e);
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
