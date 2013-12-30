package com.jivesoftware.jivesdk.impl.http.ssl.ssl;

import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 5/8/13
 * Time: 4:10 PM
 */
public class SubjectVerifierSocketFactory {
    private static SchemeSocketFactory socketFactoryInstance = null;
    private static final Object lock = new Object();

    public static SchemeSocketFactory createSocketFactory(String subject) throws Exception {
        if (socketFactoryInstance == null) {
            synchronized (lock) {
                if (socketFactoryInstance == null) {
                    // Related reference: http://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https
                    SSLContext sslContext = SSLContext.getInstance(SSLSocketFactory.SSL);
                    X509TrustManager trustManager = new JavaDefaultX509TrustManager();
                    trustManager = new SubjectVerifierX509TrustManager(trustManager, subject);
                    sslContext.init(null, new TrustManager[]{trustManager}, null);
                    socketFactoryInstance = new SSLSocketFactory(sslContext);
                }
            }
        }

        return socketFactoryInstance;
    }

    public static void clearSocketFactory() {
        synchronized (lock) {
            socketFactoryInstance = null;
        }
    }
}
