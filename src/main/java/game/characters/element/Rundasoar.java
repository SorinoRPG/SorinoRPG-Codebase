package game.characters.element;

import game.characters.Ignatiamon;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Rundasoar extends Element implements Ignatiamon {
    @Override
    public int getHealth() {
        return 350;
    }

    @Override
    public int getEnergy() {
        return 450;
    }

    @Override
    public int getRarity() {
        return 480;
    }

    @Override
    public Optional<Ignatiamon> getIgnatiamon(String ignatiamon) {
        return ignatiamon.equalsIgnoreCase("Rundasoar") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Ignatiamon ignatiamon) {
        return super.getIfWeakness(ignatiamon);
    }

    //Train
    private final Move train = new Move(0.3, "Trained to increase defence agility",
            true, 60,
            "https://cdn.discordapp.com/attachments/768534237493985291/777629361427316766/barbell_abs_main.png");
    @Override
    public Move customElementMove(Ignatiamon initiator) {
        train.addIgnatiamon(initiator);
        return train;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = getMovesAbs();
        moves.add("Train");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Ignatiamon initiator) {
        switch (move.toUpperCase()){
            case "HARNESS": return Optional.ofNullable(harness(initiator));
            case "BURN": return Optional.ofNullable(burn(initiator));
            case "FREEZE": return Optional.ofNullable(freeze(initiator));
            case "TRAIN": return Optional.ofNullable(customElementMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.harness, super.burn, super.freeze, train));
    }

    @Override
    public String getName() {
        return "Rundasoar: Element type";
    }

    @Override
    public String toString() {
        return getName();
    }
}
