package game.heist;

public class HeistStartupException extends Exception{
    String reason;

    public HeistStartupException(String reason){
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
