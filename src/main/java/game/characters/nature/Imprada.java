package game.characters.nature;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Imprada extends Nature implements Sorino {
    @Override
    public int getHealth(int level) {
        return 385 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 420 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 1610;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Imprada") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Enhance
    private final Move enhance = new Move(0.2, "Enhanced the defence!",
            true, 50,
            "https://cdn.discordapp.com/attachments/768534237493985291/777631094216327168/68-684174_increase-png-green-upward-arrow-png.png");
    @Override
    public Move customNatureMove(Sorino initiator) {
        enhance.addSorino(initiator);
        return enhance;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Enhance");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "ENHANCE": return Optional.ofNullable(customNatureMove(initiator));
            case "POISON": return Optional.ofNullable(super.poisonIvy(initiator));
            case "GROW": return Optional.ofNullable(super.grow(initiator));
            case "UPROOT": return Optional.ofNullable(super.uproot(initiator));
            default: return Optional.empty();
        }
    }


    @Override
    public String getName() {
        return "Imprada: Nature type | Uncommon";
    }

    @Override
    public String toString() {
        return getName();
    }
}
