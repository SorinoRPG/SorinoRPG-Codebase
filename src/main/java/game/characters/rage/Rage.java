package game.characters.rage;

import game.characters.Ignatiamon;
import game.characters.smart.Smart;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Rage {
    double getIfWeakness(Ignatiamon ignatiamon){
        if (ignatiamon instanceof Smart)
            return 0.3;
        return 0;
    }

    final Move ballistic = new Move(26, "Went on an inaccurate rampage!",
            false, 19,
            "https://cdn.discordapp.com/attachments/768534237493985291/777633571774791710/Science_australiafires-1191951200.png");
    Move ballistic(Ignatiamon initiator){
        ballistic.addIgnatiamon(initiator);
        return ballistic;
    }

    final Move charge = new Move(40, "An accurate hit",
            false, 30,
            "https://cdn.discordapp.com/attachments/768534237493985291/777633937886281778/116950449-stock-vector-cartoon-red-bull-attack.png");
    Move charge(Ignatiamon initiator){
        charge.addIgnatiamon(initiator);
        return charge;
    }

    final Move gouge = new Move(35, "Gouged the opponent",
            false, 20,
            "https://cdn.discordapp.com/attachments/768534237493985291/777635540551598100/14-144676_flying-rocks-png-kuiper-belt-png-transparent-png.png");
    Move gouge(Ignatiamon initiator){
        gouge.addIgnatiamon(initiator);
        return gouge;
    }

    abstract Move customRageMove(Ignatiamon initiator);

    List<String> getMovesAbs(){
        return new ArrayList<>(Arrays.asList("Ballistic", "Gouge", "Charge"));
    }
}
