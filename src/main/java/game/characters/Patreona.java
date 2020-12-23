package game.characters;

import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Patreona implements Sorino{
    @Override
    public int getHealth(int level) {
        return 1300 + (level + 10);
    }

    @Override
    public int getEnergy(int level) {
        return 1300 + (level + 10);
    }

    @Override
    public int getRarity() {
        return 0;
    }

    @Override
    public Optional<Sorino> getSorino(String sorino) {
        return sorino.equalsIgnoreCase("Patreona") ? Optional.of(this) : Optional.empty();
    }

    @Override
    public double getIfWeakness(Sorino sorino) {
        return 0;
    }

    @Override
    public List<String> getMoves() {
        return new ArrayList<>(Arrays.asList("Loyal", "Speed Hit", "Swarm", "Centipunch", "Force field"));
    }

    private final Move loyal = new Move(0.25, "Shows loyalty!",
            true, 20,
            "https://cdn.discordapp.com/attachments/784891397584060459/790610462663245854/ktlpzRAAAAAElFTkSuQmCC.png");
    private final Move speedHit = new Move(40, "Hits opponent with great speed!",
            false, 25,
            "https://cdn.discordapp.com/attachments/784891397584060459/790610860699680778/speed-light.png");
    private final Move swarm = new Move(50, "Swarms the opponent",
            false, 30,
            "https://cdn.discordapp.com/attachments/784891397584060459/790611179167940608/Z.png");
    private final Move centipunch = new Move(70, "Punches opponent 100 times",
            false, 42,
            "https://cdn.discordapp.com/attachments/784891397584060459/790612336029204520/AMzhjcz.jpg");
    private final Move forceField = new Move(0.3, "Creates a force field!",
            true, 39,
            "https://cdn.discordapp.com/attachments/784891397584060459/790613402976387112/fvqa0OlVYOkh9ckAe8enVADkgXk3ZvpnphCXr6wWw0U_rzPMyAm-QL-Mp9kQkG0ZvxTS-SdB8C_bt28q09jhAkBNHH-QvdVL5bva.png");
    @Override
    public Optional<Move> getMove(String move, Sorino initiator) {
        switch (move.toUpperCase()){
            case "FORCE": return Optional.of(forceField);
            case "CENTIPUNCH": return Optional.of(centipunch);
            case "SWARM": return Optional.of(swarm);
            case "SPEED": return Optional.of(speedHit);
            case "LOYAL": return Optional.of(loyal);
            default: return Optional.empty();
        }
    }

    @Override
    public String getName() {
        return "Patreona: Patron type: Exclusive";
    }


    @Override
    public String toString() {
        return getName();
    }
}
