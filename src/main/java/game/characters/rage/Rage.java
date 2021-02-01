package game.characters.rage;

import game.characters.Sorino;
import game.characters.smart.Smart;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Rage {
    protected double getIfWeakness(Sorino sorino){
        if (sorino instanceof Smart)
            return 0.3;
        return 0;
    }

    final Move ballistic = new Move(26, "Went on an inaccurate rampage!",
            false, 19,
            "https://cdn.discordapp.com/attachments/768534237493985291/777633571774791710/Science_australiafires-1191951200.png");
    protected Move ballistic(Sorino initiator){
        ballistic.addSorino(initiator);
        return ballistic;
    }

    final Move charge = new Move(40, "An accurate hit",
            false, 30,
            "https://cdn.discordapp.com/attachments/768534237493985291/777633937886281778/116950449-stock-vector-cartoon-red-bull-attack.png");
    protected Move charge(Sorino initiator){
        charge.addSorino(initiator);
        return charge;
    }

    final Move gouge = new Move(35, "Gouged the opponent",
            false, 20,
            "https://images.ctfassets.net/u4vv676b8z52/4B4aASUoMBqZhfEQVGUe32/49f351e15e189c3750ce696932982f5c/dry-eye-12-treatments-1200x630.png?fm=jpg&q=80");
    protected Move gouge(Sorino initiator){
        gouge.addSorino(initiator);
        return gouge;
    }

    protected abstract Move customRageMove(Sorino initiator);

    protected List<String> getMovesAbs(){
        return new ArrayList<>(Arrays.asList("Ballistic", "Gouge", "Charge"));
    }
}
