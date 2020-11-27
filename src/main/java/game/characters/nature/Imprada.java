package game.characters.nature;

import game.characters.Ignatiamon;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Imprada extends Nature implements Ignatiamon{
    @Override
    public int getHealth() {
        return 385;
    }

    @Override
    public int getEnergy() {
        return 420;
    }

    @Override
    public int getRarity() {
        return 410;
    }

    @Override
    public Optional<Ignatiamon> getIgnatiamon(String ignatiamon) {
        return ignatiamon.equalsIgnoreCase("Imprada") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Ignatiamon ignatiamon) {
        return super.getIfWeakness(ignatiamon);
    }

    //Enhance
    private final Move enhance = new Move(0.2, "Enhanced the defence!",
            true, 50,
            "https://cdn.discordapp.com/attachments/768534237493985291/777631094216327168/68-684174_increase-png-green-upward-arrow-png.png");
    @Override
    public Move customNatureMove(Ignatiamon initiator) {
        enhance.addIgnatiamon(initiator);
        return enhance;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Enhance");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Ignatiamon initiator) {
        switch(move.toUpperCase()){
            case "ENHANCE": return Optional.ofNullable(customNatureMove(initiator));
            case "POISON IVY": return Optional.ofNullable(super.poisonIvy(initiator));
            case "GROW": return Optional.ofNullable(super.grow(initiator));
            case "UPROOT": return Optional.ofNullable(super.uproot(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.poisonIvy, super.uproot, super.grow, enhance));
    }

    @Override
    public String getName() {
        return "Imprada: Nature type";
    }

    @Override
    public String toString() {
        return getName();
    }
}
