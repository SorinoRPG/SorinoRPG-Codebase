package game.characters.smart;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Omega extends Smart implements Sorino {
    @Override
    public int getHealth(int level) {
        return 650 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 680 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 50;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Omega") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Ego
    private final Move manipulate = new Move(142, "Manipulated the opponent!",
            false, 100,
            "https://cdn.discordapp.com/attachments/784891397584060459/790529149940531220/1y8AXJpQKxcIDFw1j9gCXCw.png");
    @Override
    public Move customSmartMove(Sorino initiator) {
        manipulate.addSorino(initiator);
        return manipulate;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Manipulate");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "CONFUSE": return Optional.of(confuse(initiator));
            case "MIND": return Optional.of(mindTap(initiator));
            case "LEARN": return Optional.of(learn(initiator));
            case "MANIPULATE" : return Optional.of(customSmartMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Omega: Smart type | Lost";
    }



    @Override
    public String toString() {
        return getName();
    }
}
