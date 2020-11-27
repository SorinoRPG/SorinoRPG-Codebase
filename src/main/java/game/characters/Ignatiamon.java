package game.characters;


import data.Profile;
import data.ProfileNotFoundException;
import data.files.Logger;
import game.IgnatiamonNotFoundException;
import game.characters.element.*;
import game.characters.nature.*;
import game.characters.op.DementedBone;
import game.characters.op.DentedDemented;
import game.characters.op.DiseaseRiddenBackHead;
import game.characters.rage.*;
import game.characters.smart.*;
import game.characters.starter.Gray;
import game.fight.Move;
import game.fight.Opponent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.io.Serializable;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Arrays;
import java.util.Random;

public interface Ignatiamon extends Serializable{
    int getHealth();
    int getEnergy();
    int getRarity();
    Optional<Ignatiamon> getIgnatiamon(String ignatiamon);
    String toString();
    double getIfWeakness(Ignatiamon ignatiamon);
    List<String> getMoves();
    Optional<Move> getMove(String move, Ignatiamon initiator);
    List<Move> getAllMoves();
    String getName();

    class DeadIgnatiamon{
        private final Ignatiamon ignatiamon;
        Opponent opponent;

        public DeadIgnatiamon(Ignatiamon ignatiamon, Opponent opponent){
            this.ignatiamon = ignatiamon;
            this.opponent = opponent;
        }

        public static List<Ignatiamon> asIgnatiamon(List<DeadIgnatiamon> ignatiamonList){
            ArrayList<Ignatiamon> ignatiamonArrayList = new ArrayList<>();

            for (DeadIgnatiamon deadIgnatiamon : ignatiamonList)
                ignatiamonArrayList.add(deadIgnatiamon.ignatiamon);
            return ignatiamonArrayList;
        }
    }

    interface GetIgnatiamon{
        Ignatiamon getIgnatiamon();
    }

    @SuppressWarnings("unused")
    enum AllIgnatiamon implements GetIgnatiamon{
        GRAY {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new Gray();
            }
        },
        CALKANOR {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new Calkanor();
            }
        },
        LOGITO {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new Logito();
            }
        },
        ZOOKRATAR {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new Zookratar();
            }
        },
        RAVAGER {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new Ravager();
            }
        },
        IMPALER {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new Impaler();
            }
        },
        BUNDLEBEAST {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new Bundlebeast();
            }
        },
        AGRESASOAR {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new Agresasoar();
            }
        },
        WILDILIO {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new Wildilio();
            }
        },
        TRAOLTER {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new Traolter();
            }
        },
        IMPRADA {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new Imprada();
            }
        },
        PATRAKA {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new Patraka();
            }
        },
        QUAINTUS {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new Quaintus();
            }
        },
        RUNDASOAR {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new Rundasoar();
            }
        },
        DEMENTEDBONE {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new DementedBone();
            }
        },
        DENTEDDEMENTED {
            @Override
            public Ignatiamon getIgnatiamon() {
                return new DentedDemented();
            }
        },
        DISEASERIDDENBACKHEAD{
            @Override
            public Ignatiamon getIgnatiamon() {
                return new DiseaseRiddenBackHead();
            }
        };
        public static boolean isIgnatiamon(String command){
            command = command.replace("-=", "");
            List<AllIgnatiamon> ignatiamonList
                    = new ArrayList<>(EnumSet.allOf(AllIgnatiamon.class));
            for (AllIgnatiamon ignatiamon : ignatiamonList) {
                if(ignatiamon.getIgnatiamon().getIgnatiamon(command).isPresent())
                    return true;
            }
            return false;
        }

        public static Ignatiamon getOP(String op) throws IgnatiamonNotFoundException {
            List<Ignatiamon> opIgnatiamon =
                    new ArrayList<>(Arrays.asList(DENTEDDEMENTED.getIgnatiamon(),
                            DISEASERIDDENBACKHEAD.getIgnatiamon(),
                            DEMENTEDBONE.getIgnatiamon()));
            for (Ignatiamon ignatiamon:
                 opIgnatiamon) {
                if(ignatiamon.getIgnatiamon(op).isPresent())
                    return ignatiamon;
            }
            throw new IgnatiamonNotFoundException("String was incorrect") ;
        }
        public static Ignatiamon getIgnatiamon(String ig) throws IgnatiamonNotFoundException {
            List<AllIgnatiamon> opIgnatiamon =
                    new ArrayList<>(EnumSet.allOf(AllIgnatiamon.class));
            for (AllIgnatiamon ignatiamon:
                    opIgnatiamon) {
                if(ignatiamon.getIgnatiamon().getIgnatiamon(ig).isPresent())
                    return ignatiamon.getIgnatiamon();
            }
            throw new IgnatiamonNotFoundException("String was incorrect");
        }
        public static Ignatiamon getRandom(GuildMessageReceivedEvent event){
            ArrayList<AllIgnatiamon> ignatiamon = new ArrayList<>(EnumSet.allOf(AllIgnatiamon.class));
            ArrayList<Ignatiamon> randomIgnatiamon = new ArrayList<>();

            for (AllIgnatiamon allIgnatiamon : ignatiamon) {
                Ignatiamon currIgnatiamon = allIgnatiamon.getIgnatiamon();
                try {
                    if (Profile.getProfile(event).getIgnatiamonAsList().contains(currIgnatiamon))
                        continue;
                } catch (IOException | ClassNotFoundException e) {
                    Logger logger =
                            new Logger("Error in finding Profile due to IO and Classes \n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                            "Could not find profile due to IO and Classes "
                    ).queue();
                    try{
                        logger.logError();
                        logger.closeLogger();
                    } catch (IOException excI){
                        event.getChannel().sendMessage(
                                "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                        Logger.exceptionAsString(excI)
                        ).queue();
                    }
                } catch (ProfileNotFoundException e) {
                    Logger logger =
                            new Logger("Error in finding Profile \n" +
                                    Logger.exceptionAsString(e));

                    event.getChannel().sendMessage(
                            "Could not find profile!"
                    ).queue();
                    try{
                        logger.logError();
                        logger.closeLogger();
                    } catch (IOException excI){
                        event.getChannel().sendMessage(
                                "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                        Logger.exceptionAsString(excI)
                        ).queue();
                    }
                }
                for(int i = 0; i < currIgnatiamon.getRarity(); i++)
                    randomIgnatiamon.add(currIgnatiamon);
            }

            return randomIgnatiamon.get(new Random().nextInt(randomIgnatiamon.size()));
        }
    }
}
