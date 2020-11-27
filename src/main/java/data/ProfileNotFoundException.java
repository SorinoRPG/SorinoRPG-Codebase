package data;

public class ProfileNotFoundException extends Exception{
    private final String cause;

    ProfileNotFoundException(String cause){
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "Profile was not found because: " +
                cause;
    }
}
