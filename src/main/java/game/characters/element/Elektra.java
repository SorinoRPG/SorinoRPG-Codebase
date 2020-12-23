package game.characters.element;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Elektra extends Element implements Sorino {
    @Override
    public int getHealth(int level) {
        return 650 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 525 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 1780;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Elektra") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Train
    private final Move shock = new Move(42, "Shocks the opponent",
            false, 60,
            "https://cdn.discordapp.com/attachments/784891397584060459/790570038238380052/864246208-1024x724.png");
    @Override
    public Move customElementMove(Sorino initiator) {
        shock.addSorino(initiator);
        return shock;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = getMovesAbs();
        moves.add("Shock");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch (move.toUpperCase()){
            case "HARNESS": return Optional.ofNullable(harness(initiator));
            case "BURN": return Optional.ofNullable(burn(initiator));
            case "FREEZE": return Optional.ofNullable(freeze(initiator));
            case "SHOCK": return Optional.ofNullable(customElementMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Elektra: Element type | Uncommon";
    }

    @Override
    public String toString() {
        return getName();
    }
}
