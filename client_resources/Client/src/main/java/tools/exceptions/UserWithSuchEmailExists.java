package tools.exceptions;

public class UserWithSuchEmailExists extends Exception{
    public UserWithSuchEmailExists(String message){
        super(message);
    }
}
