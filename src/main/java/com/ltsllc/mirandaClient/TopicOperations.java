package com.ltsllc.mirandaClient;

import com.google.gson.reflect.TypeToken;
import com.ltsllc.miranda.Results;
import com.ltsllc.miranda.servlet.ReadObject;
import com.ltsllc.miranda.servlet.objects.RequestObject;
import com.ltsllc.miranda.servlet.objects.ResultObject;
import com.ltsllc.miranda.servlet.topic.TopicRequestObject;
import com.ltsllc.miranda.servlet.topic.TopicResultObject;
import com.ltsllc.miranda.servlet.topic.TopicsResultObject;
import com.ltsllc.miranda.topics.Topic;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import javax.xml.transform.Result;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clark on 6/5/2017.
 */
public class TopicOperations extends Operations {
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

