package game.characters.op;

import game.characters.Sorino;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DiseaseRiddenBackHead extends OP implements Sorino {
    @Override
    public int getHealth(int level) {
        return 700 + (level * 10);
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
        return sorino.equalsIgnoreCase("DiseaseRiddenBackHead") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return 0;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = getMovesAbs();
        moves.add("Infect");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
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
    public Move customOPMove(Sorino initiator) {
        infect.addSorino(initiator);
        return infect;
    }

    @Override
    public String toString() {
        return getName();
    }
}
