package com.dw.userstatistics.health;

import com.codahale.metrics.health.HealthCheck;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

/**
 * Created by alberto on 2016-01-26.
 */
public class UserStatisticsHealthCheck extends HealthCheck {
    private final Client client;

    public UserStatisticsHealthCheck() {
        this.client = ClientBuilder.newClient();
    }

    @Override
    protected Result check() throws Exception {
        try{
            Response response = client.target("http://localhost:8080/").path("loginAttempts/1")
                    .request().get();
            if(response.getStatus()>=500){
                return Result.unhealthy("Login Attempts failed !");
            }
        }catch (Exception e){
            return Result.unhealthy(e);
        }finally {
            client.close();
        }
        return Result.healthy();
    }
}
