package tools.workWithServer;

import tools.exceptions.UserWithSuchEmailExists;

import java.io.IOException;

public class RegisterRequest {
    public static void registerUser(String email, String password) throws UserWithSuchEmailExists, IOException {

        //Send email to check if such one exists.
        //if exists, throw exception
        //if not exists, pass params to WorkWithServer.registerUser() and register user
        if(WorkWithServer.checkIfUserExists(email)) throw new UserWithSuchEmailExists("Such user exists");
        WorkWithServer.registerUser(email,password);
    }
}
