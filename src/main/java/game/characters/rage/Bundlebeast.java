package game.characters.rage;

import game.characters.Ignatiamon;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Bundlebeast extends Rage implements Ignatiamon {
    @Override
    public int getHealth() {
        return 340;
    }

    @Override
    public int getEnergy() {
        return 380;
    }

    @Override
    public int getRarity() {
        return 420;
    }

    @Override
    public Optional<Ignatiamon> getIgnatiamon(String ignatiamon) {
        return ignatiamon.equalsIgnoreCase("Bundlebeast") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Ignatiamon ignatiamon) {
        return super.getIfWeakness(ignatiamon);
    }

    //Bundle Run
    private final Move bundleRun = new Move(32, "Runs and hits into opponent",
            false, 4,
            "https://cdn.discordapp.com/attachments/768534237493985291/777636582127042580/c8e882ed12a4e9165ed40165ee9a28b0.png");
    @Override
    public Move customRageMove(Ignatiamon initiator) {
        bundleRun.addIgnatiamon(initiator);
        return bundleRun;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Bundle Run");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Ignatiamon initiator) {
        switch(move.toUpperCase()){
            case "BUNDLE RUN": return Optional.ofNullable(customRageMove(initiator));
            case "GOUGE": return Optional.ofNullable(super.gouge(initiator));
            case "BALLISTIC": return Optional.ofNullable(super.ballistic(initiator));
            case "CHARGE": return Optional.ofNullable(super.charge(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.gouge, super.ballistic, super.charge, bundleRun));
    }

    @Override
    public String getName() {
        return "Bundlebeast: Rage type";
    }

    @Override
    public String toString() {
        return getName();
    }
}
