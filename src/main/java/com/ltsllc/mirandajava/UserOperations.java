package com.ltsllc.mirandajava;

import com.google.gson.reflect.TypeToken;
import com.ltsllc.miranda.servlet.ReadObject;
import com.ltsllc.miranda.servlet.objects.RequestObject;
import com.ltsllc.miranda.servlet.user.UserRequestObject;
import com.ltsllc.miranda.user.User;

import java.lang.reflect.Type;

/**
 * Created by Clark on 6/5/2017.
 */
public class UserOperations extends ReadWriteOperations {
    public UserOperations (Session session) {
        super(session);
    }

    public RequestObject getRequestObject (String sessionId, Object object) {
        return new UserRequestObject(sessionId, object);
    }

    public String getCreateUrl () {
        return "/servlets/createUser";
    }

    public String getReadUrl () {
        return "/servlets/getUser";
    }

    public String getUpdateUrl () {
        return "/servlets/updateUser";
    }

    public String getDeleteUrl () {
        return "/servlets/deleteUser";
    }

    public String getListUrl () {
        return "/servlets/getUsers";
    }

    public Type getReadObjectType () {
        return new TypeToken<ReadObject<User>>(){}.getType();
    }
}
