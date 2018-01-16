package com.github.tonytangandroid.client.certificate.demo;


import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class ClientCertificateUtil {


    private static KeyStore provideKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(DemoApplication.getInstance().getAssets().open(Constants.ASSET_FILE_NAME), Constants.PASSWORD.toCharArray());
            return keyStore;
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static KeyManager[] getKeyManager() {
        try {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
            kmf.init(provideKeyStore(), Constants.PASSWORD.toCharArray());
            return kmf.getKeyManagers();
        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static SSLSocketFactory provideSSLSocketFactory() {

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(getKeyManager(), null, null);
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }


    public static X509TrustManager provideX509TrustManager() {

        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            X509TrustManager x509TrustManager = null;
            for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
                System.out.println(trustManager);
                if (trustManager instanceof X509TrustManager) {
                    x509TrustManager = (X509TrustManager) trustManager;
                    break;
                }
            }

            return x509TrustManager;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
