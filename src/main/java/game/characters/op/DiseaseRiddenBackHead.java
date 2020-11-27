package game.characters.op;

import game.characters.Ignatiamon;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DiseaseRiddenBackHead extends OP implements Ignatiamon {
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
        return ignatiamon.equalsIgnoreCase("DiseaseRiddenBackHead") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Ignatiamon ignatiamon) {
        return 0;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = getMovesAbs();
        moves.add("Infect");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Ignatiamon initiator) {
        switch (move.toUpperCase()){
            case "SCRATCH": return Optional.of(scratch(initiator));
            case "PUNCH": return Optional.of(punch(initiator));
            case "EAT": return Optional.of(eat(initiator));
            case "INFECT": return Optional.of(customOPMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.punch, super.eat, super.scratch, infect));
    }

    @Override
    public String getName() {
        return "DiseaseRiddenBackHead: OP type";
    }

    //Infect
    private final Move infect = new Move(70, "Infected the opponent with back head disease",
            false, 10,
            "https://cdn.discordapp.com/attachments/768534237493985291/777632800341229588/DTthCSt77XxueHZxyMn5CA.png");
    @Override
    public Move customOPMove(Ignatiamon initiator) {
        infect.addIgnatiamon(initiator);
        return infect;
    }

    @Override
    public String toString() {
        return getName();
    }
}
