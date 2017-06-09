package com.ltsllc.mirandaClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ltsllc.miranda.Results;
import com.ltsllc.miranda.servlet.ListObject;
import com.ltsllc.miranda.servlet.ReadObject;
import com.ltsllc.miranda.servlet.objects.RequestObject;
import com.ltsllc.miranda.servlet.objects.ResultObject;
import com.ltsllc.miranda.user.JSPublicKeySerializer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by Clark on 6/5/2017.
 */
public abstract class Operations {
    abstract public RequestObject getRequestObject (String sessionId, Object object);
    abstract public String getCreateUrl ();
    abstract public String getReadUrl ();
    abstract public String getListUrl ();
    abstract public Type getReadObjectType();

    private static Gson gson = createGson();

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

    public static Gson createGson () {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(java.security.PublicKey.class, new JSPublicKeySerializer());
        return gsonBuilder.create();
    }
    public Gson getGson () {
        return getSession().getGson();
    }

    public <T> T getReply (HttpResponse httpResponse, Type type) throws IOException {
        return getSession().getReply(httpResponse, type);
    }

    public <E> ReadObject read (String sessionId, E e) throws IOException {
        String url = getUrl() + getReadUrl();
        HttpPost httpPost = new HttpPost(url);

        RequestObject requestObject = getRequestObject(sessionId, e);
        String json = getGson().toJson(requestObject);
        StringEntity stringEntity = new StringEntity(json);
        httpPost.setEntity(stringEntity);

        HttpResponse httpResponse = getHttpClient().execute(httpPost);
        ReadObject readResult = getReply(httpResponse, getReadObjectType());

        if (null == readResult) {
            readResult = new ReadObject();
            readResult.setResult(Results.SessionNotFound);
        }

        return readResult;
    }

    public <T> Results create (String sessionId, T t) throws IOException, OperationException {
        String url = getUrl() + getCreateUrl();
        HttpPost httpPost = new HttpPost(url);

        RequestObject requestObject = getRequestObject(sessionId, t);
        String json = getGson().toJson(requestObject);

        StringEntity stringEntity = new StringEntity(json);
        httpPost.setEntity(stringEntity);

        HttpResponse httpResponse = getHttpClient().execute(httpPost);
        ResultObject resultObject = getReply(httpResponse, ResultObject.class);

        if (resultObject == null) {
            throw new OperationException ("null response object");
        } else {
            return resultObject.getResult();
        }
    }


    public ListObject list (String sessionId) throws IOException {
        String url = getUrl() + getListUrl();
        HttpPost httpPost = new HttpPost(url);

        RequestObject requestObject = getRequestObject(sessionId, null);
        String json = getGson().toJson(requestObject);
        StringEntity stringEntity = new StringEntity(json);
        httpPost.setEntity(stringEntity);

        HttpResponse httpResponse = getHttpClient().execute(httpPost);
        ListObject listResult = getReply(httpResponse, ListObject.class);

        if (null == listResult) {
            listResult = new ListObject();
            listResult.setResult(Results.SessionNotFound);
        }

        return listResult;
    }
}
