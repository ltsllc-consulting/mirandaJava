package com.ltsllc.mirandaClient;

import com.ltsllc.miranda.Results;
import com.ltsllc.miranda.servlet.objects.ResultObject;
import com.ltsllc.miranda.servlet.topic.TopicRequestObject;
import com.ltsllc.miranda.servlet.topic.TopicResultObject;
import com.ltsllc.miranda.topics.Topic;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

/**
 * Created by Clark on 6/5/2017.
 */
public class TopicOperations extends Operations{
    public static class RetrieveResults {
        public Results result;
        public Topic topic;
    }

    public TopicOperations (Session session) {
        super(session);
    }

    public Results createTopic (Topic topic) throws IOException {
        TopicRequestObject topicRequestObject = new TopicRequestObject();
        topicRequestObject.setSessionIdString(getSessionId());
        topicRequestObject.setTopic(topic);
        String json = getGson().toJson(topicRequestObject);

        String url = getUrl() + "/servlets/createTopic";

        HttpPost httpPost = new HttpPost(url);
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

    public RetrieveResults retrieveTopic (Topic topic) throws IOException {
        TopicRequestObject topicRequestObject = new TopicRequestObject();
        topicRequestObject.setSessionIdString(getSessionId());
        topicRequestObject.setTopic(topic);
        String json = getGson().toJson(topicRequestObject);

        String url = getUrl() + "/servlets/getTopic";
        HttpPost httpPost = new HttpPost(url);

        StringEntity stringEntity = new StringEntity(json);
        httpPost.setEntity(stringEntity);

        HttpResponse httpResponse = getHttpClient().execute(httpPost);
        TopicResultObject topicResultObject = getReply(httpResponse, TopicResultObject.class);

        RetrieveResults retrieveResults = new RetrieveResults();
        retrieveResults.result = topicResultObject.getResult();
        retrieveResults.topic = topicResultObject.getTopic();

        return retrieveResults;
    }
}

