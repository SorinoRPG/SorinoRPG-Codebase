package game.characters.op;

import game.characters.Sorino;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DentedDemented extends OP implements Sorino {
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
        return sorino.equalsIgnoreCase("DentedDemented") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return 0;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = getMovesAbs();
        moves.add("Dented Cranium");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch (move.toUpperCase()){
            case "SCRATCH": return Optional.ofNullable(scratch(initiator));
            case "PUNCH": return Optional.ofNullable(punch(initiator));
            case "EAT": return Optional.ofNullable(eat(initiator));
            case "DENTED CRANIUM": return Optional.ofNullable(customOPMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.punch, super.eat, super.scratch, dentedCranium));
    }

    @Override
    public String getName() {
        return "DentedDemented: OP type";
    }

    //Dented Cranium
    private final Move dentedCranium = new Move(69, "Dented the opponents Crainium!",
            false, 6,
            "https://cdn.discordapp.com/attachments/768534237493985291/777632672465551380/5e42e007210000bc01838509.png");
    @Override
    public Move customOPMove(Sorino initiator) {
        dentedCranium.addSorino(initiator);
        return dentedCranium;
    }

    @Override
    public String toString() {
        return getName();
    }
}
