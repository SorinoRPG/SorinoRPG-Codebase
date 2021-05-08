package game.fight.streetfight.protectors;

import game.characters.Sorino;
import game.characters.nature.Nature;
import game.fight.Move;
import game.fight.streetfight.StreetProtector;

import java.util.List;
import java.util.Optional;

public class Xerxes implements StreetProtector {

    @Override
    public String getName() {
        return "Xerxes";
    }

    @Override
    public String getStreetName() {
        return "Xenia Street";
    }

    @Override
    public String getSarcasticRemark() {
        return "Your death is certain.";
    }

    @Override
    public String getWinningRemark() {
        return "Run along child.";
    }

    @Override
    public String getLosingRemark() {
        return "Hmph";
    }

    @Override
    public Sorino getGuardianSorino() {
        return new XeniaGuardian();
    }
    public static class XeniaGuardian extends Nature implements Sorino {
        @Override
        public int getHealth(int level) {
            return 1465 + (level * 10);
        }

        @Override
        public int getEnergy(int level) {
            return 1300 + (level * 10);
        }

        @Override
        public int getRarity() {
            return 0;
        }

        @Override
        public Optional<Sorino> getSorino(String sorino) {
            return sorino.equalsIgnoreCase("XeniaGuardian") ? Optional.of(this) : Optional.empty();
        }

        @Override
        public double getIfWeakness(Sorino sorino) {
            return super.getIfWeakness(sorino);
        }

        private final Move infect = new Move(105, "Infects!",
                false, 42,
                "https://media.discordapp.net/attachments/803353528088133632/807647014384566322/Ebola-virus.png?width=711&height=498");
        @Override
        public Move customNatureMove(Sorino initiator) {
            infect.addSorino(initiator);
            return infect;
        }

        @Override
        public List<String> getMoves() {
            List<String> moves = super.getMovesAbs();
            moves.add("Infect");
            return moves;
        }

        @Override
        public Optional<Move> getMove(String move, Sorino initiator) {
            switch(move.toUpperCase()){
                case "INFECT": return Optional.ofNullable(customNatureMove(initiator));
                case "POISON": return Optional.ofNullable(super.poisonIvy(initiator));
                case "GROW": return Optional.ofNullable(super.grow(initiator));
                case "UPROOT": return Optional.ofNullable(super.uproot(initiator));
                default: return Optional.empty();
            }
        }

        @Override
        public String getName() {
            return "XeniaGuardian: Nature-Guardian type | Guardian";
        }

        @Override
        public String toString() {
            return getName();
        }
    }

}
