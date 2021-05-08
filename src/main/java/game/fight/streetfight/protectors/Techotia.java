package game.fight.streetfight.protectors;

import game.characters.Sorino;
import game.characters.element.Element;
import game.fight.Move;
import game.fight.streetfight.StreetProtector;

import java.util.List;
import java.util.Optional;

public class Techotia implements StreetProtector {

    @Override
    public String getName() {
        return "Techotia";
    }

    @Override
    public String getStreetName() {
        return "Technoville Steet";
    }

    @Override
    public String getSarcasticRemark() {
        return "My technology will crush you!";
    }

    @Override
    public String getWinningRemark() {
        return "Failures make me sick";
    }

    @Override
    public String getLosingRemark() {
        return "You got lucky";
    }

    @Override
    public Sorino getGuardianSorino() {
        return new TechnovilleGuardian();
    }
    public static class TechnovilleGuardian extends Element implements Sorino {
        @Override
        public int getHealth(int level) {
            return 1295 + (level * 10);
        }

        @Override
        public int getEnergy(int level) {
            return 1200 + (level * 10);
        }

        @Override
        public int getRarity() {
            return 0;
        }

        @Override
        public Optional<Sorino> getSorino(String sorino) {
            return sorino.equalsIgnoreCase("TechnovilleGuardian") ? Optional.of(this) : Optional.empty();
        }

        @Override
        public double getIfWeakness(Sorino sorino) {
            return super.getIfWeakness(sorino);
        }

        //Recycle
        private final Move steampunk = new Move(160, "Manipulates steampunk power!",
                false, 35,
                "https://media.discordapp.net/attachments/803353528088133632/807651865847791666/d4mvbp1-88bcd80e-42fc-423c-9ce0-ad4c2c0970b0.png?width=885&height=498");
        @Override
        public Move customElementMove(Sorino initiator) {
            steampunk.addSorino(initiator);
            return steampunk;
        }

        @Override
        public List<String> getMoves() {
            List<String> moves = getMovesAbs();
            moves.add("Steampunk");
            return moves;
        }

        @Override
        public Optional<Move> getMove(String move, Sorino initiator) {
            switch (move.toUpperCase()){
                case "HARNESS": return Optional.ofNullable(harness(initiator));
                case "BURN": return Optional.ofNullable(burn(initiator));
                case "FREEZE": return Optional.ofNullable(freeze(initiator));
                case "STEAMPUNK": return Optional.ofNullable(customElementMove(initiator));
                default: return Optional.empty();
            }
        }

        @Override
        public String getName() {
            return "TechnovilleGuardian: Element-Guardian type | Guardian";
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}
