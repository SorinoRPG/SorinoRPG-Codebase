package game.heist;

public class HeistNotFoundException extends Exception {
    String cause;

    public HeistNotFoundException(String cause){
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "HeistNotFoundException{" +
                "cause='" + cause + '\'' +
                '}';
    }
}
