package game;

import java.io.Serializable;

public class Coins implements Serializable {
    private int amount;

    public Coins(int amount){
        this.amount = amount;
    }

    public void increaseCoins(int increase){
        this.amount += increase;
    }
    public void decreaseCoins(int increase){
        this.amount -= increase;
    }
    public int getCoins(){
        return this.amount;
    }
}
