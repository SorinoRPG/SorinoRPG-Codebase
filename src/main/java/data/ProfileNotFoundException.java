package data;

public class ProfileNotFoundException extends Exception{
    private final String cause;

    public ProfileNotFoundException(){
        this.cause = " The user does not have a created profile";
    }

    @Override
    public String toString() {
        return "Profile was not found because: " +
                cause;
    }
}
