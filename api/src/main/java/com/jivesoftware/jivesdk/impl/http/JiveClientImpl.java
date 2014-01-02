package com.jivesoftware.jivesdk.impl.http;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.jivesoftware.jivesdk.api.*;
import com.jivesoftware.jivesdk.api.tiles.ActivityData;
import com.jivesoftware.jivesdk.api.tiles.TileData;
import com.jivesoftware.jivesdk.impl.http.post.RestPostRequestBuilder;
import com.jivesoftware.jivesdk.impl.http.put.RestPutRequestBuilder;
import com.jivesoftware.jivesdk.impl.utils.JiveSDKUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of a client to jive.
 */
public class JiveClientImpl implements JiveClient {
    public static final String PATH_ACTIVITIES = "/activities";
    public static final String TEMPLATE_PATH_EXT_COMMENTS = "/extactivities/%s/comments";
    public static final String LOG_ACTIVITY_SENDING_MESSAGE = "Sent activity data to gateway, received status code: %d";
    public static final String LOG_DATA_SENDING_MESSAGE = "Sent tile data to gateway, received status code: %d";
    public static final Set<Integer> EXPECTED_RESPONSES_ACTIVITY = Sets.newHashSet(HttpStatus.SC_CREATED, HttpStatus.SC_CONFLICT);
    public static final Set<Integer> EXPECTED_RESPONSES_TILE_UPDATE = Sets.newHashSet(HttpStatus.SC_NO_CONTENT);
	protected Logger log = LoggerFactory.getLogger(getClass());
	protected ObjectMapper objectMapper = new ObjectMapper();

	protected RestDriver restDriver;
	protected JiveTokenRefresher jiveTokenRefresher;

	public JiveClientImpl() {
		objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
	}

	@Override
	public boolean sendExternalStreamComment(@Nonnull TileInstance tileInstance, @Nonnull ActivityData data,
											 @Nonnull String parentId) throws TileUninstalledException,
			InvalidRequestException, IOException {
		String json = objectMapper.writeValueAsString(data);

		return sendExternalStreamComment(tileInstance, json, parentId);
	}

	@Override
	public boolean sendExternalStreamActivity(@Nonnull TileInstance tileInstance, @Nonnull ActivityData data) throws
			TileUninstalledException, InvalidRequestException, IOException {
		Map<String, ActivityData> map = new HashMap<String, ActivityData>();
		map.put("activity", data);
		String json = objectMapper.writeValueAsString(map);

		return sendExternalStreamActivity(tileInstance, json);
	}

	@Override
	public boolean sendExternalStreamComment(@Nonnull TileInstance tileInstance, @Nonnull String data,
											 @Nonnull String parentId) throws TileUninstalledException,
			InvalidRequestException {
		return sendExternalStreamComment(tileInstance, new StringEntity(data, ContentType.APPLICATION_JSON), parentId);
	}

	@Override
	public boolean sendExternalStreamActivity(@Nonnull TileInstance tileInstance, @Nonnull String data) throws
			TileUninstalledException, InvalidRequestException {
		return sendExternalStreamActivity(tileInstance, new StringEntity(data, ContentType.APPLICATION_JSON));
	}

	protected boolean sendExternalStreamActivity(@Nonnull TileInstance item, @Nonnull HttpEntity data)
			throws InvalidRequestException, TileUninstalledException {
		String itemUrl = item.getJivePushUrl();
        String url = JiveSDKUtils.createUrl(itemUrl, PATH_ACTIVITIES);
        RestPostRequestBuilder request = RestRequestFactory.createPostRequestBuilder(url)
                .withEntity(data)
                .withOAuth(item.getCredentials().getAuthToken());

        HttpResponse response = sendItemToJive(request, item, LOG_ACTIVITY_SENDING_MESSAGE, EXPECTED_RESPONSES_ACTIVITY);
        if (response != null) {
            int statusCode = response.getStatusCode();
            if (statusCode == HttpStatus.SC_CONFLICT) {
                log.info("Ignoring already-sent external stream activity for item: " + item);
            }

            return EXPECTED_RESPONSES_ACTIVITY.contains(statusCode);
        }

        return false;
    }


    protected boolean sendExternalStreamComment(@Nonnull TileInstance item, @Nonnull HttpEntity data, @Nonnull String parentId)
			throws InvalidRequestException, TileUninstalledException {
        String path = String.format(TEMPLATE_PATH_EXT_COMMENTS, parentId);
		String itemUrl = item.getJivePushUrl();
        String url = JiveSDKUtils.createUrl(itemUrl, path);
        RestPostRequestBuilder request = RestRequestFactory.createPostRequestBuilder(url)
                .withEntity(data)
                .withOAuth(item.getCredentials().getAuthToken());

        HttpResponse response = sendItemToJive(request, item, LOG_ACTIVITY_SENDING_MESSAGE, EXPECTED_RESPONSES_ACTIVITY);
        if (response != null) {
            int statusCode = response.getStatusCode();
            if (statusCode == HttpStatus.SC_CONFLICT) {
                log.info("Ignoring already-sent external stream comment for item: " + item);
            }

            return EXPECTED_RESPONSES_ACTIVITY.contains(statusCode);
        }

        return false;
    }
	@Override
	public boolean sendPutTileUpdate(TileInstance tileInstance, @Nonnull TileData data) throws
			IOException, InvalidRequestException, TileUninstalledException {
		Map<String, TileData> map = new HashMap<String, TileData>();
		map.put("data", data);
		String json = objectMapper.writeValueAsString(map);
		return sendPutTileUpdate(tileInstance, new StringEntity(json, ContentType.APPLICATION_JSON));
	}

