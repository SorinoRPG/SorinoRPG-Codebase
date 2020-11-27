package game.characters.nature;

import game.characters.Ignatiamon;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Traolter extends Nature implements Ignatiamon {
    @Override
    public int getHealth() {
        return 390;
    }

    @Override
    public int getEnergy() {
        return 410;
    }

    @Override
    public int getRarity() {
        return 108;
    }

    @Override
    public Optional<Ignatiamon> getIgnatiamon(String ignatiamon) {
        return ignatiamon.equalsIgnoreCase("Traolter") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Ignatiamon ignatiamon) {
        return super.getIfWeakness(ignatiamon);
    }

    // Trod
    private final Move trod = new Move(35, "Trod on the opponent!",
            false, 40,
            "https://cdn.discordapp.com/attachments/768534237493985291/777631298037743656/wolf-animal-tracks.png");
    @Override
    public Move customNatureMove(Ignatiamon initiator) {
        trod.addIgnatiamon(initiator);
        return trod;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Trod");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Ignatiamon initiator) {
        switch(move.toUpperCase()){
            case "TROD": return Optional.ofNullable(customNatureMove(initiator));
            case "POISON IVY": return Optional.ofNullable(super.poisonIvy(initiator));
            case "GROW": return Optional.ofNullable(super.grow(initiator));
            case "UPROOT": return Optional.ofNullable(super.uproot(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.poisonIvy, super.uproot, super.grow, trod));
    }

    @Override
    public String getName() {
        return "Traolter: Nature type";
    }

    @Override
    public String toString() {
        return getName();
    }
}
