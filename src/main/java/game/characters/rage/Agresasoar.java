package game.characters.rage;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Agresasoar extends Rage implements Sorino {
    @Override
    public int getHealth(int level) {
        return 315 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 360 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 1670;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Agresasoar") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Reckless
    private final Move reckless = new Move(29, "Performed a reckless attack",
            false, 29,
            "https://cdn.discordapp.com/attachments/784891397584060459/790526803663257610/fba8acf7d8861e5d7e18369e9fa37118_large.jpg");
    @Override
    public Move customRageMove(Sorino initiator) {
        reckless.addSorino(initiator);
        return reckless;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Reckless");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "RECKLESS": return Optional.ofNullable(customRageMove(initiator));
            case "GOUGE": return Optional.ofNullable(super.gouge(initiator));
            case "BALLISTIC": return Optional.ofNullable(super.ballistic(initiator));
            case "CHARGE": return Optional.ofNullable(super.charge(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Agresasoar: Rage type | Uncommon";
    }

    @Override
    public String toString() {
        return getName();
    }
}
