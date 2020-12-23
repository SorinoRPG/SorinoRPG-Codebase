package game.characters.nature;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Evergreen extends Nature implements Sorino {
    @Override
    public int getHealth(int level) {
        return 965 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 1000 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 1;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Evergreen") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Enhance
    private final Move evergreen = new Move(0.25, "Hides in the evergreen!",
            true, 12,
            "https://cdn.discordapp.com/attachments/784891397584060459/790563817629155338/evergreen.png");
    @Override
    public Move customNatureMove(Sorino initiator) {
        evergreen.addSorino(initiator);
        return evergreen;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Evergreen");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "EVERGREEN": return Optional.ofNullable(customNatureMove(initiator));
            case "POISON": return Optional.ofNullable(super.poisonIvy(initiator));
            case "GROW": return Optional.ofNullable(super.grow(initiator));
            case "UPROOT": return Optional.ofNullable(super.uproot(initiator));
            default: return Optional.empty();
        }
    }


    @Override
    public String getName() {
        return "Evergreen: Nature type | Extinct";
    }

    @Override
    public String toString() {
        return getName();
    }
}
