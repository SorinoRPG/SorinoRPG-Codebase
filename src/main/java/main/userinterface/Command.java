package main.userinterface;

import data.Profile;
import data.ProfileNotFoundException;
import data.ProfileStore;
import data.logging.Logger;

import game.fight.*;
import game.fight.streetfight.StreetFight;
import game.fight.streetfight.StreetProtector;
import game.heist.Heist;
import game.heist.HeistNotFoundException;
import game.value.Coins;
import game.SorinoNotFoundException;
import game.characters.Sorino;
import game.characters.starter.Gray;

import game.value.Slots;
import game.value.Wrap;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


public enum Command {
    HELP(event -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Help -- February 18 update is live!");
            embedBuilder.setColor(0x000dff);
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

                Profile profile = Profile.getProfile(event);

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
            } catch (IOException | ClassNotFoundException e) {
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
            } catch (ProfileNotFoundException e) {
                Logger logger =
                        new Logger("Error in finding Profile \n" +
                                Logger.exceptionAsString(e));
                try{
                    logger.logError();
                } catch (IOException excI){
                    excI.printStackTrace();
                }
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
        } else {
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
                event.getChannel().sendMessage(Profile.getProfile(event)
                        .showLevel(event
                                .getChannel())).queue();
            } catch (IOException | ClassNotFoundException e) {
                Logger logger1 =
                        new Logger("Error in finding Profile due to IO and Classes \n" +
                                Logger.exceptionAsString(e));
                event.getChannel().sendMessage(
                        "You don't have a profile! Enter: `" + Prefix.guildPrefix(event.getGuild().getId()) +
                                "C`\n" +
                                "If you do have an account, join the support server!"
                ).queue(message ->
                        message.delete().queueAfter(25000, TimeUnit.MILLISECONDS)
                );
                try{
                    logger1.logError();
                } catch (IOException excI){
                    excI.printStackTrace();
                }
            } catch (ProfileNotFoundException e) {
                logger =
                        new Logger("Error in finding Profile \n" +
                                Logger.exceptionAsString(e));
                try{
                    logger.logError();
                } catch (IOException excI){
                    excI.printStackTrace();
                }
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
        new Thread(() -> {
            Logger logger;

            try {
                Profile profile = Profile.getProfile(event);
                int probabilityResult = new Random().nextInt(100);
                if (probabilityResult < 90) {
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
                    profile.recreateProfile();
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
                        profile.recreateProfile();
                    } else
                        event.getChannel().sendMessage(event.getAuthor().getName() +
                                " attempted to catch a " + sorino.getName() +
                                " but failed!").queue();
                }
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
            try {
                logger = new Logger("Search");
                logger.logAction();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }),
    FIGHT(event -> {
        String rawMessage = Prefix.removeFightPrefix(event);

        class Check {
            boolean matchID(String id){
                try {
                    long idSum = event.getAuthor().getIdLong() +
                            event.getMessage().getMentionedUsers().get(0).getIdLong();

                    Fight fight = Fight.readFight(event.getGuild().getId(), String.valueOf(idSum));

                    return fight.usersID.get(fight.currFighter).equals(id);
                } catch (Exception e){
                    return false;
                }
            }
            boolean checkMove(String input){
                try {
                    long idSum = event.getAuthor().getIdLong() +
                            event.getMessage().getMentionedUsers().get(0).getIdLong();
                    Fight fight = Fight.readFight(event.getGuild().getId(), String.valueOf(idSum));

                    return fight.fighters.get(fight.currFighter).getMove(input,
                            fight.fighters.get(fight.currFighter)).isPresent();
                }catch (FightNotFoundException e) {
                    event.getChannel().sendMessage("You did not create a fight!").queue();

                    Logger fightLogger = new Logger(Logger.exceptionAsString(e));
                    try {
                        fightLogger.logError();
                    } catch (IOException e1){
                        e1.printStackTrace();
                    }
                    return false;
                } catch (IndexOutOfBoundsException e){
                    event.getChannel().sendMessage("See, I can't really know who you are fighting if you" +
                            " don't mention them at the end of your message!").queue();
                    return false;
                }
            }
            Move getMove(String input){
                try {
                    long idSum = event.getAuthor().getIdLong() +
                            event.getMessage().getMentionedUsers().get(0).getIdLong();
                    Fight fight = Fight.readFight(event.getGuild().getId(), String.valueOf(idSum));

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
        try {
            if (rawMessage.toUpperCase().contains("START")) {
                Fight fight = new Fight();
                long idSum = event.getAuthor().getIdLong() + event.getMessage().getMentionedUsers().get(0).getIdLong();
                FightManager.fightPhase1(event, fight, String.valueOf(idSum));

            } else if (rawMessage.toUpperCase().contains("END")) {
                try {
                    long idSum = event.getAuthor().getIdLong() +
                            event.getMessage().getMentionedUsers().get(0).getIdLong();
                    Fight fight = Fight.readFight(event.getGuild().getId(), String.valueOf(idSum));
                    if (fight.endFight(event.getGuild().getId(), String.valueOf(idSum)))
                        event.getChannel().sendMessage("Fight with " + event.getAuthor().getName() + " and " +
                                event.getMessage().getMentionedUsers().get(0).getName() + " has ended").queue();
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
                    rawMessage.substring(0, rawMessage.indexOf(" ")))) {
                try {
                    Profile profile = Profile.getProfile(event);
                    Sorino sorino = profile.getSpecificSorino(rawMessage.substring(0, rawMessage.indexOf(" ")));

                    long idSum = event.getAuthor().getIdLong() +
                            event.getMessage().getMentionedUsers().get(0).getIdLong();
                    Fight fight = Fight.readFight(event.getGuild().getId(), String.valueOf(idSum));
                    FightManager.fightPhase2(event, sorino, fight, String.valueOf(idSum));
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
                } catch (IOException e) {
                    Logger logger = new Logger("Error in saving fight" +
                            Logger.exceptionAsString(e));
                    event.getChannel().sendMessage("There was a server error in saving the " +
                            "fight! Please end it!").queue();
                    try {
                        logger.logError();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (ProfileNotFoundException e) {
                    Logger logger =
                            new Logger("Error in finding Profile \n" +
                                    Logger.exceptionAsString(e));

                    try {
                        logger.logError();
                    } catch (IOException excI) {
                        excI.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    try {
                        Logger logger = new Logger("Class change failure");
                        event.getChannel().sendMessage("There was a server error, tweet us " +
                                "@Rpgsorino to fix this ASAP")
                                .queue();
                        logger.logError();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                } catch (FightNotFoundException e) {
                    event.getChannel().sendMessage("You did not create a fight!").queue();
                    Logger fightLogger = new Logger(Logger.exceptionAsString(e));
                    try {
                        fightLogger.logError();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            } else if (new Check().checkMove(rawMessage.substring(0, rawMessage.indexOf(" "))) &&
                    new Check().matchID(event.getAuthor().getId())) {
                try {
                    long idSum = event.getAuthor().getIdLong() +
                            event.getMessage().getMentionedUsers().get(0).getIdLong();
                    Fight fight = Fight.readFight(event.getGuild().getId(), String.valueOf(idSum));
                    if (FightManager.fightPhase3(new Check().getMove(rawMessage.substring(0,
                            rawMessage.indexOf(" "))), event, fight).isPresent()) {
                        FightManager.fightPhase3(new Check().getMove(rawMessage.substring(0,
                                rawMessage.indexOf(" "))), event, fight)
                                .ifPresent((s) -> {
                                    int[] coins = FightManager.fightPhase4(event, s);
                                    event.getJDA().retrieveUserById(s.getWinner()).queue(user -> {
                                        EmbedBuilder message = new EmbedBuilder();
                                        message.setColor(0x000dff);
                                        message.setTitle("WINNER");
                                        message.setThumbnail(user.getAvatarUrl());
                                        event.getJDA().retrieveUserById(s.getLoser()).queue(user1 -> {
                                                    message.setDescription(user.getName() + " has won " + coins[0]
                                                            + " coins for beating " + user1.getName());
                                                    message.setFooter( "has won " + coins[0]
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
                                                    message.setDescription(user.getName() + " has lost " + (coins[1] - (coins[1] * 2))
                                                            + " coins for losing to " + user1.getName());
                                                    message.setFooter("has lost " + (coins[1] - (coins[1] * 2))
                                                                    + " coins for losing to " + user1.getName(),
                                                            user.getAvatarUrl());
                                                    event.getChannel().sendMessage(message.build()).queue();
                                                    fight.endFight(event.getGuild().getId(), String.valueOf(idSum));
                                                }
                                        );
                                    });
                                });
                    } else fight.saveFight(event.getGuild().getId(), String.valueOf(idSum));
                } catch (IOException e) {
                    try {
                        Logger logger = new Logger("Class change failure");
                        logger.logError();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
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
        } catch (StringIndexOutOfBoundsException e){
            event.getChannel().sendMessage("You did not specify your opponent!").queue();
        }
    }),
    HEIST(event -> {
        String message = event.getMessage()
                .getContentRaw().toUpperCase().replace(".H", "");

        if(message.equals("CREATE")){
            if(Heist.HeistUtil.exists(event.getGuild().getId(), event.getChannel().getId())) {
                event.getChannel().sendMessage("Sorry! There is already a heist " +
                        "in this channel!").queue();
                return;
            }
            try {
                Profile.getProfile(event);
            } catch (Exception e){
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
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("Available heists!");
            embedBuilder.setColor(0x000dff);

            for(Heist heist : Heist.HeistUtil.getAll())
                embedBuilder.addField(heist.getName() + " | Type: " + heist.getType(),
                                "SETUP COST: " + heist.setupCost() + " coins\n" +
                                "PAYOUT: " + heist.payout() + " coins\n" +
                                "ENTER TO CHOOSE: " +
                                "`" + Prefix.guildPrefix(event.getGuild().getId()) +
                                "H" + heist.getName() + "`",
                        true);

            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } else if(Heist.HeistUtil.isHeist(message)){
            if(Heist.HeistUtil.exists(event.getGuild().getId(), event.getChannel().getId())) {
                event.getChannel().sendMessage("Sorry! There is already a heist " +
                        "in this channel!").queue();
                return;
            }

            try {


                Heist heist = Heist.HeistUtil.getHeist(message);

                Profile profile = Profile.getProfile(event);
                if(profile.spend(heist.setupCost())){
                    event.getChannel().sendMessage("You can't afford this heist!").queue();
                    return;
                }

                heist.setLeaderID(event.getAuthor().getId());
                heist.addMember(event.getAuthor().getId());

                event.getChannel().sendMessage(heist.heistState()).queue();

                heist.saveHeist(event.getGuild().getId(), event.getChannel().getId());
            } catch (Exception e){
                event.getChannel().sendMessage("There was an error in creating this heist!" +
                        "\nMaybe this heist does not exist, make sure the heists' name is " +
                        "what you have entered.").queue();
                e.printStackTrace();
            }
        } else if(message.equals("JOIN")){
            try {
                Profile.getProfile(event);
            } catch (Exception e){
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
            try {
                Heist heist = Heist.HeistUtil.getCurrentHeist(event.getGuild().getId(),
                        event.getChannel().getId());

                if(heist.isFull()){
                    event.getChannel().sendMessage("Yeah I'm sorry but this heist is FULL")
                            .queue();
                    return;
                } else if(heist.getAllID().contains(event.getAuthor().getId())){
                    event.getChannel().sendMessage("You are already in this heist!")
                            .queue();
                    return;
                }

                heist.addMember(event.getAuthor().getId());
                heist.saveHeist(event.getGuild().getId(),
                        event.getChannel().getId());
                event.getChannel().sendMessage(heist.heistState()).queue();
            } catch (HeistNotFoundException e){
                event.getChannel().sendMessage(
                        "Imagine trying to join a heist that does not exist").queue();
            } catch (IOException e){
                event.getChannel().sendMessage("Hmmmm, there seems to be a problem" +
                        " joining this heist...\nTalk to the support server, so we can fix" +
                        " it").queue();
                e.printStackTrace();
                Logger log = new Logger(Logger.exceptionAsString(e));
                try{
                    log.logError();
                } catch (IOException exc2){
                    exc2.printStackTrace();
                }
            }
        } else if (message.equals("START")){
            try {
                Heist heist = Heist.HeistUtil.getCurrentHeist(event.getGuild().getId(),
                        event.getChannel().getId());
                if(!heist.getLeaderID().equals(event.getAuthor().getId())){
                    event.getChannel().sendMessage("You are not the leader!").queue();
                    heist.heistState();
                    return;
                } else if(!heist.isFull()){
                    event.getChannel().sendMessage("You can't expect to pull this off" +
                            " if you don't have a full team!").queue();
                    heist.heistState();
                    return;
                }

                heist.setCurrentID(heist.getAllID().get(new Random().nextInt(4)));
                heist.saveHeist(event.getGuild().getId(),
                        event.getChannel().getId());
                event.getJDA().retrieveUserById(heist.currentID()).queue(user -> event.getChannel().sendMessage(
                        heist.getCurrentStage().ask(Prefix.guildPrefix(event.getGuild().getId()), user)).queue());
            } catch (HeistNotFoundException e){
                event.getChannel().sendMessage("This heist does not exist in this channel!")
                        .queue();
                e.printStackTrace();
            } catch (IOException e){
                event.getChannel().sendMessage("There is a problem with starting this" +
                        " heist!").queue();
                Logger log = new Logger(Logger.exceptionAsString(e));

                try {
                    log.logError();
                } catch (IOException exc){
                    exc.printStackTrace();
                }
            }

        } else if(message.endsWith("A") ||
                  message.endsWith("B") ||
                  message.endsWith("C")){
            try {
                 Heist heist = Heist.HeistUtil.getCurrentHeist(event.getGuild().getId(),
                        event.getChannel().getId());
                if(!heist.currentID().equals(event.getAuthor().getId())){
                    event.getChannel().sendMessage("It's not your turn!\n" +
                            "Hold back or we will be caught!").queue();
                    return;
                }
                switch (heist.getCurrentStage().processChoice(message.charAt(message.length()-1))){
                    case 0xA:{ // Chose correctly
                        String oldId = heist.currentID();
                        while(oldId.equals(heist.currentID()))
                            heist.setCurrentID(heist.getAllID().get(new Random().nextInt(4)));
                        heist.setCurrentID(heist.getAllID().get(new Random().nextInt(4)));
                        event.getJDA().retrieveUserById(heist.currentID()).queue(user -> {
                                event.getChannel().sendMessage(heist.getCurrentStage().success(user)).queue();
                                heist.incrementStage();
                                event.getChannel().sendMessage(
                                heist.getCurrentStage()
                                        .ask(Prefix.guildPrefix(event.getGuild().getId()), user)).queue();
                            try {
                                heist.saveHeist(event.getGuild().getId(), event.getChannel().getId());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        return;
                    }
                    case 0xB:{ // Chose incorrectly
                        event.getJDA().retrieveUserById(heist.currentID()).queue(user ->
                                event.getChannel().sendMessage(heist.getCurrentStage().failure(user)).queue());
                        for(String Id : heist.getAllID()){
                            event.getJDA().retrieveUserById(Id).queue(user -> {

                                try{
                                    Profile profile = Profile.getProfile(user, event);
                                    profile.setCoins(-heist.bail());
                                    profile.recreateProfile();
                                } catch (Exception e) {
                                    Logger logger1 =
                                            new Logger(
                                                    Logger.exceptionAsString(e));
                                    event.getChannel().sendMessage(
                                            "There seems to be a problem with your account " +
                                                    user.getAsMention() + "\n" +
                                                    "Join the support server, So we can get it fixed!"
                                    ).queue();
                                    try{
                                        logger1.logError();
                                    } catch (IOException excI){
                                        excI.printStackTrace();
                                    }
                                }
                            });
                        }
                        File heistFile = new File("/db/" + event.getGuild().getId() + "/heists/%" +
                                event.getChannel().getId() + ".txt");

                        FileUtils.forceDelete(heistFile);
                        return;
                    }
                    case 0xC:{ // Won heist
                        event.getJDA().retrieveUserById(heist.currentID()).queue(user ->
                        event.getChannel().sendMessage(heist.getCurrentStage().success(user)).queue());

                        for(String Id : heist.getAllID()){
                            event.getJDA().retrieveUserById(Id).queue(user -> {
                                try{
                                    Profile profile = Profile.getProfile(user, event);
                                    profile.setCoins(heist.payout());
                                    profile.recreateProfile();
                                } catch (Exception e) {
                                    Logger logger1 =
                                            new Logger(
                                                    Logger.exceptionAsString(e));
                                    event.getChannel().sendMessage(
                                            "There seems to be a problem with your account " +
                                                    user.getAsMention() + "\n" +
                                                    "Join the support server, So we can get it fixed!"
                                    ).queue();
                                    try{
                                        logger1.logError();
                                    } catch (IOException excI){
                                        excI.printStackTrace();
                                    }
                                }
                            });
                        }
                        File heistFile = new File("/db/" + event.getGuild().getId() + "/heists/%" +
                                event.getChannel().getId() + ".txt");

                        FileUtils.forceDelete(heistFile);
                    }
                }
            } catch (HeistNotFoundException e){
                    event.getChannel().sendMessage("This heist does not exist in this channel!")
                            .queue();
                    e.printStackTrace();
                } catch (IOException e){
                    event.getChannel().sendMessage("There is a problem with starting this" +
                            " heist!").queue();
                    Logger log = new Logger(Logger.exceptionAsString(e));

                    try {
                        log.logError();
                    } catch (IOException exc){
                        exc.printStackTrace();
                    }
                }
            }
        else if(message.endsWith("ABORT")){
                try {
                    Heist heist = Heist.HeistUtil.getCurrentHeist(event.getGuild().getId(),
                            event.getChannel().getId());
                    if(!heist.getLeaderID().equals(event.getAuthor().getId())){
                        event.getChannel().sendMessage("Don't try and end something you didn't start!")
                                .queue();
                        return;
                    }
                    EmbedBuilder embedBuilder = new EmbedBuilder();

                    embedBuilder.setTitle(event.getAuthor().getName() + " has abandoned their heist!");
                    embedBuilder.setDescription("They aren't getting refunded");
                    embedBuilder.setColor(0x000dff);

                    File heistFile = new File("/db/" + event.getGuild().getId() + "/heists/%" +
                            event.getChannel().getId() + ".txt");

                    FileUtils.forceDelete(heistFile);
                    event.getChannel().sendMessage(embedBuilder.build()).queue();
                } catch (Exception e){
                    event.getChannel().sendMessage("There was an error in aborting!" +
                            "\n Maybe the heist does not exist!").queue();
                    Logger log = new Logger(Logger.exceptionAsString(e));
                    try {
                        log.logError();
                    } catch (IOException exc){
                    exc.printStackTrace();
                }
            }
        }
    }),
    CREATE_PROFILE(event -> {
        if(new File("/db/" + event.getGuild().getId() +
                "/@@" + event.getAuthor().getId() + ".txt").exists()){
            event.getChannel().sendMessage("You already have an account!").queue();
            return;
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    new FileInputStream(new File("/db/USERLIST.txt"))
            );

            @SuppressWarnings("unchecked")
            HashMap<String, ProfileStore> userList =
                    (HashMap<String, ProfileStore>) objectInputStream.readObject();
            objectInputStream.close();
            if(userList.containsKey(event.getAuthor().getId())){
                ProfileStore profStore = userList.get(event.getAuthor().getId());
                Profile profile = Profile.storeToProfile(profStore);
                profile = profile.changeGuildID(event.getGuild().getId());

                profile.createProfile();

                EmbedBuilder message = new EmbedBuilder();

                message.setColor(0x000dff);
                message.setTitle(event.getAuthor().getName() + "'s Profile");
                message.setImage(event.getAuthor().getAvatarUrl());
                message.setDescription(profile.toString());
                message.setFooter("Updated their profile to a new server.",
                        event.getAuthor().getAvatarUrl());

                event.getChannel().sendMessage(message.build()).queue();
                return;
            }
        } catch (Exception e){
            Logger logger = new Logger(Logger.exceptionAsString(e));
            event.getChannel().sendMessage("There was an error updating your" +
                    " account to a new server!\n" +
                    "Join our support server so we can fix his ASAP!").queue();

            try {
                logger.logError();
            } catch (IOException excI){
                excI.printStackTrace();
            }

            return;
        }

        Logger logger
                = new Logger("Created profile");
        // Only one phase, no method needed
        Profile userProfile =
                new Profile(new ArrayList<>(Collections.singletonList(new Gray())),
                        new Coins(50),
                        event.getAuthor().getId(), event.getAuthor().getName()
                        ,0, 0, event.getAuthor().getAvatarUrl(),
                        event.getGuild().getId());
        try {
            userProfile.createProfile();
        } catch(IOException e){

            logger = new Logger("Error in creating profile + \n" +
                    Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "There was a server error in creating your profile."
            ).queue();
            System.out.println(Logger.exceptionAsString(e));
            try {
                logger.logError();
                return;
            } catch (IOException exc){
                exc.printStackTrace();
                return;
            }
        }
        EmbedBuilder message = new EmbedBuilder();
        message.setColor(0x000dff);

        message.setTitle(event.getAuthor().getName() + "'s Profile");
        message.setImage(event.getAuthor().getAvatarUrl());
        message.setDescription(userProfile.toString());
        message.setFooter("Created a profile", event.getAuthor().getAvatarUrl());

        event.getChannel().sendMessage(message.build()).queue();
        try {
            logger.logAction();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }),
    SEE_PROFILE(event -> {
        Logger logger
            = new Logger("Checked profile");
        // Only one phase no methods needed
        try {
            if(event.getMessage().getMentionedUsers().size() == 1){
                Profile profile = Profile.getProfile(event.getMessage().getMentionedUsers().get(0)
                ,event);

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
            Profile profile = Profile.getProfile(event);
            EmbedBuilder message = new EmbedBuilder();
            message.setColor(0x000dff);

            message.setTitle(event.getAuthor().getName() + "'s Profile");
            message.setImage(event.getAuthor().getAvatarUrl());
            message.setDescription(profile.toString());
            message.setFooter("Requested to see their profile", event.getAuthor().getAvatarUrl());

            event.getChannel().sendMessage(message.build()).queue();
        } catch (IOException | ClassNotFoundException e) {
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
        } catch (ProfileNotFoundException e) {
            logger =
                    new Logger("Error in finding Profile \n" +
                            Logger.exceptionAsString(e));
            try{
                logger.logError();
            } catch (IOException excI){
                excI.printStackTrace();
            }
        }
        try {
            logger.logAction();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }),
    WRAP(event -> {
        String message = event.getMessage().getContentRaw();
        try {
            Profile userProfile = Profile.getProfile(event);

            if(message.toUpperCase().contains(Wrap.CHAMPIONS.toString())){
                Wrap.CHAMPIONS.action(userProfile, event);
                userProfile.recreateProfile();
                return;
            } else if(message.toUpperCase().contains(Wrap.PREMIUM.toString())){
                Wrap.PREMIUM.action(userProfile, event);
                userProfile.recreateProfile();
                return;
            } else if(message.toUpperCase().contains(Wrap.STANDARD.toString())){
                Wrap.STANDARD.action(userProfile, event);
                userProfile.recreateProfile();
                return;
            } else if(message.toUpperCase().contains(Wrap.BASIC.toString())){
                Wrap.BASIC.action(userProfile, event);
                userProfile.recreateProfile();
                return;
            }
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

        String prefix = Prefix.guildPrefix(event.getGuild().getId());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);


        embedBuilder.setTitle("Wraps");

        embedBuilder.addField("BASIC: 1,500 coins",
                "Contains 1 Common Sorino\n" +
                      "Enter `" + prefix + "WBASIC` to open.",
                true);
        embedBuilder.addField("STANDARD: 10,000 coins",
                "Contains a mix of 2 Common or Uncommon Sorino\n" +
                        "Enter `" + prefix + "WSTANDARD` to open.",
                true);
        embedBuilder.addField("PREMIUM: 45,000 coins",
                "Contains a mix of 3 Uncommon or Rare Sorino\n" +
                        "Enter `" + prefix + "WPREMIUM` to open.",
                true);
        embedBuilder.addField("CHAMPIONS: 100,000 coins",
                "Contains a mix of 3 Hidden or Lost Sorino\n" +
                        "Enter `" + prefix + "WCHAMPIONS` to open.",
                true);

        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }),
    CHANGE(event -> {
        event.getGuild().retrieveMember(event.getAuthor()).queue( member -> {
            if (!member.hasPermission(Permission.MESSAGE_MANAGE)){
                event.getChannel().sendMessage("You do not have the permission to do such a command!")
                        .queue();
                return;
            }

            if(event.getMessage().getContentRaw().contains("setchannel")){
                try (FileWriter fw = new FileWriter(
                        new File("/db/" + event.getGuild().getId() + "/CHANNEL.txt"))){
                    fw.write(event.getMessage().getMentionedChannels().get(0).getId());
                    event.getChannel().sendMessage(
                            "Saved: " + event.getMessage().getMentionedChannels().get(0).getName()
                    ).queue();
                } catch (IOException e){
                    event.getChannel().sendMessage(
                            "You don't have a profile! Enter: `" + Prefix.guildPrefix(event.getGuild().getId()) +
                                    "C`\n" +
                                    "If you do have an account, join the support server!"
                    ).queue();

                    try {
                        Logger logger =
                                new Logger(Logger.exceptionAsString(e));
                        logger.logError();
                    } catch (IOException exc){
                        exc.printStackTrace();
                    }
                }
            }
            else if (event.getMessage().getContentRaw().contains("setprefix")){
                if(!event.getMessage().getContentRaw().contains(" ")) return;
                String newPrefix = event.getMessage().getContentRaw().substring(
                        event.getMessage().getContentRaw().indexOf(" ") + 1
                );
                if(newPrefix.length() >= 5) newPrefix = newPrefix.substring(0, 5);

                try (FileWriter fileWriter = new FileWriter(new File(
                        "/db/" + event.getGuild().getId() + "/PREFIX.txt"
                ), false)) {
                    fileWriter.write(newPrefix);
                    event.getGuild().getSelfMember().modifyNickname("[" + newPrefix + "] SorinoRPG")
                            .queue();
                    event.getChannel().sendMessage("Changed server prefix to: " + newPrefix).queue();
                } catch (IOException e) {
                    Logger logger = new Logger(Logger.exceptionAsString(e));
                    try {
                        logger.logError();
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
            }
        });
    }),
    SLOT(event -> {
        try {
            int stake = Integer.parseInt(event.getMessage().getContentRaw().substring(
                    event.getMessage().getContentRaw().indexOf(" ")+1
            ));

            Profile profile = Profile.getProfile(event);
            if(profile.spend(stake)) {
                event.getChannel().sendMessage(event.getAuthor().getName() + " has insufficient funds!")
                        .queue();
                return;
            } else if(stake < 0) return;
            else if(stake >= 200000){
                event.getChannel().sendMessage("You can't gamble 200,000 coins or more at once!")
                        .queue();
                return;
            }
            profile.recreateProfile();

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
                profile.recreateProfile();
            } else {
                if(Arrays.asList(randoms).contains(Slots.Slot.SAPPHIRES.img())){
                    profile.setCoins((int) (stake * 1.5));
                    profile.recreateProfile();
                    event.getChannel().sendMessage(
                            event.getAuthor().getName() + " found a Sapphire and will receive " +
                                    stake * 1.5  + " coins!"
                    ).queue();
                    return;
                } else if (Arrays.asList(randoms).contains(Slots.Slot.BLUE_BEARS.img())){
                    profile.setCoins((int) (stake * 1.25));
                    profile.recreateProfile();
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
        embedBuilder.setTitle("How to play Slots!");
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
        String command = event.getMessage().getContentRaw().toUpperCase().replace(
                Prefix.guildPrefix(event.getGuild().getId()) + "B", "");
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
                StreetFight fight = StreetFight.readFight(event1.getGuild().getId(), event1.getAuthor().getId());
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
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(0x000dff);
                embedBuilder.setThumbnail(move.getUrl());
                embedBuilder.setFooter(" gained " + move.getEffect() + " defence",
                        event.getAuthor().getAvatarUrl());


                event.getChannel().sendMessage(embedBuilder.build()).queue();

                String ranMove = fight.protector
                        .getGuardianSorino()
                        .getMoves()
                        .get(new Random().nextInt(4));
                if(ranMove.contains(" ")) ranMove = ranMove.substring(0, ranMove.indexOf(" "));
                fight.protector.getGuardianSorino().getMove(
                        ranMove,
                        fight.protector.getGuardianSorino()
                ).ifPresent(protectorMove -> {
                    if(protectorMove.isDefensive()){
                        fight.opponents.get(0).defenseUp(protectorMove, event);
                        EmbedBuilder embedBuilder1 = new EmbedBuilder();
                        embedBuilder1.setColor(0x000dff);
                        embedBuilder1.setThumbnail(protectorMove.getUrl());
                        embedBuilder1.setFooter(fight.protector.getName()
                                + " gained " + protectorMove.getEffect() + " defence");

                        event.getChannel().sendMessage(embedBuilder1.build()).queue();
                    } else {
                        fight.opponents.get(1).takeDamage(protectorMove, event);
                        fight.opponents.get(0).dropEnergy(protectorMove);

                        EmbedBuilder embedBuilder2 = new EmbedBuilder();
                        embedBuilder2.setColor(0x000dff);
                        embedBuilder2.setThumbnail(protectorMove.getUrl());
                        embedBuilder2.setFooter(" lost " + protectorMove.getEffect() + " health",
                                event.getAuthor().getAvatarUrl());

                        event.getChannel().sendMessage(embedBuilder2.build())
                                .queue();
                    }
                });
            } else {
                fight.opponents.get(0).takeDamage(move, event);
                fight.opponents.get(1).dropEnergy(move);

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(0x000dff);
                embedBuilder.setThumbnail(move.getUrl());
                embedBuilder.setFooter(fight.protector.getName() + " lost " + move.getEffect() + " health");

                event.getChannel().sendMessage(embedBuilder.build()).queue();

                String ranMove = fight.protector
                        .getGuardianSorino()
                        .getMoves()
                        .get(new Random().nextInt(4));
                if(ranMove.contains(" ")) ranMove = ranMove.substring(0, ranMove.indexOf(" "));
                fight.protector.getGuardianSorino().getMove(
                        ranMove,
                        fight.protector.getGuardianSorino()
                ).ifPresent(protectorMove -> {
                    if(protectorMove.isDefensive()){
                        fight.opponents.get(0).defenseUp(protectorMove, event);
                        EmbedBuilder embedBuilder1 = new EmbedBuilder();
                        embedBuilder1.setColor(0x000dff);
                        embedBuilder1.setThumbnail(protectorMove.getUrl());
                        embedBuilder1.setFooter(fight.protector.getName()
                                + " gained " + protectorMove.getEffect() + " defence");

                        event.getChannel().sendMessage(embedBuilder1.build()).queue();
                    } else {
                        fight.opponents.get(1).takeDamage(protectorMove, event);
                        fight.opponents.get(0).dropEnergy(protectorMove);

                        EmbedBuilder embedBuilder2 = new EmbedBuilder();
                        embedBuilder2.setColor(0x000dff);
                        embedBuilder2.setThumbnail(protectorMove.getUrl());
                        embedBuilder2.setFooter(" lost " + protectorMove.getEffect() + " health",
                                event.getAuthor().getAvatarUrl());

                        event.getChannel().sendMessage(embedBuilder2.build())
                                .queue();
                    }
                });
            }


            if(fight.opponents.get(1).hasConceded())
                return Optional.of(new Fight.GameInfo("Protector", "User"));
            else if(fight.opponents.get(0).hasConceded())
                return Optional.of(new Fight.GameInfo("User", "Protector"));

            EmbedBuilder embed  = new EmbedBuilder();
            embed.setTitle("Health of the Fighters!");
            embed.setColor(0x000dff);
            embed.addField(fight.protector.getName() + "'s Health \t\t\t ",
                    "HEALTH: " + fight.opponents.get(0).getHealth() + "\n" +
                            "ENERGY: " + fight.opponents.get(0).getEnergy() + "\n" +
                            "DEFENCE: " + fight.opponents.get(0).getDecrease() + " drop-off",
                    true);
            embed.addField(event.getAuthor().getName() + "'s Health \t\t\t ",
                    "HEALTH: " + fight.opponents.get(1).getHealth() + "\n" +
                            "ENERGY: " + fight.opponents.get(1).getEnergy() + "\n" +
                            "DEFENCE: " + fight.opponents.get(1).getDecrease() + " drop-off",
                    true);
            event.getChannel().sendMessage(embed.build())
                    .queue();

            EmbedBuilder message = new EmbedBuilder();
            message.setColor(0x000dff);
            message.setTitle("Enter your move " + event.getAuthor().getName());
            message.setFooter("Needs to enter their move", event.getAuthor().getAvatarUrl());
            message.addField("Moves: ", fight.userSorino.get(0).getMoves().toString()
                    + "\n" +
                    "To enter a move, type `" + Prefix.guildPrefix(event.getGuild().getId()) +
                    "B<MOVE>`, `<MOVE>` being the move you want to use.", false);
            event.getChannel().sendMessage(message.build())
                    .queue();

            return Optional.empty();
        };
        try {
            if(command.toUpperCase().contains("START")){
                List<StreetProtector> protectors = StreetProtector.Protectors.getAllProtectors();

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Choose a Street Protector to battle!");
                embedBuilder.setColor(0x000dff);

                for(StreetProtector protector : protectors)
                    embedBuilder.addField(protector.getName() + ": " + protector.getStreetName(),
                            "Guardian Sorino: " + protector.getGuardianSorino().getName() + "\n" +
                                    "Enter: `" +
                                    Prefix.guildPrefix(event.getGuild().getId()) + "B" + protector.getName() +
                                            "` to battle " + protector.getName(),
                                    true);
                event.getChannel().sendMessage(embedBuilder.build()).queue();
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
                try {
                    for (Sorino sorino : Profile.getProfile(event).getSorinoAsList())
                        message.addField(sorino.getName(),
                                "HEALTH: " + sorino.getHealth(Profile.getProfile(event).getLevel()) +
                                        "\nENERGY: " + sorino.getEnergy(Profile.getProfile(event).getLevel())
                                        + "\nTo choose this Sorino, enter `" + Prefix.guildPrefix(event.getGuild().getId())
                                        + "B" + sorino.getName().substring(0, sorino.getName().indexOf(":")) + "`",
                                true);

                    event.getChannel().sendMessage(message.build()).queue();
                    streetFight.saveFight(event.getGuild().getId(), event.getAuthor().getId());
                } catch (IOException | ClassNotFoundException e) {
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
                } catch (ProfileNotFoundException e) {
                    Logger logger =
                            new Logger("Error in finding Profile \n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                            "You don't have a profile! Enter: `" + Prefix.guildPrefix(event.getGuild().getId()) +
                                    "C`\n" +
                                    "If you do have an account, join the support server!"
                    ).queue();
                    try{
                        logger.logError();
                    } catch (IOException excI){
                        excI.printStackTrace();
                    }
                }
            } else if(Sorino.AllSorino.isSorino(command)){
                StreetFight fight = StreetFight.readFight(event.getGuild().getId(), event.getAuthor().getId());
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
                fight.saveFight(event.getGuild().getId(), event.getAuthor().getId());
            } else if(checkMove.apply(event).isPresent()){
                Move move = checkMove.apply(event).get();
                StreetFight fight = StreetFight.readFight(event.getGuild().getId(), event.getAuthor().getId());

                Optional<Fight.GameInfo> gameInfo = moveFunction.apply(new MoveFight(move, fight));
                fight.saveFight(event.getGuild().getId(), event.getAuthor().getId());
                gameInfo.ifPresent(gameInfo1 -> {
                    if(gameInfo1.winner.equals("Protector")){
                        try {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setColor(0x000dff);
                            embedBuilder.setTitle("You lost to: " + fight.protector.getName());
                            embedBuilder.setDescription(fight.protector.getWinningRemark());
                            event.getChannel().sendMessage(embedBuilder.build()).queue();

                            Profile profile = Profile.getProfile(event);
                            profile.incrementLoss();
                            profile.incrementXP(50, event);
                            profile.recreateProfile();
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
                            Profile profile = Profile.getProfile(event);

                            profile.incrementXP(400, event);
                            profile.incrementWin();
                            profile.addSorino(fight.protector.getGuardianSorino());
                            profile.setCoins(10000);
                            profile.recreateProfile();
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
                    fight.endFight(event.getGuild().getId(), event.getAuthor().getId());
                });
            } else if(command.toUpperCase().contains("END")){
                event.getChannel().sendMessage(event.getAuthor().getName() + " has ended his Street Fight!")
                        .queue();
                StreetFight.readFight(event.getGuild().getId(), event.getAuthor().getId())
                        .endFight(event.getGuild().getId(), event.getAuthor().getId());
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

        embedBuilder.setTitle("SorinoRPG is currently v0.2");
        embedBuilder.setDescription("[Update Log](https://sorinorpg.github.io/Sorino-Update-Log/)");

        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }),
    VOTE(event -> {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("Vote for 7,000 coins!");
        embedBuilder.setDescription("[Click to Vote](https://top.gg/bot/764566349543899149/vote)");

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
        }
        try {
            Profile giver = Profile.getProfile(event);
            giver.setCoins(-value);
            giver.recreateProfile();

            Profile receiver = Profile.getProfile(event.getMessage().getMentionedUsers().get(0) , event);
            receiver.setCoins(value);
            receiver.recreateProfile();

            event.getChannel().sendMessage(giver.getName() + " successfully donated " + value
                    + " to " + receiver.getName()).queue();
        } catch (Exception e) {
            event.getChannel().sendMessage("There was an error sending coins. Assert that all parties " +
                    "have an account and have entered `" + Prefix.guildPrefix(event.getGuild().getId()) + "C`." +
                    " If not, join the support server so we can fix this issue!").queue();
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
                put(Prefix.PrefixString.VOTE, Command.VOTE);
                put(Prefix.PrefixString.UPDATE, Command.UPDATE);
                put(Prefix.PrefixString.INFO, Command.INFO);
            }
        };

        String mess = message.getMessage().getContentDisplay()
                .replace(Prefix.guildPrefix(message.getGuild().getId()), "").toUpperCase();
        for(Prefix.PrefixString prefix : EnumSet.allOf(Prefix.PrefixString.class))
            if(mess.startsWith(prefix.prefix())) return commandHashMap.get(prefix);

        return Command.ERROR;
    }

    public static void registerProfile(GuildMessageReceivedEvent event) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    new FileInputStream(new File("/db/USERLIST.txt"))
            );

            @SuppressWarnings("unchecked")
            HashMap<String, ProfileStore> userList = (HashMap<String, ProfileStore>) objectInputStream.readObject();
            Profile profile = Profile.getProfile(event);
            ProfileStore ps = profile.profileToStore();

            userList.put(event.getAuthor().getId(), ps);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(
                            new File("/db/USERLIST.txt")
                    )
            );
            objectOutputStream.flush();
            objectOutputStream.writeObject(userList);
            objectOutputStream.close();

            profile.recreateProfile();

            event.getChannel().sendMessage("Saved account!").queue();
        } catch (Exception e){
            Logger logger = new Logger(Logger.exceptionAsString(e));
            event.getChannel().sendMessage("There was a problem saving your profile!\n" +
                    "Contact our Official Server to get help!").queue();

            try{
                logger.logError();
            } catch (IOException eI){
                eI.printStackTrace();
            }
        }
    }
}

