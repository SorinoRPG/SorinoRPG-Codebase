package game.characters.smart;

import game.characters.Ignatiamon;
import game.characters.nature.Nature;
import game.fight.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Smart{
    double getIfWeakness(Ignatiamon ignatiamon) {
        if(ignatiamon instanceof Nature)
            return 0.25;
        return 0;
    }

    final Move confuse = new Move(40, "Confused the opponent",
            false, 20,
            "https://cdn.discordapp.com/attachments/768534237493985291/777637494036365312/Z.png");
    Move confuse(Ignatiamon initiator){
        confuse.addIgnatiamon(initiator);
        return confuse;
    }

    final Move learn = new Move(0.2, "Learnt for defense",
            true, 32,
            "https://cdn.discordapp.com/attachments/768534237493985291/777637637388894208/2Q.png");
    Move learn(Ignatiamon initiator){
        learn.addIgnatiamon(initiator);
        return learn;
    }

    final Move mindTap = new Move(45, "Tapped into the opponent's mind",
            false, 16,
            "https://cdn.discordapp.com/attachments/768534237493985291/777637956016930867/48b422ea-75a0-4ee2-9fb9-153bdcb61843-620x372.png");
    Move mindTap(Ignatiamon initiator){
        mindTap.addIgnatiamon(initiator);
        return mindTap;
    }

    abstract Move customSmartMove(Ignatiamon initiator);

    List<String> getMovesAbs(){
        return new ArrayList<>(Arrays.asList("Mind Tap", "Learn", "Confuse"));
    }
}
