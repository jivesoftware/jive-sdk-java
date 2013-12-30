package com.jivesoftware.jivesdk.impl.http.ssl.ssl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created with IntelliJ IDEA.
 * User: Zvoykish
 * Date: 5/8/13
 * Time: 1:37 PM
 */
public class SubjectVerifierX509TrustManager implements X509TrustManager {
    private static final Logger log = LoggerFactory.getLogger(SubjectVerifierX509TrustManager.class);
    private static final String SUBJECT_PREFIX = ",O=";
    private static final String SUBJECT_PREFIX_2 = "O=";

    public static final String HACK_SUBJECT_PROPERTY_NAME = "AppsMarketSubjectString";

    @Nonnull
    private X509TrustManager delegate;
    @Nonnull
    private String subject;

    public SubjectVerifierX509TrustManager(@Nonnull X509TrustManager delegate, @Nonnull String subject) {
        this.delegate = delegate;
        this.subject = subject;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        delegate.checkClientTrusted(x509Certificates, s);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String s) throws CertificateException {
        // First check if the certificate is trusted
        delegate.checkServerTrusted(chain, s);

        // Only if it's trusted - move on to validating it's a Jive cert.
        if (!StringUtils.isEmpty(subject)) {
            if (chain == null || chain.length < 1) {
                String msg = "Failed authenticating SSL certificate, Invalid certificate chain";
                log.error(msg);
                throw new CertificateException(msg);
            }

            // Grab the last certificate in the chain (the actual certificate of the target server)
            X509Certificate certificate = chain[0];
            try {
                // No proper API for that in Java security, manual search for text... :(
//                String principalName = certificate.getSubjectDN().getName();
                String principalName = certificate.getSubjectX500Principal().toString();
                log.debug("Subject verifier trust manager received X500 principal: " + principalName);
                int index = principalName.indexOf(SUBJECT_PREFIX);
                int tokenLength = SUBJECT_PREFIX.length();
                if (index == -1) {
                    // Just in case that attribute is the first in the string... (I think the attributes are in fixed order, but that won't hurt us...)
                    index = principalName.indexOf(SUBJECT_PREFIX_2);
                    tokenLength = SUBJECT_PREFIX_2.length();
                }

                String certSubjectString = principalName.substring(index + tokenLength);
                System.setProperty(HACK_SUBJECT_PROPERTY_NAME, certSubjectString);
                if (!(certSubjectString.startsWith('"' + subject + "\",") || certSubjectString.startsWith(subject + ','))) {
                    String msg = "Invalid certificate subject, expecting: [" + subject + "] but received principal: " + principalName;
                    log.error(msg);
                    throw new CertificateException(msg);
                }
            } catch (CertificateException e) {
                throw e;
            } catch (Throwable t) {
                String msg = "Failed parsing certificate subject for validation: " + certificate;
                log.error(msg);
                throw new CertificateException(msg, t);
            }
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return delegate.getAcceptedIssuers();
    }
}
