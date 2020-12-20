package main.userinterface;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * <h1>UserAction</h1>
 *
 * A functional interface used to do an Action
 * with a lambda expression, mainly used in the
 * {@link Command} class for the command actions, however
 * it is also used once in the {@link game.fight.FightManager} class
 * as a way to show someones current status
 *
 * <p>
 *     UserAction is similar to many Java functional interfaces,
 *     like {@link Runnable} but holds its differences in that it
 *     is only meant to interface with the channel from the event,
 *     giving the object flexibility.
 * </p>
 *
 * @author Emmanuel Okafor
 * @see Command
 * @see game.fight.FightManager
 */

public interface UserAction {
    /**
     * A method used to perform an action with a Channel or Guild
     *
     * @param event The message received event is how this method interacts with the
     *              guild or channel, it utilizes all the methods provided from the
     *              {@link GuildMessageReceivedEvent} to perform the needed action.
     * @see GuildMessageReceivedEvent
     */
    void action(GuildMessageReceivedEvent event);
}