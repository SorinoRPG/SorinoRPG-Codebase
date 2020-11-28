package game.characters.element;

import game.characters.Sorino;
import game.characters.rage.Rage;
import game.characters.smart.Smart;
import game.fight.Move;

import java.util.*;

public abstract class Element {
    double getIfWeakness(Sorino sorino){
        if(sorino instanceof Rage || sorino instanceof Smart)
            return 0.45;
        return 0;
    }

    List<String> getMovesAbs(){
        return new ArrayList<>(Arrays.asList("Freeze", "Burn", "Harness"));
    }

    final Move freeze = new Move(33, "Froze the opponent",
            false, 25,
            "https://cdn.discordapp.com/attachments/768534237493985291/777624898205384724/IMG_E2376.JPG");
    Move freeze(Sorino initiator){
        freeze.addSorino(initiator);
        return freeze;
    }

    final Move burn = new Move(33, "Burned the opponent",
            false, 30,
            "https://cdn.discordapp.com/attachments/768534237493985291/777624484814520350/IMG_E2373.JPG");
    Move burn(Sorino initiator){
        burn.addSorino(initiator);
        return burn;
    }

    final Move harness = new Move(0.25, "Harnessed the elements",
            true, 30,
            "https://cdn.discordapp.com/attachments/768534237493985291/777628163357802526/download_1.jpg");
    Move harness(Sorino initiator){
        harness.addSorino(initiator);
        return harness;
    }

    abstract Move customElementMove(Sorino initiator);
}
