package game.characters.smart;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Alpha extends Smart implements Sorino{
    @Override
    public int getHealth(int level) {
        return 1080 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 860 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 42;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Alpha") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Create
    private final Move evolve = new Move(0.55, "Evolved to improve!",
            true, 50,
            "https://cdn.discordapp.com/attachments/784891397584060459/790530920800190464/https3A2F2Fblogs-images.png");
    @Override
    public Move customSmartMove(Sorino initiator) {
        evolve.addSorino(initiator);
        return evolve;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Evolve");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "CONFUSE": return Optional.of(confuse(initiator));
            case "MIND": return Optional.of(mindTap(initiator));
            case "LEARN": return Optional.of(learn(initiator));
            case "EVOLVE" : return Optional.of(customSmartMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Alpha: Smart type | Lost";
    }



    @Override
    public String toString() {
        return getName();
    }
}
