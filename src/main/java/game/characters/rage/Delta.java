package game.characters.rage;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Delta extends Rage implements Sorino{
    @Override
    public int getHealth(int level) {
        return 840 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 900 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 72;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Delta") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Bundle Run
    private final Move vengeance = new Move(93, "Takes vengeance on the enemy!",
            false, 70,
            "https://cdn.discordapp.com/attachments/784891397584060459/790557677725679666/1f552ad4f22fa1ea0400dea21f6ebe9b.png");
    @Override
    public Move customRageMove(Sorino initiator) {
        vengeance.addSorino(initiator);
        return vengeance;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Vengeance");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "VENGEANCE": return Optional.ofNullable(customRageMove(initiator));
            case "GOUGE": return Optional.ofNullable(super.gouge(initiator));
            case "BALLISTIC": return Optional.ofNullable(super.ballistic(initiator));
            case "CHARGE": return Optional.ofNullable(super.charge(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Delta: Rage type | Lost";
    }

    @Override
    public String toString() {
        return getName();
    }
}
