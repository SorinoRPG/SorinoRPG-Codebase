package game.fight;

import game.characters.Sorino;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Optional;

public class Opponent {
    private final Sorino sorino;
    private int health;
    private int energy;
    private boolean conceded = false;
    private int switchOut = 3;

    Opponent(Sorino sorino, GuildMessageReceivedEvent event){
        this.sorino = sorino;
        this.health = sorino.getHealth();
        this.energy = sorino.getEnergy();
    }
    private double damageDecrease = 0;

    public boolean hasConceded() {
        return conceded || energy < 0;
    }
    public int getHealth() {
        return health;
    }
    public int getEnergy(){
        return energy;
    }
    public double getDecrease(){
        return damageDecrease;
    }
    public boolean switchOut(){
        switchOut--;
        return switchOut == 0;
    }

    public Optional<String> takeDamage(Move move, GuildMessageReceivedEvent event){
        if (energy < 0)
            return Optional.of("You have no more energy left!");
        if(damageDecrease + sorino.getIfWeakness(move.getSorino()) > 1.00)
            damageDecrease = 1 - (sorino.getIfWeakness(move.getSorino())+0.01);
        health -= move.getEffect() -
                (move.getEffect() * damageDecrease) +
                (move.getEffect() * sorino.getIfWeakness(move.getSorino()));

        event.getChannel().sendMessage(
                move.getDesc()
        ).queue();
        conceded = this.health < 0;
        return Optional.empty();
    }
    public Optional<String> defenseUp(Move move, GuildMessageReceivedEvent event){
        if (energy < 0)
            return Optional.of("You have no more usages!");
        damageDecrease += move.getEffect();
        energy -= move.getEnergy();
        event.getChannel().sendMessage(
                move.getDesc()
        ).queue();
        return Optional.empty();
    }
    public void dropEnergy(Move move){
        energy -= move.getEnergy();
    }
}
