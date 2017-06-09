package com.ltsllc.mirandaClient;

import com.google.gson.reflect.TypeToken;
import com.ltsllc.miranda.servlet.ReadObject;
import com.ltsllc.miranda.servlet.objects.RequestObject;
import com.ltsllc.miranda.servlet.subscription.SubscriptionRequestObject;
import com.ltsllc.miranda.subsciptions.Subscription;

import java.lang.reflect.Type;

/**
 * Created by Clark on 6/7/2017.
 */
public class SubscriptionOperations extends ReadWriteOperations {
    public SubscriptionOperations (Session session) {
        super(session);
    }

    public RequestObject getRequestObject (String sessionId, Object object) {
        Subscription subscription = (Subscription) object;

        return new SubscriptionRequestObject(sessionId, subscription);
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
