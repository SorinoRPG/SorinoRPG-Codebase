package game.fight;

import data.Profile;
import data.ProfileNotFoundException;
import game.characters.Sorino;

import main.userinterface.Prefix;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.Serializable;

public class Opponent implements Serializable {
    private final Sorino sorino;
    private int health;
    private int energy;

    public Opponent(Sorino sorino, GuildMessageReceivedEvent event){
        this.sorino = sorino;
        try {
            this.health = sorino.getHealth(Profile.getProfile(event.getAuthor()).getLevel());
            this.energy = sorino.getEnergy(Profile.getProfile(event.getAuthor()).getLevel());
        } catch (ProfileNotFoundException e) {
            event.getChannel().sendMessage("You do not have a profile! Enter: `" +
                    Prefix.guildPrefix(event.getGuild().getId()) + "C`").queue();
        }
    }
    private double damageDecrease = 0;

    public boolean hasConceded() {
        return this.health <= 0 || energy <= 0;
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

    public void takeDamage(Move move, GuildMessageReceivedEvent event){
        if(damageDecrease + sorino.getIfWeakness(move.getSorino()) >= 1.00)
            damageDecrease = 1 - (sorino.getIfWeakness(move.getSorino())+0.1);
        health -= move.getEffect() -
                (move.getEffect() * damageDecrease) +
                (move.getEffect() * sorino.getIfWeakness(move.getSorino()));

    }
    public void defenseUp(Move move, GuildMessageReceivedEvent event){
        damageDecrease += move.getEffect();
        energy -= move.getEnergy();
    }
    public void dropEnergy(Move move){
        energy -= move.getEnergy();
    }
}
