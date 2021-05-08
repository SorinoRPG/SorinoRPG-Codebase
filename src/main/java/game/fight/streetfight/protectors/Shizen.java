package game.fight.streetfight.protectors;

import game.characters.Sorino;
import game.characters.nature.Nature;
import game.fight.Move;
import game.fight.streetfight.StreetProtector;

import java.util.List;
import java.util.Optional;

public class Shizen implements StreetProtector {

    @Override
    public String getName() {
        return "Shizen";
    }

    @Override
    public String getStreetName() {
        return "Honihola Street";
    }

    @Override
    public String getSarcasticRemark() {
        return "A challenger, very well, prepare your doom.";
    }

    @Override
    public String getWinningRemark() {
        return "Puny. Don't waste my time again, I have a Street to protect.";
    }

    @Override
    public String getLosingRemark() {
        return "Well done, not many defeat my Guardian Sorino, you can have it.";
    }

    public static class HoniholaGuardian extends Nature implements Sorino {
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
            return sorino.equalsIgnoreCase("HoniholaGuardian") ? Optional.of(this) : Optional.empty();
        }

        @Override
        public double getIfWeakness(Sorino sorino) {
            return super.getIfWeakness(sorino);
        }

        private final Move honiHold = new Move(64, "Squeezed the opponent!",
                false, 30,
                "https://cdn.discordapp.com/attachments/803353528088133632/803353838533869636/R666c7d702f00ad803b4c408ccd045e48.png");
        @Override
        public Move customNatureMove(Sorino initiator) {
            honiHold.addSorino(initiator);
            return honiHold;
        }

        @Override
        public List<String> getMoves() {
            List<String> moves = super.getMovesAbs();
            moves.add("Honi Hold");
            return moves;
        }

        @Override
        public Optional<Move> getMove(String move, Sorino initiator) {
            switch(move.toUpperCase()){
                case "HONI": return Optional.ofNullable(customNatureMove(initiator));
                case "POISON": return Optional.ofNullable(super.poisonIvy(initiator));
                case "GROW": return Optional.ofNullable(super.grow(initiator));
                case "UPROOT": return Optional.ofNullable(super.uproot(initiator));
                default: return Optional.empty();
            }
        }


        @Override
        public String getName() {
            return "HoniholaGuardian: Nature-Guardian type | Guardian";
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    @Override
    public Sorino getGuardianSorino() {
        return new HoniholaGuardian();
    }
}
