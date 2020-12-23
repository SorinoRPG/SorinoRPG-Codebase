package game.value;

import java.io.Serializable;

public class Coins implements Serializable {
    private int amount;

    public Coins(int amount){
        this.amount = amount;
    }

    public void setCoins(int coins){
        amount += coins;
    }
    public boolean spend(int coins){
        if(amount - coins < 0) return false;
        amount -= coins;
        return true;
    }
    public int getCoins(){
        return this.amount;
    }
}
