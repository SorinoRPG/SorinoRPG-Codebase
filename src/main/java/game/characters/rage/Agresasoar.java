package game.characters.rage;

import game.characters.Ignatiamon;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Agresasoar extends Rage implements Ignatiamon {
    @Override
    public int getHealth() {
        return 315;
    }

    @Override
    public int getEnergy() {
        return 360;
    }

    @Override
    public int getRarity() {
        return 380;
    }

    @Override
    public Optional<Ignatiamon> getIgnatiamon(String ignatiamon) {
        return ignatiamon.equalsIgnoreCase("Agresasoar") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Ignatiamon ignatiamon) {
        return super.getIfWeakness(ignatiamon);
    }

    //Reckless
    private final Move reckless = new Move(29, "Performed a reckless attack",
            false, 4,
            "https://cdn.discordapp.com/attachments/768534237493985291/777636306397298708/d4ngiyr-c99998eb-3b87-4c57-8a67-ddf320cbb08d.png");
    @Override
    public Move customRageMove(Ignatiamon initiator) {
        reckless.addIgnatiamon(initiator);
        return reckless;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Reckless");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Ignatiamon initiator) {
        switch(move.toUpperCase()){
            case "RECKLESS": return Optional.ofNullable(customRageMove(initiator));
            case "GOUGE": return Optional.ofNullable(super.gouge(initiator));
            case "BALLISTIC": return Optional.ofNullable(super.ballistic(initiator));
            case "CHARGE": return Optional.ofNullable(super.charge(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.gouge, super.ballistic, super.charge, reckless));
    }

    @Override
    public String getName() {
        return "Agresasoar: Rage type";
    }

    @Override
    public String toString() {
        return getName();
    }
}
