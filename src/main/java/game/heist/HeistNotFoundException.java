package game.heist;

public class HeistNotFoundException extends Exception{
    String channelID;

    HeistNotFoundException(String channelId){
        this.channelID = channelId;
    }

    public String getChannelID() {
        return channelID;
    }
}
