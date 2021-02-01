package main.userinterface;

import data.Profile;
import data.ProfileNotFoundException;
import data.logging.Logger;

import game.fight.*;
import game.fight.streetfight.StreetFight;
import game.fight.streetfight.StreetProtector;
import game.value.Coins;
import game.SorinoNotFoundException;
import game.characters.Sorino;
import game.characters.starter.Gray;

import game.value.Slots;
import game.value.Wrap;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


public enum Command {
    HELP(event -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Help -- January 27 update is live!");
            embedBuilder.setColor(0x000dff);
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

            event.getChannel().sendMessage(embedBuilder.build()).queue();
    }),
    SEE_RANK(event -> {
        new Thread(() -> {
            Logger logger;
            try {
                if(event.getMessage().getMentionedUsers().size() == 1){
                    event.getChannel().sendMessage(
                            Profile.getProfile((event.getMessage().getMentionedUsers().get(0)), event)
                            .showLevel(event
                                    .getChannel())).queue();
                }
                event.getChannel().sendMessage(Profile.getProfile(event)
                        .showLevel(event
                                .getChannel())).queue();
            } catch (IOException | ClassNotFoundException e) {
                Logger logger1 =
                        new Logger("Error in finding Profile due to IO and Classes \n" +
                                Logger.exceptionAsString(e));
                event.getChannel().sendMessage(
                        "Could not find profile due to a SorinoRPG error." +
                                " These could have been the causes:\n\n" +
                                "1. You simply have not created an account, use the .help command to find out how \n\n" +
                                "2. Your account is being used in something else," +
                                " most likely in a fight. You can end a fight with this command, (.FEND) so" +
                                " you can use your account.\n\n" +
                                "3. Your account is currently being reviewed or processed, this could be due" +
                                " to suspicious activity or receiving an award of some sort. For the latter," +
                                " you will be able to use your account in a minute or so. For the former," +
                                " this may happen regularly on and off until we can take further action.\n\n" +
                                "4. There was a server issue, please report this to our email " +
                                "SorinoRPG@gmail.com or mention us on twitter @Rpgsorino"
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
                    message.setFooter(event.getMessage().getAuthor().getName()
                            + " found " + coins + " coins!", event.getAuthor().getAvatarUrl());
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
                        message.setFooter(" successfully found a " + sorino.getName(),
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
            } catch (IOException | ClassNotFoundException e) {
                Logger logger1 =
                        new Logger("Error in finding Profile due to IO and Classes \n" +
                                Logger.exceptionAsString(e));
                event.getChannel().sendMessage(
                        "Could not find profile due to a SorinoRPG error." +
                                " These could have been the causes:\n\n" +
                                "1. You simply have not created an account, use the .help command to find out how \n\n" +
                                "2. Your account is being used in something else," +
                                " most likely in a fight. You can end a fight with this command, (.FEND) so" +
                                " you can use your account.\n\n" +
                                "3. Your account is currently being reviewed or processed, this could be due" +
                                " to suspicious activity or receiving an award of some sort. For the latter," +
                                " you will be able to use your account in a minute or so. For the former," +
                                " this may happen regularly on and off until we can take further action.\n\n" +
                                "4. There was a server issue, please report this to our email " +
                                "SorinoRPG@gmail.com or mention us on twitter @Rpgsorino"
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
                                        message.setImage(user.getAvatarUrl());
                                        event.getJDA().retrieveUserById(s.getLoser()).queue(user1 -> {
                                                    message.setDescription("has won " + coins[0]
                                                            + " coins for beating " + user1.getName());
                                                    message.setFooter("has won " + coins[0]
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
                                        message.setImage(user.getAvatarUrl());
                                        event.getJDA().retrieveUserById(s.getWinner()).queue(user1 -> {
                                                    message.setDescription("has lost " + (coins[1] - (coins[1] * 2))
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
    CREATE_PROFILE(event -> {
        if(new File("/db/" + event.getGuild().getId() +
                "/@@" + event.getAuthor().getId() + ".txt").exists()){
            event.getChannel().sendMessage("You already have an account!").queue();
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
                    "Could not find profile due to a SorinoRPG error." +
                            " These could have been the causes:\n\n" +
                            "1. You simply have not created an account, use the .help command to find out how \n\n" +
                            "2. Your account is being used in something else," +
                            " most likely in a fight. You can end a fight with this command, (.FEND) so" +
                            " you can use your account.\n\n" +
                            "3. Your account is currently being reviewed or processed, this could be due" +
                            " to suspicious activity or receiving an award of some sort. For the latter," +
                            " you will be able to use your account in a minute or so. For the former," +
                            " this may happen regularly on and off until we can take further action.\n\n" +
                            "4. There was a server issue, please report this to our email " +
                            "SorinoRPG@gmail.com or mention us on twitter @Rpgsorino"
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
        } catch (IOException | ClassNotFoundException e) {
            Logger logger1 =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile due to a SorinoRPG error." +
                            " These could have been the causes:\n\n" +
                            "1. You simply have not created an account, use the .help command to find out how \n\n" +
                            "2. Your account is being used in something else," +
                            " most likely in a fight. You can end a fight with this command, (.FEND) so" +
                            " you can use your account.\n\n" +
                            "3. Your account is currently being reviewed or processed, this could be due" +
                            " to suspicious activity or receiving an award of some sort. For the latter," +
                            " you will be able to use your account in a minute or so. For the former," +
                            " this may happen regularly on and off until we can take further action.\n\n" +
                            "4. There was a server issue, please report this to our email " +
                            "SorinoRPG@gmail.com or mention us on twitter @Rpgsorino"
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

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);


        embedBuilder.setTitle("Wraps");
        embedBuilder.setDescription("Enter .WWRAP_NAME to buy a package, \n" +
                "For example to buy the Standard package one would type: .WStandard");
        embedBuilder.addField("BASIC: 1,500 coins",
                "Contains 1 Common Sorino",
                true);
        embedBuilder.addField("STANDARD: 10,000 coins",
                "Contains a mix of 2 Common or Uncommon Sorino",
                true);
        embedBuilder.addField("PREMIUM: ~~45,000~~ 30,000 coins",
                "Contains a mix of 3 Uncommon or Rare Sorino",
                true);
        embedBuilder.addField("CHAMPIONS: ~~100,000~~ 75,000 coins",
                "Contains a mix of 3 Hidden or Lost Sorino",
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
                            "Could not save that channel!"
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
        catch (IOException | ClassNotFoundException e) {
            Logger logger1 =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile due to a SorinoRPG error." +
                            " These could have been the causes:\n\n" +
                            "1. You simply have not created an account, use the .help command to find out how \n\n" +
                            "2. Your account is being used in something else," +
                            " most likely in a fight. You can end a fight with this command, (.FEND) so" +
                            " you can use your account.\n\n" +
                            "3. Your account is currently being reviewed or processed, this could be due" +
                            " to suspicious activity or receiving an award of some sort. For the latter," +
                            " you will be able to use your account in a minute or so. For the former," +
                            " this may happen regularly on and off until we can take further action.\n\n" +
                            "4. There was a server issue, please report this to our email " +
                            "SorinoRPG@gmail.com or mention us on twitter @Rpgsorino"
            ).queue();
            try{
                logger1.logError();
            } catch (IOException excI){
                excI.printStackTrace();
            }
            return;
        } catch (ProfileNotFoundException e) {
            Logger logger =
                    new Logger("Error in finding Profile \n" +
                            Logger.exceptionAsString(e));
            try{
                logger.logError();
            } catch (IOException excI){
                excI.printStackTrace();
            }
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);
        embedBuilder.setTitle("How to play Slots!");
        embedBuilder.setDescription("To start you need to enter `" +
                Prefix.guildPrefix(event.getGuild().getId()) + "G stake`. If I wanted to " +
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
                "This will give you 2x your stake!",
                true);
        embedBuilder.addField("3 Sapphires",
                "This will give you 4x your stake!",
                true);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }),
    STREET_FIGHT(event -> {
        String command = event.getMessage().getContentRaw().toUpperCase().substring(
                event.getMessage().getContentRaw().indexOf("B")+1
        ).trim();
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
                embedBuilder.setImage(move.getUrl());
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
                        embedBuilder1.setImage(protectorMove.getUrl());
                        embedBuilder1.setFooter(fight.protector.getName()
                                + " gained " + protectorMove.getEffect() + " defence");

                        event.getChannel().sendMessage(embedBuilder1.build()).queue();
                    } else {
                        fight.opponents.get(1).takeDamage(protectorMove, event);
                        fight.opponents.get(0).dropEnergy(protectorMove);

                        EmbedBuilder embedBuilder2 = new EmbedBuilder();
                        embedBuilder2.setColor(0x000dff);
                        embedBuilder2.setImage(protectorMove.getUrl());
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
                embedBuilder.setImage(move.getUrl());
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
                        embedBuilder1.setImage(protectorMove.getUrl());
                        embedBuilder1.setFooter(fight.protector.getName()
                                + " gained " + protectorMove.getEffect() + " defence");

                        event.getChannel().sendMessage(embedBuilder1.build()).queue();
                    } else {
                        fight.opponents.get(1).takeDamage(protectorMove, event);
                        fight.opponents.get(0).dropEnergy(protectorMove);

                        EmbedBuilder embedBuilder2 = new EmbedBuilder();
                        embedBuilder2.setColor(0x000dff);
                        embedBuilder2.setImage(protectorMove.getUrl());
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
            message.addField("Moves: ", fight.userSorino.get(0).getMoves().toString(), false);
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

                message.setTitle("Specify your Sorino " + event.getAuthor().getName());
                message.setFooter("is choosing their Sorino", event.getAuthor().getAvatarUrl());
                message.setDescription("Choose one of your Sorino");
                try {
                    for (Sorino sorino : Profile.getProfile(event).getSorinoAsList())
                        message.addField(sorino.getName(),
                                "HEALTH: " + sorino.getHealth(Profile.getProfile(event).getLevel()) +
                                        "\nENERGY: " + sorino.getEnergy(Profile.getProfile(event).getLevel()),
                                true);

                    event.getChannel().sendMessage(message.build()).queue();
                    streetFight.saveFight(event.getGuild().getId(), event.getAuthor().getId());
                } catch (IOException | ClassNotFoundException e) {
                    Logger logger1 =
                            new Logger("Error in finding Profile due to IO and Classes \n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                            "Could not find profile due to IO and Classes"
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
                            "Could not find profile!"
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
                message.setFooter("Needs to enter their move", event.getAuthor().getAvatarUrl());
                message.addField("Moves: ", fight.userSorino.get(0).getMoves().toString(), false);
                event.getChannel().sendMessage(message.build())
                        .queue();
                fight.saveFight(event.getGuild().getId(), event.getAuthor().getId());
            } else if(checkMove.apply(event).isPresent()){
                Move move = checkMove.apply(event).get();
                StreetFight fight = StreetFight.readFight(event.getGuild().getId(), event.getAuthor().getId());

                Optional<Fight.GameInfo> gameInfo = moveFunction.apply(new MoveFight(move, fight));
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
                        } catch (IOException | ClassNotFoundException e) {
                            Logger logger1 =
                                    new Logger("Error in finding Profile due to IO and Classes \n" +
                                            Logger.exceptionAsString(e));
                            event.getChannel().sendMessage(
                                    "Could not find profile due to IO and Classes"
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
                                    "Could not find profile!"
                            ).queue();
                            try{
                                logger.logError();
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
                        }  catch (IOException | ClassNotFoundException e) {
                            Logger logger1 =
                                    new Logger("Error in finding Profile due to IO and Classes \n" +
                                            Logger.exceptionAsString(e));
                            event.getChannel().sendMessage(
                                    "Could not find profile due to IO and Classes"
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
                                    "Could not find profile!"
                            ).queue();
                            try{
                                logger.logError();
                            } catch (IOException excI){
                                excI.printStackTrace();
                            }
                        }
                    }
                });
                fight.saveFight(event.getGuild().getId(), event.getAuthor().getId());
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
    ERROR(event -> {
    });

    UserAction userAction;

    Command(UserAction userAction){
        this.userAction = userAction;
    }
    static Command getCommand(GuildMessageReceivedEvent message){
        HashMap<Prefix.PrefixString, Command> commandHashMap = new HashMap<>() {
            {
                put(Prefix.PrefixString.FIGHT, Command.FIGHT);
                put(Prefix.PrefixString.SEARCH, Command.SEARCH);
                put(Prefix.PrefixString.SEE_PROFILE, Command.SEE_PROFILE);
                put(Prefix.PrefixString.CREATE_PROFILE, Command.CREATE_PROFILE);
                put(Prefix.PrefixString.SEE_RANK, Command.SEE_RANK);
                put(Prefix.PrefixString.HELP, Command.HELP);
                put(Prefix.PrefixString.WRAP, Command.WRAP);
                put(Prefix.PrefixString.SLOT, Command.SLOT);
                put(Prefix.PrefixString.STREET_FIGHT, Command.STREET_FIGHT);
            }
        };

        String prefix = Prefix.cutPrefix(message);
        char c = prefix.charAt(Prefix.guildPrefix(message.getGuild().getId()).length());
        prefix = prefix.replace(c, Character.toUpperCase(c));

        if(Prefix.PrefixString.getPrefix(prefix, message.getGuild().getId()).isPresent())
            return commandHashMap.get(Prefix.PrefixString.getPrefix(prefix,
                    message.getGuild().getId()).get());
        return Command.ERROR;
    }

    public static void registerProfile(GuildMessageReceivedEvent event) throws IOException, ClassNotFoundException, ProfileNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(new File("/db/USERLIST.txt"))
        );

        @SuppressWarnings("unchecked")
        HashMap<String, Profile> userList = (HashMap<String, Profile>) objectInputStream.readObject();
        Profile profile = Profile.getProfile(event);

        userList.put(event.getAuthor().getId(), profile);
        profile.recreateProfile();
    }
}

