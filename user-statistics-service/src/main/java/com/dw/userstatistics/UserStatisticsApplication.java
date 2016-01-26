package com.dw.userstatistics;

import com.dw.userstatistics.auth.UserAuthenticator;
import com.dw.userstatistics.db.api.LoginAttemptAPI;
import com.dw.userstatistics.db.api.UserAccessAPI;
import com.dw.userstatistics.db.dao.LoginAttemptDAO;
import com.dw.userstatistics.db.dao.UserDAO;
import com.dw.userstatistics.db.entities.LoginAttempt;
import com.dw.userstatistics.db.entities.User;
import com.dw.userstatistics.health.UserStatisticsHealthCheck;
import com.dw.userstatistics.resources.LoginAttemptsResource;
import com.dw.userstatistics.resources.LoginResource;
import com.dw.userstatistics.resources.UserRegistrationResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Alberto on 2016-01-19.
 */
public class UserStatisticsApplication extends Application<UserStatisticsConfiguration>{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserStatisticsApplication.class);

    public static void main(String[] args) throws Exception{
        new UserStatisticsApplication().run(args);
    }
    private final HibernateBundle<UserStatisticsConfiguration> hibernate =
            new HibernateBundle<UserStatisticsConfiguration>(User.class, LoginAttempt.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(UserStatisticsConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };


    @Override
    public void initialize(Bootstrap<UserStatisticsConfiguration> configurationBootstrap){
        configurationBootstrap.addBundle(hibernate);
    }
    @Override
    public void run(UserStatisticsConfiguration userStatisticsConfiguration, Environment environment) throws Exception {
        LOGGER.info("User Statistics service #run method called");
        SessionFactory sessionFactory = hibernate.getSessionFactory();
        final UserAccessAPI userAccessAPI = new UserDAO(sessionFactory);
        final LoginAttemptAPI loginAttemptAPI = new LoginAttemptDAO(sessionFactory);

        LOGGER.info("Database, initialized with hibernate");

        //healthchecks here!



        final UserRegistrationResource userRegistrationResource = new UserRegistrationResource(userAccessAPI);
        final LoginResource loginResource = new LoginResource(loginAttemptAPI);
        final LoginAttemptsResource loginAttemptsResource = new LoginAttemptsResource(loginAttemptAPI);

        environment.jersey().register(AuthFactory.binder(new BasicAuthFactory<>(new UserAuthenticator(userAccessAPI),
                "REALM_MESSAGE",User.class)));

        LOGGER.info("Wiring up the health checks");
        environment.healthChecks().register("Get Login Attempts", new UserStatisticsHealthCheck());

        LOGGER.info("Wiring up the resources to the application");
        //wiring here!

        environment.jersey().register(userRegistrationResource);
        environment.jersey().register(loginResource);
        environment.jersey().register(loginAttemptsResource);
    }
}
