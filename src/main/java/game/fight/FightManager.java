package game.fight;


import data.Profile;
import data.ProfileNotFoundException;

import data.files.Logger;

import game.SorinoNotFoundException;
import game.characters.Sorino;

import main.userinterface.Prefix;
import main.userinterface.UserAction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class FightManager {
    public static ArrayList<Sorino> currentFighters = new ArrayList<>();
    public static ArrayList<Sorino.DeadSorino> deadSorino = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Opponent> opponents = new ArrayList<>();

    public static class GameInfo {
        private final User winner;
        private final User loser;

        public GameInfo(User winner, User loser){
            this.winner = winner;
            this.loser = loser;
        }

        public User getLoser() {
            return loser;
        }

        public User getWinner() {
            return winner;
        }
    }

    public static ArrayList<User> fightPhase1(GuildMessageReceivedEvent event) {
        ArrayList<User> users =
                new ArrayList<>();

        try {
            users.add(event.getAuthor());
            users.add(event.getMessage().getMentionedUsers().get(0));

            EmbedBuilder message = new EmbedBuilder();

            message.setTitle("Specify your Sorino " + event.getAuthor().getName());
            message.setFooter("is choosing their Sorino", event.getAuthor().getAvatarUrl());
            message.setDescription("Choose one of your Sorino!");
            message.addField("Sorino: ", Profile.getProfile(event).getSorino(), false);

            event.getChannel().sendMessage(message.build()).queue();
        } catch(IndexOutOfBoundsException e){
            EmbedBuilder message = new EmbedBuilder();

            message.setTitle("You did not tell me who you wish to fight!");
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
            } catch (IOException excI){
                event.getChannel().sendMessage(
                        "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                Logger.exceptionAsString(excI)
                ).queue();
            }
        }
        return users;
    }

    private static int calls = 0;
    public static void fightPhase2(GuildMessageReceivedEvent event,
                                   Profile profile) {
        if(FightManager.calls == 0){
            try{
                EmbedBuilder message = new EmbedBuilder();

                Sorino sorino = profile.getSpecificSorino(
                         Prefix.removeFightPrefix(event.getMessage().getContentRaw()));
                calls++;
                currentFighters.add(sorino);
                message.setTitle("Specify your Sorino " + users.get(1).getName());
                message.setFooter("is choosing their Sorino", users.get(1).getAvatarUrl());
                message.setDescription("Choose one of your Sorino!");
                message.addField("Sorino: ", Profile.getProfile(users.get(1), event).getSorino(), false);
                event.getChannel().sendMessage(message.build()).queue();
            } catch (SorinoNotFoundException exc){
                Logger logger =
                        new Logger("Error in finding Sorino \n" +
                                Logger.exceptionAsString(exc));
                event.getChannel().sendMessage(
                        "Could not find your Sorino! You can try again!"
                ).queue();
                try{
                    logger.logError();
                } catch (IOException excI){
                    event.getChannel().sendMessage(
                            "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                    Logger.exceptionAsString(excI)
                    ).queue();
                }
                calls = 0;
                currentFighters.remove(0);
            } catch (IOException | ClassNotFoundException e) {
                Logger logger1 =
                        new Logger("Error in finding Profile due to IO and Classes \n" +
                                Logger.exceptionAsString(e));
                event.getChannel().sendMessage(
                        "Could not find profile due to IO and Classes "
                ).queue();
                try{
                    logger1.logError();
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
                } catch (IOException excI){
                    event.getChannel().sendMessage(
                            "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                    Logger.exceptionAsString(excI)
                    ).queue();
                }
            }
        } else if(FightManager.calls == 1){
            try{
                Sorino sorino = profile.getSpecificSorino(
                        Prefix.removeFightPrefix(event.getMessage().getContentRaw())
                );
                calls++;
                currentFighters.add(sorino);
            } catch (SorinoNotFoundException exc){
                Logger logger =
                        new Logger("Error in finding Sorino \n" +
                                Logger.exceptionAsString(exc));
                event.getChannel().sendMessage(
                        "Could not find your Sorino! You can try again!"
                ).queue();
                calls = 1;
                currentFighters.remove(1);
                try {
                    logger.logError();
                } catch (IOException excI){
                    event.getChannel().sendMessage(
                            "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                    Logger.exceptionAsString(excI)
                    ).queue();
                }
            }
            opponents.add(
                    new Opponent(
                            currentFighters.get(0),
                            event));
            opponents.add(
                    new Opponent(
                            currentFighters.get(1),
                            event));
            calls = 0;
            EmbedBuilder message = new EmbedBuilder();

            message.setTitle("Enter your move " + users.get(0).getName());
            message.setFooter("Needs to enter their move", users.get(0).getAvatarUrl());
            message.addField("Moves: ", currentFighters.get(0).getMoves().toString(), false);


            event.getChannel().sendMessage(message.build())
                    .queue();
        }
    }



    public static int currentFighter = 0;
    public static Optional<GameInfo> fightPhase3(Move nextMove, GuildMessageReceivedEvent event) {
        // Display the fighter information
        UserAction info = event1 -> {
              EmbedBuilder embed  = new EmbedBuilder();
              embed.setTitle("Health of the Fighters!");

              embed.addField(users.get(0).getName() + "'s Health \t\t\t ",
                      "HEALTH: " + opponents.get(0).getHealth() + "\n" +
                              "ENERGY: " + opponents.get(0).getEnergy() + "\n" +
                              "DEFENCE: " + opponents.get(0).getDecrease() + " drop-off",
                      true);
              embed.addField(users.get(1).getName() + "'s Health",
                      "HEALTH: " + opponents.get(1).getHealth() + "\n" +
                              "ENERGY: " + opponents.get(1).getEnergy() + "\n" +
                              "DEFENCE: " + opponents.get(1).getDecrease() + " drop-off",
                      true);

              event1.getChannel().sendMessage(embed.build()).queue();
        };

        if(currentFighter == 0){
            Optional<String> noUsage;
            if(nextMove.isDefensive()) {
                noUsage = opponents.get(currentFighter).defenseUp(
                        nextMove);
                noUsage.ifPresentOrElse(s ->
                                event.getChannel().sendMessage(s).queue(),
                        () -> {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setFooter(" gained " + nextMove.getEffect() + " defence",
                                    users.get(currentFighter).getAvatarUrl());
                            event.getChannel().sendMessage(embedBuilder.build()).queue();
                        });
            }
            else {
                noUsage = opponents.get(currentFighter + 1).takeDamage(
                        nextMove);
                noUsage.ifPresentOrElse(s ->
                            event.getChannel().sendMessage(s).queue(),
                        () -> {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setImage(nextMove.getUrl());
                            embedBuilder.setFooter("was hit with " + nextMove.getEffect() + " damage!"
                                    , users.get(currentFighter+1).getAvatarUrl());
                            event.getChannel().sendMessage(embedBuilder.build()).queue();
                        });
            }


            opponents.get(currentFighter).dropEnergy(nextMove);
            info.action(event);
            if(opponents.get(currentFighter).hasConceded()) {
                try {
                    if(opponents.get(currentFighter).switchOut())
                        return Optional.of(new GameInfo(users.get(currentFighter + 1), users.get(loserIndex)));

                    EmbedBuilder message = new EmbedBuilder();
                    message.setTitle("Specify your switch-out Sorino " + users.get(1).getName());
                    message.setFooter("is choosing their Sorino", users.get(1).getAvatarUrl());
                    message.setDescription("Choose one of your Sorino! -- suffix with !!");

                    ArrayList<Sorino> sorinoArrayList = Profile.getProfile(users.get(1), event).getSorinoAsList();
                    sorinoArrayList.removeAll(Sorino.DeadSorino.asSorino(deadSorino));

                    message.addField("Sorino: ", sorinoArrayList.toString(), false);
                    event.getChannel().sendMessage(message.build()).queue();
                    loserIndex = currentFighter;
                }  catch (IOException | ClassNotFoundException e) {
                    Logger logger =
                            new Logger("Error in finding Profile due to IO and Classes \n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                            "Could not find profile due to IO and Classes "
                    ).queue();
                    try{
                        logger.logError();
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
                    } catch (IOException excI){
                        event.getChannel().sendMessage(
                                "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                        Logger.exceptionAsString(excI)
                        ).queue();
                    }
                }
            }
            currentFighter = 1;
        } else if(currentFighter == 1){
            if(nextMove.isDefensive()) {
                Optional<String> noUsage = opponents.get(currentFighter).defenseUp(
                        nextMove);
                noUsage.ifPresentOrElse(s ->
                            event.getChannel().sendMessage(s).queue(),
                        () -> {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setFooter(" gained " + nextMove.getEffect() + " defence",
                                    users.get(currentFighter).getAvatarUrl());
                            event.getChannel().sendMessage(embedBuilder.build()).queue();
                        });
            }
            else {
                Optional<String> noUsage = opponents.get(currentFighter-1).takeDamage(
                        nextMove);
                noUsage.ifPresentOrElse(s ->
                            event.getChannel().sendMessage(s).queue(),
                        () -> {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setImage(nextMove.getUrl());
                            embedBuilder.setFooter("was hit with " + nextMove.getEffect() + " damage!"
                                    , users.get(currentFighter-1).getAvatarUrl());
                            event.getChannel().sendMessage(embedBuilder.build()).queue();
                        });
            }

            opponents.get(currentFighter).dropEnergy(nextMove);
            info.action(event);
            if(opponents.get(currentFighter).hasConceded()) {
                try {
                    if(opponents.get(currentFighter).switchOut())
                        return Optional.of(new GameInfo(users.get(currentFighter + 1), users.get(loserIndex)));


                    EmbedBuilder message = new EmbedBuilder();
                    message.setTitle("Specify your switch-out Sorino " + users.get(1).getName());
                    message.setFooter("is choosing their Sorino", users.get(1).getAvatarUrl());
                    message.setDescription("Choose one of your Sorino! -- suffix with !!");

                    ArrayList<Sorino> sorinoArrayList = Profile.getProfile(users.get(1), event).getSorinoAsList();
                    sorinoArrayList.removeAll(Sorino.DeadSorino.asSorino(deadSorino));

                    message.addField("Sorino: ", sorinoArrayList.toString(), false);
                    event.getChannel().sendMessage(message.build()).queue();
                    loserIndex = currentFighter;
                }  catch (IOException | ClassNotFoundException e) {
                    Logger logger =
                            new Logger("Error in finding Profile due to IO and Classes \n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                            "Could not find profile due to IO and Classes "
                    ).queue();
                    try{
                        logger.logError();
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
                    } catch (IOException excI){
                        event.getChannel().sendMessage(
                                "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                        Logger.exceptionAsString(excI)
                        ).queue();
                    }
                }
            }
            currentFighter = 0;
        }

        EmbedBuilder message = new EmbedBuilder();

        message.setTitle("Enter your move " + users.get(currentFighter).getName());
        message.setFooter("Needs to enter their move", users.get(currentFighter).getAvatarUrl());
        message.addField("Moves: ", currentFighters.get(currentFighter).getMoves().toString(), false);


        event.getChannel().sendMessage(message.build())
                .queue();

        return Optional.empty();
    }

    public static void addSwitchOut(Sorino switchOut, GuildMessageReceivedEvent event){


        if(currentFighter == 0) {
            if(deadSorino.contains(new Sorino.DeadSorino(switchOut, opponents.get(1))))
                event.getChannel().sendMessage("That Sorino has died!")
                    .queue();
            opponents.set(1,
                    new Opponent(switchOut, event));
            currentFighters.set(1,
                    switchOut);
        } else if(currentFighter == 1){
            if(deadSorino.contains(new Sorino.DeadSorino(switchOut, opponents.get(0))))
                event.getChannel().sendMessage("That Sorino has died!")
                        .queue();
            opponents.set(0,
                    new Opponent(switchOut, event));
            currentFighters.set(0,
                    switchOut);
        }
    }
    private static int loserIndex;
    public static void fightPhase4(GuildMessageReceivedEvent event) throws ProfileNotFoundException, IOException, ClassNotFoundException {
        Profile winner;
        Profile loser = Profile.getProfile(users.get(loserIndex), event);
        if(loserIndex == 0)
            winner = Profile.getProfile(users.get(loserIndex+1), event);
        else
            winner = Profile.getProfile(users.get(loserIndex-1), event);
        winner.setCoins(300);
        winner.incrementWin();
        winner.incrementXP(200, event.getChannel());
        loser.incrementLoss();
        loser.setCoins(-300);

        users = new ArrayList<>();
        currentFighters = new ArrayList<>();
        currentFighter = 0;
        opponents = new ArrayList<>();
        deadSorino = new ArrayList<>();
        loserIndex = 0;

        winner.recreateProfile();
        loser.recreateProfile();
    }

}
