package game.fight;

import data.Profile;
import data.ProfileNotFoundException;
import data.logging.Logger;
import game.characters.Sorino;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Opponent implements Serializable {
    private final Sorino sorino;
    private int health;
    private int energy;

    Opponent(Sorino sorino, GuildMessageReceivedEvent event){
        this.sorino = sorino;
        try {
            this.health = sorino.getHealth(Profile.getProfile(event).getLevel());
            this.energy = sorino.getEnergy(Profile.getProfile(event).getLevel());
        } catch (IOException | ClassNotFoundException e) {
            Logger logger =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try {
                logger.logError();
            } catch (IOException excI) {
                excI.printStackTrace();
            }
        } catch (ProfileNotFoundException e) {
            Logger logger =
                    new Logger("Error in finding Profile \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile!"
            ).queue();
            try{
                logger.logError();
            } catch (IOException excI){
                excI.printStackTrace();
            }
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
