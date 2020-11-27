package game.characters.starter;


import game.characters.Ignatiamon;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Gray extends Starter implements Ignatiamon {
    @Override
    public int getHealth() {
        return 300;
    }

    @Override
    public int getEnergy() {
        return 300;
    }

    @Override
    public int getRarity() {
        return 600;
    }

    @Override
    public Optional<Ignatiamon> getIgnatiamon(String ignatiamon) {
        return ignatiamon.equalsIgnoreCase("Gray") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Ignatiamon ignatiamon) {
        return super.getIfWeakness();
    }

    @Override
    public List<String> getMoves() {
        return new ArrayList<>(Arrays.asList("Scratch", "Punch", "Eat"));
    }

    @Override
    public Optional<Move> getMove(String move, Ignatiamon initiator) {
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
        return "Gray: Starter type";
    }



    @Override
    public String toString() {
        return getName();
    }
}
