package com.ltsllc.mirandajava;

import com.ltsllc.miranda.Results;
import com.ltsllc.miranda.servlet.ListObject;
import com.ltsllc.miranda.servlet.ReadObject;
import com.ltsllc.miranda.topics.Topic;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Clark on 6/5/2017.
 */
public class TestTopicOperations extends TestSession {
    private TopicOperations topicOperations;
    private Topic topic;

    public Topic getTopic() {
        return topic;
    }

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

        try {
            getSession().connect();
            topicOperations = getSession().getTopicOperations();
            topic = new Topic(TEST_TOPIC_NAME, TEST_TOPIC_OWNER);
            createTopic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void cleanup () {
        Results result = null;

        try {
            result = getTopicOperations().delete(getSession().getSessionId(), getTopic());
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert (result == Results.Success || result == Results.TopicNotFound);
    }

    public void createTopic () {
        Results result = null;

        try {
            result = getTopicOperations().create(getSession().getSessionId(), getTopic());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert (result == Results.Success || result == Results.Duplicate);
    }

    public static String TEST_TOPIC_NAME = "whatever";
    public static String TEST_TOPIC_OWNER = "admin";



    @Test
    public void testRead () throws Exception {
        ReadObject readObject = getTopicOperations().read(getSession().getSessionId(), getTopic());
        assert (readObject.getResult() == Results.Success);
    }

    @Test
    public void testList () throws Exception {
        ListObject listingResult = getTopicOperations().list(getSession().getSessionId());

        assert (listingResult.getResult() == Results.Success);
        assert (listingResult.getList().size() > 0);
    }
}
