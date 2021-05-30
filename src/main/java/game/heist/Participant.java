package game.heist;

import data.Profile;
import data.ProfileNotFoundException;
import net.dv8tion.jda.api.entities.User;

public class Participant {
    public String userID;
    public String userName;
    public HeistPosition heistPosition;
    public int cut;

    public Participant(User user, HeistPosition heistPosition, int cut){
        this.userID = user.getId();
        this.userName = user.getName();
        this.heistPosition = heistPosition;
        this.cut = cut;
    }

    public Participant(String userID, String username, HeistPosition heistPosition, int cut){
        this.userID = userID;
        this.userName = username;
        this.heistPosition = heistPosition;
        this.cut = cut;
    }

    public Profile toProfile(){
        try {
            return Profile.getProfile(userID);
        } catch (ProfileNotFoundException e) {
            return null;
        }
    }


    public enum HeistPosition { LEADER, PARTICIPANT}
}
