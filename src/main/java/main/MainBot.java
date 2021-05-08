package main;

import data.Mongo;
import main.tasks.*;
import main.springboot.SpringApp;
import main.userinterface.GuildListener;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.discordbots.api.client.DiscordBotListAPI;

import java.util.*;

/**
 * <h1>MainBot</h1>
 * <p>
 *     <b>SorinoRPG</b>
 *     See this website
 *              https://sorinorpg.github.io/SorinoRPG/
 *     To learn more
 *
 * @author Emmanuel Okafor
 * @since 20/10/2020
 * @version 0.3
 */

public class MainBot {
    public static java.util.logging.Logger logger = java.util.logging.Logger.getAnonymousLogger();

    /**
     * The initializer for the bot is of course the main method.
     * It has set the activity to a "-help for information" and the
     * status to Do Not Disturb
     *
     * @param args Used to see the token
     * @throws LoginException If there is an error in logging into discord
     * @see JDABuilder
     */
    public static void main(String[] args) throws LoginException, InterruptedException {
        logger.info("Started Execution");
        SpringApp.init(args);
        Mongo.initMongo();

        jda = JDABuilder.createLight("token",
                GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .addEventListeners(new GuildListener())
                .setStatus(OnlineStatus.ONLINE)
                .build();
        DiscordBotListAPI api = new DiscordBotListAPI.Builder()
                .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ijc2NDU2NjM0OTU0Mzg5OTE0OSIsImJvdCI6dHJ1ZSwiaWF0IjoxNjA5MzYwMDE1fQ.4woBRWlUh_vG2tCVbrmY8Gg_V7-I9dH6twlEYyjQI_4")
                .botId("764566349543899149")
                .build();
        Thread.sleep(10000);

        Timer timer = new Timer();
        ScheduledTasks tasks = new ScheduledTasks(
                new ServerCountChanger(api, jda),
                new StatusChanger(jda),
                new LogRecycler());

        timer.schedule(tasks, 0, 3600000);
    }
    public static String dbl_webhook = "239E4B95CD52AAD71638B61698DC59FE01E2200A4D4FC1E636091D0DAC3728EE";
    public static String dbots = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGkiOnRydWUsImlkIjoiNjcyODgzMjA4ODExMTg0MTY2IiwiaWF0IjoxNjE4MjI2MDY2fQ.zyG83xkF0Ang9bVOXE7tTYQy0lD9IE3pNdsj-Rb6PvA";
    private static JDA jda;
    public static JDA getJda() {
        return jda;
    }
}
