package main.userinterface;

import data.Profile;
import data.files.Logger;
import game.Coins;
import game.characters.starter.Gray;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.*;

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
                "I will need the DELETE_MESSAGE permission to prevent spam.");
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
        if (!guild.mkdirs()) System.out.println(event.getGuild().getName() + " exits in directory");
    }

    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {
        File guild = new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                "/src/main/java/data/files/" + event.getGuild().getId() + "/fights");
        event.getUser().openPrivateChannel().queue((channel -> channel.sendMessage(
                "https://discord.com/oauth2/authorize?client_id=764566349543899149&scope=bot"
        ).queue()));
        if (!guild.delete()) System.out.println("Guild has already been deleted");
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot() ||
                !Prefix.assertPrefix(event.getMessage()))
            return;

        Command command =
                Command.getCommand(event.getMessage());
        command.userAction
                .action(event);
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
