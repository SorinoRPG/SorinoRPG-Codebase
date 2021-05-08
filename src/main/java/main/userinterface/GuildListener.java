package main.userinterface;

import data.Mongo;
import data.Profile;
import data.ProfileNotFoundException;
import data.logging.Logger;

import game.characters.starter.Gray;
import main.MainBot;
import main.Paginator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GuildListener extends ListenerAdapter {
    static java.util.logging.Logger logger = java.util.logging.Logger.getAnonymousLogger();

    @Override
    public void onException(@NotNull ExceptionEvent event) {
        StringWriter sw = new StringWriter();
        event.getCause().printStackTrace(new PrintWriter(sw));
        Logger logger = new Logger(sw.toString());

        try {
            logger.logError();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisconnect(@Nonnull DisconnectEvent event) {
        logger.info("Disconnected");

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
        logger.info("Joined " + event.getGuild().getName());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);
        embedBuilder.setTitle("Thank you for inviting SorinoRPG to "
                + event.getGuild().getName());
        embedBuilder.setDescription("`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "C` Creates an account **This is required to start playing SorinoRPG**\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "account_set_primary` To set the current account as the primary. All other data in other " +
                "servers will be overwritten.\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "account_remove_primary` To remove the primary account, all accounts will then update seperately " +
                "from one another.\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "P` Let's you view your, or someone else's, account details like coins, Sorino, etc\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "R` Let's you view your Rank\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "S` Searches for a Sorino or Coins\n" +
                "\n" + "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "vote` will give you the link to vote for 7,000 coins!\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "invite` will give you the link to invite to a server you own for 20 ,000 coins!\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "update` will show you the latest bot updates\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "donate <coins> @mention` to give coins to someone\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "bug <bug_you_found>` Will send the developers any bugs. Spamming or Trolling is not tolerated\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "setprefix <prefix>` to change the prefix. " +
                "\nTo change the channel where level up messages" +
                " are sent, enter `" + Prefix.guildPrefix(event.getGuild().getId()) +
                "setlevel <#channel>` You can also end the message with `OFF` so no messages are sent at all.\n\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "HHELP` Shows you how to play Heists\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "WHELP` Shows you how to use Wraps\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "GHELP` Shows you how to Gamble\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "FHELP` To understand how to use fights\n" +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "BHELP` To understand how to use Street Fight.\n"  +
                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "info HELP` To get help on info\n");
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
        embedBuilder.addField("Join our Server for coin giveaways!",
                "[Server](https://discord.gg/2sC9RmkZbJ)",
                true);

        event.getJDA().retrieveUserById(event.getGuild().getOwnerId()).queue(user -> {
            if(Profile.profileExists(user.getId())){
                try{
                    Profile profile = Profile.getProfile(user);
                    profile.setCoins(20000);
                    profile.recreate();
                } catch(Exception ignored){}
            }
            Profile userProfile = new Profile(new ArrayList<>(Collections.singletonList(new Gray())), 20000,
                    user.getId(), user.getName(), 0, 0, user.getAvatarUrl(),
                    event.getGuild().getId());
            userProfile.createProfile();
        });
        for(TextChannel channel : event.getGuild().getTextChannels()) {
            if (channel.canTalk()){
                channel.sendMessage(embedBuilder.build()).queue();
                break;
            }
        }
    }


    Map<String, String> spamControl = ExpiringMap
            .builder()
            .maxSize(10000)
            .expiration(2, TimeUnit.SECONDS)
            .expirationListener((k, v) -> {
                try {
                    logger.info( k + " || " + v);
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
            .build();
    public static Map<String, String> heistControl = ExpiringMap
            .builder()
            .maxSize(100000000)
            .expiration(1, TimeUnit.MINUTES)
            .expirationListener((k, v) -> MainBot.getJda().retrieveUserById((String) k).queue(user ->
                user.openPrivateChannel().queue(channel -> {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(0x000dff);

                    embedBuilder.setTitle("The wait is over!");
                    embedBuilder.setDescription("You may now participate in a heist.");
                    channel.sendMessage(embedBuilder.build()).queue();
            })))
            .build();
    public static Map<String, String> prison = ExpiringMap
            .builder()
            .maxSize(100000000)
            .expiration(10, TimeUnit.MINUTES)
            .expirationListener((k, v) -> MainBot.getJda().retrieveUserById((String) k).queue(user ->
                    user.openPrivateChannel().queue(channel -> {
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setColor(0x000dff);

                        embedBuilder.setTitle("Your sentence is over");
                        embedBuilder.setDescription("You may now continue using SorinoRPG in the server you were " +
                                "arrested in.");
                        channel.sendMessage(embedBuilder.build()).queue();
                    })))
            .build();
    public static Map<String, String> bugControl = ExpiringMap
            .builder()
            .maxSize(1000)
            .expiration(1, TimeUnit.HOURS)
            .build();
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        try {
            Profile profile = Profile.getProfile(event.getAuthor());
            profile.incrementXP(10, event);
        } catch (ProfileNotFoundException ignored) {}
        if(event.getMessage().getMentionedUsers().contains(event.getJDA().getSelfUser())){
            Command.HELP.userAction.action(event);
            return;
        }

        if(!Prefix.assertPrefix(event)) return;

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

        Command command = Command.getCommand(event);
        if(command == Command.ERROR) return;

        if(Mongo.mongoClient()
                .getDatabase("user")
                .getCollection("blacklist")
                .find(new Document("ID", event.getAuthor().getId())).iterator().hasNext()){
            event.getChannel().sendMessage("You have been blacklisted from SorinoRPG.").queue();
            return;
        }


        if(prison.containsKey(event.getAuthor().getId()) &&
                prison.get(event.getAuthor().getId()).equals(event.getGuild().getId())){
            if(command != Command.BRIBE) {
                event.getChannel().sendMessage(event.getAuthor().getName() +
                        " is still in prison")
                        .queue();
                return;
            }
        } else if(spamControl.containsKey(event.getAuthor().getId() + "//" + event.getGuild().getId())){
            event.getChannel().sendMessage("Calm down with the commands!").queue();
            return;
        } else if(command == Command.SLOT && gamblingControl
                .containsKey(event.getAuthor().getId() + "//" + event.getGuild().getId())){
            event.getChannel().sendMessage(event.getAuthor().getName() +
                    " has already gambled in the last 10 seconds!")
                    .queue();
            return;
        } else if(heistControl.containsKey(event.getAuthor().getId()) &&
                heistControl.get(event.getAuthor().getId()).equals(event.getGuild().getId()) &&
                command == Command.HEIST){
            event.getChannel().sendMessage(event.getAuthor().getName() +
                    " has already recently participated in a heist.").queue();
            return;
        } else if(bugControl.containsKey(event.getAuthor().getId()) &&
                command == Command.BUG){
            event.getChannel().sendMessage("You have already reported a bug recently").queue();
            return;
        }
        command.userAction.action(event);
        spamControl.put(event.getAuthor().getId() + "//" + event.getGuild().getId(),
                event.getMessage().getContentDisplay());
    }


    public static Map<String, String> appealControl = ExpiringMap
            .builder()
            .maxSize(100000000)
            .expiration(1, TimeUnit.DAYS)
            .build();

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        logger.info("Kicked from " + event.getGuild().getName() + " | " + event.getGuild().getId());

        event.getJDA().retrieveUserById(event.getGuild().getOwnerId()).queue(user -> {
            if(Profile.profileExists(user.getId())){
                try {
                    Profile profile = Profile.getProfile(user);
                    profile.setCoins(-20000);
                    profile.recreate();
                } catch (Exception ignored){}
            }
        });
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;

        String mess = event.getMessage().getContentRaw();
        if(mess.startsWith("APPEAL:")){
            if(appealControl.containsKey(event.getAuthor().getId())){
                event.getChannel().sendMessage("You must wait at least 1 day to send another appeal").queue();
                return;
            }

            String appealMessage = mess.replace("APPEAL: ", "");
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x000dff);

            embedBuilder.setTitle(event.getAuthor().getName() + " appeal");
            embedBuilder.setDescription("APPEAL: " + appealMessage);
            embedBuilder.setFooter(event.getAuthor().getId());

            Objects.requireNonNull(event.getJDA().getTextChannelById("820318304148914236"))
                    .sendMessage(embedBuilder.build()).queue();

            event.getChannel().sendMessage("Your appeal has been sent to the moderators").queue();
            appealControl.put(event.getAuthor().getId(), "");
        }
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        event.retrieveUser().queue(user -> {
            if(user.isBot()) return;
            event.retrieveMessage().queue(message -> {
                if(!message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
                    return;
                String messID = event.getMessageId();

                if(Paginator.paginators.containsKey(messID)){
                    Paginator pageinator = Paginator.paginators.get(messID);

                    if(event.getReaction().getReactionEmote().getAsCodepoints().equals("U+2b05")){
                        pageinator.previousPage();
                        message.editMessage(pageinator.currentPage.build()).queue();
                    } else if(event.getReaction().getReactionEmote().getAsCodepoints().equals("U+27a1")){
                        pageinator.nextPage();
                        message.editMessage(pageinator.currentPage.build()).queue();
                    }
                    Paginator.paginators.replace(messID, pageinator);
                }
            }, e -> {});
        });
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        event.retrieveUser().queue(user -> {
            if(user.isBot()) return;
            event.retrieveMessage().queue(message -> {
                if(!message.getAuthor().getId().equals(event.getJDA().getSelfUser().getId()))
                    return;
                String messID = event.getMessageId();

                if(Paginator.paginators.containsKey(messID)){
                    Paginator pageinator = Paginator.paginators.get(messID);

                    if(event.getReaction().getReactionEmote().getAsCodepoints().equals("U+2b05")){
                        pageinator.previousPage();
                        message.editMessage(pageinator.currentPage.build()).queue();
                    } else if(event.getReaction().getReactionEmote().getAsCodepoints().equals("U+27a1")){
                        pageinator.nextPage();
                        message.editMessage(pageinator.currentPage.build()).queue();
                    }
                    Paginator.paginators.replace(messID, pageinator);
                }
            });
        });
    }
}
