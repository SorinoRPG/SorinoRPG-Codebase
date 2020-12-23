package game.characters.nature;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Plantia extends Nature implements Sorino{
    @Override
    public int getHealth(int level) {
        return 765 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 820 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 130;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Plantia") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Enhance
    private final Move plantOvergrowth = new Move(35, "Causes a plant overgrowth!",
            false, 12,
            "https://cdn.discordapp.com/attachments/784891397584060459/790562854327549962/2Fmethode2Ftimes2Fprod2Fweb2Fbin2Faf9dcc00-50e5-11ea-a869-24971f770bf3.png");
    @Override
    public Move customNatureMove(Sorino initiator) {
        plantOvergrowth.addSorino(initiator);
        return plantOvergrowth;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Plant Overgrowth");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "PLANT": return Optional.ofNullable(customNatureMove(initiator));
            case "POISON": return Optional.ofNullable(super.poisonIvy(initiator));
            case "GROW": return Optional.ofNullable(super.grow(initiator));
            case "UPROOT": return Optional.ofNullable(super.uproot(initiator));
            default: return Optional.empty();
        }
    }


    @Override
    public String getName() {
        return "Plantia: Nature type | Hidden";
    }

    @Override
    public String toString() {
        return getName();
    }
}
