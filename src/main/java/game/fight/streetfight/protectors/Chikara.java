package game.fight.streetfight.protectors;

import game.characters.Sorino;
import game.characters.rage.Rage;
import game.fight.Move;
import game.fight.streetfight.StreetProtector;

import java.util.List;
import java.util.Optional;

public class Chikara implements StreetProtector {
    @Override
    public String getName() {
        return "Chikara";
    }

    @Override
    public String getStreetName() {
        return "Kariniro Street";
    }

    @Override
    public String getSarcasticRemark() {
        return "You better be good.";
    }

    @Override
    public String getWinningRemark() {
        return "What a waste of time.";
    }

    @Override
    public String getLosingRemark() {
        return "I haven't lost in a long time, take my Sorino as a gift.";
    }

    public static class KariniroGuardian extends Rage implements Sorino {
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
            return sorino.equalsIgnoreCase("KariniroGuardian") ? Optional.of(this) : Optional.empty();
        }

        @Override
        public double getIfWeakness(Sorino sorino) {
            return super.getIfWeakness(sorino);
        }

        //Bundle Run
        private final Move kariniroBlock = new Move(0.3, "Performs a powerful block!",
                true, 40,
                "https://cdn.discordapp.com/attachments/803353528088133632/803360864841236520/th.png");
        @Override
        public Move customRageMove(Sorino initiator) {
            kariniroBlock.addSorino(initiator);
            return kariniroBlock;
        }

        @Override
        public List<String> getMoves() {
            List<String> moves = super.getMovesAbs();
            moves.add("Kariniro Block");
            return moves;
        }

        @Override
        public Optional<Move> getMove(String move, Sorino initiator) {
            switch(move.toUpperCase()){
                case "KARINIRO": return Optional.ofNullable(customRageMove(initiator));
                case "GOUGE": return Optional.ofNullable(super.gouge(initiator));
                case "BALLISTIC": return Optional.ofNullable(super.ballistic(initiator));
                case "CHARGE": return Optional.ofNullable(super.charge(initiator));
                default: return Optional.empty();
            }
        }

        @Override
        public String getName() {
            return "KariniroGuardian: Rage-Guardian type | Guardian";
        }

        @Override
        public String toString() {
            return getName();
        }
    }
    @Override
    public Sorino getGuardianSorino() {
        return new KariniroGuardian();
    }
}
