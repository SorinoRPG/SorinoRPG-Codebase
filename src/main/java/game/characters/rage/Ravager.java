package game.characters.rage;

import game.characters.Sorino;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Ravager extends Rage implements Sorino {
    @Override
    public int getHealth(int level) {
        return 330 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 500 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 2520;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Ravager") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    // Scavenge
    private final Move scavenge = new Move(0.1, "Scavenged armour",
            true, 50,
            "https://cdn.discordapp.com/attachments/768534237493985291/777637163776344064/9k.png");
    @Override
    public Move customRageMove(Sorino initiator) {
        scavenge.addSorino(initiator);
        return scavenge;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Scavenge");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
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
        return "Ravager: Rage type | Common";
    }

    @Override
    public String toString() {
        return getName();
    }
}
