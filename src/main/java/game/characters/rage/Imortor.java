package game.characters.rage;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Imortor extends Rage implements Sorino {
    @Override
    public int getHealth(int level) {
        return 1240 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 780 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 2;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Imortor") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Bundle Run
    private final Move immortal = new Move(0.8, "Makes itself immortal!",
            true, 120,
            "https://cdn.discordapp.com/attachments/784891397584060459/790555434804576256/9k.png");
    @Override
    public Move customRageMove(Sorino initiator) {
        immortal.addSorino(initiator);
        return immortal;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Immortal");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "IMMORTAL": return Optional.ofNullable(customRageMove(initiator));
            case "GOUGE": return Optional.ofNullable(super.gouge(initiator));
            case "BALLISTIC": return Optional.ofNullable(super.ballistic(initiator));
            case "CHARGE": return Optional.ofNullable(super.charge(initiator));
            default: return Optional.empty();
        }
    }


    @Override
    public String getName() {
        return "Imortor: Rage type | Extinct";
    }

    @Override
    public String toString() {
        return getName();
    }
}
