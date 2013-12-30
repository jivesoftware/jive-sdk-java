package com.jivesoftware.jivesdk.impl.http;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.jivesoftware.jivesdk.api.RestAccessException;
import com.jivesoftware.jivesdk.api.RestDriver;
import com.jivesoftware.jivesdk.impl.http.get.RestGetRequest;
import com.jivesoftware.jivesdk.impl.http.post.RestPostRequest;
import com.jivesoftware.jivesdk.impl.http.put.RestPutRequest;
import com.jivesoftware.jivesdk.impl.utils.DealRoomUtils;
import com.jivesoftware.jivesdk.server.ServerConstants;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 9/1/13
 * Time: 11:17 AM
 */
public class RestDriverImpl implements RestDriver {
    private static final Logger log = LoggerFactory.getLogger(RestDriverImpl.class);

    private static final AtomicLong NEXT_VERSION = new AtomicLong(1);
    public static final String DUMP_DELIMIT = ";;;";
    public static final int DEFAULT_HTTP_NUM_OF_RETRIES = 3;

    private HttpClientFactory httpClientFactory;

    @Nonnull
    @Override
    public HttpResponse execute(@Nonnull RestRequest request) throws AuthTokenException, RestAccessException {
        return executeWithClient(request, getHttpClient());
    }

    @Nonnull
    @Override
    public HttpResponse execute(@Nonnull RestRequest request, int timeout) throws AuthTokenException, RestAccessException {
        return executeWithClient(request, getHttpClient(timeout));
    }

    @Nonnull
    @Override
    public HttpResponse executeWithClient(@Nonnull RestRequest request, @Nonnull HttpClient client) throws AuthTokenException, RestAccessException {
        return executeWithClient(request, client, DEFAULT_HTTP_NUM_OF_RETRIES);
    }

    @Nonnull
    @Override
    public HttpResponse executeWithClient(@Nonnull RestRequest request, @Nonnull HttpClient client,
                                                  int numberOfRetries) throws AuthTokenException, RestAccessException {
        HttpRequestBase httpRequest = null;
        HttpResponse response = null;
        HttpEntity entity = null;
        long currentVer = -1;
        try {
            currentVer = dumpRequestIfDebugging(request);
            httpRequest = request.create();
			httpRequest.setHeader("user-agent", "jive-sdk-java/0.1");
            org.apache.http.HttpResponse httpResponse = client.execute(httpRequest);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            entity = httpResponse.getEntity();
            if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                String uri = httpRequest.getURI().toString();
                String responseBody = getEntityBody(entity);
                log.error(String.format("Rest execution unauthorized to %s. Response: %s", uri, responseBody));
                throw new AuthTokenException(uri, responseBody);
            }

            if (ServerConstants.RETRIABLE_RESPONSE_CODES.contains(statusCode) && numberOfRetries > 0) {
                log.warn(String.format("Re-trying HTTP request in 500ms due to bad response %d: %s", statusCode, getEntityBody(entity)));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }

                return executeWithClient(request, client, numberOfRetries - 1);
            }

            Map<String, String> headers = Maps.newHashMap();
            for (Header header : httpResponse.getAllHeaders()) {
                headers.put(header.getName(), header.getValue());
            }

            if (statusCode >= 200 && statusCode <= 299) {
                if (entity != null) {
                    response = new DealRoomHttpResponseImpl(statusCode, headers, entity.getContent());
                } else {
                    response = new DealRoomHttpResponseImpl(statusCode, headers, "");
                }
            } else {
                String responseBody = getEntityBody(entity);
                log.error(String.format("Received HTTP response [%d]: %s", statusCode, responseBody));
                response = new DealRoomHttpResponseImpl(statusCode, headers, responseBody);
            }

