package game.characters.nature;

import game.characters.Sorino;
import game.characters.element.Element;
import game.characters.rage.Rage;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Nature {
    protected double getIfWeakness(Sorino sorino){
        if(sorino instanceof Element || sorino instanceof Rage)
            return 0.1;
        return 0;
    }

    protected List<String> getMovesAbs(){
        return new ArrayList<>(Arrays.asList("Grow",
                "Poison Ivy",
                "Uproot"));
    }

    final Move grow = new Move(0.3, "Grows trees for defence",
            true, 60,
            "https://cdn.discordapp.com/attachments/768534237493985291/777629612439502878/2Q.png");
    protected Move grow(Sorino initiator){
        grow.addSorino(initiator);
        return grow;
    }

    final Move poisonIvy = new Move(34, "Stung opponent with Poison Ivy",
            false, 20,
            "https://cdn.discordapp.com/attachments/768534237493985291/777629997476216892/good-friday_0.png");
    protected Move poisonIvy(Sorino initiator){
        poisonIvy.addSorino(initiator);
        return poisonIvy;
    }

    final Move uproot = new Move(43, "Uprooted the ground!",
            false, 30,
            "https://cdn.discordapp.com/attachments/768534237493985291/777630541158547476/QEaDHl-TbqIEvbQ9rrEILwQIBQHyj0E0p2QzL7RDFwqJ5_3It9PZs2lQwJV8o_TGQ37tpGA3YJbQDwNgb28BHPaExrIdZMfctCHt.png");
    protected Move uproot(Sorino initiator){
        uproot.addSorino(initiator);
        return uproot;
    }

    protected abstract Move customNatureMove(Sorino initiator);
}
