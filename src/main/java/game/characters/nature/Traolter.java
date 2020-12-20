package game.characters.nature;

import game.characters.Sorino;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Traolter extends Nature implements Sorino {
    @Override
    public int getHealth(int level) {
        return 390 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 410 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 860;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Traolter") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    // Trod
    private final Move trod = new Move(35, "Trod on the opponent!",
            false, 40,
            "https://cdn.discordapp.com/attachments/768534237493985291/777631298037743656/wolf-animal-tracks.png");
    @Override
    public Move customNatureMove(Sorino initiator) {
        trod.addSorino(initiator);
        return trod;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Trod");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "TROD": return Optional.ofNullable(customNatureMove(initiator));
            case "POISON": return Optional.ofNullable(super.poisonIvy(initiator));
            case "GROW": return Optional.ofNullable(super.grow(initiator));
            case "UPROOT": return Optional.ofNullable(super.uproot(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.poisonIvy, super.uproot, super.grow, trod));
    }

    @Override
    public String getName() {
        return "Traolter: Nature type | Rare";
    }

    @Override
    public String toString() {
        return getName();
    }
}
