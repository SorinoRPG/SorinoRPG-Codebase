package game.characters.op;

import game.characters.Ignatiamon;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DementedBone extends OP implements Ignatiamon {
    @Override
    public int getHealth() {
        return 700;
    }

    @Override
    public int getEnergy() {
        return 1000;
    }

    @Override
    public int getRarity() {
        return 0;
    }

    @Override
    public Optional<Ignatiamon> getIgnatiamon(String ignatiamon) {
        return ignatiamon.equalsIgnoreCase("DementedBone") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Ignatiamon ignatiamon) {
        return 0;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = getMovesAbs();
        moves.add("Dement It");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Ignatiamon initiator) {
        switch (move.toUpperCase()){
            case "SCRATCH": return Optional.ofNullable(scratch(initiator));
            case "PUNCH": return Optional.ofNullable(punch(initiator));
            case "EAT": return Optional.ofNullable(eat(initiator));
            case "DEMENT IT": return Optional.ofNullable(customOPMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.punch, super.eat, super.scratch, dementIt));
    }

    @Override
    public String getName() {
        return "DementedBone: OP type";
    }

    //Dement It
    private final Move dementIt = new Move(70, "Demented the opponent causing insane damage",
            false, 30,
            "https://cdn.discordapp.com/attachments/768534237493985291/777632401408917524/pwwnGkoX5U6N7du92E9BwhHZu32RBhA44QxsmST2HchbDoW0aKdzLaMZDNBcKyB3-YskXR6tFG7gfDzwP7jowPayqvBIUws.png");
    @Override
    public Move customOPMove(Ignatiamon initiator) {
        dementIt.addIgnatiamon(initiator);
        return dementIt;
    }

    @Override
    public String toString() {
        return getName();
    }
}
