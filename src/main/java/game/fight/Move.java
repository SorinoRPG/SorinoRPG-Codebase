package game.fight;

import game.characters.Ignatiamon;

import java.io.Serializable;

public class Move implements Serializable {
    private final double effect;
    private final String desc;
    private Ignatiamon ignatiamon;
    private final boolean defensive;
    private final int energy;
    private final String imageUrl;

    public Move(double effect, String desc, Ignatiamon initiator, boolean defensive, int energy, String imageUrl) {
        this.effect = effect;
        this.desc = desc;
        this.ignatiamon = initiator;
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
    public void addIgnatiamon(Ignatiamon initiator){
        this.ignatiamon = initiator;
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
    public Ignatiamon getIgnatiamon() {
        return this.ignatiamon;
    }
    public int getEnergy(){
        return energy;
    }
    public String getUrl(){
        return imageUrl;
    }
}