package com.ltsllc.mirandajava;

import com.google.gson.reflect.TypeToken;
import com.ltsllc.miranda.servlet.ReadObject;
import com.ltsllc.miranda.servlet.objects.RequestObject;
import com.ltsllc.miranda.servlet.objects.ResultObject;
import com.ltsllc.miranda.servlet.topic.TopicRequestObject;
import com.ltsllc.miranda.servlet.topic.TopicResultObject;
import com.ltsllc.miranda.topics.Topic;

import java.lang.reflect.Type;

/**
 * Created by Clark on 6/5/2017.
 */
public class TopicOperations extends ReadWriteOperations {
    public TopicOperations (Session session) {
        super(session);
    }

    public RequestObject getRequestObject(String sessionId, Object object) {
        return new TopicRequestObject(sessionId, object);
    }

    public String getUpdateUrl () {
        return "/servlets/updateTopic";
    }

    public String getCreateUrl () {
        return "/servlets/createTopic";
    }

    public String getReadUrl () {
        return "/servlets/getTopic";
    }

    public String getDeleteUrl () {
        return "/servlets/deleteTopic";
    }

    public String getListUrl () {
        return "/servlets/getTopics";
    }

    public ResultObject getResultObject () {
        return new TopicResultObject();
    }

    public Type getReadObjectType () {
        return new TypeToken<ReadObject<Topic>>(){}.getType();
    }
}

