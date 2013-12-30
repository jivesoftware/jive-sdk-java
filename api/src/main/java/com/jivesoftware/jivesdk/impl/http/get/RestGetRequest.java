package com.jivesoftware.jivesdk.impl.http.get;

import com.jivesoftware.jivesdk.impl.http.RestRequest;
import org.apache.http.client.methods.HttpGet;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 9/1/13
 * Time: 6:25 PM
 */
public interface RestGetRequest extends RestRequest<HttpGet> {
    @Nullable
    Map<String, String> getQueryParams();
}
