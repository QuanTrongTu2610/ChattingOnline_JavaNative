package com.example.chattingonlineapplication.Utils.SSL;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.android.gms.security.ProviderInstaller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/*
 * for client to load cert from server and have to check by CA.
 * */
public class SSLProvider {
    private static String Tag = SSLProvider.class.getSimpleName();

    public static SSLContext getSSLContextForCertificateFile(Context context, String fileName, String fileKeyStore, String fileTrust, char[] keypass) {
        SSLContext sslContext = null;
        try {
            KeyStore keyStore = SSLProvider.getKeyStore(context, fileName, fileKeyStore, keypass);

            InputStream trustIn = new BufferedInputStream(context.getAssets().open(fileTrust));
            keyStore.load(trustIn, keypass);
            // trust store

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            sslContext = SSLContext.getInstance("TLSv1");
            sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    public static KeyStore getKeyStore(Context context, String fileCer, String fileKeyStore, char[] keypass) {
        KeyStore keystore = null;
        try {
            keystore = KeyStore.getInstance("BKS");
            // Load CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(context.getAssets().open(fileCer));
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            InputStream ksInput = new BufferedInputStream(context.getAssets().open(fileKeyStore));
            keystore.load(ksInput, keypass);
            ksInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keystore;
    }
}
