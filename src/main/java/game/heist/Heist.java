package game.heist;

import game.heist.availableheists.artilleryessential.ArtilleryEssential;
import game.heist.availableheists.bluechip.BlueChip;
import game.heist.availableheists.casinorush.CasinoRush;
import game.heist.availableheists.codebreaker.Codebreaker;
import game.heist.availableheists.serverwipe.ServerWipe;
import game.heist.availableheists.traincatch.TrainCatch;
import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.jodah.expiringmap.ExpiringMap;

import java.io.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface Heist extends Serializable {
    ExpiringMap<String, Heist> heistMap = ExpiringMap.builder()
            .maxSize(10000000)
            .expiration(1, TimeUnit.HOURS)
            .expirationListener((k, v) -> {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(0x00dff);
                embed.setTitle("This heist has gone on for 1 hour.");
                embed.setDescription("The heist in this channel has gone on too long, therefore it was deleted.");
                embed.setFooter("All user-data will stay as it was before the heist.");

                //noinspection ConstantConditions
                MainBot.getJda().getTextChannelById((String) k).sendMessage(embed.build()).queue();
            }).build();


    String getName();
    String getType();
    int levelBoundary();
    String getLeaderID();
    String currentID();
    void setCurrentID(String id);
    List<String> getAllID();
    void setLeaderID(String ID);
    void addMember(String ID);
    MessageEmbed heistState(String p);
    boolean isFull();
    Stage getCurrentStage();
    void incrementStage();
    String getDesc();
    int setupCost();
    int payout();
    int bail();
    void saveHeist(String channelID);



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

        public static boolean exists(String channelID){
            return heistMap.containsKey(channelID);
        }
        public static Heist getCurrentHeist(String channelID)
                throws HeistNotFoundException{
            if(!heistMap.containsKey(channelID)) throw new HeistNotFoundException("Heist was not in map!");
            return heistMap.get(channelID);
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
