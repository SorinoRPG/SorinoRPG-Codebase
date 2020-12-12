package game.characters.smart;

import game.characters.Sorino;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Anno extends Smart implements Sorino{
    @Override
    public int getHealth(int level) {
        return 360 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 420 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 712;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Anno") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Create
    private final Move create = new Move(0.34, "Creates a defensive barrier!",
            true, 50,
            "https://www.callcentrehelper.com/images/stories/2019/04/mind-control-hand-puppet-760.jpg");
    @Override
    public Move customSmartMove(Sorino initiator) {
        create.addSorino(initiator);
        return create;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Create");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "CONFUSE": return Optional.of(confuse(initiator));
            case "MIND TAP": return Optional.of(mindTap(initiator));
            case "LEARN": return Optional.of(learn(initiator));
            case "CREATE" : return Optional.of(customSmartMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.confuse, super.mindTap, super.learn, create));
    }

    @Override
    public String getName() {
        return "Anno: Smart type | Hidden";
    }



    @Override
    public String toString() {
        return getName();
    }
}
