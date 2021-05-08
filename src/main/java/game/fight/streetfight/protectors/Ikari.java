package game.fight.streetfight.protectors;


import game.characters.Sorino;
import game.characters.rage.Rage;
import game.fight.Move;
import game.fight.streetfight.StreetProtector;

import java.util.List;
import java.util.Optional;

public class Ikari implements StreetProtector {

    @Override
    public String getName() {
        return "Ikari";
    }

    @Override
    public String getStreetName() {
        return "Sunishi Street";
    }

    @Override
    public String getSarcasticRemark() {
        return "You dare challenge me? You've already lost.";
    }

    @Override
    public String getWinningRemark() {
        return "Weak.";
    }

    @Override
    public String getLosingRemark() {
        return "Your Sorino is trained, take a Guardian for your journey.";
    }

    public static class SunishiGuardian extends Rage implements Sorino {
        @Override
        public int getHealth(int level) {
            return 940;
        }

        @Override
        public int getEnergy(int level) {
            return 780;
        }

        @Override
        public int getRarity() {
            return 0;
        }

        @Override
        public Optional<Sorino> getSorino(String sorino) {
            return sorino.equalsIgnoreCase("SunishiGuardian") ? Optional.of(this) : Optional.empty();
        }

        @Override
        public double getIfWeakness(Sorino sorino) {
            return super.getIfWeakness(sorino);
        }

        //Bundle Run
        private final Move sunishiSmash = new Move(72, "Smashes the battleground",
                false, 40,
                "https://cdn.discordapp.com/attachments/803353528088133632/803354758328614952/Rb8eb6d5bbe6987bbe94cae03683f90b5.png");
        @Override
        public Move customRageMove(Sorino initiator) {
            sunishiSmash.addSorino(initiator);
            return sunishiSmash;
        }

        @Override
        public List<String> getMoves() {
            List<String> moves = super.getMovesAbs();
            moves.add("Sunishi Smash");
            return moves;
        }

        @Override
        public Optional<Move> getMove(String move, Sorino initiator) {
            switch(move.toUpperCase()){
                case "SUNISHI": return Optional.ofNullable(customRageMove(initiator));
                case "GOUGE": return Optional.ofNullable(super.gouge(initiator));
                case "BALLISTIC": return Optional.ofNullable(super.ballistic(initiator));
                case "CHARGE": return Optional.ofNullable(super.charge(initiator));
                default: return Optional.empty();
            }

        }

        @Override
        public String getName() {
            return "SunishiGuardian: Rage-Guardian type | Guardian";
        }

        @Override
        public String toString() {
            return getName();
        }
    }
    @Override
    public Sorino getGuardianSorino() {
        return new SunishiGuardian();
    }
}
