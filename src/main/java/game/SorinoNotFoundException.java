package game;


public class SorinoNotFoundException extends Exception{
    String cause;

    public SorinoNotFoundException(String cause){
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "SorinoNotFoundException{" +
                "cause='" + cause + '\'' +
                '}';
    }
}