            return response;
        } catch (IOException e) {
            String msg = "Failed executing REST request: " + request.getUrl();
            if (numberOfRetries > 0) {
                log.warn(String.format("%s. Re-trying HTTP request due to IOException", msg), e);
                return executeWithClient(request, client, numberOfRetries - 1);
            } else {
                log.error(msg, e);
                throw new RestAccessException(msg, request.getUrl(), e);
            }
        } finally {
            dumpResponseIfDebugging(response, currentVer);
            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (IOException ignored) {
                }
            }
            if (httpRequest != null) {
                try {
                    httpRequest.releaseConnection();
                } catch (Exception ignored) {
                }
            }
        }
    }

    @Nonnull
    @Override
    public HttpResponse executeForResponseBody(@Nonnull RestRequest request) throws AuthTokenException, RestAccessException {
        HttpRequestBase httpRequest = null;
        HttpResponse response = null;
        long currentVer = -1;
        try {
            currentVer = dumpRequestIfDebugging(request);
            HttpClient client = getHttpClient();
            httpRequest = request.create();
            org.apache.http.HttpResponse httpResponse = client.execute(httpRequest);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            HttpEntity entity = httpResponse.getEntity();
            String responseBody = getEntityBody(entity);
            if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                String uri = httpRequest.getURI().toString();
                log.error(String.format("Rest execution unauthorized to %s. Response: %s", uri, responseBody));
                throw new AuthTokenException(request.getUrl(), responseBody);
            }

            Map<String, String> headers = Maps.newHashMap();
            for (Header header : httpResponse.getAllHeaders()) {
                headers.put(header.getName(), header.getValue());
            }

            response = new DealRoomHttpResponseImpl(statusCode, headers, responseBody);
            if (statusCode < 200 || statusCode > 299) {
                log.error(String.format("Received HTTP response [%d]: %s", statusCode, responseBody));
                // TODO: Is it ok to throw a RestAccessException here instead of returning the response?
                throw new RestAccessException(statusCode + ": " + responseBody, request.getUrl());
            }

            return response;
        } catch (IOException e) {
            String msg = "Failed executing REST request: " + request.getUrl();
            log.error(msg, e);
            throw new RestAccessException(msg, request.getUrl(), e);
        } finally {
            dumpResponseIfDebugging(response, currentVer);
            if (httpRequest != null) {
                try {
                    httpRequest.releaseConnection();
                } catch (Exception ignored) {
                }
            }
        }
    }

    @Nonnull
    private HttpClient getHttpClient() {
        return httpClientFactory.getClient();
    }

    @Nonnull
    private HttpClient getHttpClient(int timeout) {
        return httpClientFactory.getClient(timeout);
    }

    private String getEntityBody(HttpEntity entity) throws IOException {
        return entity != null ? EntityUtils.toString(entity, DealRoomUtils.UTF_8) : "";
    }

    private long dumpRequestIfDebugging(@Nonnull RestRequest request) {
        if (log.isDebugEnabled() && ServerConstants.shouldDumpRequest.get()) {
            long version = NEXT_VERSION.getAndIncrement();
            StringBuilder sb = new StringBuilder();
            sb.append("---[").append(version).append("] START").append(DUMP_DELIMIT);

            if (request instanceof RestGetRequest) {
                sb.append("GET ");
            } else if (request instanceof RestPostRequest) {
                sb.append("POST ");
            } else if (request instanceof RestPutRequest) {
                sb.append("PUT ");
            }

            sb.append(request.getUrl()).append(DUMP_DELIMIT);
            if (request instanceof RestGetRequest) {
                Map<String, String> queryParams = ((RestGetRequest) request).getQueryParams();
                if (queryParams != null && !queryParams.isEmpty()) {
                    sb.append("Query params: ");
                    for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                        sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
                    }
                }
            }

            if (request instanceof RestRequestWithEntity) {
                RestRequestWithEntity requestWithEntity = (RestRequestWithEntity) request;
                Optional<HttpEntity> entity = requestWithEntity.getEntity();
                if (entity.isPresent()) {
                    sb.append("Entity: ");
                    ByteArrayOutputStream outputStream = null;
                    try {
                        outputStream = new ByteArrayOutputStream();
                        entity.get().writeTo(outputStream);
                        sb.append(new String(outputStream.toByteArray(), DealRoomUtils.UTF_8)).append(DUMP_DELIMIT);
                    } catch (IOException e) {
                        log.error("Failed dumping request body", e);
                    } finally {
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException ignored) {
                            }
                        }
                    }
                }
            }

            sb.append("---[").append(version).append("] END");
            log.debug(sb.toString());
            return version;
        }

        return -1;
    }

    private void dumpResponseIfDebugging(@Nullable HttpResponse response, long version) {
        if (response != null && version > -1 && ServerConstants.shouldDumpResponse.get()) {
            Optional<String> optional = response.getResponseBody();
            String body = "Empty body";
            if (optional.isPresent()) {
                body = optional.get();
            }

            log.debug("---[" + String.valueOf(version) + "] " + body);
        }
    }

    public void setHttpClientFactory(HttpClientFactory httpClientFactory) {
        this.httpClientFactory = httpClientFactory;
    }
}
