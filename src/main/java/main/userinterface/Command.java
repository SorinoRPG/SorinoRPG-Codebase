package main.userinterface;

import data.Profile;
import data.ProfileNotFoundException;
import data.files.Logger;

import game.Coins;
import game.IgnatiamonNotFoundException;
import game.characters.Ignatiamon;
import game.characters.starter.Gray;
import game.fight.FightManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public enum Command {
    LEADERBOARD(event -> {
        Logger logger;

        class Sorting {
            void quickSort(Profile[] arr, int begin, int end) {
                if (begin < end) {
                    int partitionIndex = partition(arr, begin, end);

                    quickSort(arr, begin, partitionIndex-1);
                    quickSort(arr, partitionIndex+1, end);
                }
            }

            private int partition(Profile[] arr, int begin, int end) {
                int pivot = arr[end].getLevel();
                int i = (begin - 1);

                for (int j = begin; j < end; j++) {
                    if (arr[j].getLevel() <= pivot) {
                        i++;

                        Profile swapTemp = arr[i];
                        arr[i] = arr[j];
                        arr[j] = swapTemp;
                    }
                }

                Profile swapTemp = arr[i + 1];
                arr[i + 1] = arr[end];
                arr[end] = swapTemp;
                
                return i+1;
            }
        }

        try {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Leaderboard");
            builder.setDescription("The leaderboard for the server");

            Profile[] profileArr = Profile.allProfiles(Profile.getUserSize(event.getGuild()));
            new Sorting().quickSort(profileArr, 0, profileArr.length);

            for(int i = 0; i < profileArr.length; i++)
                builder.addField(i+1 + profileArr[i].getName() + " -- Level " + profileArr[i].getLevel(),
                        "", false);

            event.getChannel().sendMessage(builder.build()).queue();
        } catch (IOException | ClassNotFoundException e) {
            logger =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try {
                logger.logError();
                logger.closeLogger();
            } catch (IOException excI) {
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }

        try {
            logger = new Logger("Shown rank");

            logger.logAction();
            logger.closeLogger();
        } catch (IOException ioException) {
            logger =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(ioException));

            event.getChannel().sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try{
                logger.logError();
                logger.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }
    }),
    SEE_RANK(event -> {
        Logger logger;
        try {
            event.getChannel().sendMessage(Profile.getProfile(event)
                    .showLevel(event
                            .getChannel())).queue();
        }catch (IOException | ClassNotFoundException e) {
            logger =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try{
                logger.logError();
                logger.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        } catch (ProfileNotFoundException e) {
            logger =
                    new Logger("Error in finding Profile \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile!"
            ).queue();
            try{
                logger.logError();
                logger.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }

        try {
            logger = new Logger("Shown rank");

            logger.logAction();
            logger.closeLogger();
        } catch (IOException ioException) {
            logger =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(ioException));

            event.getChannel().sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try{
                logger.logError();
                logger.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }
    }),
    ERASE_PROFILE(event -> {
        Logger logger = new Logger("Erased profile");
        Optional<Role> userRole = Objects
                .requireNonNull(event
                        .getMember())
                .getRoles()
                .stream()
                .filter(role -> role.getName().equals("Developers") || role.getName().equals("Owner"))
                .findFirst();
        if (userRole.isEmpty()) return;

        event.getChannel().sendMessage("Erasing " + event
                .getMessage()
                .getMentionedUsers()
                .get(0).getAsMention() + " profile...").queue();


        String message = event.getMessage().getContentRaw();
        event.getAuthor().openPrivateChannel()
                .flatMap(channel -> channel.sendMessage("You have been deleted for: " + message.substring(2,
                        message.indexOf(" "))))
                .queue();
        Profile.eraseProfile(event
                .getMessage()
                .getMentionedUsers()
                .get(0), event);
        event.getChannel().sendMessage("The user has been erased successfully!")
                    .queue();

        try{
            logger.logAction();
            logger.closeLogger();
        } catch (IOException e){
            logger =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));

            event.getChannel().sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try{
                logger.logError();
                logger.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }
    }),
    SEARCH(event -> {
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
                profile.increaseCoins(coins);
                profile.incrementXP(coins, event.getChannel());
                profile.recreateProfile();
            } catch (IOException | ClassNotFoundException e) {
                logger =
                        new Logger("Error in finding Profile due to IO and Classes \n" +
                                Logger.exceptionAsString(e));
                event.getChannel().sendMessage(
                        "Could not find profile due to IO and Classes "
                ).queue();
                try{
                    logger.logError();
                    logger.closeLogger();
                } catch (IOException excI){
                    event.getChannel().sendMessage(
                            "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                    Logger.exceptionAsString(excI)
                    ).queue();
                }
            } catch (ProfileNotFoundException e) {
                logger =
                        new Logger("Error in finding Profile \n" +
                                Logger.exceptionAsString(e));
                event.getChannel().sendMessage(
                        "Could not find profile!"
                ).queue();
                try{
                    logger.logError();
                    logger.closeLogger();
                } catch (IOException excI){
                    event.getChannel().sendMessage(
                            "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                    Logger.exceptionAsString(excI)
                    ).queue();
                }
            }
        }
        else {
            boolean didCatch = new Random().nextInt(100) > 85;
            Ignatiamon ignatiamon = Ignatiamon.AllIgnatiamon.getRandom(event);
            if(didCatch){
                EmbedBuilder message = new EmbedBuilder();
                message.setTitle(event.getAuthor().getName() + " found something!");
                message.setDescription("Successfully found a " + ignatiamon.getName());
                message.setFooter(" successfully found a " + ignatiamon.getName(),
                        event.getAuthor().getAvatarUrl());
                event.getChannel().sendMessage(message.build()).queue();
                try {
                    Profile profile = Profile.getProfile(event);
                    profile.addIgnatiamon(ignatiamon);
                    profile.incrementXP(30, event.getChannel());
                    profile.recreateProfile();
                } catch (IOException | ClassNotFoundException e) {
                    logger =
                            new Logger("Error in finding Profile due to IO and Classes \n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                            "Could not find profile due to IO and Classes "
                    ).queue();
                    try{
                        logger.logError();
                        logger.closeLogger();
                    } catch (IOException excI){
                        event.getChannel().sendMessage(
                                "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                        Logger.exceptionAsString(excI)
                        ).queue();
                    }
                } catch (ProfileNotFoundException e) {
                    logger =
                            new Logger("Error in finding Profile \n" +
                                    Logger.exceptionAsString(e));

                    event.getChannel().sendMessage(
                            "Could not find profile!"
                    ).queue();
                    try{
                        logger.logError();
                        logger.closeLogger();
                    } catch (IOException excI){
                        event.getChannel().sendMessage(
                                "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                        Logger.exceptionAsString(excI)
                        ).queue();
                    }
                }
            } else
                event.getChannel().sendMessage("You attempted to catch a " + ignatiamon.getName() +
                    " but failed!").queue();
        }
        try {
            logger = new Logger("Search");
            logger.logAction();
            logger.closeLogger();
        } catch (IOException e) {
            Logger logger1 =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));

            event.getChannel().sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try{
                logger1.logError();
                logger1.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }

    }),
    FIGHT(event -> {
        Logger logger;
        String rawMess = event.getMessage().getContentRaw();
        if(rawMess.endsWith("!!")){
            try {
                FightManager.addSwitchOut(Ignatiamon.AllIgnatiamon.getIgnatiamon(
                        rawMess.substring(2, rawMess.indexOf("!"))
                ), event);
            } catch(IgnatiamonNotFoundException e){
                logger =
                        new Logger("Error in finding ignatiamon\n" +
                                Logger.exceptionAsString(e));
                event.getChannel().sendMessage(
                        "Could not find profile!"
                ).queue();
                try{
                    logger.logError();
                    logger.closeLogger();
                } catch (IOException excI){
                    event.getChannel().sendMessage(
                            "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                    Logger.exceptionAsString(excI)
                    ).queue();
                }
            }
        }
        else if(rawMess.startsWith(
                Prefix.PrefixString.FIGHT.prefix()
        ) && event.getMessage().getMentionedUsers().size() == 1) {

            FightManager.users =
                    FightManager.fightPhase1(event);
        }else if(Ignatiamon.AllIgnatiamon.isIgnatiamon(rawMess)){
            try {
                FightManager.fightPhase2(event,
                        Profile.getProfile(event));
            } catch (IOException | ClassNotFoundException e) {
                Logger logger1 =
                        new Logger("Error in finding Profile due to IO and Classes \n" +
                                Logger.exceptionAsString(e));
                event.getChannel().sendMessage(
                        "Could not find profile due to IO and Classes "
                ).queue();
                try{
                    logger1.logError();
                    logger1.closeLogger();
                } catch (IOException excI){
                    event.getChannel().sendMessage(
                            "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                    Logger.exceptionAsString(excI)
                    ).queue();
                }
            } catch (ProfileNotFoundException e) {
                logger =
                        new Logger("Error in finding Profile \n" +
                                Logger.exceptionAsString(e));
                event.getChannel().sendMessage(
                        "Could not find profile!"
                ).queue();
                try{
                    logger.logError();
                    logger.closeLogger();
                } catch (IOException excI){
                    event.getChannel().sendMessage(
                            "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                    Logger.exceptionAsString(excI)
                    ).queue();
                }
            }
        }else if(FightManager.currentFighters.get(
                FightManager.currentFighter).getMove(Prefix.removeFightPrefix(rawMess),
                FightManager.currentFighters.get(FightManager.currentFighter)).isPresent()) {

            Optional<FightManager.GameInfo> didWin = FightManager.fightPhase3(FightManager.currentFighters
                    .get(FightManager.currentFighter).getMove(Prefix.removeFightPrefix(rawMess),
                    FightManager.currentFighters.get(FightManager.currentFighter)).get(), event);
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
                    FightManager.fightPhase4(event);
                } catch (IOException | ClassNotFoundException e) {
                    Logger logger1 =
                            new Logger("Error in finding Profile due to IO and Classes \n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                           "Could not find profile due to IO and Classes "
                    ).queue();
                    try{
                        logger1.logError();
                        logger1.closeLogger();
                    } catch (IOException excI){
                        event.getChannel().sendMessage(
                                "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                        Logger.exceptionAsString(excI)
                        ).queue();
                    }
                } catch (ProfileNotFoundException e) {
                    Logger logger1 =
                            new Logger("Error in finding Profile \n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                            "Could not find profile!"
                    ).queue();
                    try{
                        logger1.logError();
                        logger1.closeLogger();
                    } catch (IOException excI){
                        event.getChannel().sendMessage(
                                "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                        Logger.exceptionAsString(excI)
                        ).queue();
                    }
                }
            });
        }

        try {
            logger = new Logger("Started fight");
            logger.logAction();
            logger.closeLogger();
        } catch (IOException e) {
            Logger logger1 =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try{
                logger1.logError();
                logger1.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }
    }),
    CREATE_PROFILE(event -> {
        Logger logger
            = new Logger("Created profile");
        // Only one phase, no method needed
        Profile userProfile =
                new Profile(new ArrayList<>(Collections.singletonList(new Gray())),
                        new Coins(50),
                        event.getAuthor().getName(), 0, 0, event.getAuthor().getAvatarUrl(),
                        event.getGuild());
        try {
            userProfile.createProfile();
        } catch(IOException e){

            logger = new Logger("Error in creating profile + \n" +
                    Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Error in creating files, mention a dev to get it fixed! @Developers\n"
            ).queue();
            System.out.println(Logger.exceptionAsString(e));
            try {
                logger.logError();
                logger.closeLogger();
                return;
            } catch (IOException exc){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(exc)
                ).queue();
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
            logger.closeLogger();
        } catch (IOException e) {
            Logger logger1 =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try{
                logger1.logError();
                logger1.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }
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
                    "Could not find profile due to IO and Classes "
            ).queue();
            try{
                logger1.logError();
                logger1.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        } catch (ProfileNotFoundException e) {
            logger =
                    new Logger("Error in finding Profile \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile!"
            ).queue();
            try{
                logger.logError();
                logger.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }
        try {
            logger.logAction();
            logger.closeLogger();
        } catch (IOException e) {
            Logger logger1 =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try{
                logger1.logError();
                logger1.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
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
            ArrayList<Ignatiamon> userIgnatiamon = new ArrayList<>();
            StringBuilder listBuild = new StringBuilder(userInfo.get(0));

            //Counts the occurrences of regex ","
            Pattern pat = Pattern.compile(",");
            Matcher matcher = pat.matcher(userInfo.get(0));
            int occurrences = 0;
            while(matcher.find()) occurrences++;

            //Adds each element of the "list" as an Ignatiamon
            for(int i = 0; i < occurrences; i++){
                try {
                    userIgnatiamon.add(Ignatiamon.AllIgnatiamon.getIgnatiamon(listBuild
                            .substring(0,
                                    listBuild
                                            .indexOf(","))));

                    //Deletes latest element in the list
                    listBuild.delete(0, listBuild.indexOf(",") + 1);
                } catch(IgnatiamonNotFoundException e){
                    Logger logger =
                            new Logger("Error in finding ignatiamon\n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                            "Could not find profile!"
                    ).queue();
                    try{
                        logger.logError();
                        logger.closeLogger();
                    } catch (IOException excI){
                        event.getChannel().sendMessage(
                                "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                        Logger.exceptionAsString(excI)
                        ).queue();
                    }
                }
            }
            Profile newProfile = new Profile(userIgnatiamon,
                    new Coins(Integer.parseInt(userInfo.get(1).substring(7))),
                    event.getAuthor().getName(),
                    Integer.parseInt(userInfo.get(2).substring(6)),
                    Integer.parseInt(userInfo.get(3).substring(7)),
                    event.getAuthor().getAvatarUrl(), event.getGuild());
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
                logger.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        } catch (IOException e) {
            Logger logger1 =
                    new Logger("Error in updating profile\n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try{
                logger1.logError();
                logger1.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }
    }),
    AWARD_OP(event -> {
        Optional<Role> userRole = Objects
                .requireNonNull(event
                        .getMember())
                .getRoles()
                .stream()
                .filter(role -> role.getName().equals("Developers") || role.getName().equals("Owner"))
                .findFirst();
        if(userRole.isEmpty()) return;
        try {
            event.getChannel().sendMessage("You are eligible to Award OP's!").queue();
            String message = event.getMessage().getContentRaw();
            User awardee = event.getMessage().getMentionedUsers().get(0);
            Profile awardeeProfile = Profile.getProfile(awardee, event);
            awardeeProfile.addIgnatiamon(Ignatiamon.AllIgnatiamon.getOP(
                    message.substring(2,
                            event.getMessage().getContentRaw().indexOf(" "))
            ));

            awardeeProfile.recreateProfile();
            try {
                Logger logger = new Logger("Awarded OP Ignatiamon");
                logger.logAction();
                logger.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger logger1 =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try{
                logger1.logError();
                logger1.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
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
                logger.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        } catch(IgnatiamonNotFoundException e){
            Logger logger =
                    new Logger("Error in finding ignatiamon\n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile!"
            ).queue();
            try{
                logger.logError();
                logger.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }
    }),
    /* TODO In progress
    RETRACT_OP(event -> {
        if(!event.getAuthor().getName().equalsIgnoreCase("DentedHead") ||
            !event.getAuthor().getName().equalsIgnoreCase("DevXtra"))
            return;
        try {
            event.getChannel().sendMessage("You are eligible to Award OP's!").queue();
            String message = event.getMessage().getContentRaw();
            User awardee = event.getMessage().getMentionedUsers().get(0);
            Profile awardeeProfile = Profile.getProfile(awardee);
            awardeeProfile.removeIgnatiamon(Ignatiamon.AllIgnatiamon.getOP(
                    message.substring(2,
                            event.getMessage().getContentRaw().indexOf(" "))
            ));
            awardeeProfile.recreateProfile();
            try {
                Logger logger = new Logger("Retracted OP Ignatiamon");
                logger.logAction();
                logger.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        } catch (IOException | ClassNotFoundException e) {
            Logger logger1 =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try{
                logger1.logError();
                logger1.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
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
                logger.closeLogger();
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }
    }), */
    ADD_IGNATIAMON(event -> {
        Optional<Role> userRole = Objects
                .requireNonNull(event
                        .getMember())
                .getRoles()
                .stream()
                .filter(role -> role.getName().equals("Developers"))
                .findFirst();

        if (userRole.isPresent()){
            event.getChannel().sendMessage("You are eligible to give an Ignatiamon").queue();
            try {
                Profile profile = Profile.getProfile(event
                        .getMessage()
                        .getMentionedUsers()
                        .get(0), event);
                profile.addIgnatiamon(Ignatiamon
                        .AllIgnatiamon
                        .getIgnatiamon(event
                                .getMessage()
                                .getContentRaw()
                                .substring(2,
                        event.getMessage().getContentRaw().indexOf(" "))));
                profile.recreateProfile();
            } catch (IOException | ClassNotFoundException e) {
                Logger logger1 =
                        new Logger("Error in finding Profile due to IO and Classes \n" +
                                Logger.exceptionAsString(e));
                event.getChannel().sendMessage(
                        "Could not find profile due to IO and Classes "
                ).queue();
                try{
                    logger1.logError();
                    logger1.closeLogger();
                } catch (IOException excI){
                    event.getChannel().sendMessage(
                            "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                    Logger.exceptionAsString(excI)
                    ).queue();
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
                    logger.closeLogger();
                } catch (IOException excI){
                    event.getChannel().sendMessage(
                            "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                    Logger.exceptionAsString(excI)
                    ).queue();
                }
            } catch(IgnatiamonNotFoundException e){
                Logger logger =
                        new Logger("Error in finding ignatiamon\n" +
                                Logger.exceptionAsString(e));
                event.getChannel().sendMessage(
                        "Could not find profile!"
                ).queue();
                try{
                    logger.logError();
                    logger.closeLogger();
                } catch (IOException excI){
                    event.getChannel().sendMessage(
                            "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                    Logger.exceptionAsString(excI)
                    ).queue();
                }
            }
        }
    }),
    /* TODO In progress
    REMOVE_IGNATIAMON(event -> {
        Optional<Role> userRole = Objects
                .requireNonNull(event
                .getMember())
                .getRoles()
                .stream()
                .filter(role -> role.getName().equals("Developers"))
                .findFirst();

        if (userRole.isPresent()){
            event.getChannel().sendMessage("You are eligible to remove Ignatiamon").queue();
            try {
                Profile profile = Profile.getProfile(event
                        .getMessage()
                        .getMentionedUsers()
                        .get(0));
                profile.removeIgnatiamon(Ignatiamon
                        .AllIgnatiamon
                        .getIgnatiamon(event
                                .getMessage()
                                .getContentRaw()
                                .substring(2,
                                        event.getMessage().getContentRaw().indexOf(" "))));
                profile.recreateProfile();
            } catch (IOException | ClassNotFoundException e) {
                Logger logger1 =
                        new Logger("Error in finding Profile due to IO and Classes \n" +
                                Logger.exceptionAsString(e));
                event.getChannel().sendMessage(
                        "Could not find profile due to IO and Classes "
                ).queue();
                try{
                    logger1.logError();
                    logger1.closeLogger();
                } catch (IOException excI){
                    event.getChannel().sendMessage(
                            "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                    Logger.exceptionAsString(excI)
                    ).queue();
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
                    logger.closeLogger();
                } catch (IOException excI){
                    event.getChannel().sendMessage(
                            "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                    Logger.exceptionAsString(excI)
                    ).queue();
                }
            }
        }
    }),*/
    GET_LOGS(event -> {
        Logger logger = new Logger("Logs requested");

        Optional<Role> userRole = Objects
                .requireNonNull(event
                        .getMember())
                .getRoles()
                .stream()
                .filter(role -> role.getName().equals("Developers"))
                .findFirst();
        if (userRole.isEmpty()) return;

        event.getAuthor().openPrivateChannel()
                .flatMap(channel -> channel.sendFile(
                        new File("/Users/Emman/" +
                                "IdeaProjects/Ignatiamon!/src/main/java/data/files/ActionLogs.log")))
                .queue();
        event.getAuthor().openPrivateChannel()
                .flatMap(channel -> channel.sendFile(
                        new File("/Users/Emman/" +
                                "IdeaProjects/Ignatiamon!/src/main/java/data/files/ErrorLogs.log")))
                .queue();
        event.getChannel().sendMessage("I have sent the Logs in a DM to " + event.getAuthor().getAsMention())
                .queue();

        try {
            logger.logAction();
            logger.closeLogger();
        } catch (IOException e){
            Logger logger1 =
                    new Logger("Error in finding Profile due to IO and Classes \n" +
                            Logger.exceptionAsString(e));

            event.getChannel().sendMessage(
                    "Could not find profile due to IO and Classes "
            ).queue();
            try{
                logger1.logError();
                logger1.closeLogger();
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
            logger.closeLogger();
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
                put(Prefix.PrefixString.AWARD_OP, Command.AWARD_OP);
//                put(Prefix.PrefixString.RETRACT_OP, Command.RETRACT_OP);
                put(Prefix.PrefixString.SEE_PROFILE, Command.SEE_PROFILE);
                put(Prefix.PrefixString.UPDATE_PROFILE, Command.UPDATE_PROFILE);
                put(Prefix.PrefixString.CREATE_PROFILE, Command.CREATE_PROFILE);
                put(Prefix.PrefixString.ADD_IGNATIAMON, Command.ADD_IGNATIAMON);
//                put(Prefix.PrefixString.REMOVE_IGNATIAMON, Command.REMOVE_IGNATIAMON);
                put(Prefix.PrefixString.GET_LOGS, Command.GET_LOGS);
                put(Prefix.PrefixString.ERASE_PROFILE, Command.ERASE_PROFILE);
                put(Prefix.PrefixString.SEE_RANK, Command.SEE_RANK);
                put(Prefix.PrefixString.LEADERBOARD, Command.LEADERBOARD);
            }
        };

        String prefix = Prefix.cutPrefix(message.getContentRaw());
        System.out.println(prefix);
        if(Prefix.PrefixString.getPrefix(prefix).isPresent())
            return commandHashMap.get(Prefix.PrefixString.getPrefix(prefix).get());
        return Command.ERROR;
    }
}

