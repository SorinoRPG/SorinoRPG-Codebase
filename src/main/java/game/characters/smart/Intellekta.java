package game.characters.smart;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Intellekta extends Smart implements Sorino {
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
        return 79;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Intellekta") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Control
    private final Move intellekt = new Move(86, "Made an intellectual move!",
            false, 36,
            "https://cdn.discordapp.com/attachments/784891397584060459/790534451314294814/untitled-design16.png");
    @Override
    public Move customSmartMove(Sorino initiator) {
        intellekt.addSorino(initiator);
        return intellekt;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Intellekt");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "CONFUSE": return Optional.of(confuse(initiator));
            case "MIND": return Optional.of(mindTap(initiator));
            case "LEARN": return Optional.of(learn(initiator));
            case "INTELLEKT" : return Optional.of(customSmartMove(initiator));
            default: return Optional.empty();
        }
    }
    @Override
    public String getName() {
        return "Intellekta: Smart type | Hidden";
    }



    @Override
    public String toString() {
        return getName();
    }

}
