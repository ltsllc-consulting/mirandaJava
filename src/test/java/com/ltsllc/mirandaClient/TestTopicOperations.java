package com.ltsllc.mirandaClient;

import com.ltsllc.miranda.Results;
import com.ltsllc.miranda.servlet.topic.TopicRequestObject;
import com.ltsllc.miranda.test.TestCase;
import com.ltsllc.miranda.topics.Topic;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Clark on 6/5/2017.
 */
public class TestTopicOperations extends TestSession {
    private TopicOperations topicOperations;

    public TopicOperations getTopicOperations() {
        return topicOperations;
    }

    public void reset () {
        super.reset();

        topicOperations = null;
    }

    @Before
    public void setup () {
        reset();

        super.setup();

        topicOperations = getSession().getTopicOperations();
    }

    public static String TEST_TOPIC_NAME = "whatever";
    public static String TEST_TOPIC_OWNER = "admin";


    public void whatever () throws Exception {
        getSession().connect();
        Topic topic = new Topic(TEST_TOPIC_NAME, TEST_TOPIC_OWNER, Topic.RemotePolicies.None);
        Results result = getTopicOperations().deleteTopic(topic);
        assert (result == Results.Success);
    }

    @Test
    public void testCreate () throws Exception {
        getSession().connect();

        Topic topic = new Topic(TEST_TOPIC_NAME, TEST_TOPIC_OWNER, Topic.RemotePolicies.None);
        Results result = getTopicOperations().createTopic(topic);
        assert (result == Results.Success);

        result = getTopicOperations().deleteTopic (topic);
        assert (result == Results.Success);
    }

    @Test
    public void testGet () throws Exception {
        Topic topic = new Topic(TEST_TOPIC_NAME);
        TopicOperations.RetrieveResults retrieveResults = getTopicOperations().retrieveTopic(topic);
        assert (retrieveResults.result == Results.Success);
    }
}
