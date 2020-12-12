package game.characters.rage;

import game.characters.Sorino;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Impaler extends Rage implements Sorino {
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
        return 1100;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Impaler") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Impale
    private final Move impale = new Move(50, "Impaled the opponent",
             false, 35,
            "https://cdn.discordapp.com/attachments/768534237493985291/777636770866659428/rsz_1derelict_dragons_teeth_5262.png");
    @Override
    public Move customRageMove(Sorino initiator) {
        impale.addSorino(initiator);
        return impale;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Impale");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "IMPALE": return Optional.ofNullable(customRageMove(initiator));
            case "GOUGE": return Optional.ofNullable(super.gouge(initiator));
            case "BALLISTIC": return Optional.ofNullable(super.ballistic(initiator));
            case "CHARGE": return Optional.ofNullable(super.charge(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.gouge, super.ballistic, super.charge, impale));
    }

    @Override
    public String getName() {
        return "Impaler: Rage type | Uncommon";
    }

    @Override
    public String toString() {
        return getName();
    }
}
