package game.characters.rage;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class FGP extends Rage implements Sorino {
    @Override
    public int getHealth(int level) {
        return 1020 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 980 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 10;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("FGP") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Bundle Run
    private final Move likesmash = new Move(100, "SMASH THAT LIKE BUTTON!",
            false, 40,
            "https://media.discordapp.net/attachments/803353528088133632/807641936637853706/1200px-Facebook_Thumb_icon.png?width=644&height=498");
    @Override
    public Move customRageMove(Sorino initiator) {
        likesmash.addSorino(initiator);
        return likesmash;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Like Smash");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "LIKE": return Optional.ofNullable(customRageMove(initiator));
            case "GOUGE": return Optional.ofNullable(super.gouge(initiator));
            case "BALLISTIC": return Optional.ofNullable(super.ballistic(initiator));
            case "CHARGE": return Optional.ofNullable(super.charge(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "FGP: Rage type | Extinct";
    }

    @Override
    public String toString() {
        return getName();
    }
}
