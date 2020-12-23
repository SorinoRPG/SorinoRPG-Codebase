package game.characters.smart;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Logito extends Smart implements Sorino {
    @Override
    public int getHealth(int level) {
        return 350 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 420 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 1680;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Logito") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Adapt
    private final Move adapt = new Move(0.25, "Adapted to harsh environment",
            true, 40,
            "https://cdn.discordapp.com/attachments/784891397584060459/790529717841297438/harsh-landscape_1426-1569.png");
    @Override
    public Move customSmartMove(Sorino initiator) {
        adapt.addSorino(initiator);
        return adapt;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Adapt");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "CONFUSE": return Optional.of(confuse(initiator));
            case "MIND": return Optional.of(mindTap(initiator));
            case "LEARN": return Optional.of(learn(initiator));
            case "ADAPT" : return Optional.of(customSmartMove(initiator));
            default: return Optional.empty();
        }
    }


    @Override
    public String getName() {
        return "Logito: Smart type | Uncommon";
    }



    @Override
    public String toString() {
        return getName();
    }
}
