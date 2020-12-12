package game.characters.nature;

import game.characters.Sorino;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Wildilio extends Nature implements Sorino {
    @Override
    public int getHealth(int level) {
        return 350 + (level * 10);
    }

    @Override
    public int getEnergy(int level) {
        return 460 + (level * 10);
    }

    @Override
    public int getRarity() {
        return 560;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Wildilio") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return super.getIfWeakness(sorino);
    }

    // Wild Punch
    private final Move wildPunch = new Move(50, "Inaccurate punch",
            false, 30,
            "https://cdn.discordapp.com/attachments/768534237493985291/777631480373444628/Kr6KcnVN1S6a1hmF3xNYDqV14p95Vp68bIJRzue3eMk18xn0Tr6LkzXyv3eTXEbH2ev8XRgzdBSg47alV17ylvy6I2KP3BWtS5cu.png");
    @Override
    public Move customNatureMove(Sorino initiator) {
        wildPunch.addSorino(initiator);
        return wildPunch;
    }

    @Override
    public List<String> getMoves() {
        List<String> moves = super.getMovesAbs();
        moves.add("Wild Punch");
        return moves;
    }


    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch(move.toUpperCase()){
            case "WILD PUNCH": return Optional.ofNullable(customNatureMove(initiator));
            case "POISON IVY": return Optional.ofNullable(super.poisonIvy(initiator));
            case "GROW": return Optional.ofNullable(super.grow(initiator));
            case "UPROOT": return Optional.ofNullable(super.uproot(initiator));
            default: return Optional.empty();
        }
    }

    @Override
    public List<Move> getAllMoves() {
        return new ArrayList<>(Arrays.asList(super.poisonIvy, super.uproot, super.grow, wildPunch));
    }

    @Override
    public String getName() {
        return "Wildilio: Nature type | Rare";
    }

    @Override
    public String toString() {
        return getName();
    }
}
