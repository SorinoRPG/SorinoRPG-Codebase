package game;

import java.io.Serializable;

public class Coins implements Serializable {
    private int amount;

    public Coins(int amount){
        this.amount = amount;
    }

    public void setCoins(int coins){
        amount += coins;
    }
    public int getCoins(){
        return this.amount;
    }
}
