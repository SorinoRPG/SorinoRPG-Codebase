package main.userinterface;

import com.mongodb.client.MongoCollection;
import data.Mongo;
import data.Profile;
import data.ProfileNotFoundException;
import data.logging.Logger;

import game.GameSaver;
import game.fight.*;
import game.fight.streetfight.StreetFight;
import game.fight.streetfight.StreetProtector;
import game.heist.*;
import game.SorinoNotFoundException;
import game.characters.Sorino;
import game.characters.starter.Gray;

import game.items.type.Item;
import game.items.type.ItemNotFound;
import game.value.Slots;
import game.value.transfer.*;
import main.Paginator;
import main.userinterface.parser.MarketParser;
import main.userinterface.parser.StringParseException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;


public enum Command {
    HELP(event -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Help");
            embedBuilder.setColor(0x000dff);
        embedBuilder.setDescription("`" + Prefix.guildPrefix(event.getGuild().getId()) +
                "C` Creates an account **This is required to start playing SorinoRPG**\n" +
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
                "MHELP` To understand how to use the Transfer Market.\n"  +
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
            embedBuilder.addField("Our Official Server!",
                        "[Server](https://discord.gg/2sC9RmkZbJ)",
                    true);

            event.getChannel().sendMessage(embedBuilder.build()).queue();
    }),
    INFO(event -> {
        String message = event.getMessage().getContentRaw()
                .replace(Prefix.guildPrefix(event.getGuild().getId()) + "info ", "");
        if(Sorino.AllSorino.isSorino(message)) {
            try {
                Sorino sorino = Sorino.AllSorino.getSorino(message);

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle(sorino.getName());
                embedBuilder.setColor(0x000dff);

                Profile profile = Profile.getProfile(event.getAuthor());

                embedBuilder.setDescription(
                        "BASE HEALTH: " + (sorino.getHealth(profile.getLevel()) -
                                (profile.getLevel() * 10)) +
                        "\nUPGRADED HEALTH: " + sorino.getHealth(profile.getLevel()) + "\n\n" +
                        "BASE ENERGY: " + (sorino.getEnergy(profile.getLevel()) -
                                (profile.getLevel() * 10)) +
                        "\nUPGRADED ENERGY: " + sorino.getEnergy(profile.getLevel()));

                for(String moveStr : sorino.getMoves()){
                    Optional<Move> moveOptional;
                    if(moveStr.contains(" "))
                        moveOptional =
                                sorino.getMove(moveStr = moveStr.substring(0, moveStr.indexOf(" ")), sorino);
                    else moveOptional = sorino.getMove(moveStr, sorino);
                    if(moveOptional.isPresent()){
                        Move move = moveOptional.get();
                        if(move.isDefensive())
                            embedBuilder.addField(moveStr,
                                    "DAMAGE DROP-OFF: " + move.getEffect() +
                                          "\nENERGY-USE: " + move.getEnergy(),
                                    true);
                        else
                            embedBuilder.addField(moveStr,
                                    "DAMAGE: " + move.getEffect() +
                                            "\nENERGY-USE: " + move.getEnergy(),
                                    true);
                    }
                }
                event.getChannel().sendMessage(embedBuilder.build()).queue();
            } catch (SorinoNotFoundException e) {
                e.printStackTrace();
            } catch (ProfileNotFoundException e) {
                event.getChannel().sendMessage("You do not have a profile! Enter: `" +
                        Prefix.guildPrefix(event.getGuild().getId()) + "C`").queue();
            }
        }
        else if(StreetProtector.Protectors.containsProtector(message).isPresent()){
            StreetProtector protector = StreetProtector.Protectors.containsProtector(message).get();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(protector.getName());
            embedBuilder.setColor(0x000dff);
            embedBuilder.setDescription(protector.getStreetName());

            embedBuilder.addField("Guardian Sorino", protector.getGuardianSorino().getName(), true);
            embedBuilder.addField("Beginning remark", protector.getSarcasticRemark(), true);
            embedBuilder.addField("Winning remark", protector.getWinningRemark(), true);
            embedBuilder.addField("Losing remark", protector.getLosingRemark(), true);

            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } else if (message.equalsIgnoreCase("HELP")){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x000dff);
            embedBuilder.setDescription("To view the data on Sorino or Protectors, enter : `" +
                    Prefix.guildPrefix(event.getGuild().getId()) + "info <item>`");

            embedBuilder.addField("Element",
                    "Disadvantage: 45%\n" +
                          "Disadvantaged types: Rage, Smart",
                    true);
            embedBuilder.addField("Nature",
                    "Disadvantage: 10%\n" +
                    "Disadvantaged types: Element, Rage",
                    true);
            embedBuilder.addField("Rage",
                    "Disadvantage: 30%\n" +
                          "Disadvantaged type: Smart",
                    true);
            embedBuilder.addField("Smart",
                    "Disadvantage: 25%\n" +
                          "Disadvantage type: Rage",
                    true);

            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
    }),
    SEE_RANK(event -> {
        new Thread(() -> {
            Logger logger;
            try {
                event.getChannel().sendMessage(Profile.getProfile(event.getAuthor())
                        .showLevel()).queue();
            } catch (ProfileNotFoundException e) {
                event.getChannel().sendMessage("You do not have a profile! Enter: `" +
                        Prefix.guildPrefix(event.getGuild().getId()) + "C`").queue();
            }

            try {
                logger = new Logger("Shown rank");

                logger.logAction();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }).start();
    }),
    SEARCH(event -> {
            try {
                Profile profile = Profile.getProfile(event.getAuthor());
                int probabilityResult = new Random().nextInt(100);
                if (probabilityResult > 0 && probabilityResult < 25) {
                    EmbedBuilder message = new EmbedBuilder();
                    message.setColor(0x000dff);

                    int coins = new Random().nextInt(401);
                    if (coins == 0) return;
                    message.setTitle(event.getAuthor().getName() + " found something!");
                    message.setDescription(event.getMessage().getAuthor().getName()
                            + " found " + coins + " coins!");
                    message.setFooter("Nice!", event.getAuthor().getAvatarUrl());
                    event.getChannel().sendMessage(message.build()).queue();
                    profile.setCoins(coins);
                    profile.incrementXP(coins / 2, event);
                    profile.recreate();
                } else if(probabilityResult > 25 && probabilityResult < 51) {
                   Item item = Item.AllItems.randomItem();
                   profile.addItem(item);
                   item = profile.getSpecificItem(item.getName());

                   EmbedBuilder message = new EmbedBuilder();
                   message.setColor(0x000dff);
                   message.setTitle(event.getAuthor().getName() + " found something!");
                   message.setDescription(event.getMessage().getAuthor().getName()
                           + " found a " + item.getName() + "(" + item.getDuplication() + ")");
                   message.setFooter("Nice!", event.getAuthor().getAvatarUrl());


                    event.getChannel().sendMessage(message.build()).queue();
                   profile.recreate();
                } else {
                    boolean didCatch = new Random().nextInt(100) > 70;
                    Sorino sorino = Sorino.AllSorino.getRandom();
                    if(profile.getLevel() < 7 && sorino.getName().contains("Hidden")){
                        event.getChannel().sendMessage("You cannot collect Hidden " +
                                "Sorino since you haven't reached Level 7").queue();
                        return;
                    } else if(profile.getLevel() < 14 && sorino.getName().contains("Lost")){
                        event.getChannel().sendMessage("You cannot collect Lost " +
                                "Sorino since you haven't reached Level 14").queue();
                        return;
                    } else if (profile.getLevel() < 21 && sorino.getName().contains("Extinct")){
                        event.getChannel().sendMessage("You cannot collect Extinct " +
                                "Sorino since you haven't reached Level 21").queue();
                        return;
                    }
                    if (didCatch) {
                        EmbedBuilder message = new EmbedBuilder();
                        message.setColor(0x000dff);
                        message.setTitle(event.getAuthor().getName() + " found something!");
                        message.setDescription("Successfully found a " + sorino.getName());
                        message.setFooter("Nice! " + sorino.getName(),
                                event.getAuthor().getAvatarUrl());
                        event.getChannel().sendMessage(message.build()).queue();
                        profile.addSorino(sorino);
                        profile.incrementXP(50, event);
                        profile.recreate();
                    } else
                        event.getChannel().sendMessage(event.getAuthor().getName() +
                                " attempted to catch a " + sorino.getName() +
                                " but failed!").queue(ignored -> {}, e -> {});
                }
            } catch (Exception e) {
                event.getChannel().sendMessage("You do not have a profile! Enter: `" +
                        Prefix.guildPrefix(event.getGuild().getId()) + "C`").queue();
                e.printStackTrace();
            }
    }),
    FIGHT(event -> {
        String rawMessage = Prefix.removeFightPrefix(event);

        class Check {
            boolean checkMove(String input){
                try {
                    Fight fight = Fight.readFight(event.getChannel().getId());
                    if(input.contains(" ")) input = input.substring(0, input.indexOf(" "));

                    return fight.fighters.get(fight.currFighter).getMove(input,
                            fight.fighters.get(fight.currFighter)).isPresent();
                } catch (FightNotFoundException e) {
                    event.getChannel().sendMessage("You did not create a fight!").queue();

                    Logger fightLogger = new Logger(Logger.exceptionAsString(e));
                    try {
                        fightLogger.logError();
                    } catch (IOException e1){
                        e1.printStackTrace();
                    }
                    return false;
                }
            }
            Move getMove(String input){
                try {
                    Fight fight = Fight.readFight(event.getChannel().getId());
                    if(input.contains(" ")) input = input.substring(0, input.indexOf(" "));

                    if (fight.fighters.get(fight.currFighter).getMove(input,
                            fight.fighters.get(fight.currFighter)).isPresent())
                        return fight.fighters.get(fight.currFighter).getMove(input,
                                fight.fighters.get(fight.currFighter)).get();
                    else return null;
                }catch (FightNotFoundException e) {
                    event.getChannel().sendMessage("You did not create a fight!").queue();

                    Logger fightLogger = new Logger(Logger.exceptionAsString(e));
                    try {
                        fightLogger.logError();
                    } catch (IOException e1){
                        e1.printStackTrace();
                    }
                    return null;
                }
            }
        }

         if (rawMessage.equalsIgnoreCase("HELP")) {
             String p = Prefix.guildPrefix(event.getGuild().getId());

             EmbedBuilder embedBuilder = new EmbedBuilder();
             embedBuilder.setColor(0x000dff);

             embedBuilder.setTitle("Fight");

             embedBuilder.addField(p + "FSTART @mention",
                     "This will begin a fight with the mentioned user." +
                             "For example, I could enter `" + p + "FSTART @Manny`. To begin a fight with " +
                             "`@Manny`.",
                     true);
             embedBuilder.addField(p + "FEND",
                     "This will end the fight in the channel",
                     true);
             embedBuilder.addField(p + "F<SORINO>",
                     "This selects `<SORINO>` as your Sorino to battle" +
                             "**The Sorino must be listen in your Sorino**",
                     true);
             embedBuilder.addField(p + "F<MOVE>",
                     "This perform `<MOVE>` on the mentioned user." +
                             "**The move must be a move listen in your Sorinos' moves**",
                     true);

             event.getChannel().sendMessage(embedBuilder.build()).queue();

         } else if(rawMessage.toUpperCase().contains("START")){
             if(event.getMessage().getMentionedUsers().get(0).equals(event.getAuthor())){
                 event.getChannel().sendMessage("You cannot battle yourself!").queue();
                 return;
             } else if(GameSaver.exists(event.getChannel().getId())){
                 event.getChannel().sendMessage("There is an event already happening in this channel.").queue();
                 return;
             }

             Fight fight = new Fight();
             FightManager.fightPhase1(event, fight, event.getChannel().getId());
         } else if (rawMessage.toUpperCase().contains("END")) {
             try {
                 Fight fight = Fight.readFight(event.getChannel().getId());

                 if(!fight.usersID.contains(event.getAuthor().getId())){
                     event.getChannel().sendMessage("You are not a member of the fight in this channel.")
                             .queue();

                     return;
                 }

                 fight.endFight(event.getChannel().getId());
                 event.getChannel().sendMessage("The fight in this channel has ended").queue();
             }catch (FightNotFoundException e) {
                 event.getChannel().sendMessage("You did not create a fight!").queue();
                 Logger fightLogger = new Logger(Logger.exceptionAsString(e));
                 try {
                     fightLogger.logError();
                 } catch (IOException e1) {
                     e1.printStackTrace();
                 }
             }
         } else if (Sorino.AllSorino.isSorino(
                 rawMessage)) {
             try {
                 Profile profile = Profile.getProfile(event.getAuthor());
                 Sorino sorino = profile.getSpecificSorino(rawMessage);
                 Fight fight = Fight.readFight(event.getChannel().getId());
                 if(!event.getAuthor().getId().equals(fight.usersID.get(fight.phase2Calls))) return;

                 FightManager.fightPhase2(event, sorino, fight, event.getChannel().getId());
             }catch (SorinoNotFoundException e) {
                 try {
                     Logger logger = new Logger("System sorino bypass, check source code");
                     logger.logError();
                 } catch (IOException exc) {
                     Logger logger = new Logger("Error in saving fight" +
                             Logger.exceptionAsString(exc));
                     event.getChannel().sendMessage("There was a server error in saving the " +
                             "fight! Please end it!").queue();
                     try {
                         logger.logError();
                     } catch (IOException ex) {
                         ex.printStackTrace();
                     }
                 }
             } catch (ProfileNotFoundException e) {
                 event.getChannel().sendMessage("You do not have a profile! Enter: `" +
                         Prefix.guildPrefix(event.getGuild().getId()) + "C`").queue();
             } catch (FightNotFoundException e) {
                 event.getChannel().sendMessage("You did not create a fight!").queue();
                 Logger fightLogger = new Logger(Logger.exceptionAsString(e));
                 try {
                     fightLogger.logError();
                 } catch (IOException e1) {
                     e1.printStackTrace();
                 }
             }
         } else if (new Check().checkMove(rawMessage)) {
             try {
                 Fight fight = Fight.readFight(event.getChannel().getId());
                 if(!event.getAuthor().getId().equals(fight.usersID.get(fight.currFighter))) return;
                 if (FightManager.fightPhase3(new Check().getMove(rawMessage), event, fight).isPresent()) {
                     FightManager.fightPhase3(new Check().getMove(rawMessage), event, fight)
                             .ifPresent((s) -> {
                                 double cm = FightManager.fightPhase4(event, s);
                                 int winCoins = (int) Math.floor((600) * cm);
                                 int loseCoins = (int) Math.floor((-150) * cm);
                                 event.getJDA().retrieveUserById(s.getWinner()).queue(user -> {
                                     EmbedBuilder message = new EmbedBuilder();
                                     message.setColor(0x000dff);
                                     message.setTitle("WINNER");
                                     message.setThumbnail(user.getAvatarUrl());
                                     event.getJDA().retrieveUserById(s.getLoser()).queue(user1 -> {
                                                 message.setDescription(user.getName() + " has won " + winCoins
                                                         + " coins for beating " + user1.getName());
                                                 message.setFooter( "has won " + winCoins
                                                                 + " coins for beating " + user1.getName(),
                                                         user.getAvatarUrl());

                                                 event.getChannel().sendMessage(message.build()).queue();
                                             }
                                     );
                                 });
                                 event.getJDA().retrieveUserById(s.getLoser()).queue(user -> {
                                     EmbedBuilder message = new EmbedBuilder();
                                     message.setColor(0x000dff);
                                     message.setTitle("LOSER");
                                     message.setThumbnail(user.getAvatarUrl());
                                     event.getJDA().retrieveUserById(s.getWinner()).queue(user1 -> {
                                                 message.setDescription(user.getName() + " has lost " + (loseCoins - (loseCoins * 2))
                                                         + " coins for losing to " + user1.getName());
                                                 message.setFooter("has lost " + (loseCoins - (loseCoins * 2))
                                                                 + " coins for losing to " + user1.getName(),
                                                         user.getAvatarUrl());
                                                 event.getChannel().sendMessage(message.build()).queue();
                                                 fight.endFight(event.getChannel().getId());
                                             }
                                     );
                                 });
                             });
                 } else fight.saveFight(event.getChannel().getId());
             } catch (FightNotFoundException e) {
                 event.getChannel().sendMessage("You did not create a fight!").queue();
                 Logger fightLogger = new Logger(Logger.exceptionAsString(e));
                 try {
                     fightLogger.logError();
                 } catch (IOException e1) {
                     e1.printStackTrace();
                 }
             }
         }
    }),
    HEIST(event -> {
        if(!event.getAuthor().getAsTag().equals("Manny#6363"))
            event.getChannel().sendMessage("Sorry! Heists are being revamped, please come back soon!").queue();

        String p = Prefix.guildPrefix(event.getGuild().getId());
        String command = event.getMessage().getContentRaw().replace(p
                        + "H", "");
        String commandArgument = command.substring(command.lastIndexOf(" ")).strip();

        if(command.equalsIgnoreCase("HELP")){
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(0x00dff);
            embed.addField(p + "HCHOOSE <HEIST>",
                    "This will allow you to the selected <HEIST>. If no heist is selected, or the heist selected " +
                            "does not exist, then a list of heists will be shown.",
                    true);
            embed.addField(p + "HJOIN",
                     "This will allow you to join the current heist in a channel, YOU CANNOT JOIN A HEIST AFTER IT HAS " +
                             "ALREADY STARTED",
                    true);
            embed.addField(p + "HKICK @mention",
                    "This will allow you to remove @mention from the current heist in a channel, only the heist " +
                            "leader can do this, and you cannot kick someone after the heist has started.",
                    true);
            embed.addField(p + "HCUT @mention <PERCENT>",
                    "This will allow you to set @mention's cut of the heist rewards to a <PERCENT>. " +
                            "You cannot set cuts during setup heists, or after a heist has started. The cuts are set " +
                            "by the heist leader, and cannot add up to over 100.",
                    true);
            embed.addField(p + "HSTART",
                    "This will start the heist.",
                    true);
            embed.addField(p + "HUSE <Item>",
                    "This will allow you to use <Item> in a heist.",
                    true);

        } else if(command.toUpperCase().startsWith("CHOOSE")){
            if(GameSaver.exists(event.getChannel().getId())){
                event.getChannel().sendMessage("There is already an event in this channel, please try again later, " +
                        "or in a different channel.").queue();
                return;
            }
            String choice = command.replace("CHOOSE ", "");
            Heist heist = Heist.HeistUtil.getHeist(choice);

            if(heist == null){
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Available Heists");
                embed.setColor(0x000dff);

                Paginator paginator = new Paginator(event.getAuthor().getName() + " is choosing their Sorino",
                        0x000dff);
                for(Heist heist1 : Heist.HeistUtil.getHeists()){
                    if(embed.getFields().size() % 6 == 0 && embed.getFields().size() != 0){
                        paginator.addPage(embed);
                        embed = new EmbedBuilder();
                        embed.setTitle("Available Heists");
                        embed.setColor(0x000dff);
                    }

                    embed.addField(heist1.getName() + " | " + heist1.getType().getName() ,
                            heist1.getDesc() + p + "H" + heist1.getName(),
                            true);
                }


                if(paginator.embeds.size() == 0) {
                    event.getChannel().sendMessage(embed.build()).queue();
                    return;
                }
                paginator.addPage(embed);
                paginator.paginate();

                if(paginator.isSinglePage){
                    event.getChannel().sendMessage(paginator.currentPage.build()).queue();
                } else {
                    event.getChannel().sendMessage(paginator.currentPage.build()).queue(message1 -> {
                     Paginator.paginators.put(message1.getId(), paginator);
                     message1.addReaction("\u2B05").queue();
                     message1.addReaction("\u27A1").queue();
                    });
                }
            } else {
                heist.initialize(event.getChannel().getId(), new Participant(event.getAuthor(),
                        Participant.HeistPosition.LEADER, 100));
                heist.save();

                event.getChannel().sendMessage(heist.heistState()).queue();
            }
        } else if(command.toUpperCase().startsWith("JOIN")){
            try {
                Heist heist = Heist.HeistUtil.getHeistById(event.getChannel().getId());
                if(heist.getAllParticipants().size() == heist.getMaxPlayers()){
                    event.getChannel().sendMessage("This heist is full at the moment!").queue();
                    return;
                }

                heist.addParticipant(new Participant(event.getAuthor(), Participant.HeistPosition.PARTICIPANT,
                        0));
                heist.generateNewCut();
                heist.save();
                event.getChannel().sendMessage(heist.heistState()).queue();
            } catch (HeistNotFoundException e) {
                event.getChannel().sendMessage("There was no heist found with the channel ID of: " +
                        e.getChannelID()).queue();
            }
        } else if(command.toUpperCase().startsWith("KICK")){
            try {
                Heist heist = Heist.HeistUtil.getHeistById(event.getChannel().getId());
                Participant participant = heist.getParticipant(event.getMessage().getMentionedUsers().get(0).getId());
                if(participant == null){
                    event.getChannel().sendMessage("This participant does not exist!").queue();
                    return;
                }
                heist.removeParticipant(participant);
                heist.generateNewCut();
                heist.save();

                event.getChannel().sendMessage(heist.heistState()).queue();
            } catch (HeistNotFoundException e) {
                event.getChannel().sendMessage("There was no heist found with the channel ID of: " +
                        e.getChannelID()).queue();
            } catch (IndexOutOfBoundsException e){
                event.getChannel().sendMessage("You need to ping a user!").queue();
            }
        } else if(command.toUpperCase().startsWith("CUT")){
            try {
                int cut = Integer.parseInt(commandArgument);
                if(cut > 100){
                    event.getChannel().sendMessage("The cut cannot be larger than 100!").queue();
                    return;
                }

                Heist heist = Heist.HeistUtil.getHeistById(event.getChannel().getId());
                Participant former = heist.getParticipant(event.getMessage().getMentionedUsers().get(0).getId());
                if(former == null){
                    event.getChannel().sendMessage("The person you pinged is not in the heist!").queue();
                    return;
                }

                Participant latter = new Participant(former.userID, former.userName, former.heistPosition, cut);
                heist.replaceParticipant(former, latter);
                heist.save();

                event.getChannel().sendMessage(heist.heistState()).queue();
            } catch (HeistNotFoundException e) {
                event.getChannel().sendMessage("There was no heist found with the channel ID of: " +
                        e.getChannelID()).queue();
            } catch (IndexOutOfBoundsException e){
                event.getChannel().sendMessage("You need to ping a user!").queue();
            }
        } else if(command.equalsIgnoreCase("START")){
            try {
                Heist heist = Heist.HeistUtil.getHeistById(event.getChannel().getId());
                heist.start();
                heist.save();

                Stage stage = heist.getCurrentStage();
                event.getChannel().sendMessage(stage.option()).queue();
            } catch (HeistNotFoundException e) {
                event.getChannel().sendMessage("There was no heist found with the channel ID of: " +
                        e.getChannelID()).queue();
            } catch (HeistStartupException e) {
                event.getChannel().sendMessage(e.getReason()).queue();
            }
        } else if(command.toUpperCase().startsWith("USE")){
            try {
                Heist heist = Heist.HeistUtil.getHeistById(event.getChannel().getId());
                Participant participant = heist.getParticipant(event.getAuthor().getId());

                Item item = participant.toProfile()
                        .getSpecificItem(commandArgument);
                Stage stage = heist.getCurrentStage();
                Stage.Result result = stage.result(item);

                event.getChannel().sendMessage(result.embed).queue();

                switch (result.resultInteger){
                    case 0xA: {
                        heist.nextStage();
                        event.getChannel().sendMessage(stage.option()).queue();
                        heist.save();
                    }
                    case 0xB: {
                        for(Participant participant1 : heist.getAllParticipants()){
                            if(participant1 == participant) continue;
                            GuildListener.heistControl.put(participant1.userID, heist.getName());
                        }
                        GuildListener.prison.put(participant.userID, heist.getName());
                        heist.delete();
                    }
                    case 0xC: {
                        heist.reward();
                        heist.delete();
                    }
                }
            } catch (HeistNotFoundException e) {
                event.getChannel().sendMessage("There was no heist found with the channel ID of: " +
                        e.getChannelID()).queue();
            } catch (ItemNotFound itemNotFound) {
                event.getChannel().sendMessage("There were no items found names " + itemNotFound.getItem() +
                        " that you own.").queue();
            }
        }
    }),
    CREATE_PROFILE(event -> {
        if(Mongo.mongoClient().getDatabase("user")
                .getCollection("account")
                .find(new Document("userID", event.getAuthor().getId())).iterator().hasNext()){
            event.getChannel().sendMessage("You already have an account!").queue();
            return;
        }
        Profile userProfile = new Profile(new ArrayList<>(Collections.singletonList(new Gray())), 50,
                event.getAuthor().getId(), event.getAuthor().getName(), 0, 0, event.getAuthor().getAvatarUrl(),
                0, new ArrayList<>());
        userProfile.createProfile();
        EmbedBuilder message = new EmbedBuilder();
        message.setColor(0x000dff);

        message.setTitle(event.getAuthor().getName() + "'s Profile");
        message.setImage(event.getAuthor().getAvatarUrl());
        message.setDescription(userProfile.toString());
        message.setFooter("Created a profile", event.getAuthor().getAvatarUrl());

        event.getChannel().sendMessage(message.build()).queue();
    }),
    SEE_PROFILE(event -> {
        try {
            if(event.getMessage().getMentionedUsers().size() == 1){
                Profile profile = Profile.getProfile(event.getMessage().getMentionedUsers().get(0));

                EmbedBuilder message = new EmbedBuilder();
                message.setColor(0x000dff);

                message.setTitle(event.getMessage().getMentionedUsers()
                        .get(0).getName() + "'s Profile");
                message.setImage(event.getMessage().getMentionedUsers().get(0).getAvatarUrl());
                message.setDescription(profile.toString());
                message.setFooter("Viewed " +
                                event.getMessage().getMentionedUsers().get(0).getName() + "'s profile",
                        event.getAuthor().getAvatarUrl());

                event.getChannel().sendMessage(message.build()).queue();
                return;
            }
        }catch (ProfileNotFoundException e) {
            event.getChannel().sendMessage("You do not have a profile! Enter: `" +
                    Prefix.guildPrefix(event.getGuild().getId()) + "C`").queue();
        }


        try {
            Profile profile = Profile.getProfile(event.getAuthor());
            EmbedBuilder message = new EmbedBuilder();
            message.setColor(0x000dff);

            message.setTitle(event.getAuthor().getName() + "'s Profile");
            message.setImage(event.getAuthor().getAvatarUrl());
            message.setDescription(profile.toString());
            message.setFooter("Requested to see their profile", event.getAuthor().getAvatarUrl());

            event.getChannel().sendMessage(message.build()).queue();
        } catch (ProfileNotFoundException e) {
            event.getChannel().sendMessage("You do not have a profile! Enter: `" +
                    Prefix.guildPrefix(event.getGuild().getId()) + "C`").queue();
        }
    }),
    WRAP(event -> {
        event.getChannel().sendMessage("Apologies, but Wraps have been removed until further notice.").queue();
    }),
    CHANGE(event -> {
        event.getGuild().retrieveMember(event.getAuthor()).queue( member -> {
            if (!member.hasPermission(Permission.MESSAGE_MANAGE)){
                event.getChannel().sendMessage("You do not have the permission to do such a command!")
                        .queue();
                return;
            }

            if(event.getMessage().getContentRaw().contains("setlevel")){
                MongoCollection<Document> collection = Mongo.mongoClient()
                        .getDatabase("guild")
                        .getCollection("channel");
                String id;

                if(event.getMessage().getContentRaw().contains("OFF")) id = "OFF";
                else id = event.getMessage().getMentionedChannels().get(0).getId();
                if(collection.find(new Document("guildID", event.getGuild().getId())).iterator().hasNext()){
                    collection.replaceOne(new Document("guildID", event.getGuild().getId()),
                            new Document("guildID", event.getGuild().getId())
                                    .append("channel", id));
                    return;
                }
                collection.insertOne(new Document("guildID", event.getGuild().getId())
                                .append("channel", id));
            }
            else if (event.getMessage().getContentRaw().contains("setprefix")){
                if(!event.getMessage().getContentRaw().contains(" ")) return;
                String newPrefix = event.getMessage().getContentRaw().substring(
                        event.getMessage().getContentRaw().indexOf(" ") + 1
                );
                MongoCollection<Document> collection = Mongo.mongoClient()
                        .getDatabase("guild")
                        .getCollection("prefix");
                if(collection.find(new Document("guildID", event.getGuild().getId())).iterator().hasNext()){
                    collection.replaceOne(new Document("guildID", event.getGuild().getId()),
                            new Document("guildID", event.getGuild().getId())
                            .append("prefix", newPrefix));
                    return;
                }
                collection.insertOne(new Document("guildID", event.getGuild().getId())
                        .append("prefix", newPrefix));
                event.getGuild().getSelfMember().modifyNickname("[" + newPrefix + "] SorinoRPG").queue();
            }
        });
    }),
    SLOT(event -> {
        try {
            int stake = Integer.parseInt(event.getMessage().getContentRaw().substring(
                    event.getMessage().getContentRaw().indexOf(" ")+1
            ));

            Profile profile = Profile.getProfile(event.getAuthor());
            if(!profile.spend(stake)) {
                event.getChannel().sendMessage(event.getAuthor().getName() + " has insufficient funds!")
                        .queue();
                return;
            } else if(stake < 0) return;
            else if(stake >= 200000){
                event.getChannel().sendMessage("You can't gamble 200,000 coins or more at once!")
                        .queue();
                return;
            }
            profile.recreate();

            Slots slots = new Slots();
            String[] randoms = slots.random();

            EmbedBuilder slot1 = new EmbedBuilder();
            slot1.setColor(0x000dff);
            slot1.setTitle("Slot 1");
            slot1.setImage(randoms[0]);

            EmbedBuilder slot2 = new EmbedBuilder();
            slot2.setColor(0x000dff);
            slot2.setTitle("Slot 2");
            slot2.setImage(randoms[1]);

            EmbedBuilder slot3 = new EmbedBuilder();
            slot3.setColor(0x000dff);
            slot3.setTitle("Slot 3");
            slot3.setImage(randoms[2]);

            event.getChannel().sendMessage(slot1.build()).queue();
            event.getChannel().sendMessage(slot2.build()).queue();
            event.getChannel().sendMessage(slot3.build()).queue();

            if(Slots.checkSame(randoms)){
                Slots.Slot slot = slots.getInfo(randoms[1]);
                slot.action(profile, stake, event.getChannel());
                profile.recreate();
            } else {
                if(Arrays.asList(randoms).contains(Slots.Slot.SAPPHIRES.img())){
                    profile.setCoins((int) (stake * 1.5));
                    profile.recreate();
                    event.getChannel().sendMessage(
                            event.getAuthor().getName() + " found a Sapphire and will receive " +
                                    stake * 1.5  + " coins!"
                    ).queue();
                    return;
                } else if (Arrays.asList(randoms).contains(Slots.Slot.BLUE_BEARS.img())){
                    profile.setCoins((int) (stake * 1.25));
                    profile.recreate();
                    event.getChannel().sendMessage(
                            event.getAuthor().getName() + " found a Blue Bear and will receive " +
                                    (int) (stake * 1.25) + " coins!"
                    ).queue();
                    return;
                }
                event.getChannel().sendMessage(event.getAuthor().getName() + " lost " + stake
                + " coins").queue();
            }
            GuildListener.gamblingControl.put(event.getAuthor().getId() + "//" + event.getGuild().getId(),
                    System.currentTimeMillis());
            return;
        }catch (NumberFormatException ignored){}
        catch (Exception e) {
            Logger logger1 =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "You don't have a profile! Enter: `" + Prefix.guildPrefix(event.getGuild().getId()) +
                            "C`\n" +
                            "If you do have an account, join the support server!"
            ).queue();
            try{
                logger1.logError();
            } catch (IOException excI){
                excI.printStackTrace();
            }
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);
        embedBuilder.setTitle("Slots");
        embedBuilder.setDescription("If I wanted to " +
                "play with 100 coins I would enter `" +
                Prefix.guildPrefix(event.getGuild().getId()) + "G 100`.");
        embedBuilder.addField("3 Stars",
                "This will get you a random Sorino, no matter the type!",
                true);
        embedBuilder.addField("3 Bananas",
                "This give you 1.25x your stake!",
                true);
        embedBuilder.addField("3 Cherries",
                "This will give you 1.5x your stake!",
                true);
        embedBuilder.addField("3 Bears",
                "This will give you 1.75x your stake!",
                true);
        embedBuilder.addField("3 Blue Bears",
                "This will give you 2x your stake! Finding one will get you 1.25x your stake!",
                true);
        embedBuilder.addField("3 Sapphires",
                "This will give you 4x your stake! Finding one will get you 1.25x your stake!",
                true);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }),
    STREET_FIGHT(event -> {
        String mainCommand = event.getMessage()
                .getContentRaw().replace(Prefix.guildPrefix(event.getGuild().getId()),
                        "");

        String command = mainCommand.toUpperCase().substring(1);
        class MoveFight {
            final Move move;
            final StreetFight fight;

            MoveFight(Move move, StreetFight fight){
                this.move = move;
                this.fight = fight;
            }
        }
        Function<GuildMessageReceivedEvent, Optional<Move>> checkMove = event1 -> {
            try{
                StreetFight fight = StreetFight.readFight(event1.getChannel().getId());
                Sorino sorino = fight.userSorino.get(0);
                if(command.contains(" "))
                    return sorino.getMove(command.substring(0, command.indexOf(" ")), sorino);
                return sorino.getMove(command, sorino);
            } catch (FightNotFoundException e){
                return Optional.empty();
            }
        };
        Function<MoveFight, Optional<Fight.GameInfo>> moveFunction = moveFight -> {
            StreetFight fight = moveFight.fight;
            Move move = moveFight.move;

            if(fight.opponents.get(1).hasConceded())
                return Optional.of(new Fight.GameInfo("Protector", "User"));
            else if(fight.opponents.get(0).hasConceded())
                return Optional.of(new Fight.GameInfo("User", "Protector"));

            if(move.isDefensive()){
                fight.opponents.get(1).defenseUp(move, event);
                if(fight.opponents.get(1).hasConceded())
                    return Optional.of(new Fight.GameInfo("Protector", "User"));
                else if(fight.opponents.get(0).hasConceded())
                    return Optional.of(new Fight.GameInfo("User", "Protector"));

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(0x000dff);
                embedBuilder.setThumbnail(move.getUrl());
                embedBuilder.setAuthor(event.getAuthor().getName() + " gained " + move.getEffect() + " defence");
                embedBuilder.setFooter(move.getDesc(), event.getAuthor().getAvatarUrl());


                event.getChannel().sendMessage(embedBuilder.build()).queue();

                String ranMove = fight.protector
                        .getGuardianSorino()
                        .getMoves()
                        .get(new Random().nextInt(4));
                if(ranMove.contains(" ")) ranMove = ranMove.substring(0, ranMove.indexOf(" "));

                if(fight.protector.getGuardianSorino().getMove(
                        ranMove,
                        fight.protector.getGuardianSorino()).isPresent()){
                    Move protectorMove = fight.protector.getGuardianSorino().getMove(
                            ranMove,
                            fight.protector.getGuardianSorino()).get();
                    if(protectorMove.isDefensive()){
                        fight.opponents.get(0).defenseUp(protectorMove, event);
                        if(fight.opponents.get(1).hasConceded())
                            return Optional.of(new Fight.GameInfo("Protector", "User"));
                        else if(fight.opponents.get(0).hasConceded())
                            return Optional.of(new Fight.GameInfo("User", "Protector"));

                        EmbedBuilder embedBuilder1 = new EmbedBuilder();
                        embedBuilder1.setColor(0x000dff);
                        embedBuilder1.setThumbnail(protectorMove.getUrl());
                        embedBuilder1.setAuthor(fight.protector.getName()
                                + " gained " + protectorMove.getEffect() + " defence");
                        embedBuilder1.setFooter(protectorMove.getDesc());

                        event.getChannel().sendMessage(embedBuilder1.build()).queue();
                    } else {
                        fight.opponents.get(1).takeDamage(protectorMove, event);
                        fight.opponents.get(0).dropEnergy(protectorMove);
                        if(fight.opponents.get(1).hasConceded())
                            return Optional.of(new Fight.GameInfo("Protector", "User"));
                        else if(fight.opponents.get(0).hasConceded())
                            return Optional.of(new Fight.GameInfo("User", "Protector"));

                        EmbedBuilder embedBuilder2 = new EmbedBuilder();
                        embedBuilder2.setColor(0x000dff);
                        embedBuilder2.setThumbnail(protectorMove.getUrl());
                        embedBuilder2.setAuthor(event.getAuthor().getName() +
                                " lost " + protectorMove.getEffect() + " health");
                        embedBuilder2.setFooter(protectorMove.getDesc());

                        event.getChannel().sendMessage(embedBuilder2.build())
                                .queue();
                    }
                }
            } else {
                fight.opponents.get(0).takeDamage(move, event);
                fight.opponents.get(1).dropEnergy(move);
                if(fight.opponents.get(1).hasConceded())
                    return Optional.of(new Fight.GameInfo("Protector", "User"));
                else if(fight.opponents.get(0).hasConceded())
                    return Optional.of(new Fight.GameInfo("User", "Protector"));


                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(0x000dff);
                embedBuilder.setThumbnail(move.getUrl());
                embedBuilder.setAuthor(fight.protector.getName() + " lost " + move.getEffect() + " health");
                embedBuilder.setFooter(move.getDesc());

                event.getChannel().sendMessage(embedBuilder.build()).queue();

                String ranMove = fight.protector
                        .getGuardianSorino()
                        .getMoves()
                        .get(new Random().nextInt(4));
                if(ranMove.contains(" ")) ranMove = ranMove.substring(0, ranMove.indexOf(" "));

                if(fight.protector.getGuardianSorino().getMove(
                        ranMove,
                        fight.protector.getGuardianSorino()
                ).isPresent()){
                    Move protectorMove = fight.protector.getGuardianSorino().getMove(
                            ranMove,
                            fight.protector.getGuardianSorino()
                    ).get();
                    if(protectorMove.isDefensive()){
                        fight.opponents.get(0).defenseUp(protectorMove, event);
                        if(fight.opponents.get(1).hasConceded())
                            return Optional.of(new Fight.GameInfo("Protector", "User"));
                        else if(fight.opponents.get(0).hasConceded())
                            return Optional.of(new Fight.GameInfo("User", "Protector"));

                        EmbedBuilder embedBuilder1 = new EmbedBuilder();
                        embedBuilder1.setColor(0x000dff);
                        embedBuilder1.setThumbnail(protectorMove.getUrl());
                        embedBuilder1.setAuthor(fight.protector.getName()
                                + " gained " + protectorMove.getEffect() + " defence");
                        embedBuilder1.setFooter(protectorMove.getDesc());

                        event.getChannel().sendMessage(embedBuilder1.build()).queue();
                    } else {
                        fight.opponents.get(1).takeDamage(protectorMove, event);
                        fight.opponents.get(0).dropEnergy(protectorMove);
                        if(fight.opponents.get(1).hasConceded())
                            return Optional.of(new Fight.GameInfo("Protector", "User"));
                        else if(fight.opponents.get(0).hasConceded())
                            return Optional.of(new Fight.GameInfo("User", "Protector"));

                        EmbedBuilder embedBuilder2 = new EmbedBuilder();
                        embedBuilder2.setColor(0x000dff);
                        embedBuilder2.setThumbnail(protectorMove.getUrl());
                        embedBuilder2.setAuthor(event.getAuthor().getName() +
                                " lost " + protectorMove.getEffect() + " health");
                        embedBuilder2.setFooter(protectorMove.getDesc());

                        event.getChannel().sendMessage(embedBuilder2.build())
                                .queue();
                    }
                }
            }


            if(fight.opponents.get(1).hasConceded())
                return Optional.of(new Fight.GameInfo("Protector", "User"));
            else if(fight.opponents.get(0).hasConceded())
                return Optional.of(new Fight.GameInfo("User", "Protector"));

            EmbedBuilder embed  = new EmbedBuilder();
            embed.setTitle("Health of the Fighters!");
            embed.setColor(0x000dff);
            embed.addField(fight.protector.getName() + "'s Health",
              "HEALTH: " + fight.opponents.get(0).getHealth() + "\n" +
                    "ENERGY: " + fight.opponents.get(0).getEnergy() + "\n"+
                    "DEFENCE: " + fight.opponents.get(0).getDecrease() + " drop-off",
                    true);
            embed.addField(event.getAuthor().getName() + "'s Health",
                    "HEALTH: " + fight.opponents.get(1).getHealth() + "\n" +
                    "ENERGY: " + fight.opponents.get(1).getEnergy() + "\n"+
                    "DEFENCE: " + fight.opponents.get(1).getDecrease(),
                    true);
            embed.addField("Enter your move " + event.getAuthor().getName(),
                    "Moves: " + fight.userSorino.get(0).getMoves().toString()
                            + "\n" +
                            "To enter a move, type `" + Prefix.guildPrefix(event.getGuild().getId()) +
                            "B<MOVE>`, `<MOVE>` being the move you want to use.",
                    false);
            embed.setFooter(" needs to enter their move", event.getAuthor().getAvatarUrl());

            event.getChannel().sendMessage(embed.build())
                    .queue();

            return Optional.empty();
        };
        try {
            if(command.toUpperCase().contains("START")){

                if(GameSaver.exists(event.getChannel().getId())){
                    event.getChannel().sendMessage("There is a battle going on currently in this channel.").queue();
                    return;
                }

                List<StreetProtector> protectors = StreetProtector.Protectors.getAllProtectors();

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Choose a Street Protector to Battle");
                embedBuilder.setColor(0x000dff);

                Paginator paginator = new Paginator(DateTimeFormatter.ofPattern("HH:mm").format(LocalDateTime.now())
                        + " GMT", 0x000dff);
                for(StreetProtector protector : protectors) {
                    if(embedBuilder.getFields().size() % 6 == 0 && embedBuilder.getFields().size() != 0){
                        paginator.addPage(embedBuilder);
                        embedBuilder = new EmbedBuilder();
                        embedBuilder.setTitle("Choose a Street Protector to Battle");
                    }
                    embedBuilder.addField(protector.getName() + ": " + protector.getStreetName(),
                            "Guardian Sorino: " + protector.getGuardianSorino().getName() + "\n" +
                                    "Enter: `" +
                                    Prefix.guildPrefix(event.getGuild().getId()) + "B" + protector.getName() +
                                    "` to battle " + protector.getName(),
                            true);
                }
                if(paginator.embeds.size() == 0) {
                    event.getChannel().sendMessage(embedBuilder.build()).queue();
                    return;
                }
                paginator.addPage(embedBuilder);
                paginator.paginate();

                if(paginator.isSinglePage){
                    event.getChannel().sendMessage(paginator.currentPage.build()).queue();
                } else {
                    event.getChannel().sendMessage(paginator.currentPage.build()).queue(message -> {
                        Paginator.paginators.put(message.getId(), paginator);
                        message.addReaction("\u2B05").queue();
                        message.addReaction("\u27A1").queue();
                    });
                }

            } else if(StreetProtector.Protectors.containsProtector(command).isPresent()){
                StreetProtector protector = StreetProtector.Protectors.containsProtector(command).get();


                StreetFight streetFight =
                        new StreetFight(protector);


                EmbedBuilder message = new EmbedBuilder();
                message.setTitle(protector.getName() + " says: " + protector.getSarcasticRemark());
                message.setColor(0x000dff);
                event.getChannel().sendMessage(message.build()).queue();

                message = new EmbedBuilder();
                message.setColor(0x000dff);

                message. setTitle("Specify your Sorino " + event.getAuthor().getName());
                message.setFooter("is choosing their Sorino", event.getAuthor().getAvatarUrl());
                message.setDescription("Choose one of your Sorino");

                Paginator paginator = new Paginator(event.getAuthor().getName() + " is choosing their Sorino",
                        0x000dff);
                try {
                    for (Sorino sorino : Profile.getProfile(event.getAuthor()).getSorinoAsList()) {
                        if(message.getFields().size() % 6 == 0 && message.getFields().size() != 0){
                            paginator.addPage(message);
                            message = new EmbedBuilder();
                            message.setTitle("Specify your Sorino " + event.getAuthor().getName());
                            message.setDescription("Choose one of your Sorino");
                        }
                        message.addField(sorino.getName(),
                                "HEALTH: " + sorino.getHealth(Profile.getProfile(event.getAuthor()).getLevel()) +
                                        "\nENERGY: " + sorino.getEnergy(Profile.getProfile(event.getAuthor()).getLevel())
                                        + "\nTo choose this Sorino, enter `" + Prefix.guildPrefix(event.getGuild().getId())
                                        + "B" + sorino.getName().substring(0, sorino.getName().indexOf(":")) + "`",
                                true);
                    }

                    if(paginator.embeds.size() == 0) {
                        event.getChannel().sendMessage(message.build()).queue();
                        return;
                    }
                    paginator.addPage(message);
                    paginator.paginate();

                    if(paginator.isSinglePage){
                        event.getChannel().sendMessage(paginator.currentPage.build()).queue();
                    } else {
                        event.getChannel().sendMessage(paginator.currentPage.build()).queue(message1 -> {
                            Paginator.paginators.put(message1.getId(), paginator);
                            message1.addReaction("\u2B05").queue();
                            message1.addReaction("\u27A1").queue();
                        });
                    }
                    streetFight.saveFight(event.getChannel().getId());
                }catch (ProfileNotFoundException e) {
                    event.getChannel().sendMessage("You do not have a profile! Enter: `" +
                            Prefix.guildPrefix(event.getGuild().getId()) + "C`").queue();
                }
            } else if(Sorino.AllSorino.isSorino(command)){
                StreetFight fight = StreetFight.readFight(event.getChannel().getId());
                fight.userSorino.add(Sorino.AllSorino.getSorino(command));
                fight.opponents.add(new Opponent(fight.protector.getGuardianSorino(), event));
                fight.opponents.add(new Opponent(fight.userSorino.get(0), event));

                EmbedBuilder message = new EmbedBuilder();
                message.setColor(0x000dff);

                message.setTitle("Enter your move " + event.getAuthor().getName());
                message.addField("Moves: ", fight.userSorino.get(0).getMoves().toString()
                        + "\n" +
                        "To enter a move, type `" + Prefix.guildPrefix(event.getGuild().getId()) +
                        "B<MOVE>`, `<MOVE>` being the move you want to use.", false);
                message.setFooter("Needs to enter their move", event.getAuthor().getAvatarUrl());

                event.getChannel().sendMessage(message.build())
                        .queue();
                fight.saveFight(event.getChannel().getId());
            } else if(checkMove.apply(event).isPresent()){
                Move move = checkMove.apply(event).get();
                StreetFight fight = StreetFight.readFight(event.getChannel().getId());

                Optional<Fight.GameInfo> gameInfo = moveFunction.apply(new MoveFight(move, fight));
                fight.saveFight(event.getChannel().getId());
                gameInfo.ifPresent(gameInfo1 -> {
                    if(gameInfo1.winner.equals("Protector")){
                        try {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setColor(0x000dff);
                            embedBuilder.setTitle("You lost to: " + fight.protector.getName());
                            embedBuilder.setDescription(fight.protector.getWinningRemark());
                            event.getChannel().sendMessage(embedBuilder.build()).queue();

                            Profile profile = Profile.getProfile(event.getAuthor());
                            profile.incrementLoss();
                            profile.incrementXP(50, event);
                            profile.recreate();
                        } catch (Exception e) {
                            Logger logger1 =
                                    new Logger("Error in finding Profile due to IO and Classes \n" +
                                            Logger.exceptionAsString(e));
                            event.getChannel().sendMessage(
                                    "You don't have a profile! Enter: `" + Prefix.guildPrefix(event.getGuild().getId()) +
                                            "C`\n" +
                                            "If you do have an account, join the support server!"
                            ).queue();
                            try{
                                logger1.logError();
                            } catch (IOException excI){
                                excI.printStackTrace();
                            }
                        }
                    } else {
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setColor(0x000dff);
                        embedBuilder.setTitle("You beat: " + fight.protector.getName());
                        embedBuilder.setDescription(fight.protector.getLosingRemark());
                        embedBuilder.setFooter("You also received 10,000 coins!");
                        event.getChannel().sendMessage(embedBuilder.build()).queue();

                        try {
                            Profile profile = Profile.getProfile(event.getAuthor());

                            profile.incrementXP(400, event);
                            profile.incrementWin();
                            profile.addSorino(fight.protector.getGuardianSorino());
                            profile.setCoins(10000);
                            profile.recreate();
                        }  catch (Exception e) {
                            Logger logger1 =
                                    new Logger("Error in finding Profile due to IO and Classes \n" +
                                            Logger.exceptionAsString(e));
                            event.getChannel().sendMessage(
                                    "You don't have a profile! Enter: `" + Prefix.guildPrefix(event.getGuild().getId()) +
                                            "C`\n" +
                                            "If you do have an account, join the support server!"
                            ).queue();
                            try{
                                logger1.logError();
                            } catch (IOException excI){
                                excI.printStackTrace();
                            }
                        }
                    }
                    fight.endFight(event.getChannel().getId());
                });
            } else if(command.toUpperCase().contains("END")){
                event.getChannel().sendMessage(event.getAuthor().getName() + " has ended his Street Fight!")
                        .queue();
                StreetFight.readFight(event.getChannel().getId())
                        .endFight(event.getChannel().getId());
            } else if(command.equalsIgnoreCase("HELP")){
                String p = Prefix.guildPrefix(event.getGuild().getId());

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(0x000dff);

                embedBuilder.setTitle("Street Fight");

                embedBuilder.addField(p + "BSTART",
                        "This allows you to begin a Street Fight",
                        true);
                embedBuilder.addField(p + "B<PROTECTOR>",
                        "This allows you to start a street fight with `<PROTECTOR>`." +
                                "If I enter `" + p + "BXerxes`. I will enter a Street Fight with " +
                                "`Xerxes`.",
                        true);
                embedBuilder.addField(p + "B<SORINO>",
                        "This allows you to select a Sorino. If I enter `" + p + "BGray`, I would be selecting " +
                                "`Gray` as my Sorino.",
                        true);
                embedBuilder.addField(p + "BEND",
                        "This ends a current Street Fight",
                        true);

                event.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        } catch (Exception e) {
            Logger logger1 =
                    new Logger("Error: " +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "There was an error, please try again!"
            ).queue();
            try{
                logger1.logError();
            } catch (IOException excI){
                excI.printStackTrace();
            }
        }
    }),
    UPDATE(event -> {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("SorinoRPG is currently v0.3");
        embedBuilder.setDescription("[Update Log](https://sorinorpg.github.io/Sorino-Update-Log/)");
        embedBuilder.setThumbnail(event.getAuthor().getAvatarUrl());

        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }),
    VOTE(event -> {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("Vote for 7,000 coins!");
        embedBuilder.setDescription("[Click to Vote](https://top.gg/bot/764566349543899149/vote)");
        embedBuilder.setThumbnail(event.getAuthor().getAvatarUrl());
        embedBuilder.setFooter("Webhooks have now been fixed, voting will now give coins!");

        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }),
    DONATE(event -> {
        Integer value = Integer.valueOf(
                event.getMessage().getContentDisplay().toUpperCase()
                        .replace(Prefix.guildPrefix(event.getGuild().getId()) +
                "DONATE ", "").substring(0, event.getMessage().getContentDisplay().toUpperCase()
                        .replace(Prefix.guildPrefix(event.getGuild().getId()) +
                                "DONATE ", "").indexOf(" ")));
        if(value >= 50000){
            event.getChannel().sendMessage("You can't send 50,000 or more!").queue();
            return;
        } else if (value < 0 ){
            event.getChannel().sendMessage("Don't even try").queue();
            return;
        }

        try {
            Profile giver = Profile.getProfile(event.getAuthor());
            if(!giver.spend(value)) {
                event.getChannel().sendMessage("You can't give more money than you don't have!")
                        .queue();
                return;
            }
            giver.recreate();

            Profile receiver = Profile.getProfile(event.getMessage().getMentionedUsers().get(0) );
            receiver.setCoins(value);
            receiver.recreate();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x000dff);

            embedBuilder.setTitle("Successful Donation!");
            embedBuilder.setDescription(giver.getName() + " successfully donated " + value
                    + " coins to " + receiver.getName());
            embedBuilder.setThumbnail(event.getAuthor().getAvatarUrl());

            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } catch (Exception e) {
            event.getChannel().sendMessage("There was an error sending coins. Assert that all parties " +
                    "have an account and have entered `" + Prefix.guildPrefix(event.getGuild().getId()) + "C`." +
                    " If not, join the support server so we can fix this issue!").queue();
        }
    }),
    INVITE(event -> {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("Invite SorinoRPG to a server that you are an owner of, and receive 20,000 coins!");
        embedBuilder.setDescription("[Invite](https://discord.com/oauth2/authorize?client_id=764566349543899149&scope=bot&permissions=27648)");
        embedBuilder.setThumbnail(event.getAuthor().getAvatarUrl());

        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }),
    BUG(event -> {
        String bug = event.getMessage().getContentRaw().replace(Prefix.guildPrefix(event.getGuild().getId()),
                "").toUpperCase().replace("BUG ", "");

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(0x000dff);
        embed.setTitle("Bug reported");
        embed.setDescription("Bug: " + bug);
        embed.setThumbnail(event.getAuthor().getAvatarUrl());
        embed.setFooter("Any spam will not be tolerated and will lead to a blacklist");

        event.getChannel().sendMessage(embed.build()).queue();

        embed.setFooter(event.getAuthor().getId());
        Objects.requireNonNull(event.getJDA().getTextChannelById("820318304148914236"))
                .sendMessage(embed.build()).queue();
        GuildListener.bugControl.put(event.getAuthor().getId(), event.getGuild().getId());
    }),
    BRIBE(event -> {
        try {
            Profile profile = Profile.getProfile(event.getAuthor());

            if(!profile.spend(5000)){
                event.getChannel().sendMessage("Yeah, come back when you have 5000 coins on you.").queue();
                return;
            } else if(!GuildListener.prison.containsKey(event.getAuthor().getId())){
                event.getChannel().sendMessage("You're not a prisoner!").queue();
                return;
            }

            profile.recreate();

            GuildListener.prison.remove(event.getAuthor().getId());

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x000dff);

            embedBuilder.setTitle("You bribed a Guard!");
            embedBuilder.setDescription("A greedy Guard agreed to release you from prison for a fee!");
            embedBuilder.setThumbnail(event.getAuthor().getAvatarUrl());

            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }catch (Exception e) {
            Logger logger1 =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "There was an error in bribing the guard!"
            ).queue();
            try{
                logger1.logError();
            } catch (IOException excI){
                excI.printStackTrace();
            }
        }
    }),
    MARKET(event -> {
        String p = Prefix.guildPrefix(event.getGuild().getId());

        int l = p.length() +1;
        String command = event.getMessage().getContentRaw().substring(l);
        if(command.equals("HELP")){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x00dff);

            embedBuilder.setTitle("Transfer Market Help");
            embedBuilder.addField(p + "MSELL <Item/Sorino> <Time in hours> <Initial Price>",
                    "This allows you to post an Item/Sorino on the transfer market. Example: `" + p +
                    "MSELL Urwald 2 25000`. This is what you would enter, to post an Urwald, for 2 hours at a " +
                            "start price of 25,000 coins.", true);
            embedBuilder.addField(p + "MBID <ID> <Bid>",
                    "This allows you to bid on a post on the transfer market. The bid MUST be higher than the current " +
                            "price. Example: `" + p + "MBID qUAs25a 26,000`. This is what you would enter to bid " +
                            "26,000 coins on Urwald.", true);
            embedBuilder.addField(p + "MPERSONAL_BIDS",
                    "This allows you to view your own bids, and to see how your bids are doing", true);
            embedBuilder.addField(p + "MSEARCH <Search Term>",
                    "This allows you to see what is on the market, based on the Search term you have entered. " +
                            "Example: `" + p + "MSEARCH Common`. This will allow me to view the common posts" +
                            " on the transfer market.", true);
            embedBuilder.addField(p + "MTOP <pages>",
                    "This allows you to view the top posts on the market. If I wanted to " +
                            "view the top 6 pages, I would enter `"+ p + "MTOP 6`. Typing in no numbers, or an invalid" +
                            " number will give the top 3 pages." , true);

            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } else if(command.toUpperCase().startsWith("BID")){
            try {
                event.getChannel().sendMessage("Processing bid....").queue();
                Market.update();

                ArrayList<Object> data = MarketParser.bid(command, Prefix.guildPrefix(event.getGuild().getId()));


                event.getChannel().sendMessage(Market.bid(Listing.getListingById((String) data.get(0)),
                        (int) data.get(1), event.getAuthor(), Profile.getProfile(event.getAuthor()))).queue();
            } catch(StringParseException e){
                event.getChannel().sendMessage("Your input format was incorrect, the correct format is: `" +
                        e.getProperFormat() + "`").queue();
            } catch (ListingNotFoundException e){
                event.getChannel().sendMessage("The listing ID you entered does not exist. `" + e.getId() + "`")
                        .queue();
            } catch (ProfileNotFoundException e){
                event.getChannel().sendMessage("You do not have a profile! Enter: `" +
                        Prefix.guildPrefix(event.getGuild().getId()) + "C`").queue();
            }
        } else if(command.toUpperCase().startsWith("SELL")){
            try {
                event.getChannel().sendMessage("Processing sell request....").queue();
                Market.update();

                ArrayList<Object> data = MarketParser.sell(command, Prefix.guildPrefix(event.getGuild().getId()));

                if(data.get(0) instanceof Sorino){
                    Listing listing = new SorinoListing(event.getAuthor(), (Sorino) data.get(0), (int) data.get(1),
                            (long) data.get(2));

                    event.getChannel().sendMessage(Market.sell(listing, event.getAuthor()))
                    .queue();
                } else if(data.get(0) instanceof Item){
                    Listing listing = new ItemListing(event.getAuthor(), (Item) data.get(0), (int) data.get(1),
                            (long) data.get(2));

                    event.getChannel().sendMessage(Market.sell(listing, event.getAuthor()))
                            .queue();
                }
            } catch (StringParseException e){
                event.getChannel().sendMessage("Your input format was incorrect, the correct format is: `" +
                        e.getProperFormat() + "`").queue();
            } catch(ProfileNotFoundException e){
                event.getChannel().sendMessage("You do not have a profile! Enter: `" +
                        Prefix.guildPrefix(event.getGuild().getId()) + "C`").queue();
            }
        } else if(command.toUpperCase().startsWith("PERSONAL_BIDS")){
            event.getChannel().sendMessage("Processing personal bid viewing....").queue();
            Market.update();

            Paginator paginator = Market.personalItems(event.getAuthor());
            if(paginator.isSinglePage){
                event.getChannel().sendMessage(paginator.currentPage.build()).queue();
            } else {
                event.getChannel().sendMessage(paginator.currentPage.build()).queue(message1 -> {
                    Paginator.paginators.put(message1.getId(), paginator);
                    message1.addReaction("\u2B05").queue();
                    message1.addReaction("\u27A1").queue();
                });
            }
        } else if(command.toUpperCase().startsWith("SEARCH")){
            try {
                String searchTerm = MarketParser.oneArg(command, "SEARCH ", p + "MSEARCH <Search Term>");
                event.getChannel().sendMessage("Processing search for " + searchTerm + " ....").queue();
                Market.update();

                Paginator paginator = Market.search(searchTerm);
                if(paginator.isSinglePage){
                    event.getChannel().sendMessage(paginator.currentPage.build()).queue();
                } else {
                    event.getChannel().sendMessage(paginator.currentPage.build()).queue(message1 -> {
                        Paginator.paginators.put(message1.getId(), paginator);
                        message1.addReaction("\u2B05").queue();
                        message1.addReaction("\u27A1").queue();
                    });
                }
            } catch (StringParseException e){
                event.getChannel().sendMessage("Your input format was incorrect, the correct format is: `" +
                        e.getProperFormat() + "`").queue();
            }
        } else if(command.toUpperCase().startsWith("TOP")){
            event.getChannel().sendMessage("Processing top listing request....").queue();
            Market.update();

            int value;

            try {
                String valStr = MarketParser.oneArg(command, "TOP ", "");
                value = Integer.parseInt(valStr);
            } catch (StringParseException e){
                value = 3;
            }

            Paginator paginator = Market.top(value);
            paginator.paginate();
            if(paginator.isSinglePage){
                event.getChannel().sendMessage(paginator.currentPage.build()).queue();
            } else {
                event.getChannel().sendMessage(paginator.currentPage.build()).queue(message1 -> {
                    Paginator.paginators.put(message1.getId(), paginator);
                    message1.addReaction("\u2B05").queue();
                    message1.addReaction("\u27A1").queue();
                });
            }
        }


    }),
    ERROR(event -> {
    });

    UserAction userAction;

    Command(UserAction userAction){
        this.userAction = userAction;
    }
    static Command getCommand(GuildMessageReceivedEvent message){
        HashMap<Prefix.PrefixString, Command> commandHashMap = new HashMap<>() {
            {
                put(Prefix.PrefixString.HELP, Command.HELP);
                put(Prefix.PrefixString.BRIBE, Command.BRIBE);
                put(Prefix.PrefixString.BUG, Command.BUG);
                put(Prefix.PrefixString.VOTE, Command.VOTE);
                put(Prefix.PrefixString.UPDATE, Command.UPDATE);
                put(Prefix.PrefixString.INFO, Command.INFO);
                put(Prefix.PrefixString.INVITE, Command.INVITE);
                put(Prefix.PrefixString.CHANGE, Command.CHANGE);
                put(Prefix.PrefixString.DONATE, Command.DONATE);
                put(Prefix.PrefixString.FIGHT, Command.FIGHT);
                put(Prefix.PrefixString.SEARCH, Command.SEARCH);
                put(Prefix.PrefixString.SEE_PROFILE, Command.SEE_PROFILE);
                put(Prefix.PrefixString.CREATE_PROFILE, Command.CREATE_PROFILE);
                put(Prefix.PrefixString.SEE_RANK, Command.SEE_RANK);
                put(Prefix.PrefixString.WRAP, Command.WRAP);
                put(Prefix.PrefixString.SLOT, Command.SLOT);
                put(Prefix.PrefixString.HEISTS, Command.HEIST);
                put(Prefix.PrefixString.STREET_FIGHT, Command.STREET_FIGHT);
                put(Prefix.PrefixString.MARKET, Command.MARKET);
            }
        };

        String mess = message.getMessage().getContentDisplay()
                .replace(Prefix.guildPrefix(message.getGuild().getId()), "").toUpperCase();
        for(Prefix.PrefixString prefix : EnumSet.allOf(Prefix.PrefixString.class))
            if(mess.startsWith(prefix.prefix()) || mess.equals(prefix.prefix())) return commandHashMap.get(prefix);

        return Command.ERROR;
    }
}

