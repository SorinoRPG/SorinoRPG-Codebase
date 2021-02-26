package game.characters.element;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Mountitude extends Element implements Sorino {
    @Override
    public int getHealth(int level) {
        return 690 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 420 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 220;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Mountitude") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Recycle
    private final Move avalanche = new Move(80, "Cau   sed an avalanche!",
            false, 50,
            "https://cdn.discordapp.com/attachments/794351462163546125/795371892609515550/avalanche.png");
    @Override
    public Move customElementMove(Sorino initiator) {
        avalanche.addSorino(initiator);
        return avalanche;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = getMovesAbs();
        moves.add("Avalanche");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch (move.toUpperCase()){
            case "HARNESS": return Optional.ofNullable(harness(initiator));
            case "BURN": return Optional.ofNullable(burn(initiator));
            case "FREEZE": return Optional.ofNullable(freeze(initiator));
            case "AVALANCHE": return Optional.ofNullable(customElementMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Mountitude: Element type | Hidden";
    }

    @Override
    public String toString() {
        return getName();
    }
}
