package game.fight;

import game.characters.Sorino;
import net.dv8tion.jda.api.entities.User;

import java.io.*;
import java.util.ArrayList;

public class Fight implements Serializable{
    public static class GameInfo {
        public User getWinner() {
            return winner;
        }

        public User getLoser() {
            return loser;
        }

        public User winner;
        public User loser;

        public GameInfo(User winner, User loser){
            this.winner = winner;
            this.loser = loser;
        }


    }
    public ArrayList<User> users = new ArrayList<>();
    public ArrayList<Sorino> fighters = new ArrayList<>();
    public ArrayList<Opponent> opponents = new ArrayList<>();
    int phase2Calls = 0;
    public int currFighter = 0;


    public void saveFight(String guildID, String idSum) throws IOException {
         ObjectOutputStream objectStream =
                new ObjectOutputStream(new FileOutputStream(
                        new File("C:\\Users\\Emman\\" +
                                "IdeaProjects\\SorinoRPG\\" +
                                "SorinoRPG-Codebase\\src\\" +
                                "main\\java\\data\\files\\" + guildID + "\\fights\\" + idSum + ".txt")
                ));
         objectStream.writeObject(this);
         objectStream.close();
    }
    public static Fight readFight(String guildID, String idSum) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream =
                new ObjectInputStream(new FileInputStream(
                        new File("C:\\Users\\Emman\\" +
                                "IdeaProjects\\SorinoRPG\\" +
                                "SorinoRPG-Codebase\\src\\" +
                                "main\\java\\data\\files\\" + guildID + "\\fights\\" + idSum + ".txt")
                ));
        Fight fight =  (Fight) objectInputStream.readObject();
        objectInputStream.close();
        return fight;
    }
    public boolean endFight(String guildID, String idSum){
        return new File("C:\\Users\\Emman\\" +
                "IdeaProjects\\SorinoRPG\\" +
                "SorinoRPG-Codebase\\src\\" +
                "main\\java\\data\\files\\" + guildID + "\\fights\\" + idSum + ".txt").delete();
    }

}

/*

UserAction info = event1 -> {
              EmbedBuilder embed  = new EmbedBuilder();
              embed.setTitle("Health of the Fighters!");

              embed.addField(fight.users.get(0).getName() + "'s Health \t\t\t ",
                      "HEALTH: " + fight.opponents.get(0).getHealth() + "\n" +
                              "ENERGY: " + fight.opponents.get(0).getEnergy() + "\n" +
                              "DEFENCE: " + fight.opponents.get(0).getDecrease() + " drop-off",
                      true);
              embed.addField(fight.users.get(1).getName() + "'s Health",
                      "HEALTH: " + fight.opponents.get(1).getHealth() + "\n" +
                              "ENERGY: " + fight.opponents.get(1).getEnergy() + "\n" +
                              "DEFENCE: " + fight.opponents.get(1).getDecrease() + " drop-off",
                      true);

              event1.getChannel().sendMessage(embed.build()).queue();
        };
 */
