package game.heist;

import game.heist.availableheists.artilleryessential.ArtilleryEssential;
import game.heist.availableheists.bluechip.BlueChip;
import game.heist.availableheists.casinorush.CasinoRush;
import game.heist.availableheists.codebreaker.Codebreaker;
import game.heist.availableheists.serverwipe.ServerWipe;
import game.heist.availableheists.traincatch.TrainCatch;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public interface Heist extends Serializable {
    String getName();
    String getType();
    String getLeaderID();
    String currentID();
    void setCurrentID(String id);
    List<String> getAllID();
    void setLeaderID(String ID);
    void addMember(String ID);
    MessageEmbed heistState();
    boolean isFull();
    Stage getCurrentStage();
    void incrementStage();
    String getDesc();
    int setupCost();
    long time();
    int payout();
    int bail();
    void saveHeist(String guildID, String channelID) throws IOException;



    interface GetHeist{
        Heist getHeist();
    }
    @SuppressWarnings("unused")
    enum HeistUtil implements GetHeist{
        BLUECHIP{
            @Override
            public Heist getHeist() {
                return new BlueChip();
            }
        },
        ARTILLERYESSENTIAL{
            @Override
            public Heist getHeist() {
                return new ArtilleryEssential();
            }
        },
        CASINORUSH{
            @Override
            public Heist getHeist() {
                return new CasinoRush();
            }
        },
        CODEBREAKER{
            @Override
            public Heist getHeist() {
                return new Codebreaker();
            }
        },
        SERVERWIPE{
            @Override
            public Heist getHeist() {
                return new ServerWipe();
            }
        },
        TRAINCATCH{
            @Override
            public Heist getHeist() {
                return new TrainCatch();
            }
        };

        public static boolean exists(String guildID, String channelID){
            if(new File("/db/" + guildID + "/heists").listFiles() == null) return false;
            ArrayList<File> heists =
                    new ArrayList<>(Arrays.asList(new File("/db/" + guildID + "/heists").listFiles()));
            File file = new File("/db/" + guildID + "/heists/%" + channelID + ".txt");

            return heists.contains(file);
        }
        public static Heist getCurrentHeist(String guildID, String channelID)
                throws HeistNotFoundException, IOException{
            Heist heist;

            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(
                        new FileInputStream(
                                new File("/db/" + guildID + "/heists/%" + channelID + ".txt")
                        )
                );

                heist = (Heist) objectInputStream.readObject();
                objectInputStream.close();
                return heist;
            } catch (FileNotFoundException | ClassNotFoundException e ){
                throw new HeistNotFoundException("Heist is not in folder!");
            }
        }
        public static boolean isHeist(String heistStr){
            ArrayList<HeistUtil> heists = new ArrayList<>(
                    EnumSet.allOf(HeistUtil.class)
            );

            for(HeistUtil heist : heists)
                if(heist.getHeist().getName().toUpperCase().equals(heistStr.toUpperCase()))
                    return true;
            return false;
        }
        public static List<Heist> getAll(){
            ArrayList<HeistUtil> heistUtils = new ArrayList<>(
                    EnumSet.allOf(HeistUtil.class)
            );

            List<Heist> heists = new ArrayList<>();
            for(HeistUtil heist : heistUtils)
                heists.add(heist.getHeist());
            return heists;
        }

        public static Heist getHeist(String heistStr) throws HeistNotFoundException{
            ArrayList<HeistUtil> heists = new ArrayList<>(
                    EnumSet.allOf(HeistUtil.class)
            );

            for(HeistUtil heist : heists){
                if(heist.getHeist().getName().toUpperCase().equals(heistStr.toUpperCase()))
                    return heist.getHeist();
            }
            throw new HeistNotFoundException("The name was incorrect!");
        }
    }
}
