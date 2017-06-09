package com.ltsllc.mirandaClient;

import com.google.gson.reflect.TypeToken;
import com.ltsllc.miranda.event.Event;
import com.ltsllc.miranda.servlet.ReadObject;
import com.ltsllc.miranda.servlet.event.EventRequestObject;
import com.ltsllc.miranda.servlet.objects.RequestObject;

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

    public RequestObject getRequestObject (String sessionId, Object object) {
        Event event= (Event) object;

        return new EventRequestObject(sessionId, event);
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
