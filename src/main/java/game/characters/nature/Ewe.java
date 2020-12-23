package game.characters.nature;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Ewe extends Nature implements Sorino {
    @Override
    public int getHealth(int level) {
        return 365 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 200 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 4465;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Ewe") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    @Override
    public Move customNatureMove(Sorino initiator) {
        return null;
    }

    @Override
    public List<String> getMoves() {
        return super.getMovesAbs();
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "POISON": return Optional.ofNullable(super.poisonIvy(initiator));
            case "GROW": return Optional.ofNullable(super.grow(initiator));
            case "UPROOT": return Optional.ofNullable(super.uproot(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Ewe: Nature type | Common";
    }

    @Override
    public String toString() {
        return getName();
    }
}
