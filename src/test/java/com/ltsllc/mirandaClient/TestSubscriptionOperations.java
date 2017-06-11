package com.ltsllc.mirandaClient;

import com.ltsllc.miranda.Results;
import com.ltsllc.miranda.servlet.ReadObject;
import com.ltsllc.miranda.subsciptions.Subscription;
import com.ltsllc.miranda.test.TestCase;
import com.ltsllc.miranda.topics.Topic;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Clark on 6/7/2017.
 */
public class TestSubscriptionOperations extends TestSession {
    private SubscriptionOperations subscriptionOperations;
    private Subscription subscription;

    public SubscriptionOperations getSubscriptionOperations() {
        return subscriptionOperations;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void reset () {
        super.reset();

        subscription = null;
        subscriptionOperations = null;
    }

    @Before
    public void setup () {
        reset();

        super.setup();

        try {
            subscription = new Subscription("whatever", "admin", "whatever", "http://whatever.com/data",
                    "http://whatever.com/live", Subscription.ErrorPolicies.Drop);
            getSession().connect();

            subscriptionOperations = getSession().getSubscriptionOperations();

            Topic topic = new Topic("whatever", "admin");
            Results result = getSession().getTopicOperations().create(getSession().getSessionId(), topic);
            result = subscriptionOperations.create(getSession().getSessionId(), getSubscription());
            assert (result == Results.Success || result == Results.Duplicate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void cleanup () {
        try {
            Results result = getSubscriptionOperations().delete(getSession().getSessionId(), getSubscription());
            assert (result == Results.Success || result == Results.SubscriptionNotFound);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRead () throws Exception {
        ReadObject readObject = getSubscriptionOperations().read(getSession().getSessionId(), getSubscription());
        assert (readObject.getResult() == Results.Success);
        assert (((Subscription) (readObject.getObject())).getName().equals("whatever"));
    }

    @Test
    public void testUpdate () throws Exception {
        getSubscription().setDataUrl("modify");

        Results result = getSubscriptionOperations().update(getSession().getSessionId(), getSubscription());
        assert (result == Results.Success);

        ReadObject readObject = getSubscriptionOperations().read(getSession().getSessionId(), getSubscription());
        assert (readObject.getResult() == Results.Success);
        assert (((Subscription) readObject.getObject()).getDataUrl().equals("modify"));
    }
}
