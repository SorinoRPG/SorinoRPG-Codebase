package game.heist.availableheists.traincatch;

import game.heist.GetStage;
import game.heist.Heist;
import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class TrainCatch implements Heist {
    String leaderID;
    ArrayList<String> allID = new ArrayList<>();
    int stage = 0;
    long lastTime = System.currentTimeMillis();

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
        return "Train Catch";
    }

    @Override
    public String getType() {
        return "Stealth";
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
    public MessageEmbed heistState() {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle(getName());
        embed.setDescription(getAllID().size() + "/4 players\n\n" +
                getDesc());
        embed.setColor(0x000dff);

        embed.setAuthor("Enter .HSTART to begin this heist!");
        embed.setFooter("Enter .HJOIN to join this heist!");
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
    public String getDesc() {
        return "**Jessica** Let's get straight to the point. A train is coming full of precious cargo, " +
                "we need to parachute onto the main cargo hold, take that cargo and set explosives on the " +
                "front cab without anyone noticing ok? Good. I ain't paying for the gear " +
                "too.";
    }

    @Override
    public int setupCost() {
        return 10000;
    }

    @Override
    public int payout() {
        return 50000;
    }

    @Override
    public int bail() {
        return 600;
    }

    @Override
    public void saveHeist(String guildID, String channelID) throws IOException {
        ObjectOutputStream objectOutputStream =
                new ObjectOutputStream(
                        new FileOutputStream(
                                new File("/db/" + guildID + "/heists/%" + channelID + ".txt"))
                );
        objectOutputStream.flush();
        objectOutputStream.writeObject(this);
        objectOutputStream.close();

        lastTime = System.currentTimeMillis();
    }

    @Override
    public long time() {
        return lastTime;
    }
}
