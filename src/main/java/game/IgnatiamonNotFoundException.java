package game;


public class IgnatiamonNotFoundException extends Exception{
    String cause;

    public IgnatiamonNotFoundException(String cause){
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "Ignatiamon! tried to attempt to find an Ignatiamon but failed because \n" +
                cause;
    }
}
