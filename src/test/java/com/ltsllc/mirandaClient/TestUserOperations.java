package com.ltsllc.mirandaClient;

import com.ltsllc.miranda.PublicKey;
import com.ltsllc.miranda.Results;
import com.ltsllc.miranda.test.TestCase;
import com.ltsllc.miranda.user.User;
import com.ltsllc.miranda.util.Utils;
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

    public UserOperations getUserOperations() {
        return userOperations;
    }

    public void reset () {
        super.reset();

        userOperations = null;
    }

    public void setup () {
        reset();

        super.setup();

        userOperations = getSession().getUserOperations();
    }

    public TestSession.CreateResult createUser (String name, String description, String category)
            throws GeneralSecurityException, IOException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = new PublicKey(keyPair.getPublic());
        String publicKeyPem = Utils.publicKeyToPemString(publicKey.getSecurityPublicKey());

        TestSession.CreateResult createResult = new TestSession.CreateResult();
        createResult.keyPair = keyPair;
        createResult.result = getUserOperations().createUser(name, description, category, publicKeyPem);

        return createResult;
    }

    @Test
    public void testCreateUser () throws Exception {
        getSession().connect();

        TestSession.CreateResult createResult = createUser("stan","a test user", User.UserTypes.Publisher.toString());
        assert (createResult.result == Results.Success);

        Results result = getUserOperations().deleteUser("stan");
        assert (result == Results.Success);
    }

    @Test
    public void testListUsers () throws Exception {
        getSession().connect();
        UserOperations userOperations = getSession().getUserOperations();

        List<User> userList = userOperations.listUsers();
        assert (userList != null);
    }

    @Test
    public void testDeleteUser () throws Exception {
        getSession().connect();

        TestSession.CreateResult createResult = createUser("stan", "a test user", User.UserTypes.Publisher.toString());
        assert (createResult.result == Results.Success);

        Results result = getUserOperations().deleteUser("stan");
        assert (result == Results.Success);
    }

    public User createUser () throws GeneralSecurityException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = new PublicKey(keyPair.getPublic());
        User user = new User("whatever", User.UserTypes.Publisher, "a test user", publicKey);
        return user;
    }


    @Test
    public void testModifyUser () throws Exception {
        getSession().connect();

        TestSession.CreateResult createResult = createUser("stan", "a test user", User.UserTypes.Publisher.toString());

        User user = createUser();
        user.setName("stan");
        user.setDescription("modify");

        Results result = getUserOperations().updateUser (user);
        assert (result == Results.Success);

        Session.RetrieveUserResult retrieveUserResult = getUserOperations().retrieveUser("stan");
        assert (retrieveUserResult.result == Results.Success);
        assert (retrieveUserResult.user.getName().equals("stan"));
        assert (retrieveUserResult.user.getDescription().equals("modify"));

        result = getUserOperations().deleteUser("stan");
        assert (result == Results.Success);
    }


    public void whatever () throws Exception {
        getSession().connect();
        UserOperations userOperations = getSession().getUserOperations();

        Results result = userOperations.deleteUser("stan");
        assert (result == Results.Success);
    }

    @Test
    public void testGetUser () throws Exception {
        getSession().connect();
        UserOperations userOperations = getSession().getUserOperations();

        Session.RetrieveUserResult retrieveUserResult = userOperations.retrieveUser("admin");
        assert (retrieveUserResult.result == Results.Success);
        assert (retrieveUserResult.user.getName().equals("admin"));
    }

}
