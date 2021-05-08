package game.fight.streetfight.protectors;

import game.characters.Sorino;
import game.characters.smart.Smart;
import game.fight.Move;
import game.fight.streetfight.StreetProtector;

import java.util.List;
import java.util.Optional;

public class Lightrino implements StreetProtector {

    @Override
    public String getName() {
        return "Lightrino";
    }

    @Override
    public String getStreetName() {
        return "Illume Street";
    }

    @Override
    public String getSarcasticRemark() {
        return "Get ready to see the light";
    }

    @Override
    public String getWinningRemark() {
        return "You gave into the light! You are weak!";
    }

    @Override
    public String getLosingRemark() {
        return "Strong.";
    }

    @Override
    public Sorino getGuardianSorino() {
        return new IllumeGuardian();
    }

    public static class IllumeGuardian extends Smart implements Sorino {
        @Override
        public int getHealth(int level) {
            return 1680 + (level * 10);
        }

        @Override
        public int getEnergy(int level) {
            return 1560 + (level * 10);
        }

        @Override
        public int getRarity() {
            return 0;
        }

        @Override
        public Optional<Sorino> getSorino(String sorino) {
            return sorino.equalsIgnoreCase("IllumeGuardian") ? Optional.of(this) : Optional.empty();
        }

        @Override
        public double getIfWeakness(Sorino sorino) {
            return super.getIfWeakness(sorino);
        }

        //Create
        private final Move spectrum = new Move(155, "Blasts with the light spectrum!",
                false, 100,
                "https://media.discordapp.net/attachments/803353528088133632/807660464447422504/Z.png");
        @Override
        public Move customSmartMove(Sorino initiator) {
            spectrum.addSorino(initiator);
            return spectrum;
        }

        @Override
        public List<String> getMoves() {
            List<String> moves = super.getMovesAbs();
            moves.add("Spectrum");
            return moves;
        }

        @Override
        public Optional<Move> getMove(String move, Sorino initiator) {
            switch(move.toUpperCase()){
                case "CONFUSE": return Optional.of(confuse(initiator));
                case "MIND": return Optional.of(mindTap(initiator));
                case "LEARN": return Optional.of(learn(initiator));
                case "SPECTRUM" : return Optional.of(customSmartMove(initiator));
                default: return Optional.empty();
            }
        }

        @Override
        public String getName() {
            return "IllumeGuardian: Smart-Guardian type | Guardian";
        }



        @Override
        public String toString() {
            return getName();
        }
    }

}

