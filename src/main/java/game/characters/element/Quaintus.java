package game.characters.element;

import game.characters.Sorino;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Quaintus extends Element implements Sorino {
    @Override
    public int getHealth(int level) {
        return 300 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 350 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 1650;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Quaintus") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    // Quad Force
    private final Move quadForce = new Move(40, "Used the forces of quad!",
            false, 30,
            "https://cdn.discordapp.com/attachments/768534237493985291/777628950142648330/latest.png");
    @Override
    public Move customElementMove(Sorino initiator) {
        quadForce.addSorino(initiator);
        return quadForce;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Quad Force");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch (move.toUpperCase()){
            case "HARNESS": return Optional.ofNullable(super.harness(initiator));
            case "BURN": return Optional.ofNullable(super.burn(initiator));
            case "FREEZE": return Optional.ofNullable(super.freeze(initiator));
            case "QUAD FORCE": return Optional.ofNullable(customElementMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.harness, super.burn, super.freeze, quadForce));
    }

    @Override
    public String getName() {
        return "Quaintus: Element type | Common";
    }

    @Override
    public String toString() {
        return getName();
    }
}
