package game.fight.streetfight.protectors;

import game.characters.Sorino;
import game.characters.nature.Nature;
import game.fight.Move;
import game.fight.streetfight.StreetProtector;

import java.util.List;
import java.util.Optional;

public class Tamotsu implements StreetProtector {

    @Override
    public String getName() {
        return "Tamotsu";
    }

    @Override
    public String getStreetName() {
        return "Tamushiri Street";
    }

    @Override
    public String getSarcasticRemark() {
        return "Roses are red, Violets are blue, Your Sorino are weak, Prepare for your doom.";
    }

    @Override
    public String getWinningRemark() {
        return "Get out of my sight, come back when you've trained.";
    }

    @Override
    public String getLosingRemark() {
        return "Here, take my Guardian Sorino, it'll help you along the way";
    }

    public static class TamushiriGuardian extends Nature implements Sorino{
        @Override
        public int getHealth(int level) {
            return 885;
        }

        @Override
        public int getEnergy(int level) {
            return 720;
        }

        @Override
        public int getRarity() {
            return 0;
        }

        @Override
        public Optional<Sorino> getSorino(String sorino) {
            return sorino.equalsIgnoreCase("TamushiriGuardian") ? Optional.of(this) : Optional.empty();
        }

        @Override
        public double getIfWeakness(Sorino sorino) {
            return super.getIfWeakness(sorino);
        }

        private final Move tamuVine = new Move(100, "Squeezes opponent and battleground in vines!",
                false, 60,
                "https://cdn.discordapp.com/attachments/803353528088133632/803360861376741386/th.png");
        @Override
        public Move customNatureMove(Sorino initiator) {
            tamuVine.addSorino(initiator);
            return tamuVine;
        }

        @Override
        public List<String> getMoves() {
            List<String> moves = super.getMovesAbs();
            moves.add("Tamu Vine");
            return moves;
        }

        @Override
        public Optional<Move> getMove(String move, Sorino initiator) {
            switch(move.toUpperCase()){
                case "TAMU": return Optional.ofNullable(customNatureMove(initiator));
                case "POISON": return Optional.ofNullable(super.poisonIvy(initiator));
                case "GROW": return Optional.ofNullable(super.grow(initiator));
                case "UPROOT": return Optional.ofNullable(super.uproot(initiator));
                default: return Optional.empty();
            }
        }


        @Override
        public String getName() {
            return "TamushiriGuardian: Nature-Guardian type | Guardian";
        }

        @Override
        public String toString() {
            return getName();
        }
    }
    @Override
    public Sorino getGuardianSorino() {
        return new TamushiriGuardian();
    }
}
