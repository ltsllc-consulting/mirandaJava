package com.ltsllc.mirandaClient;

import com.ltsllc.miranda.PrivateKey;
import com.ltsllc.miranda.PublicKey;
import com.ltsllc.miranda.Results;
import com.ltsllc.miranda.test.TestCase;
import com.ltsllc.miranda.user.User;
import com.ltsllc.miranda.util.Utils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.util.List;

/**
 * Created by Clark on 6/2/2017.
 */
public class TestSession extends TestCase {
    public static class CreateResult {
        public Results result;
        public KeyPair keyPair;
    }

    private Session session;

    public Session getSession() {
        return session;
    }

    public void reset () {
        super.reset();

        session = null;
    }

    public static final String USER_KEY_STORE = "userKeyStore";
    public static final String TEST_PASSWORD = "whatever";
    public static final String PRIVATE_KEY_ALIAS = "private";
    public static final String TEST_URL = "https://localhost";
    public static final String TRUST_STORE_PROPERTY = "javax.net.ssl.trustStore";

    public void setupFile () {
        File file = new File("truststore");
        System.setProperty(TRUST_STORE_PROPERTY, "truststore");

        PrivateKey privateKey = null;
        try {
            KeyStore keyStore = Utils.loadKeyStore(USER_KEY_STORE, TEST_PASSWORD);
            java.security.PrivateKey jsPrivateKey = (java.security.PrivateKey) keyStore.getKey(PRIVATE_KEY_ALIAS, TEST_PASSWORD.toCharArray());
            privateKey = new PrivateKey(jsPrivateKey);
            User admin = new User("admin", User.UserTypes.Admin, "the admin user");
            session = new Session(admin, privateKey, TEST_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setup () {
        reset();

        super.setup();

        setuplog4j();
        setupFile();
    }

    @Test
    public void testLogin () throws Exception {
        getSession().connect();

        assert (getSession().getLoggedIn());
    }


    public boolean containsUser (String name, List<User> list) {
        for (User user : list) {
            if (name.equals(user.getName())) {
                return true;
            }
        }

        return false;
    }

    public CreateResult createUser (String name, String description, String category)
            throws GeneralSecurityException, IOException{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = new PublicKey(keyPair.getPublic());
        String publicKeyPem = Utils.publicKeyToPemString(publicKey.getSecurityPublicKey());

        CreateResult createResult = new CreateResult();
        createResult.keyPair = keyPair;
        createResult.result = getSession().createUser(name, description, category, publicKeyPem);

        return createResult;
    }


    @Test
    public void testCreateUser () throws Exception {
        getSession().connect();
        CreateResult createResult = createUser("stan","a test user", User.UserTypes.Publisher.toString());
        assert (createResult.result == Results.Success);

        Results result = getSession().deleteUser("stan");
        assert (result == Results.Success);
    }

    @Test
    public void testListUsers () throws Exception {
        getSession().connect();;
        List<User> userList = getSession().listUsers();
        assert (userList != null);
    }

    public Results performDelete (String name) throws IOException {
        return getSession().deleteUser(name);
    }

    @Test
    public void testDeleteUser () throws Exception {
        getSession().connect();

        CreateResult createResult = createUser("stan", "a test user", User.UserTypes.Publisher.toString());
        assert (createResult.result == Results.Success);

        Results result = getSession().deleteUser("stan");
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

        CreateResult createResult = createUser("stan", "a test user", User.UserTypes.Publisher.toString());

        User user = createUser();
        user.setName("stan");
        user.setDescription("modify");

        Results result = getSession().updateUser (user);
        assert (result == Results.Success);

        Session.RetrieveUserResult retrieveUserResult = getSession().retrieveUser("stan");
        assert (retrieveUserResult.result == Results.Success);
        assert (retrieveUserResult.user.getName().equals("stan"));
        assert (retrieveUserResult.user.getDescription().equals("modify"));

        result = getSession().deleteUser("stan");
        assert (result == Results.Success);
    }


    public void whatever () throws Exception {
        getSession().connect();
        Results result = performDelete("stan");
        assert (result == Results.Success);
    }
}
