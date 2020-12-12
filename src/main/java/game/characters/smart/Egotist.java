package game.characters.smart;

import game.characters.Sorino;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Egotist extends Smart implements Sorino {
    @Override
    public int getHealth(int level) {
        return 950 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 880 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 79;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Egotist") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Ego
    private final Move ego = new Move(120, "Attacked the opponent with the power of ego!",
            false, 36,
            "https://www.callcentrehelper.com/images/stories/2019/04/mind-control-hand-puppet-760.jpg");
    @Override
    public Move customSmartMove(Sorino initiator) {
        ego.addSorino(initiator);
        return ego;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Ego");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "CONFUSE": return Optional.of(confuse(initiator));
            case "MIND TAP": return Optional.of(mindTap(initiator));
            case "LEARN": return Optional.of(learn(initiator));
            case "EGO" : return Optional.of(customSmartMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.confuse, super.mindTap, super.learn, ego));
    }

    @Override
    public String getName() {
        return "Egotist: Smart type | Lost";
    }



    @Override
    public String toString() {
        return getName();
    }
}
