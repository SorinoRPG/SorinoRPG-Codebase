package data;
// THIS CLASS IS NOT TO BE UPDATED!!

import game.Coins;
import game.characters.Sorino;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class ProfileStore implements Serializable {
    final ArrayList<Sorino> userSorino;
    final Coins coins;
    final String ID;
    final String name;
    final String imageUrl;
    final String guildID;
    int wins;
    int loses;
    int level;
    int xpLevelThresh;
    int xp;
    ArrayList<Object> additionalInfo;

    public ProfileStore(ArrayList<Sorino> sorino, Coins coins,
                   String id, String name, int wins, int loses, String imageUrl, String guildID, int xp,
                        int xpLevelThresh, int level, Object ... additionalInfo){
        this.userSorino = sorino;
        this.coins = coins;
        this.ID = id;
        this.name = name;
        this.wins = wins;
        this.loses = loses;
        this.imageUrl = imageUrl;
        this.guildID = guildID;
        this.level = level;
        this.xpLevelThresh = xpLevelThresh;
        this.xp = xp;
        this.additionalInfo = new ArrayList<>(Arrays.asList(additionalInfo));
    }
}
