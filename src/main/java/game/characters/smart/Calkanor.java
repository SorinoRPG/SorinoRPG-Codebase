package game.characters.smart;

import game.characters.Ignatiamon;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Calkanor extends Smart implements Ignatiamon {
    @Override
    public int getHealth() {
        return 410;
    }

    @Override
    public int getEnergy() {
        return 500;
    }

    @Override
    public int getRarity() {
        return 22;
    }

    @Override
    public Optional<Ignatiamon> getIgnatiamon(String ignatiamon) {
        return ignatiamon.equalsIgnoreCase("Calkanor") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Ignatiamon ignatiamon) {
        return super.getIfWeakness(ignatiamon);
    }

    //Tactical
    private final Move tactical = new Move(32, "Performed a tactical move",
             false, 15,
            "https://cdn.discordapp.com/attachments/768534237493985291/777638224205447208/9k.png");
    @Override
    public Move customSmartMove(Ignatiamon initiator) {
        tactical.addIgnatiamon(initiator);
        return tactical;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves =
                super.getMovesAbs();
        moves.add("Tactical");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Ignatiamon initiator) {
        switch(move.toUpperCase()){
            case "CONFUSE": return Optional.of(confuse(initiator));
            case "MIND TAP": return Optional.of(mindTap(initiator));
            case "LEARN": return Optional.of(learn(initiator));
            case "TACTICAL" : return Optional.of(customSmartMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.confuse, super.mindTap, super.learn, tactical));
    }

    @Override
    public String getName() {
        return "Calkanor: Smart type";
    }

    @Override
    public String toString() {
        return getName();
    }
}
