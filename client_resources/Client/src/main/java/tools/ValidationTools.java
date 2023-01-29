package tools;

public class ValidationTools {

    private static ValidationTools validationTools;

    private ValidationTools(){

    }

    public static ValidationTools getValidationTools() {
        if(validationTools == null){
            validationTools = new ValidationTools();
        }
        return validationTools;
    }

    public boolean validateEmailField(String email){
        return email.matches("^(.+)@(\\S+)$");
    }
    public boolean validatePassword(String password){
        return password.matches("^(?=.*[a-z]).{8,20}$");
    }

    /**
     * Check file or folder
     * */
    public boolean validateFileName(String fileName){
        return fileName.matches("[A-Z .()a-zА-Яа-я\\d]+\\.\\w+");
    }

    public boolean validateFolderName(String folderName){
        return folderName.matches(".{4,15}");
    }
}
