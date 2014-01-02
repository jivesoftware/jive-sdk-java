package com.jivesoftware.jivesdk.impl.http.post;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.net.HttpHeaders;
import com.jivesoftware.jivesdk.impl.http.AbstractRestRequestWithEntity;
import com.jivesoftware.jivesdk.impl.utils.JiveSDKUtils;
import com.jivesoftware.jivesdk.impl.utils.JsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import javax.annotation.Nonnull;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 9/1/13
 * Time: 6:34 PM
 */
public class RestPostRequestImpl extends AbstractRestRequestWithEntity<HttpPost> implements RestPostRequestBuilder {
    @Nonnull
    private Optional<Map<String, String>> params = Optional.absent();

    public RestPostRequestImpl(@Nonnull String url) {
        super(url);
    }

    @Nonnull
    @Override
    public RestPostRequestBuilder withBodyParams(@Nonnull Map<String, String> params) {
        this.params = Optional.of(params);
        return this;
    }

    @Nonnull
    @Override
    public RestPostRequestBuilder withEntity(@Nonnull HttpEntity entity) {
        setEntity(entity);
        return this;
    }

    @Nonnull
    @Override
    public RestPostRequestBuilder withOAuth(@Nonnull String token) {
        return (RestPostRequestBuilder) super.withOAuth(token);
    }

    @Nonnull
    @Override
    public RestPostRequestBuilder withBasicAuth(@Nonnull String username, @Nonnull String password) {
        return (RestPostRequestBuilder) super.withBasicAuth(username, password);
    }

    @Nonnull
    @Override
    public RestPostRequestBuilder withHeaders(@Nonnull Map<String, String> headers) {
        return (RestPostRequestBuilder) super.withHeaders(headers);
    }

    @Nonnull
    @Override
    public HttpPost create() {
        HttpPost method = new HttpPost(getUrl());
        if (params.isPresent()) {
            method.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            List<NameValuePair> parameters = Lists.newArrayList();
            Map<String, String> map = params.get();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            try {
                method.setEntity(new UrlEncodedFormEntity(parameters, JiveSDKUtils.UTF_8));
            } catch (UnsupportedEncodingException e) {
                log.warn("Failed setting POST entity using UTF-8 encoding, trying default encoding", e);
                try {
                    method.setEntity(new UrlEncodedFormEntity(parameters));
                } catch (UnsupportedEncodingException e1) {
                    log.error("Failed setting POST entity using default encoding", e);
                }
            }
        } else {
            Optional<HttpEntity> entity = getEntity();
            if (entity.isPresent()) {
                if (!isContentTypeIncludedInHeaders()) {
                    method.addHeader(HttpHeaders.CONTENT_TYPE, JsonUtils.APPLICATION_JSON);
                }
                method.setEntity(entity.get());
            }
        }

        addAuthorizationHeader(method);
        addHeaders(method);
        return method;
    }
}
