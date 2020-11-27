package main.userinterface;

import data.Profile;
import data.files.Logger;
import game.Coins;
import game.characters.starter.Gray;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

public class GuildListener extends ListenerAdapter {

    @Override
    public void onDisconnect(@Nonnull DisconnectEvent event) {
        System.out.println("Disconnected");

        Logger logger =
                new Logger("Disconnected");
        try {
            logger.logError();
            logger.closeLogger();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        Optional<Role> userRole = Objects
                .requireNonNull(event
                        .getMember())
                .getRoles()
                .stream()
                .filter(role -> role.getName().equalsIgnoreCase("Developer-In-Training")
                || role.getName().equalsIgnoreCase("Developers")
                || role.getName().equalsIgnoreCase("Testers"))
                .findFirst();
        if(event.getAuthor().isBot() ||
                !Prefix.assertPrefix(event.getMessage()) || userRole.isEmpty())
            return;

        Command command =
                Command.getCommand(event.getMessage());
        command.userAction
                .action(event);

//        event.getMessage().delete().queue();
    }
    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        try {
            Profile profile = new Profile(new ArrayList<>(Collections.singletonList(new Gray())),
                    new Coins(50), event.getMember().getUser().getName(), 0, 0, event.getMember().getUser().getAvatarUrl(), event.getGuild());
            profile.createProfile();
        } catch(IOException e){

            Logger logger = new Logger("Error in creating profile + \n");
            event.getGuild().getTextChannelsByName("ignatiamon-comments", true).get(0).sendMessage(
                    "There was an error in creating files, mention a dev to get it fixed"
            ).queue();
            System.out.println(Logger.exceptionAsString(e));
            try {
                logger.logError();
                logger.closeLogger();
            } catch (IOException exc){
                event.getGuild().getTextChannelsByName("ignatiamon-comments", true).get(0).sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n"
                ).queue();
            }
        }
    }
    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
        EmbedBuilder message = new EmbedBuilder();
        
        message.setTitle("You can't send messages privately to me!");
        event.getChannel().sendMessage(message.build()).queue();
    }
}
