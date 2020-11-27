package game.characters.rage;

import game.characters.Ignatiamon;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Ravager extends Rage implements Ignatiamon{
    @Override
    public int getHealth() {
        return 330;
    }

    @Override
    public int getEnergy() {
        return 500;
    }

    @Override
    public int getRarity() {
        return 320;
    }

    @Override
    public Optional<Ignatiamon> getIgnatiamon(String ignatiamon) {
        return ignatiamon.equalsIgnoreCase("Ravager") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Ignatiamon ignatiamon) {
        return super.getIfWeakness(ignatiamon);
    }

    // Scavenge
    private final Move scavenge = new Move(0.1, "Scavenged armour",
            true, 50,
            "https://cdn.discordapp.com/attachments/768534237493985291/777637163776344064/9k.png");
    @Override
    public Move customRageMove(Ignatiamon initiator) {
        scavenge.addIgnatiamon(initiator);
        return scavenge;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Scavenge");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Ignatiamon initiator) {
        switch(move.toUpperCase()){
            case "SCAVENGE": return Optional.of(customRageMove(initiator));
            case "GOUGE": return Optional.of(gouge(initiator));
            case "BALLISTIC": return Optional.of(ballistic(initiator));
            case "CHARGE": return Optional.of(charge(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.gouge, super.ballistic, super.charge, scavenge));
    }

    @Override
    public String getName() {
        return "Ravager: Rage type";
    }

    @Override
    public String toString() {
        return getName();
    }
}
