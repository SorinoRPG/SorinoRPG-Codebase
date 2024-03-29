package game.characters.element;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Rundasoar extends Element implements Sorino {
    @Override
    public int getHealth(int level) {
        return 350 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 450 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 3380;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Rundasoar") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Train
    private final Move train = new Move(0.3, "Trained to increase defence agility",
            true, 60,
            "https://cdn.discordapp.com/attachments/768534237493985291/777629361427316766/barbell_abs_main.png");
    @Override
    public Move customElementMove(Sorino initiator) {
        train.addSorino(initiator);
        return train;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = getMovesAbs();
        moves.add("Train");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch (move.toUpperCase()){
            case "HARNESS": return Optional.ofNullable(harness(initiator));
            case "BURN": return Optional.ofNullable(burn(initiator));
            case "FREEZE": return Optional.ofNullable(freeze(initiator));
            case "TRAIN": return Optional.ofNullable(customElementMove(initiator));
            default: return Optional.empty();
        }
    }
    @Override
    public String getName() {
        return "Rundasoar: Element type | Common";
    }

    @Override
    public String toString() {
        return getName();
    }
}
