package com.dw.userstatistics.client;

import com.dw.userstatistics.api.representations.LoginAttempt;
import com.dw.userstatistics.api.representations.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Created by alberto on 2016-01-25.
 */
public class UserLoginClientApp {

    public static UserLoginHttpClient client;
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static User user;
    private static boolean created;

    public static void main(String []args) throws Exception {

        Scanner keyboard = new Scanner(System.in);
        System.out.println("Welcome to make use of the REST client, please write the base URL with the port the " +
                "service is running:");
        String url = keyboard.nextLine();
        System.out.println("Now write the username to use for this session");
        String username = keyboard.nextLine();
        System.out.println("The password to use for the user");
        String password = keyboard.nextLine();
        client = new UserLoginHttpClient(new URI(url));
        String command="";
        while(!command.equals("exit")) {
            System.out.println("Thank you, now what do you want to do?");
            System.out.println("1. Register the user");
            System.out.println("2. Login the user");
            System.out.println("3. Get the list of login attempts for the user");
            System.out.println("To exit the application, write exit.");

            command = keyboard.nextLine();
            if(command.equals("exit")){
                continue;
            }else{
                int option = Integer.parseInt(command);
                if(option == 1){
                    registerUser(username, password);
                }else if(option ==2){
                    loginUser(username, password);
                }else if(option == 3){
                    getLoginAttempts();
                }
            }
        }
        client.close();
    }

    private static void getLoginAttempts() throws IOException {
        if(user!=null){
            Optional<List<LoginAttempt>> loginAttempts = client.listLoginAttempts(user);
            if(loginAttempts.isPresent()) {
                System.out.println(MAPPER.writeValueAsString(loginAttempts.get()));
            }else{
                System.out.println("Failed to get the login attempts from server");
            }
        }
    }

    private static void loginUser(String username, String password) throws IOException {
            Optional<User> loggedUser = client.loginUser(username, password);
            if(loggedUser.isPresent()){
                user = loggedUser.get();
                System.out.println("User has been successfully logged in");
                System.out.println(MAPPER.writeValueAsString(user));
            }else{
                System.out.println("Failed to login the user, is the user registered or is the service down?");
            }
    }

    private static void registerUser(String username, String password) throws Exception {
        //lets create a user
        created = client.registerNewUser(username,password);
        if(created){
            System.out.println("Registering the user");
        } else {
            System.out.println("User is already registered, not registering");
        }
    }
}
