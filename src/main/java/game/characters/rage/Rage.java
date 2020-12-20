package game.characters.rage;

import game.characters.Sorino;
import game.characters.smart.Smart;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Rage {
    double getIfWeakness(Sorino sorino){
        if (sorino instanceof Smart)
            return 0.3;
        return 0;
    }

    final Move ballistic = new Move(26, "Went on an inaccurate rampage!",
            false, 19,
            "https://cdn.discordapp.com/attachments/768534237493985291/777633571774791710/Science_australiafires-1191951200.png");
    Move ballistic(Sorino initiator){
        ballistic.addSorino(initiator);
        return ballistic;
    }

    final Move charge = new Move(40, "An accurate hit",
            false, 30,
            "https://cdn.discordapp.com/attachments/768534237493985291/777633937886281778/116950449-stock-vector-cartoon-red-bull-attack.png");
    Move charge(Sorino initiator){
        charge.addSorino(initiator);
        return charge;
    }

    final Move gouge = new Move(35, "Gouged the opponent",
            false, 20,
            "https://img.redbull.com/images/c_crop,x_473,y_0,h_1414,w_1131/c_fill,w_860,h_1075/q_auto,f_auto/redbullcom/2017/12/18/897034a0-ce37-40f9-9327-f3df7b051405/hitting-the-wall");
    Move gouge(Sorino initiator){
        gouge.addSorino(initiator);
        return gouge;
    }

    abstract Move customRageMove(Sorino initiator);

    List<String> getMovesAbs(){
        return new ArrayList<>(Arrays.asList("Ballistic", "Gouge", "Charge"));
    }
}
