package game.fight.streetfight.protectors;

import game.characters.Sorino;
import game.characters.element.Element;
import game.fight.Move;
import game.fight.streetfight.StreetProtector;

import java.util.List;
import java.util.Optional;

public class Soshu implements StreetProtector {

    @Override
    public String getName() {
        return "Soshu";
    }

    @Override
    public String getStreetName() {
        return "Ingosa Street";
    }

    @Override
    public String getSarcasticRemark() {
        return "We get people like you in Ingosa all the time, hurry up, don't waste my time.";
    }

    @Override
    public String getWinningRemark() {
        return "Another challenger, and another loser, come back when you actually have some skill";
    }

    @Override
    public String getLosingRemark() {
        return "Strong. Very well, take my Sorino.";
    }

    public static class IngosaGuardian extends Element implements Sorino {
        @Override
        public int getHealth(int level) {
            return 895;
        }

        @Override
        public int getEnergy(int level) {
            return 800;
        }

        @Override
        public int getRarity() {
            return 0;
        }

        @Override
        public Optional<Sorino> getSorino(String sorino) {
            return sorino.equalsIgnoreCase("IngosaGuardian") ? Optional.of(this) : Optional.empty();
        }

        @Override
        public double getIfWeakness(Sorino sorino) {
            return super.getIfWeakness(sorino);
        }

        //Recycle
        private final Move ingosaTumble = new Move(72, "Tumbled across the battle field",
                false, 35,
                "https://cdn.discordapp.com/attachments/803353528088133632/803354749637623828/rockfall-4.png");
        public Move customElementMove(Sorino initiator) {
            ingosaTumble.addSorino(initiator);
            return ingosaTumble;
        }

        @Override
        public List<String> getMoves() {
            List<String> moves = getMovesAbs();
            moves.add("Ingosa Tumble");
            return moves;
        }

        @Override
        public Optional<Move> getMove(String move, Sorino initiator) {
            switch (move.toUpperCase()){
                case "HARNESS": return Optional.ofNullable(harness(initiator));
                case "BURN": return Optional.ofNullable(burn(initiator));
                case "FREEZE": return Optional.ofNullable(freeze(initiator));
                case "INGOSA": return Optional.ofNullable(customElementMove(initiator));
                default: return Optional.empty();
            }
        }

        @Override
        public String getName() {
            return "IngosaGuardian: Element-Guardian type | Guardian";
        }

        @Override
        public String toString() {
            return getName();
        }
    }
    @Override
    public Sorino getGuardianSorino() {
        return new IngosaGuardian();
    }
}
