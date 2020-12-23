package main.userinterface;

import data.Profile;
import data.ProfileNotFoundException;
import data.files.Logger;

import game.value.Coins;
import game.SorinoNotFoundException;
import game.characters.Sorino;
import game.characters.starter.Gray;
import game.fight.Fight;
import game.fight.FightManager;

import game.fight.FightNotFoundException;
import game.fight.Move;
import game.value.Wrap;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


public enum Command {
    HELP(event -> {
        new Thread(() -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0xff0000);
            embedBuilder.setTitle("HELP");
            embedBuilder.setDescription("SorinoRPG Help and Information");
            embedBuilder.addField("Invite SorinoRPG to your server",
                    "[Invite](https://discord.com/oauth2/authorize?client_id=764566349543899149&scope=bot&permissions=392256)",
                    true);
            embedBuilder.addField("Website containing the command information",
                    "[Website](https://sorinorpg.github.io/SorinoRPG/)",
                    true);
            embedBuilder.addField("Follow Us on Twitter | Tweet us about the items you want in the " +
                            "upcoming shop!",
                    "[Twitter](https://twitter.com/RpgSorino)",
                    true);
            embedBuilder.addField("Become a Patron",
                    "[Patreon](https://www.patreon.com/sorinorpg?fan_landing=true)",
                    true);
            embedBuilder.addField("Email us!",
                    "SorinoRPG@gmail.com",
                    true);
            embedBuilder.addField("Vote for us on top.gg!",
                    "[Our Page](https://top.gg/bot/764566349543899149)",
                    true);

            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }).start();
    }),
    LEADERBOARD(event -> {
        class Sorting {
            boolean sorted = false;
            Profile temp;
                public void sort(List<Profile> a) {
                if(a.size() == 1) return;

                while(!sorted) {
                    sorted = true;
                    for (int i = 0; i < a.size() - 1; i++) {
                        try {
                            if (a.get(i).getLevel() > a.get(i + 1).getLevel()) {
                                temp = a.get(i);
                                a.set(i, a.get(i + 1));
                                a.set(i + 1, temp);
                                sorted = false;
                            }
                        } catch (NullPointerException ignored){
                            return;
                        }
                    }
                }
            }
        }

        new Thread(() -> {
            try {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Leaderboard");
                builder.setDescription("The leaderboard for the server");

                ArrayList<Profile> profileArr = Profile.getProfiles(event.getGuild().getId());
                new Sorting().sort(profileArr);

                for(int i = 0; i < profileArr.size() || i < 10; i++) {
                    try {
                        builder.addField(i + 1 + ". " +
                                        profileArr.get(i).getName() +
                                        " -- Trainer Level " + profileArr.get(i).getLevel(),
                                "", false);
                    } catch (NullPointerException | IndexOutOfBoundsException ignored){
                        break;
                    }
                }

                event.getChannel().sendMessage(builder.build()).queue();
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
                        message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                );
                try {
                    logger1.logError();
                } catch (IOException excI) {
                    excI.printStackTrace();
                }
            }

            try {
                Logger logger = new Logger("Shown rank");

                logger.logAction();
            } catch (IOException ioException) {
                Logger logger =
                        new Logger("Error in finding Profile due to IO and Classes \n" +
                                Logger.exceptionAsString(ioException));

                event.getChannel().sendMessage(
                        "Could not find profile due to IO and Classes "
                ).queue(message ->
                        message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                );
                try{
                    logger.logError();
                } catch (IOException excI){
                    excI.printStackTrace();
                }
            }
        }).start();

    }),
    SEE_RANK(event -> {
        new Thread(() -> {
            Logger logger;
            try {
                if(event.getMessage().getMentionedUsers().size() == 1){
                    event.getChannel().sendMessage(
                            Profile.getProfile((event.getMessage().getMentionedUsers().get(0)), event)
                            .showLevel(event
                                    .getChannel())).queue(message ->
                                        message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                    );
                }
                event.getChannel().sendMessage(Profile.getProfile(event)
                        .showLevel(event
                                .getChannel())).queue(message ->
                        message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                );
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
                        message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
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
                event.getChannel().sendMessage(
                        "Could not find profile. Have you created a profile? If so, try the Update Profile" +
                                " (-$) command. If the problem still persists, email SorinoRPG@gmail.com or " +
                                "mention us on twitter @Rpgsorino"
                ).queue(message ->
                        message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                );
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
                if (probabilityResult < 85) {
                    EmbedBuilder message = new EmbedBuilder();

                    int coins = new Random().nextInt(100);
                    if (coins == 0) return;
                    message.setTitle(event.getAuthor().getName() + " found something!");
                    message.setDescription(event.getMessage().getAuthor().getName()
                            + " found " + coins + " coins!");
                    message.setFooter(event.getMessage().getAuthor().getName()
                            + " found " + coins + " coins!", event.getAuthor().getAvatarUrl());
                    event.getChannel().sendMessage(message.build()).queue(message1 ->
                            message1.delete().queueAfter(5, TimeUnit.SECONDS)
                    );
                    profile.setCoins(coins);
                    profile.incrementXP(coins / 2, event.getChannel());
                    profile.recreateProfile();
                } else {
                    boolean didCatch = new Random().nextInt(100) > 93;
                    Sorino sorino = Sorino.AllSorino.getRandom();
                    if(profile.getLevel() < 7 && sorino.getName().contains("Hidden")){
                        event.getChannel().sendMessage("You cannot collect Hidden " +
                                "Sorino since you haven't reached Level 7").queue(
                                        message -> message.delete().queueAfter(5,
                                                TimeUnit.SECONDS)
                        );
                        return;
                    } else if(profile.getLevel() < 14 && sorino.getName().contains("Lost")){
                        event.getChannel().sendMessage("You cannot collect Lost " +
                                "Sorino since you haven't reached Level 14").queue(
                                message -> message.delete().queueAfter(5,
                                        TimeUnit.SECONDS)
                        );
                        return;
                    } else if (profile.getLevel() < 21 && sorino.getName().contains("Extinct")){
                        event.getChannel().sendMessage("You cannot collect Extinct " +
                                "Sorino since you haven't reached Level 21").queue(
                                message -> message.delete().queueAfter(5,
                                        TimeUnit.SECONDS)
                        );
                        return;
                    }
                    if (didCatch) {
                        EmbedBuilder message = new EmbedBuilder();
                        message.setTitle(event.getAuthor().getName() + " found something!");
                        message.setDescription("Successfully found a " + sorino.getName());
                        message.setFooter(" successfully found a " + sorino.getName(),
                                event.getAuthor().getAvatarUrl());
                        event.getChannel().sendMessage(message.build()).queue(message1 ->
                                message1.delete().queueAfter(5, TimeUnit.SECONDS)
                        );
                        profile.addSorino(sorino);
                        profile.incrementXP(50, event.getChannel());
                        profile.recreateProfile();
                    } else
                        event.getChannel().sendMessage(event.getAuthor().getName() +
                                " attempted to catch a " + sorino.getName() +
                                " but failed!").queue(message ->
                                message.delete().queueAfter(5, TimeUnit.SECONDS)
                        );
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
                ).queue(message ->
                        message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
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
                event.getChannel().sendMessage(
                        "Could not find profile. Have you created a profile? If so, try the Update Profile" +
                                " (-$) command. If the problem still persists, email SorinoRPG@gmail.com or " +
                                "mention us on twitter @Rpgsorino"
                ).queue(message ->
                        message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                );
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
        String rawMessage = Prefix.removeFightPrefix(event.getMessage().getContentRaw());
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
                    event.getChannel().sendMessage("You did not create a fight!").queue(message ->
                            message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                    );

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
                    long idSum = event.getAuthor().getIdLong() +
                            event.getMessage().getMentionedUsers().get(0).getIdLong();
                    Fight fight = Fight.readFight(event.getGuild().getId(), String.valueOf(idSum));

                    if (fight.fighters.get(fight.currFighter).getMove(input,
                            fight.fighters.get(fight.currFighter)).isPresent())
                        return fight.fighters.get(fight.currFighter).getMove(input,
                                fight.fighters.get(fight.currFighter)).get();
                    else return null;
                }catch (FightNotFoundException e) {
                    event.getChannel().sendMessage("You did not create a fight!").queue(message ->
                            message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                    );

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
                                event.getMessage().getMentionedUsers().get(0).getName() + " has ended").queue(
                                        message -> message.delete().queueAfter(5, TimeUnit.SECONDS)
                        );
                }catch (FightNotFoundException e) {
                    event.getChannel().sendMessage("You did not create a fight!").queue(message ->
                            message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                    );
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
                                "fight! Please end it!").queue(message ->
                                message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                        );
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
                            "fight! Please end it!").queue(message ->
                            message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                    );
                    try {
                        logger.logError();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (ProfileNotFoundException e) {
                    Logger logger =
                            new Logger("Error in finding Profile \n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                            "Could not find profile. Have you created a profile? If so, try the Update Profile" +
                                    " (-$) command. If the problem still persists, email SorinoRPG@gmail.com or " +
                                    "mention us on twitter @Rpgsorino"
                    ).queue(message ->
                            message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                    );
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
                                .queue(message ->
                                        message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                                );
                        logger.logError();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                } catch (FightNotFoundException e) {
                    event.getChannel().sendMessage("You did not create a fight!").queue(message ->
                            message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                    );
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
                        event.getChannel().sendMessage("There was a server error, tweet us " +
                                "@Rpgsorino to fix this ASAP")
                                .queue(message ->
                                        message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                                );
                        logger.logError();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                } catch (FightNotFoundException e) {
                    event.getChannel().sendMessage("You did not create a fight!").queue(message ->
                            message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                    );
                    Logger fightLogger = new Logger(Logger.exceptionAsString(e));
                    try {
                        fightLogger.logError();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (StringIndexOutOfBoundsException e){
            event.getChannel().sendMessage("You did not specify your opponent!").queue(message ->
                    message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
            );
        }
    }),
    CREATE_PROFILE(event -> {
        new Thread(() -> {
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
                ).queue(message ->
                        message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
                );
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

            message.setTitle(event.getAuthor().getName() + "'s Profile");
            message.setImage(event.getAuthor().getAvatarUrl());
            message.setDescription(userProfile.toString());
            message.setFooter("Created a profile", event.getAuthor().getAvatarUrl());

            event.getChannel()
                    .sendMessage(
                            message.build())
                    .queue(message1 -> message1.delete().queueAfter(
                            7, TimeUnit.SECONDS
                    ));
            try {
                logger.logAction();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }),
    SEE_PROFILE(event -> {
        Logger logger
            = new Logger("Checked profile");
        // Only one phase no methods needed
        try {
            Profile profile = Profile.getProfile(event);
            EmbedBuilder message = new EmbedBuilder();

            message.setTitle(event.getAuthor().getName() + "'s Profile");
            message.setImage(event.getAuthor().getAvatarUrl());
            message.setDescription(profile.toString());
            message.setFooter("Requested to see their profile", event.getAuthor().getAvatarUrl());

            event.getChannel().sendMessage(message.build()).queue(message1 -> message1.delete().queueAfter(
                    7, TimeUnit.SECONDS
            ));
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
                    message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
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
            event.getChannel().sendMessage(
                    "Could not find profile. Have you created a profile? If so, try the Update Profile" +
                            " (-$) command. If the problem still persists, email SorinoRPG@gmail.com or " +
                            "mention us on twitter @Rpgsorino"
            ).queue(message ->
                    message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
            );
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
            ).queue(message1 ->
                    message1.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
            );
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
                    "Could not find profile. Have you created a profile? If so, try the Update Profile" +
                            " (-$) command. If the problem still persists, email SorinoRPG@gmail.com or " +
                            "mention us on twitter @Rpgsorino"
            ).queue(message1 ->
                    message1.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
            );
            try{
                logger.logError();
            } catch (IOException excI){
                excI.printStackTrace();
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Wraps");
        embedBuilder.setDescription("Enter .WWRAP_NAME to buy a package, \n" +
                "For example to buy the Standard package one would type: .WStandard");
        embedBuilder.addField("STANDARD: 5,000 coins",
                "Contains a mix of 2 Common or Uncommon Sorino",
                true);
        embedBuilder.addField("PREMIUM: 15,000 coins",
                "Contains a mix of 3 Uncommon or Rare Sorino",
                true);
        embedBuilder.addField("CHAMPIONS: 50,000 coins",
                "Contains a mix of 3 Hidden or Lost Sorino",
                true);

        event.getChannel().sendMessage(embedBuilder.build()).queue(
                message1 -> message1.delete().queueAfter(10, TimeUnit.SECONDS)
        );
    }),
    ERROR(event -> {
        event.getChannel().sendMessage("Error in command, your syntax was incorrect, " +
                "check the syntax on the website!").queue(message ->
                message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
        );

        Logger logger =
                new Logger("Incorrect usage of syntax");

        try{
            logger.logError();
            logger.logAction();
        } catch (IOException excI){
            event.getChannel().sendMessage(
                    "Error in logging, mention a dev to get it fixed! @Developers\n" +
                            Logger.exceptionAsString(excI)
            ).queue(message ->
                    message.delete().queueAfter(7500, TimeUnit.MILLISECONDS)
            );

        }
    });

    UserAction userAction;

    Command(UserAction userAction){
        this.userAction = userAction;
    }
    static Command getCommand(Message message){
        HashMap<Prefix.PrefixString, Command> commandHashMap = new HashMap<>() {
            {
                put(Prefix.PrefixString.FIGHT, Command.FIGHT);
                put(Prefix.PrefixString.SEARCH, Command.SEARCH);
                put(Prefix.PrefixString.SEE_PROFILE, Command.SEE_PROFILE);
                put(Prefix.PrefixString.CREATE_PROFILE, Command.CREATE_PROFILE);
                put(Prefix.PrefixString.SEE_RANK, Command.SEE_RANK);
                put(Prefix.PrefixString.LEADERBOARD, Command.LEADERBOARD);
                put(Prefix.PrefixString.HELP, Command.HELP);
                put(Prefix.PrefixString.WRAP, Command.WRAP);
            }
        };

        String prefix = Prefix.cutPrefix(message.getContentRaw());
        if(Prefix.PrefixString.getPrefix(prefix).isPresent())
            return commandHashMap.get(Prefix.PrefixString.getPrefix(prefix).get());
        else if (prefix.equalsIgnoreCase(".h"))
            return Command.HELP;
        return Command.ERROR;
    }
}

