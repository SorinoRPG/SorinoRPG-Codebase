package game.characters.element;

import game.characters.Sorino;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Patraka extends Element implements Sorino {
    @Override
    public int getHealth(int level) {
        return 295 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 400 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 520;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Patraka") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Recycle
    private final Move recycle = new Move(0.1, "Recycled defense!",
            true, 35,
            "https://cdn.discordapp.com/attachments/768534237493985291/777624443618459698/IMG_E2375.JPG");
    @Override
    public Move customElementMove(Sorino initiator) {
        recycle.addSorino(initiator);
        return recycle;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = getMovesAbs();
        moves.add("Recycle");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch (move.toUpperCase()){
            case "HARNESS": return Optional.ofNullable(harness(initiator));
            case "BURN": return Optional.ofNullable(burn(initiator));
            case "FREEZE": return Optional.ofNullable(freeze(initiator));
            case "RECYCLE": return Optional.ofNullable(customElementMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.harness, super.burn, super.freeze, recycle));
    }

    @Override
    public String getName() {
        return "Patraka: Element type";
    }

    @Override
    public String toString() {
        return getName();
    }
}
