package game.fight;

import game.characters.Sorino;

import java.io.Serializable;

public class Move implements Serializable {
    private final double effect;
    private final String desc;
    private Sorino sorino;
    private final boolean defensive;
    private final int energy;
    private final String imageUrl;

    public Move(double effect, String desc, Sorino initiator, boolean defensive, int energy, String imageUrl) {
        this.effect = effect;
        this.desc = desc;
        this.sorino = initiator;
        this.defensive = defensive;
        this.energy = energy;
        this.imageUrl = imageUrl;
    }
    public Move(double effect, String desc, boolean defensive, int energy, String imageUrl) {
        this.effect = effect;
        this.desc = desc;
        this.defensive = defensive;
        this.energy = energy;
        this.imageUrl = imageUrl;
    }
    public void addSorino(Sorino initiator){
        this.sorino = initiator;
    }

    public boolean isDefensive() {
        return defensive;
    }
    public double getEffect() {
        return this.effect;
    }
    public String getDesc() {
        return this.desc;
    }
    public Sorino getSorino() {
        return this.sorino;
    }
    public int getEnergy(){
        return energy;
    }
    public String getUrl(){
        return imageUrl;
    }
}