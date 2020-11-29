package game.characters;


import data.Profile;
import data.ProfileNotFoundException;
import data.files.Logger;
import game.SorinoNotFoundException;
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

public interface Sorino extends Serializable{
    int getHealth(int level);
    int getEnergy(int level);
    int getRarity();
    Optional<Sorino> getSorino(String sorino);
    String toString();
    double getIfWeakness(Sorino sorino);
    List<String> getMoves();
    Optional<Move> getMove(String move, Sorino initiator);
    List<Move> getAllMoves();
    String getName();

    class DeadSorino {
        private final Sorino sorino;
        Opponent opponent;

        public DeadSorino(Sorino sorino, Opponent opponent){
            this.sorino = sorino;
            this.opponent = opponent;
        }

        public static List<Sorino> asSorino(List<DeadSorino> deadSorinos){
            ArrayList<Sorino> sorinoArrayList = new ArrayList<>();

            for (DeadSorino deadSorino : deadSorinos)
                sorinoArrayList.add(deadSorino.sorino);
            return sorinoArrayList;
        }
    }

    interface GetSorino {
        Sorino getSorino();
    }

    @SuppressWarnings("unused")
    enum AllSorino implements GetSorino {
        GRAY {
            @Override
            public Sorino getSorino() {
                return new Gray();
            }
        },
        CALKANOR {
            @Override
            public Sorino getSorino() {
                return new Calkanor();
            }
        },
        LOGITO {
            @Override
            public Sorino getSorino() {
                return new Logito();
            }
        },
        ZOOKRATAR {
            @Override
            public Sorino getSorino() {
                return new Zookratar();
            }
        },
        RAVAGER {
            @Override
            public Sorino getSorino() {
                return new Ravager();
            }
        },
        IMPALER {
            @Override
            public Sorino getSorino() {
                return new Impaler();
            }
        },
        BUNDLEBEAST {
            @Override
            public Sorino getSorino() {
                return new Bundlebeast();
            }
        },
        AGRESASOAR {
            @Override
            public Sorino getSorino() {
                return new Agresasoar();
            }
        },
        WILDILIO {
            @Override
            public Sorino getSorino() {
                return new Wildilio();
            }
        },
        TRAOLTER {
            @Override
            public Sorino getSorino() {
                return new Traolter();
            }
        },
        IMPRADA {
            @Override
            public Sorino getSorino() {
                return new Imprada();
            }
        },
        PATRAKA {
            @Override
            public Sorino getSorino() {
                return new Patraka();
            }
        },
        QUAINTUS {
            @Override
            public Sorino getSorino() {
                return new Quaintus();
            }
        },
        RUNDASOAR {
            @Override
            public Sorino getSorino() {
                return new Rundasoar();
            }
        },
        DEMENTEDBONE {
            @Override
            public Sorino getSorino() {
                return new DementedBone();
            }
        },
        DENTEDDEMENTED {
            @Override
            public Sorino getSorino() {
                return new DentedDemented();
            }
        },
        DISEASERIDDENBACKHEAD{
            @Override
            public Sorino getSorino() {
                return new DiseaseRiddenBackHead();
            }
        };
        public static boolean isIgnatiamon(String command){
            command = command.replace("-=", "");
            List<AllSorino> ignatiamonList
                    = new ArrayList<>(EnumSet.allOf(AllSorino.class));
            for (AllSorino ignatiamon : ignatiamonList) {
                if(ignatiamon.getSorino().getSorino(command).isPresent())
                    return true;
            }
            return false;
        }

        public static Sorino getOP(String op) throws SorinoNotFoundException {
            List<Sorino> opSorino =
                    new ArrayList<>(Arrays.asList(DENTEDDEMENTED.getSorino(),
                            DISEASERIDDENBACKHEAD.getSorino(),
                            DEMENTEDBONE.getSorino()));
            for (Sorino sorino :
                    opSorino) {
                if(sorino.getSorino(op).isPresent())
                    return sorino;
            }
            throw new SorinoNotFoundException("String was incorrect") ;
        }
        public static Sorino getIgnatiamon(String ig) throws SorinoNotFoundException {
            List<AllSorino> opIgnatiamon =
                    new ArrayList<>(EnumSet.allOf(AllSorino.class));
            for (AllSorino ignatiamon:
                    opIgnatiamon) {
                if(ignatiamon.getSorino().getSorino(ig).isPresent())
                    return ignatiamon.getSorino();
            }
            throw new SorinoNotFoundException("String was incorrect");
        }
        public static Sorino getRandom(GuildMessageReceivedEvent event){
            ArrayList<AllSorino> ignatiamon = new ArrayList<>(EnumSet.allOf(AllSorino.class));
            ArrayList<Sorino> randomSorino = new ArrayList<>();

            for (AllSorino allSorino : ignatiamon) {
                Sorino currSorino = allSorino.getSorino();
                try {
                    if (Profile.getProfile(event).getSorinoAsList().contains(currSorino))
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
                    } catch (IOException excI){
                        event.getChannel().sendMessage(
                                "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                        Logger.exceptionAsString(excI)
                        ).queue();
                    }
                }
                for(int i = 0; i < currSorino.getRarity(); i++)
                    randomSorino.add(currSorino);
            }

            return randomSorino.get(new Random().nextInt(randomSorino.size()));
        }
    }
}
