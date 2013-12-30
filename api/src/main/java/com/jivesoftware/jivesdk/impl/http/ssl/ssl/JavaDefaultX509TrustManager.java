package com.jivesoftware.jivesdk.impl.http.ssl.ssl;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 5/8/13
 * Time: 11:46 AM
 */
public class JavaDefaultX509TrustManager implements X509TrustManager {
    private X509TrustManager trustManager;

    public JavaDefaultX509TrustManager() throws Exception {
        // load keystore from specified cert store (or default)
        KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream in = new FileInputStream(getTrustStorePath());
        try {
            ts.load(in, null);
        } finally {
            in.close();
        }

        // initialize a new TMF with the ts we just loaded
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ts);

        // acquire X509 trust manager from factory
        TrustManager tms[] = tmf.getTrustManagers();
        for (TrustManager tm : tms) {
            if (tm instanceof X509TrustManager) {
                trustManager = (X509TrustManager) tm;
                return;
            }
        }

        throw new NoSuchAlgorithmException("No X509TrustManager in TrustManagerFactory");
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain,
                                   String authType) throws CertificateException {
        trustManager.checkClientTrusted(chain, authType);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain,
                                   String authType) throws CertificateException {
        trustManager.checkServerTrusted(chain, authType);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return trustManager.getAcceptedIssuers();
    }

    private String getTrustStorePath() {
        return System.getProperty("java.home") + "/lib/security/cacerts".replace('/', File.separatorChar);
    }
}
