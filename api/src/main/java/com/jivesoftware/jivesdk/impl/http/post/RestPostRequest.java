package com.jivesoftware.jivesdk.impl.http.post;

import com.jivesoftware.jivesdk.impl.http.RestRequest;
import com.jivesoftware.jivesdk.impl.http.RestRequestWithEntity;
import org.apache.http.client.methods.HttpPost;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 9/1/13
 * Time: 6:25 PM
 */
public interface RestPostRequest extends RestRequest<HttpPost>, RestRequestWithEntity<HttpPost> {
}
