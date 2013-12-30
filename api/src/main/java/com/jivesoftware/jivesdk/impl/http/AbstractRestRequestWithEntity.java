package com.jivesoftware.jivesdk.impl.http;

import com.google.common.base.Optional;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 11/7/13
 * Time: 4:02 PM
 */
public abstract class AbstractRestRequestWithEntity<T extends HttpRequestBase> extends AbstractRestRequest<T> implements RestRequestWithEntity<T> {
    protected final static Logger log = LoggerFactory.getLogger(AbstractRestRequestWithEntity.class);

    @Nonnull
    private Optional<HttpEntity> entity = Optional.absent();

    public AbstractRestRequestWithEntity(@Nonnull String url) {
        super(url);
    }

    @Nonnull
    @Override
    public Optional<HttpEntity> getEntity() {
        return entity;
    }

    public void setEntity(@Nonnull HttpEntity entity) {
        this.entity = Optional.of(entity);
    }
}
