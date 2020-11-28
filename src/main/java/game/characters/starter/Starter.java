package game.characters.starter;

import game.characters.Sorino;
import game.fight.Move;

public abstract class Starter {
    double getIfWeakness(){
        return 0.5;
    }

    final Move scratch = new Move(10, "A scratch on the opponent",
            false, 15,
            "https://cdn.discordapp.com/attachments/768534237493985291/777631806421860362/2Q.png");
    Move scratch(Sorino initiator){
        scratch.addSorino(initiator);
        return scratch;
    }

    final Move punch = new Move(15, "A punch on the opponent",
             false, 15,
            "https://cdn.discordapp.com/attachments/768534237493985291/777625671450755102/IMG_E2377.JPG");
    Move punch(Sorino initiator){
        punch.addSorino(initiator);
        return punch;
    }

    final Move eat = new Move(0.05, "Ate an apple, gained defense!",
            true, 30,
            "https://media.discordapp.net/attachments/768534237493985291/777631986331680778/11949841511552878171apple_bitten_dan_gerhard_01.png?width=428&height=498");
    Move eat(Sorino initiator){
        eat.addSorino(initiator);
        return eat;
    }
}
