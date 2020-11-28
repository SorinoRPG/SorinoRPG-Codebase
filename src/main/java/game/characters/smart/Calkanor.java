package game.characters.smart;

import game.characters.Sorino;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Calkanor extends Smart implements Sorino {
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
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Calkanor") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Tactical
    private final Move tactical = new Move(32, "Performed a tactical move",
             false, 15,
            "https://cdn.discordapp.com/attachments/768534237493985291/777638224205447208/9k.png");
    @Override
    public Move customSmartMove(Sorino initiator) {
        tactical.addSorino(initiator);
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
    public Optional<Move> getMove(String move, Sorino initiator) {
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
