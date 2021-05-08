package game.fight.streetfight;

import game.characters.Sorino;
import game.fight.Fight;
import game.fight.FightManager;
import game.fight.FightNotFoundException;
import game.fight.Opponent;

import java.io.*;
import java.util.ArrayList;

public class StreetFight extends Fight implements Serializable{
    public StreetProtector protector;
    public ArrayList<Sorino> userSorino = new ArrayList<>();
    public ArrayList<Opponent> opponents = new ArrayList<>();

     public StreetFight(StreetProtector streetProtector){
         this.protector = streetProtector;
     }

    public void saveFight(String id) {
        if(FightManager.fightMap.containsKey(id))
            FightManager.fightMap.replace(id, this);
        else FightManager.fightMap.put(id, this);
    }
    public static StreetFight readFight(String id) throws FightNotFoundException {
        if(!FightManager.fightMap.containsKey(id)) throw new FightNotFoundException();
        return (StreetFight) FightManager.fightMap.get(id);
    }
    public void endFight(String idSum){
        FightManager.fightMap.remove(idSum);
    }

}
