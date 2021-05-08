package game.heist.availableheists.bluechip;

import game.heist.GetStage;
import game.heist.Heist;
import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class BlueChip implements Heist {
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
        return "Blue Chip";
    }

    @Override
    public String getType() {
        return "Swindle";
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
    public Stage getCurrentStage() {
        return Stages.getAsList().get(stage);
    }

    @Override
    public void incrementStage() {
        stage++;
    }

    @Override
    public String getDesc() {
        return "**Marcus** Look, I hate getting stolen from. I absolutely despise it. But I don't mind " +
                "stealing. A notable hedge fund has left the bank account details of all of their clients " +
                "on his computer. There are millions of dollars on that thing. I'm gonna need a ruthless " +
                "team to go in, retrieve that data, and leave. Of course, I'll be arming you with " +
                "military grade equipment, so I'll need to be paid first to buy the stuff, but soon, " +
                "you will all be rich men.";
    }

    @Override
    public int levelBoundary() {
        return 10;
    }

    @Override
    public int setupCost() {
        return 30000;
    }

    @Override
    public int payout() {
        return 100000;
    }

    @Override
    public int bail() {
        return 5000;
    }

    @Override
    public void saveHeist(String channelID){
        if(!Heist.heistMap.containsKey(channelID))
            Heist.heistMap.put(channelID, this);
        else Heist.heistMap.replace(channelID, this);
    }
}
