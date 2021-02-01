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
        File guild = new File("/db/" + event.getGuild().getId() + "/fights");
        if (!guild.mkdirs()) System.out.println(event.getGuild().getName() + " exists in directory");
        guild = new File("/db/" + event.getGuild().getId() + "/UPDATE_STORE");
        if(!guild.mkdir()) System.out.println(event.getGuild().getName() + " exists in directory");
        try (FileWriter prefix = new FileWriter(new File("/db/" + event.getGuild().getId() +
                "/PREFIX.txt"))){
            prefix.write('.');
        } catch (IOException e) {
            e.printStackTrace();
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);
        embedBuilder.setTitle("Thank you for inviting SorinoRPG to "
                + event.getGuild().getName());
        embedBuilder.setDescription("`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "C` Creates an account **This is required to start playing SorinoRPG**\n" +
                "\n" + "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "P` Let's you view your account details like coins, Sorino, etc\n" +
                "\n" + "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "R` Let's you view your Rank, mention someone to compare ranks\n" +
                "\n" + "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "S` Searches for a Sorino or Coins\n" +
                "\n" + "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "W` Shows you the Wraps you can buy\n" +
                "\n" + "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "WStandard` Buy a standard Wrap\n" +
                "\n" + "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "WPremium` Buy a premium Wrap\n" +
                "\n" + "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "WChampions` Buy a champions Wrap\n" +
                "\n" + "\n" + "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "setprefix <prefix>` to change the prefix. " +
                "To change the channel where level up messages" +
                " are sent, enter `" + Prefix.guildPrefix(event.getGuild().getId()) + "setchannel #channel`.\n\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) + "G`" +
                " use slot machine\n\n" +
                "**How to fight**\n" +
                "To start a fight you must enter:" + "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "FSTART @mention`.\n" +
                "Note: You must always mention the person you are fighting with throughout the fight.\n" +
                "\n" +
                "To choose the Sorino you would like, enter" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "FSORINO @mention`, if I wanted to use Calkanor for example, I would enter " +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) + "FCalkanor @mention`.\n" +
                "\n" +
                "To choose a move, enter " +"`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "FMOVE @mention`, if I wanted to use Scratch, I would enter " +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) + "FScratch @mention`.\n" +
                "\n" +
                "You can end the fight anytime you wish with the command " +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "FEND @mention`\n\n" +
                "**How to Street Fight** \n" +
                "To start a street fight enter `" + Prefix.guildPrefix(event.getGuild().getId()) +
                "BSTART`. The message should tell you how to choose an opponent.\n" +
                "From their, you can enter `" + Prefix.guildPrefix(event.getGuild().getId()) +
                "BMOVE` to start fighting!");
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
        if(event.getAuthor().isBot()) return;
        if(!new File("/db/" + event.getGuild().getId() + "/fights").exists()){
            File guild = new File("/db/" + event.getGuild().getId() + "/fights");
            if (!guild.mkdirs()) System.out.println(event.getGuild().getName() + " exists in directory");
            guild = new File("/db/" + event.getGuild().getId() + "/UPDATE_STORE");
            if(!guild.mkdir()) System.out.println(event.getGuild().getName() + " exists in directory");
            try (FileWriter prefix = new FileWriter(new File("/db/" + event.getGuild().getId() +
                    "/PREFIX.txt"))){
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
