package com.ltsllc.mirandaClient;

import com.ltsllc.miranda.PublicKey;
import com.ltsllc.miranda.Results;
import com.ltsllc.miranda.servlet.ListObject;
import com.ltsllc.miranda.servlet.ReadObject;
import com.ltsllc.miranda.test.TestCase;
import com.ltsllc.miranda.user.User;
import com.ltsllc.miranda.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.List;

/**
 * Created by Clark on 6/5/2017.
 */
public class TestUserOperations extends TestSession {
    private UserOperations userOperations;
    private User user;

    public User getUser() {
        return user;
    }

    public UserOperations getUserOperations() {
        return userOperations;
    }

    public void reset() {
        super.reset();

        userOperations = null;
    }

    @Before
    public void setup() {
        reset();

        super.setup();

        try {
            userOperations = getSession().getUserOperations();
            createUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void cleanup() {
        try {
            deleteUser(getUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User createUserObject(String name, User.UserTypes category, String description) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = new PublicKey(keyPair.getPublic());

        User user = new User(name, category, description, publicKey);
        return user;
    }

    public void deleteUser(User user) {
        Results result = Results.Unknown;

        try {
            result = getUserOperations().delete(getSession().getSessionId(), user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert (result == Results.Success || result == Results.UserNotFound);
    }

    @Test
    public void testListUsers() throws Exception {
        ListObject listResult = userOperations.list(getSession().getSessionId());
        assert (listResult != null);
        assert (listResult.getResult() == Results.Success);
        assert (listResult.getList().size() > 0);
    }

    public void createUser() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = new PublicKey(keyPair.getPublic());
        this.user = new User("stan", User.UserTypes.Publisher, "a test user", publicKey);

        Results result = getUserOperations().create(getSession().getSessionId(), getUser());

        assert (result == Results.Success);
    }


    @Test
    public void testModifyUser() throws Exception {
        getUser().setDescription("modify");

        Results result = getUserOperations().update(getSession().getSessionId(), getUser());
        assert (result == Results.Success);

        ReadObject readResult = getUserOperations().read(getSession().getSessionId(), getUser());
        assert (readResult.getResult() == Results.Success);
        assert (((User) readResult.getObject()).getName().equals("stan"));
        assert (((User) readResult.getObject()).getDescription().equals("modify"));
    }

    @Test
    public void testReadUser() throws Exception {
        ReadObject readObject = getUserOperations().read(getSession().getSessionId(), getUser());
        assert (readObject.getResult() == Results.Success);
        assert (((User) readObject.getObject()).getName().equals("stan"));
    }

}
