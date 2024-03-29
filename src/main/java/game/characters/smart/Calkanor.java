package game.characters.smart;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Calkanor extends Smart implements Sorino {
    @Override
    public int getHealth(int level) {
        return 410 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 500 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 1700;
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
            case "MIND": return Optional.of(mindTap(initiator));
            case "LEARN": return Optional.of(learn(initiator));
            case "TACTICAL" : return Optional.of(customSmartMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Calkanor: Smart type | Uncommon";
    }

    @Override
    public String toString() {
        return getName();
    }
}
