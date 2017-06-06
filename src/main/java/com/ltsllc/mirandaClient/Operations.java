package com.ltsllc.mirandaClient;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by Clark on 6/5/2017.
 */
public class Operations {
    private Session session;

    public Session getSession() {
        return session;
    }

    public Operations(Session session) {
        this.session = session;
    }

    public String getSessionId () {
        return getSession().getSessionId();
    }

    public String getUrl () {
        return getSession().getUrl();
    }

    public HttpClient getHttpClient () {
        return getSession().getHttpClient();
    }

    public Gson getGson () {
        return getSession().getGson();
    }

    public <T> T getReply (HttpResponse httpResponse, Class<T> clazz) throws IOException {
        return getSession().getReply(httpResponse, clazz);
    }
}
