package com.ltsllc.mirandajava;


import com.google.gson.reflect.TypeToken;
import com.ltsllc.miranda.clientinterface.basicclasses.Subscription;
import com.ltsllc.miranda.clientinterface.objects.ReadObject;
import com.ltsllc.miranda.clientinterface.requests.Request;
import com.ltsllc.miranda.clientinterface.requests.SubscriptionRequest;

import java.lang.reflect.Type;

/**
 * Created by Clark on 6/7/2017.
 */
public class SubscriptionOperations extends ReadWriteOperations {
    public SubscriptionOperations (Session session) {
        super(session);
    }

    public Request getRequestObject (String sessionId, Object object) {
        Subscription subscription = (Subscription) object;

        return new SubscriptionRequest(sessionId, subscription);
    }

    public String getCreateUrl () {
        return "/servlets/createSubscription";
    }

    public String getReadUrl () {
        return "/servlets/getSubscription";
    }

    public String getListUrl () {
        return "/servlets/getSubscriptions";
    }

    public String getUpdateUrl () {
        return "/servlets/updateSubscription";
    }

    public String getDeleteUrl () {
        return "/servlets/deleteSubscription";
    }

    public Type getReadObjectType () {
        return new TypeToken<ReadObject<Subscription>>() {}.getType();
    }
}
