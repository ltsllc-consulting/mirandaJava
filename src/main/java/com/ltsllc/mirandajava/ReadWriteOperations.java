package com.ltsllc.mirandajava;

import com.ltsllc.miranda.clientinterface.Results;
import com.ltsllc.miranda.clientinterface.requests.Request;
import com.ltsllc.miranda.clientinterface.results.ResultObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

/**
 * Created by Clark on 6/7/2017.
 */
public abstract class ReadWriteOperations extends Operations {
    abstract String getUpdateUrl ();
    abstract String getDeleteUrl ();

    public ReadWriteOperations (Session session) {
        super(session);
    }

    public <T> Results update (String sessionId, T t) throws IOException {
        String url = getUrl() + getUpdateUrl();
        HttpPost httpPost = new HttpPost(url);

        Request requestObject = getRequestObject(sessionId, t);
        String json = getGson().toJson(requestObject);

        StringEntity stringEntity = new StringEntity(json);
        httpPost.setEntity(stringEntity);

        HttpResponse httpResponse = getHttpClient().execute(httpPost);
        ResultObject resultObject = getReply(httpResponse, ResultObject.class);

        if (null == resultObject) {
            return Results.SessionNotFound;
        } else {
            return resultObject.getResult();
        }
    }

    public <T> Results delete (String sessionId, T t) throws IOException {
        String url = getUrl() + getDeleteUrl();
        HttpPost httpPost = new HttpPost(url);

        Request requestObject = getRequestObject(sessionId, t);
        String json = getGson().toJson(requestObject);

        StringEntity stringEntity = new StringEntity(json);
        httpPost.setEntity(stringEntity);

        HttpResponse httpResponse = getHttpClient().execute(httpPost);
        ResultObject resultObject = getReply(httpResponse, ResultObject.class);

        if (null == resultObject) {
            return Results.SessionNotFound;
        } else {
            return resultObject.getResult();
        }
    }

}
