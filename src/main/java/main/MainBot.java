package main;

import data.logging.LogRecycler;
import data.logging.Logger;
import game.fight.FightRecycler;
import main.userinterface.GuildListener;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.discordbots.api.client.DiscordBotListAPI;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * <h1>MainBot</h1>
 * <p>
 *     <b>SorinoRPG</b>
 *     See this website
 *              https://sorinorpg.github.io/SorinoRPG/
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
    public static void main(String[] args) throws LoginException, InterruptedException {
        System.out.println("Started Execution: " + DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm:ss")
                .format(LocalDateTime.now()) + "\n\n");
        JDA jda = JDABuilder.createLight("NzY0NTY2MzQ5NTQzODk5MTQ5.X4IH5g.kVaBMx1eW3YZVd8E7SPwtjzTkuk",
                GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new GuildListener())
                .setActivity(Activity.playing("!help! >Pondering on the meaning of life<"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();
        DiscordBotListAPI api = new DiscordBotListAPI.Builder()
                .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ijc2NDU2NjM0OTU0Mzg5OTE0OSIsImJvdCI6dHJ1ZSwiaWF0IjoxNjA5MzYwMDE1fQ.4woBRWlUh_vG2tCVbrmY8Gg_V7-I9dH6twlEYyjQI_4")
                .botId("764566349543899149")
                .build();
        Thread.sleep(10000);



        Thread.sleep(1000);

        Timer timer = new Timer();
        ScheduledTasks tasks = new ScheduledTasks(
                new Task() {
                    @Override
                    public void doTask() {
                        api.setStats(jda.getGuilds().size());
                    }

                    @Override
                    public void printTaskStatus() {
                        System.out.println("Changed server number to: " + jda.getGuilds().size());
                    }
                },
                new Task(){
                    String fileID = "";
                    int guildsLost = 0;
                    String exc = "";

                    @Override
                    public void doTask() {
                        File[] directories = new File("/db").listFiles((current, name) ->
                                new File(current, name).isDirectory());
                        ArrayList<String> ids = new ArrayList<>();
                        for (Guild g: jda.getGuilds()) ids.add(g.getId());
                        for(File f : directories){
                            if(!ids.contains(f.getName())) {
                                try {
                                    FileUtils.copyDirectory(f, new File("/db_recycle/" + f.getName() + ""));
                                    FileUtils.deleteDirectory(f);
                                    guildsLost++;
                                    fileID = fileID.concat(f.getName() + "\\");
                                } catch (IOException e){
                                    exc = exc.concat(Logger.exceptionAsString(e) + "\n");
                                }
                            }
                        }
                    }

                    @Override
                    public void printTaskStatus() {
                        System.out.println("GUILDS LOST: " + guildsLost);
                        System.out.println("ID'S: " + fileID);
                        System.err.println("EXCEPTIONS: \n" + exc);
                    }
                }, new FightRecycler(), new LogRecycler());
        timer.schedule(tasks, 0, 3600000);
    }
}
