package com.example.chattingonlineapplication.Utils.SSL;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/*
 * for client to load cert from server and have to check by CA.
 * */
public class SSLProvider {
    private static String Tag = SSLProvider.class.getSimpleName();

    public static SSLContext getSSLContextForCertificateFile(Context context, String fileName, char[] keypass) {
        try {
            KeyStore keyStore = SSLProvider.getKeyStore(context, fileName, keypass);
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManagerFactory trust = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trust.init(keyStore);
            sslContext.init(null, trust.getTrustManagers(), new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            Log.i(Tag, "Exception occur:" + e.getMessage());
            throw new RuntimeException("Some things went wrong when get SSLContext from file");
        }
    }

    public static KeyStore getKeyStore(Context context, String file, char[] keypass) {
        KeyStore keystore = null;
        try {
            AssetManager as = context.getAssets();
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream inputStream = as.open(file);
            Certificate ca;
            try {
                ca = cf.generateCertificate(inputStream);
                Log.d(Tag, "ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                inputStream.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            keystore = KeyStore.getInstance(keyStoreType);
            keystore.load(null, keypass);
            keystore.setCertificateEntry("ca", ca);

        } catch (Exception e) {
            Log.i(Tag, "Exception occur:" + e.getMessage());
            throw new RuntimeException("Some things went wrong when get KeyStore");
        }
        return keystore;
    }
}
