package main.userinterface;

import data.Profile;
import data.ProfileNotFoundException;
import data.files.Logger;

import game.Coins;
import game.SorinoNotFoundException;
import game.characters.Sorino;
import game.characters.starter.Gray;
import game.fight.Fight;
import game.fight.FightManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public enum Command {
    HELP(event -> {
        new Thread(() -> {
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
                        "Could not find profile due to a SorinoRPG server error." +
                                " These could have been the causes:\n\n" +
                                "1. Your account is being used in something else," +
                                " most likely in a fight. You can end a fight with this command, (-=END) so" +
                                " you can use your account.\n\n" +
                                "2. Your account is currently being reviewed or processed, this could be due" +
                                " to suspicious activity or receiving an award of some sort. For the latter," +
                                " you will be able to use your account in a minute or so. For the former," +
                                " this may happen regularly on and off until we can take further action.\n\n" +
                                "3. There was a server issue, please report this to our email " +
                                "SorinoRPG@gmail.com or mention us on twitter @Rpgsorino"
                ).queue();
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
                ).queue();
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
                        "Could not find profile due to a SorinoRPG server error." +
                                " These could have been the causes:\n\n" +
                                "1. Your account is being used in something else," +
                                " most likely in a fight. You can end a fight with this command, (-=END) so" +
                                " you can use your account.\n\n" +
                                "2. Your account is currently being reviewed or processed, this could be due" +
                                " to suspicious activity or receiving an award of some sort. For the latter," +
                                " you will be able to use your account in a minute or so. For the former," +
                                " this may happen regularly on and off until we can take further action.\n\n" +
                                "3. There was a server issue, please report this to our email " +
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
                event.getChannel().sendMessage(
                        "Could not find profile. Have you created a profile? If so, try the Update Profile" +
                                " (-$) command. If the problem still persists, email SorinoRPG@gmail.com or " +
                                "mention us on twitter @Rpgsorino"
                ).queue();
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
    ERASE_PROFILE(event -> {
        Logger logger = new Logger("Erased profile");

        event.getChannel().sendMessage("Erasing " + event.getAuthor().getName() + " profile...").queue();
        event.getAuthor().openPrivateChannel()
                .flatMap(channel -> channel.sendMessage("You erased your profile.")).queue();
        Profile.eraseProfile(event.getAuthor(), event);
        event.getChannel().sendMessage("The user has been erased successfully!")
                    .queue();

        try{
            logger.logAction();
        } catch (IOException e){
            e.printStackTrace();
        }
    }),
    SEARCH(event -> {
        new Thread(() -> {
            Logger logger;

            int probabilityResult = new Random().nextInt(100);

            if(probabilityResult < 60) {
                EmbedBuilder message = new EmbedBuilder();

                int coins = new Random().nextInt(20);
                message.setTitle(event.getAuthor().getName() + " found something!");
                message.setDescription(event.getMessage().getAuthor().getName()
                        + " found " + coins + " coins!");
                message.setFooter(event.getMessage().getAuthor().getName()
                        + " found " + coins + " coins!", event.getAuthor().getAvatarUrl());
                event.getChannel().sendMessage(message.build()).queue();
                try {
                    Profile profile = Profile.getProfile(event);
                    profile.setCoins(coins);
                    profile.incrementXP(coins, event.getChannel());
                    profile.recreateProfile();
                } catch (IOException | ClassNotFoundException e) {
                    Logger logger1 =
                            new Logger("Error in finding Profile due to IO and Classes \n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                            "Could not find profile due to a SorinoRPG server error." +
                                    " These could have been the causes:\n\n" +
                                    "1. Your account is being used in something else," +
                                    " most likely in a fight. You can end a fight with this command, (-=END) so" +
                                    " you can use your account.\n\n" +
                                    "2. Your account is currently being reviewed or processed, this could be due" +
                                    " to suspicious activity or receiving an award of some sort. For the latter," +
                                    " you will be able to use your account in a minute or so. For the former," +
                                    " this may happen regularly on and off until we can take further action.\n\n" +
                                    "3. There was a server issue, please report this to our email " +
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
                    event.getChannel().sendMessage(
                            "Could not find profile. Have you created a profile? If so, try the Update Profile" +
                                    " (-$) command. If the problem still persists, email SorinoRPG@gmail.com or " +
                                    "mention us on twitter @Rpgsorino"
                    ).queue();
                    try{
                        logger.logError();
                    } catch (IOException excI){
                        excI.printStackTrace();
                    }
                }
            }
            else {
                boolean didCatch = new Random().nextInt(100) > 5;
                Sorino sorino = Sorino.AllSorino.getRandom(event);
                if(didCatch){
                    EmbedBuilder message = new EmbedBuilder();
                    message.setTitle(event.getAuthor().getName() + " found something!");
                    message.setDescription("Successfully found a " + sorino.getName());
                    message.setFooter(" successfully found a " + sorino.getName(),
                            event.getAuthor().getAvatarUrl());
                    event.getChannel().sendMessage(message.build()).queue();
                    try {
                        Profile profile = Profile.getProfile(event);
                        profile.addSorino(sorino);
                        profile.incrementXP(30, event.getChannel());
                        profile.recreateProfile();
                    } catch (IOException | ClassNotFoundException e) {
                        Logger logger1 =
                                new Logger("Error in finding Profile due to IO and Classes \n" +
                                        Logger.exceptionAsString(e));
                        event.getChannel().sendMessage(
                                "Could not find profile due to a SorinoRPG server error." +
                                        " These could have been the causes:\n\n" +
                                        "1. Your account is being used in something else," +
                                        " most likely in a fight. You can end a fight with this command, (-=END) so" +
                                        " you can use your account.\n\n" +
                                        "2. Your account is currently being reviewed or processed, this could be due" +
                                        " to suspicious activity or receiving an award of some sort. For the latter," +
                                        " you will be able to use your account in a minute or so. For the former," +
                                        " this may happen regularly on and off until we can take further action.\n\n" +
                                        "3. There was a server issue, please report this to our email " +
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
                        event.getChannel().sendMessage(
                                "Could not find profile. Have you created a profile? If so, try the Update Profile" +
                                        " (-$) command. If the problem still persists, email SorinoRPG@gmail.com or " +
                                        "mention us on twitter @Rpgsorino"
                        ).queue();
                        try{
                            logger.logError();
                        } catch (IOException excI){
                            excI.printStackTrace();
                        }
                    }
                } else
                    event.getChannel().sendMessage("You attempted to catch a " + sorino.getName() +
                            " but failed!").queue();
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
        Logger logger;
        long sum = event.getAuthor().getIdLong() + event.getMessage().getMentionedUsers().get(0).getIdLong();
        Fight fight;
        fight = Fight.getFight(String.valueOf(sum), event.getGuild().getId());
        String rawMess = event.getMessage().getContentRaw();
        if(rawMess.endsWith("!!")){
            try {
                FightManager.addSwitchOut(Sorino.AllSorino.getSorino(
                        rawMess.substring(2, rawMess.indexOf("!"))
                ), event, fight);
            } catch(SorinoNotFoundException e){
                logger =
                        new Logger("Error in finding Sorino\n" +
                                Logger.exceptionAsString(e));
                event.getChannel().sendMessage(
                        "Sorino does not exist!"
                ).queue();
                try{
                    logger.logError();
                } catch (IOException excI){
                    excI.printStackTrace();
                }
            }
        }
        else if(rawMess.startsWith(
                Prefix.PrefixString.FIGHT.prefix()
        ) && event.getMessage().getMentionedUsers().size() == 1) {
                    FightManager.fightPhase1(event, fight);
        }else if(Sorino.AllSorino.isSorino(rawMess)){
            try {
                FightManager.fightPhase2(event,
                        Profile.getProfile(event), fight);
            } catch (IOException | ClassNotFoundException e) {
                Logger logger1 =
                        new Logger("Error in finding Profile due to IO and Classes \n" +
                                Logger.exceptionAsString(e));
                event.getChannel().sendMessage(
                        "Could not find profile due to a SorinoRPG server error." +
                                " These could have been the causes:\n\n" +
                                "1. Your account is being used in something else," +
                                " most likely in a fight. You can end a fight with this command, (-=END) so" +
                                " you can use your account.\n\n" +
                                "2. Your account is currently being reviewed or processed, this could be due" +
                                " to suspicious activity or receiving an award of some sort. For the latter," +
                                " you will be able to use your account in a minute or so. For the former," +
                                " this may happen regularly on and off until we can take further action.\n\n" +
                                "3. There was a server issue, please report this to our email " +
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
                event.getChannel().sendMessage(
                        "Could not find profile. Have you created a profile? If so, try the Update Profile" +
                                " (-$) command. If the problem still persists, email SorinoRPG@gmail.com or " +
                                "mention us on twitter @Rpgsorino"
                ).queue();
                try{
                    logger.logError();
                } catch (IOException excI){
                    excI.printStackTrace();
                }
            }
        }else if(fight.currentFighters.get(fight.currentFighter).getMove(Prefix.removeFightPrefix(rawMess),
                fight.currentFighters.get(fight.currentFighter)).isPresent()) {

            Optional<Fight.GameInfo> didWin = FightManager
                    .fightPhase3(fight
                                    .currentFighters
                                    .get(fight
                                            .currentFighter)
                                    .getMove(Prefix
                                            .removeFightPrefix(rawMess),
                                            fight.currentFighters
                                                    .get(fight.currentFighter)).get(),
                    event, fight);
            didWin.ifPresent(s ->{
                EmbedBuilder message = new EmbedBuilder();
                message.setTitle("WINNER");
                message.setImage(s.getWinner().getAvatarUrl());
                message.setFooter("has won 300 coins for beating " + s.getLoser().getName(),
                        s.getWinner().getAvatarUrl());
                event.getChannel().sendMessage(message.build()).queue();

                message = new EmbedBuilder();
                message.setTitle("LOSER");
                message.setImage(s.getLoser().getAvatarUrl());
                message.setFooter("has lost 50 coins for losing to " + s.getWinner().getName(),
                        s.getLoser().getAvatarUrl());
                event.getChannel().sendMessage(message.build()).queue();
                try {
                    FightManager.fightPhase4(event, fight, String.valueOf(sum));
                } catch (IOException | ClassNotFoundException e) {
                    Logger logger1 =
                            new Logger("Error in finding Profile due to IO and Classes \n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                            "Could not find profile due to a SorinoRPG server error." +
                                    " These could have been the causes:\n\n" +
                                    "1. Your account is being used in something else," +
                                    " most likely in a fight. You can end a fight with this command, (-=END) so" +
                                    " you can use your account.\n\n" +
                                    "2. Your account is currently being reviewed or processed, this could be due" +
                                    " to suspicious activity or receiving an award of some sort. For the latter," +
                                    " you will be able to use your account in a minute or so. For the former," +
                                    " this may happen regularly on and off until we can take further action.\n\n" +
                                    "3. There was a server issue, please report this to our email " +
                                    "SorinoRPG@gmail.com or mention us on twitter @Rpgsorino"
                    ).queue();
                    try{
                        logger1.logError();
                    } catch (IOException excI){
                        excI.printStackTrace();
                    }
                } catch (ProfileNotFoundException e) {
                    Logger logger1 =
                            new Logger("Error in finding Profile \n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                            "Could not find profile. Have you created a profile? If so, try the Update Profile" +
                                    " (-$) command. If the problem still persists, email SorinoRPG@gmail.com or " +
                                    "mention us on twitter @Rpgsorino"
                    ).queue();
                    try{
                        logger1.logError();
                    } catch (IOException excI){
                        excI.printStackTrace();
                    }
                }
            });
        }

        try {
            if(fight.save) fight.saveFight(event.getGuild().getId(), String.valueOf(sum));
            logger = new Logger("Started fight");
            logger.logAction();
        } catch (IOException e) {
            e.printStackTrace();
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

            message.setTitle(event.getAuthor().getName() + "'s Profile");
            message.setImage(event.getAuthor().getAvatarUrl());
            message.setDescription(userProfile.toString());
            message.setFooter("Created a profile", event.getAuthor().getAvatarUrl());

            event.getChannel()
                    .sendMessage(
                            message.build())
                    .queue();
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

            event.getChannel().sendMessage(message.build()).queue();
        } catch (IOException | ClassNotFoundException e) {
            Logger logger1 =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile due to a SorinoRPG server error." +
                            " These could have been the causes:\n\n" +
                            "1. Your account is being used in something else," +
                            " most likely in a fight. You can end a fight with this command, (-=END) so" +
                            " you can use your account.\n\n" +
                            "2. Your account is currently being reviewed or processed, this could be due" +
                            " to suspicious activity or receiving an award of some sort. For the latter," +
                            " you will be able to use your account in a minute or so. For the former," +
                            " this may happen regularly on and off until we can take further action.\n\n" +
                            "3. There was a server issue, please report this to our email " +
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
            event.getChannel().sendMessage(
                    "Could not find profile. Have you created a profile? If so, try the Update Profile" +
                            " (-$) command. If the problem still persists, email SorinoRPG@gmail.com or " +
                            "mention us on twitter @Rpgsorino"
            ).queue();
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
    // TODO Fix algorithm
    UPDATE_PROFILE(event -> {
        event.getChannel().sendMessage("Updating " + event.getAuthor()
                .getAsMention() + "'s account to current version...").queue();

        try{
            //Gets all user info
            ArrayList<String> userInfo = Profile.getProfileAsString(event.getAuthor(), event);
            Profile.eraseProfile(event.getAuthor(), event);
            ArrayList<Sorino> userSorino = new ArrayList<>();
            StringBuilder listBuild = new StringBuilder(userInfo.get(0));

            //Counts the occurrences of regex ","
            Pattern pat = Pattern.compile(",");
            Matcher matcher = pat.matcher(userInfo.get(0));
            int occurrences = 0;
            while(matcher.find()) occurrences++;

            //Adds each element of the "list" as an Sorino
            for(int i = 0; i < occurrences; i++){
                try {
                    userSorino.add(Sorino.AllSorino.getSorino(listBuild
                            .substring(0,
                                    listBuild
                                            .indexOf(","))));

                    //Deletes latest element in the list
                    listBuild.delete(0, listBuild.indexOf(",") + 1);
                } catch(SorinoNotFoundException e){
                    Logger logger =
                            new Logger("Error in finding Sorino\n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                            "Could not find profile!"
                    ).queue();
                    try{
                        logger.logError();
                    } catch (IOException excI){
                        e.printStackTrace();
                    }
                }
            }
            Profile newProfile = new Profile(userSorino,
                    new Coins(Integer.parseInt(userInfo.get(1).substring(7))),
                    event.getAuthor().getId(),
                    event.getAuthor().getName(),
                    Integer.parseInt(userInfo.get(2).substring(6)),
                    Integer.parseInt(userInfo.get(3).substring(7)),
                    event.getAuthor().getAvatarUrl(), event.getGuild().getId());
            newProfile.createProfile();

            event.getChannel().sendMessage("Update successful!").queue();
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
                e.printStackTrace();
            }
        } catch (IOException e) {
            Logger logger1 =
                    new Logger("Error in updating profile\n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "There was a server issue with updating your profile." +
                            "This problem is most likely our of your control, so " +
                            "email SorinoRPG@gmail.com or mention us on twitter @Rpgsorino"
            ).queue();
            try{
                logger1.logError();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }
    }),
    ERROR(event -> {
        event.getChannel().sendMessage("Error in command, your syntax was incorrect," +
                "check the syntax on the website!").queue();

        Logger logger =
                new Logger("Incorrect usage of syntax");

        try{
            logger.logError();
            logger.logAction();
        } catch (IOException excI){
            event.getChannel().sendMessage(
                    "Error in logging, mention a dev to get it fixed! @Developers\n" +
                            Logger.exceptionAsString(excI)
            ).queue();

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
                put(Prefix.PrefixString.UPDATE_PROFILE, Command.UPDATE_PROFILE);
                put(Prefix.PrefixString.CREATE_PROFILE, Command.CREATE_PROFILE);
                put(Prefix.PrefixString.ERASE_PROFILE, Command.ERASE_PROFILE);
                put(Prefix.PrefixString.SEE_RANK, Command.SEE_RANK);
                put(Prefix.PrefixString.LEADERBOARD, Command.LEADERBOARD);
                put(Prefix.PrefixString.HELP, Command.HELP);
            }
        };

        String prefix = Prefix.cutPrefix(message.getContentRaw());
        if(Prefix.PrefixString.getPrefix(prefix).isPresent())
            return commandHashMap.get(Prefix.PrefixString.getPrefix(prefix).get());
        else if (prefix.equalsIgnoreCase("-h"))
            return Command.HELP;
        return Command.ERROR;
    }
}

