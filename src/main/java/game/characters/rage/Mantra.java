package game.characters.rage;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Mantra extends Rage implements Sorino {
    @Override
    public int getHealth(int level) {
        return 740 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 280 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 780;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Mantra") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Bundle Run
    private final Move mantra = new Move(0.3, "Screams the mantra!",
            false, 70,
            "https://cdn.discordapp.com/attachments/794351462163546125/795374065908187156/Gritos-Juntos-728x1030.png");
    @Override
    public Move customRageMove(Sorino initiator) {
        mantra.addSorino(initiator);
        return mantra;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Mantra");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "MANTRA": return Optional.ofNullable(customRageMove(initiator));
            case "GOUGE": return Optional.ofNullable(super.gouge(initiator));
            case "BALLISTIC": return Optional.ofNullable(super.ballistic(initiator));
            case "CHARGE": return Optional.ofNullable(super.charge(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Mantra: Rage type | Uncommon";
    }

    @Override
    public String toString() {
        return getName();
    }
}
