package game.characters.starter;


import game.characters.Sorino;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Gray extends Starter implements Sorino {
    @Override
    public int getHealth(int level) {
        return 300 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 300 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 0;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Gray") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness();
    }

    @Override
    public List<String> getMoves() {
        return new ArrayList<>(Arrays.asList("Scratch", "Punch", "Eat"));
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch (move.toUpperCase()){
            case "SCRATCH":
                return Optional.of(scratch(initiator));
            case "PUNCH":
                return Optional.of(punch(initiator));
            case "EAT":
                return Optional.of(eat(initiator));
            default:
                return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.punch, super.eat, super.scratch));
    }

    @Override
    public String getName() {
        return "Gray: Starter type | Starter";
    }

    @Override
    public String toString() {
        return getName();
    }
}
