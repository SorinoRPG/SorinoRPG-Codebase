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
    int phase2Calls = 0;
    public int currFighter = 0;


    public void saveFight(String guildID, String idSum) throws IOException {
         ObjectOutputStream objectStream =
                new ObjectOutputStream(new FileOutputStream(
                        new File("C:\\Users\\Emman\\" +
                                "IdeaProjects\\SorinoRPG\\SorinoRPG-Codebase\\src\\" +
                                "main\\java\\data\\files\\" + guildID + "\\fights\\" + idSum + ".txt")
                ));
         objectStream.writeObject(this);
         objectStream.close();
    }
    public static Fight readFight(String guildID, String idSum) throws FightNotFoundException {
        try {
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(new FileInputStream(
                            new File("C:\\Users\\Emman\\" +
                                    "IdeaProjects\\SorinoRPG\\SorinoRPG-Codebase\\src\\" +
                                    "main\\java\\data\\files\\" + guildID + "\\fights\\" + idSum + ".txt")
                    ));
            Fight fight = (Fight) objectInputStream.readObject();
            objectInputStream.close();

            return fight;
        } catch (IOException | ClassNotFoundException e){
            throw new FightNotFoundException("Fight was not created", e);
        }
    }
    public boolean endFight(String guildID, String idSum){
        return new File("C:\\Users\\Emman\\" +
                "IdeaProjects\\SorinoRPG\\src\\" +
                "main\\java\\data\\files\\" + guildID + "\\fights\\" + idSum + ".txt").delete();
    }

}
