package game.characters.smart;

import game.characters.Sorino;
import game.fight.Move;

import java.util.List;
import java.util.Optional;

public class Eko extends Smart implements Sorino {
    @Override
    public int getHealth(int level) {
        return 850 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 680 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 70;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Eko") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    //Ego
    private final Move echo = new Move(146, "A loud echo, rumbles the battlefield!",
            false, 100,
            "https://cdn.discordapp.com/attachments/794351462163546125/795373790581227540/Audio-waves.png");
    @Override
    public Move customSmartMove(Sorino initiator) {
        echo.addSorino(initiator);
        return echo;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Echo");
        return moves;
    }

    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "CONFUSE": return Optional.of(confuse(initiator));
            case "MIND": return Optional.of(mindTap(initiator));
            case "LEARN": return Optional.of(learn(initiator));
            case "ECHO" : return Optional.of(customSmartMove(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Eko: Smart type | Lost";
    }



    @Override
    public String toString() {
        return getName();
    }
}
