package game.characters.rage;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Destrukto extends Rage implements Sorino {
    @Override
    public int getHealth(int level) {
        return 640 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 380 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 180;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Destrukto") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Bundle Run
    private final Move demolition = new Move(72, "Demolishes the battle ground!",
            false, 40,
            "https://cdn.discordapp.com/attachments/784891397584060459/790527260729147402/Screenshot_20201221-103220_Photos.jpg");
    @Override
    public Move customRageMove(Sorino initiator) {
        demolition.addSorino(initiator);
        return demolition;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("DEMOLITION");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "DEMOLITION": return Optional.ofNullable(customRageMove(initiator));
            case "GOUGE": return Optional.ofNullable(super.gouge(initiator));
            case "BALLISTIC": return Optional.ofNullable(super.ballistic(initiator));
            case "CHARGE": return Optional.ofNullable(super.charge(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Destrukto: Rage type | Hidden";
    }

    @Override
    public String toString() {
        return getName();
    }
}
