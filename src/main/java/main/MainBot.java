package main;

import main.userinterface.GuildListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

/**
 * <h1>MainBot</h1>
 * <p>
 *     <b>Ignatiamon</b>
 *     This is a discord interactive text based game that was
 *     made for the purposes of making the Sti links discord more
 *     fun. However, I have plans to take this beyond the server and into
 *     many others!
 *     See this website
 *              https://real-eokafor.github.io/Ignatiamon/
 *     To learn more
 *
 * @author Emmanuel Okafor
 * @since 01/12/2020
 * @version 1.0
 */

public class MainBot {
    /**
     * The initializer for the bot is of course the main method.
     * It has set the activity to a "-help for information" and the
     * status to Do Not Disturb
     *
     * @param args Used to see the token
     * @throws LoginException If there is an error in logging into discord
     * @see JDABuilder
     */
    public static void main(String[] args) throws LoginException {
        if(args.length == 0){
            System.out.print("You needed to specify the token in the arguments!");
            return;
        }

        JDABuilder.createLight(args[0],
                GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new GuildListener())
                .setActivity(Activity.watching("-help for information"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();
    }
}
