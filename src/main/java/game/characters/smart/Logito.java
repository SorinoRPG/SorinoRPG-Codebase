package game.characters.smart;

import game.characters.Ignatiamon;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Logito extends Smart implements Ignatiamon {
    @Override
    public int getHealth() {
        return 350;
    }

    @Override
    public int getEnergy() {
        return 420;
    }

    @Override
    public int getRarity() {
        return 6;
    }

    @Override
    public Optional<Ignatiamon> getIgnatiamon(String ignatiamon) {
        return ignatiamon.equalsIgnoreCase("Logito") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Ignatiamon ignatiamon) {
        return super.getIfWeakness(ignatiamon);
    }

    //Adapt
    private final Move adapt = new Move(0.25, "Adapted to environment",
            true, 40,
            "https://cdn.discordapp.com/attachments/768534237493985291/777638669531873290/Z.png");
    @Override
    public Move customSmartMove(Ignatiamon initiator) {
        adapt.addIgnatiamon(initiator);
        return adapt;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Adapt");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Ignatiamon initiator) {
        switch(move.toUpperCase()){
            case "CONFUSE": return Optional.of(confuse(initiator));
            case "MIND TAP": return Optional.of(mindTap(initiator));
            case "LEARN": return Optional.of(learn(initiator));
            case "ADAPT" : return Optional.of(customSmartMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.confuse, super.mindTap, super.learn, adapt));
    }

    @Override
    public String getName() {
        return "Logito: Smart type";
    }



    @Override
    public String toString() {
        return getName();
    }
}