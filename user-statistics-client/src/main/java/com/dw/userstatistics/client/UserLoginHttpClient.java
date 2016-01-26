package com.dw.userstatistics.client;

import com.dw.userstatistics.api.representations.LoginAttempt;
import com.dw.userstatistics.api.representations.User;
import com.dw.userstatistics.api.representations.UserJSONRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.dropwizard.jackson.Jackson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * Created by alberto on 2016-01-24.
 */
public class UserLoginHttpClient  implements UserLoginTrackerAPI{

    private final URI base;
    private final CloseableHttpClient httpClient;
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    public UserLoginHttpClient(final URI base){
        this(base,HttpClients.createDefault());
    }

    public UserLoginHttpClient(final URI base, final CloseableHttpClient closeableHttpClient){
        this.base = base;
        this.httpClient = closeableHttpClient;
    }

    @Override
    public boolean registerNewUser(String username, String password) throws Exception{
        final UserJSONRequest request = new UserJSONRequest(username,password);
        final String resourceURL = base.toString()+"/register";

        HttpPost post = new HttpPost(resourceURL);
        String jsonRequest = MAPPER.writeValueAsString(request);
        StringEntity stringEntity = new StringEntity(jsonRequest, StandardCharsets.UTF_8.name());
        stringEntity.setContentType("application/json");
        post.setEntity(stringEntity);

        Boolean isCreated = httpClient.<Boolean>execute(post,validatePostAndGetResult);
        return isCreated;
    }

    private static ResponseHandler validatePostAndGetResult = new ResponseHandler<Boolean>() {
        @Override
        public Boolean handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
            int status = httpResponse.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_CREATED){
                return true;
            }else{
                return false;
            }
        }
    };

    @Override
    public Optional<User> loginUser(String username, String password) throws IOException {
        final String resourceURL = base.toString()+"/login";
        final Gson gson = new Gson();

        //set the credentials for the request
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username+":"+password));
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setCredentialsProvider(credentialsProvider);

        final HttpGet get = new HttpGet(resourceURL);

        final Optional<String> response = httpClient.<Optional<String>>execute(get, validateGetUserAndGetResult,localContext);

        if(!response.isPresent()){
            return Optional.empty();
        }else{
            return Optional.of(MAPPER.readValue(response.get(),User.class));
        }
    }

    private static ResponseHandler validateGetUserAndGetResult = new ResponseHandler<Optional<String>>() {
        @Override
        public Optional<String> handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
            int status = httpResponse.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_ACCEPTED){
                HttpEntity entity = httpResponse.getEntity();
                if(entity.getContentLength()==0){
                    return Optional.empty();
                }else{
                    return Optional.of(EntityUtils.toString(entity));
                }
            }else if(status == HttpStatus.SC_UNAUTHORIZED){
                throw new ClientProtocolException("The user is not registered, register it first!");
            }
            else {
                throw new ClientProtocolException("Unexpected response, is the is the server down?");
            }
        }
    };

    @Override
    public Optional<List<LoginAttempt>> listLoginAttempts(User user) throws IOException {
        final String resourceURL = base.toString() + "/loginAttempts/" + user.getId();
        final Gson gson = new Gson();

        //set the credentials for the request
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user.getUsername() + ":" + user.getPassword()));
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setCredentialsProvider(credentialsProvider);

        final HttpGet get = new HttpGet(resourceURL);

        final Optional<String> response = httpClient.<Optional<String>>execute(get, validateGetListOfAttemptsAndValidate, localContext);

        if (!response.isPresent()) {
            return Optional.empty();
        } else {
            return Optional.of(MAPPER.readValue(response.get(),new TypeReference<List<LoginAttempt>>(){}));
        }


    }
    private static ResponseHandler validateGetListOfAttemptsAndValidate = new ResponseHandler<Optional<String>>() {
        @Override
        public Optional<String> handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
            int status = httpResponse.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                if (entity.getContentLength() == 0) {
                    return Optional.empty();
                } else {
                    return Optional.of(EntityUtils.toString(entity));
                }
            } else if (status == HttpStatus.SC_UNAUTHORIZED) {
                throw new ClientProtocolException("The user is not authorized, login it first!");
            } else if (status == HttpStatus.SC_FORBIDDEN) {
                throw new ClientProtocolException("You are not authorized to get the views of another user!");
            } else {
                throw new ClientProtocolException("Unexpected response, is the server down?");
            }
        }
    };

    public void close() throws IOException {
        httpClient.close();
    }
}
