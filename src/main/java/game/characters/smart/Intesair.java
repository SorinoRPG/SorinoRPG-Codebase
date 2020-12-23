package game.characters.smart;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Intesair extends Smart implements Sorino {
    @Override
    public int getHealth(int level) {
        return 750 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 820 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 100;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Intesair") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Control
    private final Move control = new Move(70, "Took control of the opponent!",
            false, 36,
            "https://www.callcentrehelper.com/images/stories/2019/04/mind-control-hand-puppet-760.jpg");
    @Override
    public Move customSmartMove(Sorino initiator) {
        control.addSorino(initiator);
        return control;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Control");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "CONFUSE": return Optional.of(confuse(initiator));
            case "MIND": return Optional.of(mindTap(initiator));
            case "LEARN": return Optional.of(learn(initiator));
            case "CONTROL" : return Optional.of(customSmartMove(initiator));
            default: return Optional.empty();
        }
    }
    @Override
    public String getName() {
        return "Intesair: Smart type | Hidden";
    }



    @Override
    public String toString() {
        return getName();
    }
}
