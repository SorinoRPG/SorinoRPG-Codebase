package game.fight;

import game.characters.Sorino;

import java.io.*;
import java.util.ArrayList;

public class Fight implements Serializable{
    public static class GameInfo implements Serializable{
        public String getWinner() {
            return winner;
        }

        public String getLoser() {
            return loser;
        }

        public String winner;
        public String loser;

        public GameInfo(String winner, String loser){
            this.winner = winner;
            this.loser = loser;
        }
    }
    public ArrayList<String> usersID = new ArrayList<>();
    public ArrayList<Sorino> fighters = new ArrayList<>();
    public ArrayList<Opponent> opponents = new ArrayList<>();
    public int phase2Calls = 0;
    public int currFighter = 0;

    public void saveFight(String idSum) {
         if(FightManager.fightMap.containsKey(idSum))
            FightManager.fightMap.replace(idSum, this);
         else FightManager.fightMap.put(idSum, this);
    }
    public static Fight readFight(String idSum) throws FightNotFoundException {
        if(!FightManager.fightMap.containsKey(idSum)) throw new FightNotFoundException();
        return FightManager.fightMap.get(idSum);
    }
    public void endFight(String idSum){
       FightManager.fightMap.remove(idSum);
    }

}
