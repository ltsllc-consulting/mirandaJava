package com.ltsllc.mirandaClient;

import com.google.gson.Gson;
import com.ltsllc.miranda.Results;
import com.ltsllc.miranda.operations.Operation;
import com.ltsllc.miranda.operations.UserOperation;
import com.ltsllc.miranda.servlet.objects.ResultObject;
import com.ltsllc.miranda.servlet.user.GetUserResponseObject;
import com.ltsllc.miranda.servlet.user.UserListResultObject;
import com.ltsllc.miranda.servlet.user.UserObject;
import com.ltsllc.miranda.servlet.user.UserRequestObject;
import com.ltsllc.miranda.user.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.util.List;

/**
 * Created by Clark on 6/5/2017.
 */
public class UserOperations extends Operations {
    public UserOperations (Session session) {
        super(session);
    }

    public Results createUser (String name, String description, String category, String publicKeyPem) throws IOException {
        UserObject userObject = new UserObject(name, category, description, publicKeyPem);
        UserRequestObject userRequestObject = new UserRequestObject();
        userRequestObject.setUserObject(userObject);
        userRequestObject.setSessionIdString(getSessionId());
        HttpPost post = new HttpPost(getUrl() + "/servlets/createUser");
        String json = getGson().toJson(userRequestObject);
        StringEntity stringEntity = new StringEntity(json);
        post.setEntity(stringEntity);
        HttpResponse httpResponse = getHttpClient().execute(post);
        ResultObject resultObject = getReply(httpResponse, ResultObject.class);

        if (null == resultObject) {
            return Results.SessionNotFound;
        } else {
            return resultObject.getResult();
        }
    }

    public List<User> listUsers () throws IOException {
        String url = getUrl() + "/servlets/getUsers";
        UserRequestObject userRequestObject = new UserRequestObject();
        userRequestObject.setSessionIdString(getSessionId());
        HttpPost post = new HttpPost(url);
        String json = getGson().toJson(userRequestObject);
        StringEntity stringEntity = new StringEntity(json);
        post.setEntity(stringEntity);
        HttpResponse httpResponse = getHttpClient().execute(post);
        UserListResultObject userListResultObject = getReply(httpResponse, UserListResultObject.class);

        if (null == userListResultObject) {
            return null;
        } else {
            return userListResultObject.getUserList();
        }
    }

    public Results deleteUser (String name) throws IOException {
        String url = getUrl() + "/servlets/deleteUser";
        UserRequestObject userRequestObject = new UserRequestObject();
        userRequestObject.setSessionIdString(getSessionId());
        UserObject userObject = new UserObject(name, null, null, null);
        userRequestObject.setUserObject(userObject);
        String json = getGson().toJson(userRequestObject);

        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(json);
        httpPost.setEntity(stringEntity);

        HttpResponse httpResponse = getHttpClient().execute(httpPost);
        ResultObject resultObject = getReply(httpResponse, ResultObject.class);

        if (null != resultObject) {
            return resultObject.getResult();
        } else {
            return Results.SessionNotFound;
        }
    }

    public Results updateUser (User user) throws IOException {
        String url = getUrl() + "/servlets/updateUser";
        UserRequestObject userRequestObject = new UserRequestObject();
        userRequestObject.setSessionIdString(getSessionId());

        UserObject userObject = new UserObject(user.getName(), user.getCategory().toString(), user.getDescription(),
                user.getPublicKeyPem());

        userRequestObject.setUserObject(userObject);

        String json = getGson().toJson(userRequestObject);
        StringEntity stringEntity = new StringEntity(json);

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(stringEntity);

        HttpResponse httpResponse = getHttpClient().execute(httpPost);
        ResultObject resultObject = getReply(httpResponse, ResultObject.class);

        if (resultObject == null) {
            return Results.SessionNotFound;
        } else {
            return resultObject.getResult();
        }
    }


    public Session.RetrieveUserResult retrieveUser (String name) throws IOException {
        String url = getUrl() + "/servlets/getUser";

        UserRequestObject userRequestObject = new UserRequestObject();
        userRequestObject.setSessionIdString(getSessionId());

        UserObject userObject = new UserObject(name, null, null, null);
        userRequestObject.setUserObject(userObject);

        String json = getGson().toJson(userRequestObject);

        StringEntity stringEntity = new StringEntity(json);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(stringEntity);

        HttpResponse httpResponse = getHttpClient().execute(httpPost);

        GetUserResponseObject getUserResponseObject = getReply(httpResponse, GetUserResponseObject.class);

        Session.RetrieveUserResult retrieveUserResult = new Session.RetrieveUserResult();

        if (null == getUserResponseObject) {
            retrieveUserResult.result = Results.SessionNotFound;
        } else {
            retrieveUserResult.result = getUserResponseObject.getResult();
            retrieveUserResult.user = getUserResponseObject.getUserObject();
        }

        return retrieveUserResult;
    }

}
