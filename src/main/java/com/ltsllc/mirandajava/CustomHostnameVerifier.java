package com.ltsllc.mirandajava;

import org.apache.http.conn.ssl.X509HostnameVerifier;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.X509Certificate;

/**
 * Created by Clark on 6/3/2017.
 */
public class CustomHostnameVerifier implements X509HostnameVerifier {

    public boolean verify(String host, SSLSession session) {
        HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
        return hv.verify(host, session);
    }


    public void verify(String host, SSLSocket ssl) throws IOException {
    }


    public void verify(String host, X509Certificate cert) throws SSLException {

    }


    public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {

    }
}
