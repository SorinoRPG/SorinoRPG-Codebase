package game.fight.streetfight.protectors;

import game.characters.Sorino;
import game.characters.rage.Rage;
import game.fight.Move;
import game.fight.streetfight.StreetProtector;

import java.util.List;
import java.util.Optional;

public class Pewtini implements StreetProtector {
    @Override
    public String getName() {
        return "Pewtini";
    }

    @Override
    public String getStreetName() {
        return "Shouoko Street";
    }

    @Override
    public String getSarcasticRemark() {
        return "Your loss is imminent";
    }

    @Override
    public String getWinningRemark() {
        return "I saw it coming.";
    }

    @Override
    public String getLosingRemark() {
        return "Lucky";
    }

    @Override
    public Sorino getGuardianSorino() {
        return new ShouokoGuardian();
    }

    public static class ShouokoGuardian extends Rage implements Sorino {
        @Override
        public int getHealth(int level) {
            return 1540 + (level * 10);
        }

        @Override
        public int getEnergy(int level) {
            return 900 + (level * 10);
        }

        @Override
        public int getRarity() {
            return 0;
        }

        @Override
        public Optional<Sorino> getSorino(String sorino) {
            return sorino.equalsIgnoreCase("ShuokoGuardian") ? Optional.of(this) : Optional.empty();
        }

        @Override
        public double getIfWeakness(Sorino sorino) {
            return super.getIfWeakness(sorino);
        }

        //Bundle Run
        private final Move dominate = new Move(143, "Dominates the opponent!",
                false, 55,
                "https://media.discordapp.net/attachments/803353528088133632/807657481643229204/2808.png?width=498&height=498");
        @Override
        public Move customRageMove(Sorino initiator) {
            dominate.addSorino(initiator);
            return dominate;
        }

        @Override
        public List<String> getMoves() {
            List<String> moves = super.getMovesAbs();
            moves.add("Dominate");
            return moves;
        }

        @Override
        public Optional<Move> getMove(String move, Sorino initiator) {
            switch(move.toUpperCase()){
                case "DOMINATE": return Optional.ofNullable(customRageMove(initiator));
                case "GOUGE": return Optional.ofNullable(super.gouge(initiator));
                case "BALLISTIC": return Optional.ofNullable(super.ballistic(initiator));
                case "CHARGE": return Optional.ofNullable(super.charge(initiator));
                default: return Optional.empty();
            }
        }

        @Override
        public String getName() {
            return "ShuokoGuardian: Rage-Guardian type | Lost";
        }

        @Override
        public String toString() {
            return getName();
        }
    }

}


