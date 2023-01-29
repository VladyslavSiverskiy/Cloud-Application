package tools.workWithServer;

import tools.exceptions.LoginException;

import java.io.IOException;


public class LoginRequest {

    public static String loginUser(String email, String password) throws LoginException, IOException {

        /* send server login command, then send username and password server accept it
        * return string with username if email and password are correct
        * if no such user, return "FALSE" and throw LoginException
        */
        String result = WorkWithServer.loginUser(email, password);
        if(result.equals("FALSE")){
            throw new LoginException("You`ve entered wrong email or password");
        }else{
            return result;
        }
    }
}
