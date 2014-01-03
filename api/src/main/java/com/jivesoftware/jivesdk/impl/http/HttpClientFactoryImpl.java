package com.jivesoftware.jivesdk.impl.http;

import com.jivesoftware.jivesdk.impl.PropertyConfiguration;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.annotation.Nonnull;

import static com.jivesoftware.jivesdk.impl.JiveSDKConfig.CommonConfiguration.*;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 9/1/13
 * Time: 3:48 PM
 */
public class HttpClientFactoryImpl implements HttpClientFactory {
	private PropertyConfiguration configuration = PropertyConfiguration.getInstance();
	private PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
	private int connectionTimeOut;
	private int socketTimeOut;

	public void init() {
		connectionManager.setDefaultMaxPerRoute(
				Integer.parseInt(configuration.getProperty(MAX_HTTP_CONNECTIONS_PER_HOST)));
		connectionManager.setMaxTotal(Integer.parseInt(configuration.getProperty(MAX_HTTP_CONNECTIONS_TOTAL)));
		connectionTimeOut = Integer.parseInt(configuration.getProperty(HTTP_CONNECTION_TIMEOUT));
		socketTimeOut = Integer.parseInt(configuration.getProperty(HTTP_SOCKET_TIMEOUT));
	}

	@Nonnull
	@Override
	public HttpClient getClient() {
		// Doing an average of both timeout values since currently both values are the same anyways...
		return getClient((connectionTimeOut + socketTimeOut) / 2);
	}

	@Nonnull
	@Override
	public HttpClient getClient(int timeout) {
		RequestConfig requestConfig = RequestConfig.custom().
				setConnectTimeout(connectionTimeOut).
				setSocketTimeout(socketTimeOut).
				setConnectionRequestTimeout(timeout).
				build();

		HttpClient client = HttpClientBuilder.create().
				setConnectionManager(connectionManager).
				setDefaultRequestConfig(requestConfig).
				build();

		return client;
	}
}
