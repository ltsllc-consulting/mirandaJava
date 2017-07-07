package com.ltsllc.mirandajava;

import com.google.gson.reflect.TypeToken;
import com.ltsllc.miranda.clientinterface.basicclasses.Event;
import com.ltsllc.miranda.clientinterface.objects.ReadObject;
import com.ltsllc.miranda.clientinterface.requests.EventRequest;
import com.ltsllc.miranda.clientinterface.requests.Request;

import java.lang.reflect.Type;

/**
 * Created by Clark on 6/7/2017.
 */
public class EventOperations extends Operations {
    public String getCreateUrl () {
        return "/servlets/createEvent";
    }

    public String getReadUrl () {
        return "/servlets/readEvent";
    }

    public EventOperations (Session session) {
        super(session);
    }

    public Request getRequestObject (String sessionId, Object object) {
        Event event= (Event) object;

        return new EventRequest(sessionId, event);
    }

    public String getListUrl () {
        return "/servlets/getSubscriptions";
    }

    public String getUpdateUrl () {
        return "/servlets/updateSubscription";
    }

    public String getDeleteUrl () {
        throw new RuntimeException ("events cannot be deleted");
    }

    public Type getReadObjectType () {
        return new TypeToken<ReadObject<Event>>() {}.getType();
    }

}
