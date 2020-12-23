package game.characters.rage;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Reegan extends Rage implements Sorino {
    @Override
    public int getHealth(int level) {
        return 640 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 400 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 672;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Reegan") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Bundle Run
    private final Move regenerate = new Move(0.1, "Regenerates with minimal effort!",
            true, 10,
            "https://cdn.discordapp.com/attachments/784891397584060459/790559936160858173/2Q.png");
    @Override
    public Move customRageMove(Sorino initiator) {
        regenerate.addSorino(initiator);
        return regenerate;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Regenerate");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "REGENERATE": return Optional.ofNullable(customRageMove(initiator));
            case "GOUGE": return Optional.ofNullable(super.gouge(initiator));
            case "BALLISTIC": return Optional.ofNullable(super.ballistic(initiator));
            case "CHARGE": return Optional.ofNullable(super.charge(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Reegan: Rage type | Rare";
    }

    @Override
    public String toString() {
        return getName();
    }
}
