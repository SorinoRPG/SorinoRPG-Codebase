package game.characters.op;

import game.characters.Ignatiamon;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class OP {
    final Move punch = new Move(60, "An OP punched the opponent!",
            false, 30,
            "https://cdn.discordapp.com/attachments/768534237493985291/777625671450755102/IMG_E2377.JPG");
    Move punch(Ignatiamon initiator){
        punch.addIgnatiamon(initiator);
        return punch;
    }

    final Move scratch = new Move(55, "An OP scratched the opponent!",
            false, 30,
            "https://cdn.discordapp.com/attachments/768534237493985291/777631806421860362/2Q.png");
    Move scratch(Ignatiamon initiator) {
        scratch.addIgnatiamon(initiator);
        return scratch;
    }

    final Move eat = new Move(0.9, "An OP ate for strength!",
            true, 30,
            "https://cdn.discordapp.com/attachments/768534237493985291/777631986331680778/11949841511552878171apple_bitten_dan_gerhard_01.png");
    Move eat(Ignatiamon initiator){
        eat.addIgnatiamon(initiator);
        return eat;
    }

    List<String> getMovesAbs(){
        return new ArrayList<>(Arrays.asList("Punch", "Scratch", "Eat"));
    }

    abstract Move customOPMove(Ignatiamon initiator);
}
