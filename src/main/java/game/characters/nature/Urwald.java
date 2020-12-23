package game.characters.nature;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Urwald extends Nature implements Sorino {
    @Override
    public int getHealth(int level) {
        return 962 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 700 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 65;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Urwald") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Enhance
    private final Move swamp = new Move(105, "Swamps the opponent",
            false, 72,
            "https://cdn.discordapp.com/attachments/784891397584060459/790567943355891712/Swamp-1200x628.png");
    @Override
    public Move customNatureMove(Sorino initiator) {
        swamp.addSorino(initiator);
        return swamp;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Swamp");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "SWAMP": return Optional.ofNullable(customNatureMove(initiator));
            case "POISON": return Optional.ofNullable(super.poisonIvy(initiator));
            case "GROW": return Optional.ofNullable(super.grow(initiator));
            case "UPROOT": return Optional.ofNullable(super.uproot(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Urwald: Nature type | Lost";
    }

    @Override
    public String toString() {
        return getName();
    }
}
