package game.fight.streetfight.protectors;

import game.characters.Sorino;
import game.characters.rage.Mantra;
import game.characters.smart.Smart;
import game.fight.Move;
import game.fight.streetfight.StreetProtector;

import java.util.List;
import java.util.Optional;

public class Sumato implements StreetProtector{
    @Override
    public String getName() {
        return "Sumato";
    }

    @Override
    public String getStreetName() {
        return "Dongolia Street";
    }

    @Override
    public String getSarcasticRemark() {
        return "I have no words for such an ignorant fool, who dares to battle me, do your worst!";
    }

    @Override
    public String getWinningRemark() {
        return "Weak, just as I suspected. I don't want to see your face around Dongolia again!";
    }

    @Override
    public String getLosingRemark() {
        return "You must be trained. Here's my Guardian Sorino, you are clearly worthy.";
    }

    @Override
    public Sorino getGuardianSorino() {
        return new DongoliaGuardian();
    }
    public static class DongoliaGuardian extends Smart implements Sorino {
        @Override
        public int getHealth(int level) {
            return 880;
        }

        @Override
        public int getEnergy(int level) {
            return 725;
        }

        @Override
        public int getRarity() {
            return 0;
        }

        @Override
        public Optional<Sorino> getSorino(String sorino) {
            return sorino.equalsIgnoreCase("DongoliaGuardian") ? Optional.of(this) : Optional.empty();
        }

        @Override
        public double getIfWeakness(Sorino sorino) {
            return super.getIfWeakness(sorino);
        }

        private final Move dongPunch = new Move(50, "Performed Dong Punch!",
                false, 25,
                "https://cdn.discordapp.com/attachments/803353528088133632/803353540638539817/old-red-brick-wall-damaged-background-picture-id1002801182.png");
        @Override
        public Move customSmartMove(Sorino initiator) {
           dongPunch.addSorino(initiator);
            return dongPunch;
        }

        @Override
        public List<String> getMoves() {
            List<String> moves =
                    super.getMovesAbs();
            moves.add("Dong Punch");
            return moves;
        }

        @Override
        public Optional<Move> getMove(String move, Sorino initiator) {
            switch(move.toUpperCase()){
                case "CONFUSE": return Optional.of(confuse(initiator));
                case "MIND": return Optional.of(mindTap(initiator));
                case "LEARN": return Optional.of(learn(initiator));
                case "DONG" : return Optional.of(customSmartMove(initiator));
                default: return Optional.empty();
            }
        }

        @Override
        public String getName() {
            return "DongoliaGuardian: Smart-Guardian type | Guardian";
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}
