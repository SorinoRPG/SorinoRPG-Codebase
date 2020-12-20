package main.userinterface;

import data.Profile;
import data.ProfileNotFoundException;
import data.files.Logger;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.jodah.expiringmap.ExpiringMap;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
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
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("SorinoRPG has just landed!");
        embedBuilder.setDescription("Thank you for inviting SorinoRPG to " + event.getGuild().getName() + "\n" +
                "The Manage Message permission is HIGHLY recommended so SorinoRPG can prevent spam!");
        embedBuilder.addField("Invite SorinoRPG to your server",
                "[Invite](https://discord.com/oauth2/authorize?client_id=764566349543899149&scope=bot)",
                true);
        embedBuilder.addField("Website containing the command information",
                "[Website](https://sorinorpg.github.io/SorinoRPG/)",
                true);
        embedBuilder.addField("Follow Us on Twitter",
                "[Twitter](https://twitter.com/RpgSorino)",
                true);
        embedBuilder.addField("Become a Patron",
                "[Patreon](https://www.patreon.com/sorinorpg?fan_landing=true)",
                true);

        Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessage(embedBuilder.build()).queue();

        File guild = new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                "/src/main/java/data/files/" + event.getGuild().getId() + "/fights");
        if (!guild.mkdirs()) System.out.println(event.getGuild().getName() + " exists in directory");
        guild = new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                "/src/main/java/data/files/" + event.getGuild().getId() + "/UPDATE_STORE");
        if(!guild.mkdir()) System.out.println(event.getGuild().getName() + " exists in directory");
    }


    Map<Long, Long> spamControl = ExpiringMap
            .builder()
            .maxSize(10000)
            .expiration(2, TimeUnit.SECONDS)
            .expirationListener((k, v) -> {
                try {
                    System.out.println("Traffic ended from: " + k);
                    Logger logger = new Logger("Traffic ended from: " + k);
                    logger.logAction();
                } catch (IOException e){
                    e.printStackTrace();
                }
            })
            .build();
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        try {
            Profile profile = Profile.getProfile(event);
            profile.incrementXP(10, event.getChannel());
        } catch (IOException | ProfileNotFoundException | ClassNotFoundException ignored) {}
        if(event.getAuthor().isBot() ||
                !Prefix.assertPrefix(event.getMessage()))
            return;
        if(spamControl.containsKey(event.getAuthor().getIdLong())){
            event.getChannel().sendMessage("Calm down with the commands!").queue(message ->
                    message.delete().queueAfter(1500, TimeUnit.MILLISECONDS));
            if(event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_MANAGE))
                event.getMessage().delete().queue();
            spamControl.replace(event.getAuthor().getIdLong(), System.currentTimeMillis());
            return;
        }
        Command command = Command.getCommand(event.getMessage());
        command.userAction.action(event);
        if(event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MESSAGE_MANAGE))
            event.getMessage().delete().queueAfter(3500, TimeUnit.MILLISECONDS);
        else event.getChannel().sendMessage("It is HIGHLY recommended to give SorinoRPG" +
                "the Manage Messages permission to prevent spam and irrelevant messages in your server!")
        .queue(message -> message.delete().queueAfter(5, TimeUnit.MILLISECONDS));
        spamControl.put(event.getAuthor().getIdLong(), System.currentTimeMillis());
    }
    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0xff0000);
        embedBuilder.setTitle("HELP");
        embedBuilder.setDescription("SorinoRPG Help and Information");
        embedBuilder.addField("Invite SorinoRPG to your server",
                "[Invite](https://discord.com/oauth2/authorize?client_id=764566349543899149&scope=bot)",
                true);
        embedBuilder.addField("Website containing the command information",
                "[Website](https://sorinorpg.github.io/SorinoRPG/)",
                true);
        embedBuilder.addField("Follow Us on Twitter",
                "[Twitter](https://twitter.com/RpgSorino)",
                true);
        embedBuilder.addField("Become a Patron",
                "[Patreon](https://www.patreon.com/sorinorpg?fan_landing=true)",
                true);

        event.getAuthor().openPrivateChannel().queue(channel ->
                channel.sendMessage(embedBuilder.build()).queue());
    }
}
