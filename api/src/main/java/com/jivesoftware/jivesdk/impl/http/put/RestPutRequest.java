package com.jivesoftware.jivesdk.impl.http.put;

import com.jivesoftware.jivesdk.impl.http.RestRequest;
import com.jivesoftware.jivesdk.impl.http.RestRequestWithEntity;
import org.apache.http.client.methods.HttpPut;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 27/1/13
 * Time: 7:02 PM
 */
public interface RestPutRequest extends RestRequest<HttpPut>, RestRequestWithEntity<HttpPut> {
}
