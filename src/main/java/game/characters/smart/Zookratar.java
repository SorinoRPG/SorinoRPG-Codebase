package game.characters.smart;

import game.characters.Ignatiamon;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Zookratar extends Smart implements Ignatiamon {
    @Override
    public int getHealth() {
        return 370;
    }

    @Override
    public int getEnergy() {
        return 400;
    }

    @Override
    public int getRarity() {
        return 12;
    }

    @Override
    public Optional<Ignatiamon> getIgnatiamon(String ignatiamon) {
        return ignatiamon.equalsIgnoreCase("Zookratar") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Ignatiamon ignatiamon) {
        return super.getIfWeakness(ignatiamon);
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Jungle Brain");

        return moves;
    }
    // Jungle Brain
    private final Move jungleBrain = new Move(47, "Turned the opponents brain to a Jungle",
             false, 40,
            "https://cdn.discordapp.com/attachments/768534237493985291/777638811655340042/9k.png");
    @Override
    public Move customSmartMove(Ignatiamon initiator) {
        jungleBrain.addIgnatiamon(initiator);
        return jungleBrain;
    }

    @Override
    public Optional<Move> getMove(String move, Ignatiamon initiator) {
        switch (move.toUpperCase()){
            case "JUNGLE BRAIN": return Optional.of(customSmartMove(initiator));
            case "CONFUSE": return Optional.of(confuse(initiator));
            case "MIND TAP": return Optional.of(mindTap(initiator));
            case "LEARN": return Optional.of(learn(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.confuse, super.mindTap, super.learn, jungleBrain));
    }

    @Override
    public String getName() {
        return "Zookratar: Smart type";
    }



    @Override
    public String toString() {
        return getName();
    }
}