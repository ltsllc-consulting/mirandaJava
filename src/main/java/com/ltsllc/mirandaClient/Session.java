package com.ltsllc.mirandaClient;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ltsllc.miranda.PrivateKey;
import com.ltsllc.miranda.Results;
import com.ltsllc.miranda.servlet.login.LoginObject;
import com.ltsllc.miranda.servlet.login.LoginResultObject;
import com.ltsllc.miranda.user.JSPublicKeySerializer;
import com.ltsllc.miranda.user.User;
import com.ltsllc.miranda.util.Utils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Clark on 6/2/2017.
 */
public class Session {
    private static Gson gson = buildGson();

    private static Logger logger = Logger.getLogger(Session.class);

    private String url;
    private User user;
    private HttpClient httpClient;
    private String sessionId;
    private PrivateKey privateKey;
    private UserOperations userOperations;
    private TopicOperations topicOperations;
    private SubscriptionOperations subscriptionOperations;

    public Session(User user, PrivateKey privateKey, String url) {
        this.user = user;
        this.privateKey = privateKey;
        this.url = url;
        this.httpClient = createHttpClient();
        this.userOperations = new UserOperations(this);
        this.topicOperations = new TopicOperations(this);
        this.subscriptionOperations = new SubscriptionOperations(this);
    }

    public SubscriptionOperations getSubscriptionOperations() {
        return subscriptionOperations;
    }

    public TopicOperations getTopicOperations() {
        return topicOperations;
    }

    public static Gson buildGson () {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(java.security.PublicKey.class, new JSPublicKeySerializer());
        return gsonBuilder.create();
    }

    public UserOperations getUserOperations() {
        return userOperations;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public boolean getLoggedIn () {
        return getSessionId() != null;
    }

    public static Gson getGson() {
        return gson;
    }

    public <T> T getReply (HttpResponse httpResponse, Type type) throws IOException {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;

        try {
            inputStream = httpResponse.getEntity().getContent();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int c = inputStream.read();
            while (c != -1) {
                byteArrayOutputStream.write(c);
                c = inputStream.read();
            }

            String json = new String(byteArrayOutputStream.toByteArray());

            Object object = getGson().fromJson(json, type);
            return (T) object;
        } finally {
            Utils.closeIgnoreExceptions(inputStream);
            Utils.closeIgnoreExceptions(inputStreamReader);
        }
    }

    public HttpClient createHttpClient () {
        HostnameVerifier hostnameVerifier = new CustomHostnameVerifier();
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);


        final SSLConnectionSocketFactory sslsf;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault(),
                    NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", sslsf)
                .build();

        final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(100);
        return HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setConnectionManager(cm)
                .build();
    }

    public void connect () throws IOException, GeneralSecurityException {
        String url = getUrl() + "/servlets/login";

        HttpPost post = new HttpPost(url);
        LoginObject loginObject = new LoginObject();
        loginObject.setName(getUser().getName());
        String json = gson.toJson(loginObject);
        StringEntity stringEntity = new StringEntity(json);
        post.setEntity(stringEntity);
        HttpResponse httpResponse = httpClient.execute(post);
        LoginResultObject loginResultObject = getReply(httpResponse, LoginResultObject.class);
        if (loginResultObject.getResult() == Results.Success) {
            byte[] plainText = getPrivateKey().decrypt(loginResultObject.getEncryptedMessage());
            this.sessionId = new String(plainText);
            logger.info ("Successfully logged in");
        }
    }

}
