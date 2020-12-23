package game.characters.element;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Quake extends Element implements Sorino {
    @Override
    public int getHealth(int level) {
        return 850 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 725 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 280;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Quake") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Train
    private final Move earthquake = new Move(70, "Shook the ground!",
            false, 30,
            "https://cdn.discordapp.com/attachments/784891397584060459/790571329232830464/R3UVMCRFLVGL3ETTKP2VFMBO54.png");
    @Override
    public Move customElementMove(Sorino initiator) {
        earthquake.addSorino(initiator);
        return earthquake;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = getMovesAbs();
        moves.add("Earthquake");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch (move.toUpperCase()){
            case "HARNESS": return Optional.ofNullable(harness(initiator));
            case "BURN": return Optional.ofNullable(burn(initiator));
            case "FREEZE": return Optional.ofNullable(freeze(initiator));
            case "EARTHQUAKE": return Optional.ofNullable(customElementMove(initiator));
            default: return Optional.empty();
        }
    }


    @Override
    public String getName() {
        return "Quake: Element type | Hidden";
    }

    @Override
    public String toString() {
        return getName();
    }
}
