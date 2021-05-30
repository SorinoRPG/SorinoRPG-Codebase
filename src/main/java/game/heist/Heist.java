package game.heist;

import data.Profile;
import data.ProfileNotFoundException;
import game.GameSaver;
import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.jodah.expiringmap.ExpiringMap;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public interface Heist {
    ExpiringMap<String, Heist> choiceCool = ExpiringMap.builder()
            .maxSize(10000000)
            .expiration(30, TimeUnit.MINUTES)
            .expirationListener((k, v) -> {
                String channelID = (String) k;
                Heist heist = (Heist) v;

                try {
                    Heist.HeistUtil.deleteHeist(channelID);
                } catch (HeistNotFoundException e){
                    return;
                }

                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(0x00dff);
                embed.setTitle("Too slow!");
                embed.setDescription("You didn't use an item quick enough, and the heist crumbled!" +
                        heist.heistStateString());
                embed.setFooter("Try to be quicker next time!");

                //noinspection ConstantConditions
                MainBot.getJda().getTextChannelById(channelID).sendMessage(embed.build()).queue();
            }).build();

    String getName();
    Type getType();
    String getDesc();
    Reward getReward();
    Stage getCurrentStage();
    void nextStage();
    void initialize(String channelID, Participant participant);
    void start() throws HeistStartupException;
    int getMaxPlayers();
    int getMinPlayers();
    int playerCount();
    ArrayList<Participant> getAllParticipants();
    void replaceParticipant(Participant former, Participant latter);
    void addParticipant(Participant participant);
    void removeParticipant(Participant participant);
    String getHeistLeaderID();
    Participant getParticipant(String userID);
    MessageEmbed heistState();
    String heistStateString();
    String getChannel();

    default void generateNewCut(){
        int participants = this.getAllParticipants().size();
        int equalCut = 100 / participants;

        for(Participant participant : this.getAllParticipants())
            this.replaceParticipant(participant, new Participant(participant.userID, participant.userName,
                    participant.heistPosition, equalCut));
    }

    default void save(){
        if(GameSaver.heistMap.containsKey(this.getChannel()))
            GameSaver.heistMap.replace(this.getChannel(), this);
        GameSaver.heistMap.put(this.getChannel(), this);
    }

    default void delete() throws HeistNotFoundException {
        if(GameSaver.heistMap.containsKey(this.getChannel()))
            GameSaver.heistMap.remove(this.getChannel(), this);
        throw new HeistNotFoundException("null");
    }

    default void reward(){
        try {
            for (Participant participant : this.getAllParticipants()) {
                Profile userProfile = participant.toProfile();
                this.getReward().award(userProfile);
                userProfile.recreate();
            }
            Profile leader = this.getParticipant(this.getHeistLeaderID()).toProfile();
            leader.setCoins(5000);
            leader.recreate();
        } catch (ProfileNotFoundException ignored) {}
    }

    interface GetHeist {
        Heist getHeist();
    }

    enum HeistUtil implements GetHeist{
        NULL(){
            @Override
            public Heist getHeist() {
                return null;
            }
        };

        public static void deleteHeist(String id) throws HeistNotFoundException {
            if(!GameSaver.exists(id)) throw new HeistNotFoundException(id);
            GameSaver.heistMap.remove(id);
        }

        public static ArrayList<Heist> getHeists(){
            ArrayList<Heist> heists = new ArrayList<>();
            for(HeistUtil heistUtil : EnumSet.allOf(HeistUtil.class))
                heists.add(heistUtil.getHeist());
            return heists;
        }

        public static Heist getHeist(String heist){
            for(Heist heist1 : getHeists())
                if(heist1.getName().equalsIgnoreCase(heist)) return heist1;
            return NULL.getHeist();
        }

        public static Heist getHeistById(String channelID) throws HeistNotFoundException {
            if(!GameSaver.heistMap.containsKey(channelID))
                throw new HeistNotFoundException(channelID);
            return GameSaver.heistMap.get(channelID);
        }

    }

}
