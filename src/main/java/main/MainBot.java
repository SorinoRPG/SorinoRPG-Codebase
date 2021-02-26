package main;

import data.datareading.FileCommand;
import data.logging.LogRecycler;
import data.logging.Logger;
import game.fight.FightRecycler;
import game.heist.HeistRecycler;
import main.springboot.SpringApp;
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
        SpringApp.init(args);

        jda = JDABuilder.createLight("NzY0NTY2MzQ5NTQzODk5MTQ5.X4IH5g.kVaBMx1eW3YZVd8E7SPwtjzTkuk",
                GatewayIntent.GUILD_MESSAGES)
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
                new Task() {
                    String fileID = "";
                    int guildsLost = 0;
                    String exc = "";

                    @Override
                    public void doTask() {
                        File[] directories = new File("/db").listFiles((current, name) ->
                                new File(current, name).isDirectory());
                        ArrayList<String> ids = new ArrayList<>();
                        for (Guild g : jda.getGuilds()) ids.add(g.getId());
                        for (File f : directories) {
                            if (!ids.contains(f.getName())) {
                                try {
                                    FileUtils.copyDirectory(f, new File("/db_recycle/" + f.getName()));
                                    FileUtils.deleteDirectory(f);
                                    guildsLost++;
                                    fileID = fileID.concat(f.getName() + "\\");
                                } catch (IOException e) {
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
                },
                new Task() {
                    @Override
                    public void doTask() {
                        jda.getPresence().setActivity(Activity.watching(
                                phrases[new Random().nextInt(phrases.length)]));
                    }

                    @Override
                    public void printTaskStatus() {

                    }
                },
                new Task() {
                    @Override
                    public void doTask() {
                        FileCommand.DB_BACKUP.action.action("null");
                    }

                    @Override
                    public void printTaskStatus() {

                    }
                }, new FightRecycler(), new LogRecycler(), new HeistRecycler());
        timer.schedule(tasks, 0, 3600000);

        Thread.sleep(5000);

        while (true) {
            System.out.print(">>");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if(input.equals("END")) return;

            FileCommand fileCommand = FileCommand.getCmd(input);
            fileCommand.action.action(input.substring(input.indexOf(" ")+1));
        }
    }
    public static String dbl_webhook = "239E4B95CD52AAD71638B61698DC59FE01E2200A4D4FC1E636091D0DAC3728EE";
    private static JDA jda;
    public static JDA getJda() {
        return jda;
    }

    static String[] phrases = {
            "Thanos destroy half the universe",
            "Wandavision",
            "People spam search",
            "People gambling away their lives",
            "People lose to Street Protectors",
            "Jeff Kinney losing his mind",
            "Phineas and Ferb",
            "Github repositories grow old",
            "2020 on repeat",
            "MrBeast getting cancelled",
            "Discord servers die",
            "Wall Street go bankrupt",
            "People get vaccinated",
            "X-Men become part of the MCU"
    };

}
