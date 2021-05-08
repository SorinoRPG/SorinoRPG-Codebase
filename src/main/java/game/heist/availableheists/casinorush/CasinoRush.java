package game.heist.availableheists.casinorush;

import game.heist.GetStage;
import game.heist.Heist;
import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class CasinoRush implements Heist {
    String leaderID;
    ArrayList<String> allID = new ArrayList<>();
    int stage = 0;

    @SuppressWarnings("unused")
    enum Stages implements GetStage {
        STAGE_1{
            @Override
            public Stage getStage() {
                return new Stage1();
            }
        },
        STAGE_2{
            @Override
            public Stage getStage() {
                return new Stage2();
            }
        },
        STAGE_3{
            @Override
            public Stage getStage() {
                return new Stage3();
            }
        },
        STAGE_4{
            @Override
            public Stage getStage() {
                return new Stage4();
            }
        },
        STAGE_5{
            @Override
            public Stage getStage() {
                return new Stage5();
            }
        },
        STAGE_6{
            @Override
            public Stage getStage() {
                return new Stage6();
            }
        },
        STAGE_7{
            @Override
            public Stage getStage() {
                return new Stage7();
            }
        },
        STAGE_8{
            @Override
            public Stage getStage() {
                return new Stage8();
            }
        };

        static List<Stage> getAsList(){
            ArrayList<Stages> stages = new ArrayList<>(EnumSet.allOf(Stages.class));

            List<Stage> realStages = new ArrayList<>();

            for(Stages stage : stages) realStages.add(stage.getStage());
            return realStages;
        }
    }

    @Override
    public String getName() {
        return "Casino Rush";
    }

    @Override
    public String getType() {
        return "Aggressive";
    }

    @Override
    public String getLeaderID() {
        return leaderID;
    }

    @Override
    public List<String> getAllID() {
        return allID;
    }

    @Override
    public void setLeaderID(String ID) {
        this.leaderID = ID;
    }
    String currentID;
    @Override
    public String currentID() {
        return currentID;
    }

    @Override
    public void setCurrentID (String id) {
        this.currentID = id;
    }

    @Override
    public void addMember(String ID) {
        allID.add(ID);
    }

    @Override
    public MessageEmbed heistState(String p) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle(getName());
        embed.setDescription(getAllID().size() + "/4 players\n\n" +
                getDesc());
        embed.setColor(0x000dff);

        embed.setAuthor("Enter " + p + "HSTART to begin this heist!");
        embed.setFooter("Enter " + p + "HJOIN to join this heist!");
        return embed.build();
    }

    @Override
    public boolean isFull() {
        return allID.size() == 4;
    }

    @Override
    public Stage getCurrentStage() {
        return Stages.getAsList().get(stage);
    }

    @Override
    public void incrementStage() {
        stage++;
    }

    @Override
    public String getDesc() {
        return "**Marcus** You may or may not know, I'm a pretty rich dude. But as they say " +
                "\"Let the rich get richer.\" There is a guy who owns a huge chain of Casino's. " +
                "Nobody knows his real name, so we just call him **Jax**. Essentially we need to " +
                "break into one of his Casino where he stores a huge red Sapphire. We're going to need " +
                "a pretty big arsenal to break into a place like that, pay me upfront, and let us make " +
                "some money!";

    }

    @Override
    public int levelBoundary() {
        return 20;
    }

    @Override
    public int setupCost() {
        return 50000;
    }

    @Override
    public int payout() {
        return 200000;
    }

    @Override
    public int bail() {
        return 10000;
    }

    @Override
    public void saveHeist(String channelID){
        if(!Heist.heistMap.containsKey(channelID))
            Heist.heistMap.put(channelID, this);
        else Heist.heistMap.replace(channelID, this);
    }

}
