package game.characters;

import game.SorinoNotFoundException;
import game.characters.element.*;
import game.characters.nature.*;
import game.characters.rage.*;
import game.characters.smart.*;
import game.characters.starter.Gray;
import game.fight.Move;
import game.fight.Opponent;

import java.io.Serializable;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;

public interface Sorino extends Serializable {
    int getHealth(int level);
    int getEnergy(int level);
    int getRarity();
    Optional<Sorino> getSorino(String sorino);
    String toString();
    double getIfWeakness(Sorino sorino);
    List<String> getMoves();
    Optional<Move> getMove(String move, Sorino initiator);
    String getName();


    @SuppressWarnings("unused")
    class DeadSorino {
        private final Sorino sorino;
        Opponent opponent;

        public DeadSorino(Sorino sorino, Opponent opponent){
            this.sorino = sorino;
            this.opponent = opponent;
        }

        public static List<Sorino> asSorino(List<DeadSorino> deadSorino){
            ArrayList<Sorino> sorinoArrayList = new ArrayList<>();

            for (DeadSorino dead_Sorino : deadSorino)
                sorinoArrayList.add(dead_Sorino.sorino);
            return sorinoArrayList;
        }
    }

   interface GetSorino {
        Sorino getSorino();
    }

    @SuppressWarnings("unused")
    enum AllSorino implements GetSorino {
        PATREONA {
            @Override
            public Sorino getSorino() {
                return new Patreona();
            }
        },
        ELEKTRA {
            @Override
            public Sorino getSorino() {
                return new Elektra();
            }
        },
        QUAKE {
            @Override
            public Sorino getSorino() {
                return new Quake();
            }
        },
        EVERGREEN {
            @Override
            public Sorino getSorino() {
                return new Evergreen();
            }
        },
        EWE {
            @Override
            public Sorino getSorino() {
                return new Ewe();
            }
        },
        PLANTIA {
            @Override
            public Sorino getSorino() {
                return new Plantia();
            }
        },
        URWALD {
            @Override
            public Sorino getSorino() {
                return new Urwald();
            }
        },
        DESTRUKTO {
            @Override
            public Sorino getSorino() {
                return new Destrukto();
            }
        },
        REEGAN {
            @Override
            public Sorino getSorino() {
                return new Reegan();
            }
        },
        IMORTOR {
            @Override
            public Sorino getSorino() {
                return new Imortor();
            }
        },
        DELTA {
            @Override
            public Sorino getSorino() {
                return new Delta();
            }
        },
        INTELLEKTA {
            @Override
            public Sorino getSorino() {
                return new Intellekta();
            }
        },
        ALPHA {
            @Override
            public Sorino getSorino() {
                return new Alpha();
            }
        },
        OMEGA {
            @Override
            public Sorino getSorino() {
                return new Omega();
            }
        },
        ANNO {
            @Override
            public Sorino getSorino() {
                return new Anno();
            }
        },
        EGOTIST {
            @Override
            public Sorino getSorino() {
                return new Egotist();
            }
        },
        INTESAIR {
            @Override
            public Sorino getSorino() {
                return new Intesair();
            }
        },
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
        };
        public static boolean isSorino(String command){
            command = command.replace("-=", "");
            List<AllSorino> sorinoList
                    = new ArrayList<>(EnumSet.allOf(AllSorino.class));
            for (AllSorino sorino : sorinoList) {
                if(sorino.getSorino().getSorino(command).isPresent())
                    return true;
            }
            return false;
        }
        public static Sorino getSorino(String sor) throws SorinoNotFoundException {
            List<AllSorino> opSorino =
                    new ArrayList<>(EnumSet.allOf(AllSorino.class));
            for (AllSorino sorino:
                    opSorino) {
                if(sorino.getSorino().getSorino(sor).isPresent())
                    return sorino.getSorino();
            }
            throw new SorinoNotFoundException("String was incorrect");
        }
        public static Sorino getRandom(){
            ArrayList<AllSorino> sorino = new ArrayList<>(EnumSet.allOf(AllSorino.class));
            ArrayList<Sorino> randomSorino = new ArrayList<>();

            for (AllSorino allSorino : sorino) {
                Sorino currSorino = allSorino.getSorino();
                for(int i = 0; i < currSorino.getRarity(); i++)
                    randomSorino.add(currSorino);
            }

            return randomSorino.get(new Random().nextInt(randomSorino.size()));
        }

        public static ArrayList<String> listToStr(ArrayList<Sorino> sorino){
            ArrayList<String> strings = new ArrayList<>();

            for (Sorino sor : sorino)
                strings.add(sor.getName());
            return strings;
        }
        public static ArrayList<Sorino> strToList(ArrayList<String> strings){
            ArrayList<Sorino> sorino = new ArrayList<>();


            for(String str : strings){
                try {
                    sorino.add(getSorino(str.substring(0, str.indexOf(":"))));
                } catch (Exception e){
                    break;
                }
            }
            return sorino;
        }
    }
}
