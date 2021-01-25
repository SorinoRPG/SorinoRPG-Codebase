package game.fight.streetfight;

import game.characters.Sorino;
import game.fight.FightNotFoundException;
import game.fight.Opponent;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class StreetFight implements Serializable{
    public StreetProtector protector;
    public ArrayList<Sorino> userSorino = new ArrayList<>();
    public ArrayList<Opponent> opponents = new ArrayList<>();
    public long fightTime = System.currentTimeMillis();

     public StreetFight(StreetProtector streetProtector){
         this.protector = streetProtector;
     }

    public void saveFight(String guildID, String id) throws IOException {
        ObjectOutputStream objectStream =
                new ObjectOutputStream(new FileOutputStream(
                        new File("/db/" + guildID + "/fights/&&" + id + ".txt")
                ));
        objectStream.writeObject(this);
        objectStream.close();
        fightTime = System.currentTimeMillis();
    }
    public static StreetFight readFight(String guildID, String id) throws FightNotFoundException {
        try {
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(new FileInputStream(
                            new File("/db/" + guildID + "/fights/&&" + id + ".txt")
                    ));
            StreetFight fight = (StreetFight) objectInputStream.readObject();
            objectInputStream.close();

            return fight;
        } catch (IOException | ClassNotFoundException e){
            throw new FightNotFoundException(e);
        }
    }
    public boolean endFight(String guildID, String idSum){
        return new File("C:\\db\\" + guildID + "\\fights\\&&" + idSum + ".txt").delete();
    }

}
