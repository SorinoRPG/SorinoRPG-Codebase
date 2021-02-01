package game.characters.rage;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Bundlebeast extends Rage implements Sorino {
    @Override
    public int getHealth(int level) {
        return 340 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 380 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 3780;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Bundlebeast") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Bundle Run
    private final Move bundleRun = new Move(32, "Runs and hits into opponent",
            false, 20,
            "https://cdn.discordapp.com/attachments/768534237493985291/777636582127042580/c8e882ed12a4e9165ed40165ee9a28b0.png");
    @Override
    public Move customRageMove(Sorino initiator) {
        bundleRun.addSorino(initiator);
        return bundleRun;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Bundle Run");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "BUNDLE": return Optional.ofNullable(customRageMove(initiator));
            case "GOUGE": return Optional.ofNullable(super.gouge(initiator));
            case "BALLISTIC": return Optional.ofNullable(super.ballistic(initiator));
            case "CHARGE": return Optional.ofNullable(super.charge(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Bundlebeast: Rage type | Common";
    }

    @Override
    public String toString() {
        return getName();
    }
}
