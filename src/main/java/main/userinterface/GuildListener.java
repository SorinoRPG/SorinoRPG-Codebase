package main.userinterface;

import data.Profile;
import data.ProfileNotFoundException;
import data.logging.Logger;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GuildListener extends ListenerAdapter {

    @Override
    public void onDisconnect(@Nonnull DisconnectEvent event) {
        System.out.println("Disconnected");

        Logger logger =
                new Logger("Disconnected");
        try {
            logger.logError();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        System.out.println("Joined " + event.getGuild().getName());
        try {
            File fights = new File("/db/" + event.getGuild().getId() + "/fights");
            FileUtils.forceMkdir(fights);

            File update = new File("/db/" + event.getGuild().getId() + "/UPDATE_STORE");
            FileUtils.forceMkdir(update);

            File heist = new File("/db/" + event.getGuild().getId() + "/heists");
            FileUtils.forceMkdir(heist);

            try (FileWriter prefix = new FileWriter(new File("/db/" + event.getGuild().getId() +
                    "/PREFIX.txt"))){
                prefix.write('.');
            }  catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);
        embedBuilder.setTitle("Thank you for inviting SorinoRPG to "
                + event.getGuild().getName());
        embedBuilder.setDescription("`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "C` Creates an account **This is required to start playing SorinoRPG**\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "accountsave` Let's you view your, or someone else's, account details like coins, Sorino, etc\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "P` Let's you view your, or someone else's, account details like coins, Sorino, etc\n" +
                "\n" + "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "R` Let's you view your Rank\n" +
                "\n" + "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "S` Searches for a Sorino or Coins\n" +
                "\n" + "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "W` Shows you the Wraps you can buy\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "vote` will give you the link to vote for 7,000 coins!\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "update` will show you the latest bot updates\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "donate <coins> @ mention` to give coins to someone\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "setprefix <prefix>` to change the prefix. " +
                "To change the channel where level up messages" +
                " are sent, enter `" + Prefix.guildPrefix(event.getGuild().getId()) + "setchannel <#channel>`.\n\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) + "G`" +
                " use slot machine\n\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "FSTART @mention` is used to start a fight with a user.\n" +
                "You can end the fight anytime you wish with the command " +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "FEND @mention`\n\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "BSTART` To  start a Street Fight.\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "BEND` To end the fight!");
        embedBuilder.addField("Invite SorinoRPG to your server",
                "[Invite](https://discord.com/oauth2/authorize?client_id=764566349543899149&scope=bot&permissions=27648)",
                true);
        embedBuilder.addField("Vote for us on top.gg!",
                "[Our Page](https://top.gg/bot/764566349543899149)",
                true);
        embedBuilder.addField("Website",
                "[Website](https://sorinorpg.github.io/SorinoRPG/)",
                true);
        embedBuilder.addField("Follow us on Twitter",
                "[Twitter](https://twitter.com/RpgSorino)",
                true);
        embedBuilder.addField("Become a Patron for exclusive Sorino and extra" +
                        " coins!",
                "[Patreon](https://www.patreon.com/sorinorpg?fan_landing=true)",
                true);
        embedBuilder.addField("Email us!",
                "SorinoRPG@gmail.com",
                true);


        for(TextChannel channel : event.getGuild().getTextChannels()) {
            if (channel.canTalk()){
                channel.sendMessage(embedBuilder.build()).queue();
                break;
            }
        }
    }


    Map<String, Long> spamControl = ExpiringMap
            .builder()
            .maxSize(10000)
            .expiration(2, TimeUnit.SECONDS)
            .expirationListener((k, v) -> {
                try {
                    System.out.println("Traffic ended from: " + k + " at: " +
                            DateTimeFormatter
                                    .ofPattern("dd/MM/yyyy HH:mm:ss")
                                    .format(LocalDateTime.now()));
                    Logger logger = new Logger("Traffic ended from: " + k + " at: " +
                            DateTimeFormatter
                            .ofPattern("dd/MM/yyyy HH:mm:ss")
                            .format(LocalDateTime.now()));
                    logger.logAction();
                } catch (IOException e){
                    e.printStackTrace();
                }
            })
            .build();
    public static Map<String, Long> gamblingControl = ExpiringMap
            .builder()
            .maxSize(100000000)
            .expiration(10, TimeUnit.SECONDS)
            .expirationListener((k, v) -> {
                try {
                    System.out.println("Traffic ended from: " + k + " at: " +
                            DateTimeFormatter
                                    .ofPattern("dd/MM/yyyy HH:mm:ss")
                                    .format(LocalDateTime.now()));
                    Logger logger = new Logger("Traffic ended from: " + k + " at: " +
                            DateTimeFormatter
                                    .ofPattern("dd/MM/yyyy HH:mm:ss")
                                    .format(LocalDateTime.now()));
                    logger.logAction();
                } catch (IOException e){
                    e.printStackTrace();
                }
            })
            .build();
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        try {
            File fights = new File("/db/" + event.getGuild().getId() + "/fights");
            if (!fights.exists()) FileUtils.forceMkdir(fights);

            File update = new File("/db/" + event.getGuild().getId() + "/UPDATE_STORE");
            if(!update.exists()) FileUtils.forceMkdir(update);

            File heist = new File("/db/" + event.getGuild().getId() + "/heists");
            if(!heist.exists()) FileUtils.forceMkdir(heist);

            File prefix = new File("/db/" + event.getGuild().getId() +
                    "/PREFIX.txt");
            if(!prefix.exists()){
                try (FileWriter prefixW = new FileWriter(prefix)){
                    prefixW.write('.');
                }  catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        if(event.getAuthor().isBot()) return;
        assert new File("/db/" + event.getGuild().getId() + "/fights").exists() ||
                new File("/db/" + event.getGuild().getId() + "/fights").mkdirs();
        assert new File("/db/" + event.getGuild().getId() + "/UPDATE_STORE").exists() ||
                new File("/db/" + event.getGuild().getId() + "/UPDATE_STORE").mkdirs();
        try {
            File heist = new File("/db/" + event.getGuild().getId() + "/heists");
            FileUtils.forceMkdir(heist);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!new File("/db/" + event.getGuild().getId() +
                "/PREFIX.txt").exists()) {
            try (FileWriter prefix = new FileWriter(new File("/db/" + event.getGuild().getId() +
                    "/PREFIX.txt"))) {
                prefix.write('.');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Profile profile = Profile.getProfile(event);
            profile.incrementXP(10, event);
        } catch (IOException | ProfileNotFoundException | ClassNotFoundException ignored) {}

        if(event.getMessage().getContentRaw().equals("!help!")) {
            Command.HELP.userAction.action(event);
            return;
        }

        if(!Prefix.assertPrefix(event)) {
            return;
        }
        if(event.getMessage().getContentRaw().toUpperCase().contains("HELP")){
            Command.HELP.userAction.action(event);
            return;
        }

        if(!event.getGuild().getSelfMember().hasPermission(
                Permission.MESSAGE_EMBED_LINKS,
                Permission.MESSAGE_READ,
                Permission.MESSAGE_WRITE)){
            event.getChannel().sendMessage("SorinoRPG is missing permissions! It requires:\n" +
                    "Embed Links permission\n" +
                      "Message Write permission\n" +
                    "Message Read permission").queue();
            return;
        }
        if(event.getMessage().getContentRaw().contains("set")) {
            Command.CHANGE.userAction.action(event);
            return;
        }
        if(event.getMessage().getContentRaw().contains("accountsave")){
            try {
                Command.registerProfile(event);
            } catch (Exception e){
                event.getChannel().sendMessage("There was an error in saving your account!" +
                        "Please join the support server so we can help you!").queue();
            }
        }



        if(spamControl.containsKey(event.getAuthor().getId() + "//" + event.getGuild().getId())){
            event.getChannel().sendMessage("Calm down with the commands!").queue();
            return;
        }

        Command command = Command.getCommand(event);
        if(command == Command.ERROR) return;

        if(command == Command.SLOT && gamblingControl
                .containsKey(event.getAuthor().getId() + "//" + event.getGuild().getId())){
            event.getChannel().sendMessage(event.getAuthor().getName() +
                    " has already gambled in the last 10 seconds!")
                    .queue();
            return;
        }

        command.userAction.action(event);
        spamControl.put(event.getAuthor().getId() + "//" + event.getGuild().getId(),
                System.currentTimeMillis());
    }
}