	@Override
	public boolean sendPutTileUpdate(TileInstance tileInstance, @Nonnull String data) throws
			UnsupportedEncodingException, InvalidRequestException, TileUninstalledException {
		return sendPutTileUpdate(tileInstance, new StringEntity(data, ContentType.APPLICATION_JSON));
	}
	
    protected boolean sendPutTileUpdate(TileInstance tileInstance, @Nonnull HttpEntity data)
			throws InvalidRequestException, TileUninstalledException {
		String itemUrl = tileInstance.getJivePushUrl();
        RestPutRequestBuilder request = RestRequestFactory.createPutRequestBuilder(itemUrl)
                .withEntity(data)
                .withOAuth(tileInstance.getCredentials().getAuthToken());

        HttpResponse response = sendItemToJive(request, tileInstance, LOG_DATA_SENDING_MESSAGE,
				EXPECTED_RESPONSES_TILE_UPDATE);
        return response != null && EXPECTED_RESPONSES_TILE_UPDATE.contains(response.getStatusCode());
    }

    @Nullable
    private HttpResponse sendItemToJive(@Nonnull RestRequest request, @Nonnull TileInstance item,
                                                @Nonnull String logSendingMessage, Set<Integer> expectedResponses) throws
			InvalidRequestException, TileUninstalledException {
        return sendItemToJive(request, item, logSendingMessage, expectedResponses, true);
    }

    @Nullable
    private HttpResponse sendItemToJive(@Nonnull RestRequest request,
										@Nonnull TileInstance item,
										@Nonnull String logSendingMessage, Set<Integer> expectedResponses,
										boolean isFirst) throws InvalidRequestException, TileUninstalledException {
        HttpResponse response = null;
        try {
            //TODO:why do we throw an exception when we can return the result inside the object.
            response = restDriver.execute(request);

            int statusCode = response.getStatusCode();
            log.debug(String.format(logSendingMessage, statusCode));

            switch (statusCode) {
                case HttpStatus.SC_GONE: {
                    log.debug("received gone response from jive on item #" + item.getGuid());
                    throw new TileUninstalledException();
                }
                case HttpStatus.SC_FORBIDDEN: {
                    throw new AuthTokenException(request.getUrl(), JiveSDKUtils.readStringFromResponse(response));
                }
                case HttpStatus.SC_NOT_FOUND: {
                    handleNotFound(item, response);
                    break;
                }
                default:
                    break;
            }

            if (!expectedResponses.contains(statusCode)) {
                String responseStr = JiveSDKUtils.readStringFromResponse(response);
                String token = item.getCredentials().getAuthToken();
                log.error(String.format("Failed sending item to jive. Received response [%d]: %s. Token [%s], item: %s", statusCode, responseStr, token, item.toString()));
            }
        } catch (AuthTokenException e) {
            if (isFirst) {
                // Try send the item again with the new auth token
                log.info("Trying to refresh auth token and send again");
                String oldAuthToken = item.getCredentials().getAuthToken();
                if (jiveTokenRefresher.refreshToken(item) != null) {
                    String newAuthToken = item.getCredentials().getAuthToken();
                    log.debug(String.format("Refresh auth. token succeeded for item %s, updating auth. token to %s (old is: %s)", item.getGuid(), newAuthToken, oldAuthToken));
                    request.withOAuth(newAuthToken);
                    response = sendItemToJive(request, item, logSendingMessage, expectedResponses, false);
                } else {
                    log.error("Item access token unauthorized even after refresh: " + item);
                }
            } else {
                log.error("Item access token unauthorized even after refresh: " + item);
            }
        } catch (RestAccessException e) {
            log.error("Access Exception, therefore failed sending to API Gateway: item #" + item.getGuid());
        }

        return response;
    }

    private void handleNotFound(@Nonnull TileInstance item, @Nonnull HttpResponse response) throws
			InvalidRequestException {
        boolean needToFreeze = false;
        try {
            Optional<String> body = response.getResponseBody();
            if (!body.isPresent()) {
                return;
            }

            String bodyStr = body.get();
            if (bodyStr == null || bodyStr.trim().isEmpty()) {
                return;
            }

            if (bodyStr.trim().startsWith("throw")) {
                bodyStr = bodyStr.substring(1 + bodyStr.indexOf(';'));
            }

            JsonNode node = new ObjectMapper().readTree(bodyStr);
            if (node == null || !node.isObject()) {
                return;
            }

            JsonNode errorNode = node.get("error");
            if (errorNode == null || !errorNode.isObject()) {
                return;
            }

            int status = -1;
            String errMsg = null;
            JsonNode statusNode = errorNode.get("status");
            if (statusNode != null && statusNode.isNumber()) {
                status = statusNode.asInt();
            }

            JsonNode messageNode = errorNode.get("message");
            if (messageNode != null && messageNode.isTextual()) {
                errMsg = messageNode.asText();
            }

            if (status == HttpStatus.SC_NOT_FOUND && errMsg != null) {
                String lowerCased = errMsg.trim().toLowerCase();
                if (lowerCased.startsWith("missing tile id") ||
                        lowerCased.startsWith("invalid tile id") ||
                        lowerCased.startsWith("missing external stream id") ||
                        lowerCased.startsWith("invalid external stream id")) {
                    needToFreeze = true;
                }
            }
        } catch (Exception e) {
            log.error("Failed handling 'Not found' response", e);
        }

        if (needToFreeze) {
            log.debug("received freeze" + item.getGuid());
            throw new InvalidRequestException();
        }
    }

	public void setRestDriver(RestDriver restDriver) {
		this.restDriver = restDriver;
	}

	public void setJiveTokenRefresher(JiveTokenRefresher jiveTokenRefresher) {
		this.jiveTokenRefresher = jiveTokenRefresher;
	}
}
