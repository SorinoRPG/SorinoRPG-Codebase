package game.fight;


import data.Profile;
import data.ProfileNotFoundException;

import data.logging.Logger;

import game.characters.Sorino;

import main.userinterface.UserAction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class FightManager {
    interface ChooseSorino {
        void action(User user, GuildMessageReceivedEvent event);
    }
    interface ChooseMove {
        void action(User user, GuildMessageReceivedEvent event, Sorino sorino);
    }

    private static final ChooseMove chooseMove = (user, event, sorino) -> {
        EmbedBuilder message = new EmbedBuilder();
        message.setColor(0x000dff);

        message.setTitle("Enter your move " + user.getName());
        message.setFooter("Needs to enter their move", user.getAvatarUrl());
        message.addField("Moves: ", sorino.getMoves().toString(), false);
        event.getChannel().sendMessage(message.build())
                .queue();  
    };
    private static final ChooseSorino chooseSorino = (user, event) ->{
        EmbedBuilder message = new EmbedBuilder();
        message.setColor(0x000dff);

        message.setTitle("Specify your Sorino " + user.getName());
        message.setFooter("is choosing their Sorino", user.getAvatarUrl());
        message.setDescription("Choose one of your Sorino");

        try {
            for (Sorino sorino : Profile.getProfile(user, event).getSorinoAsList())
                message.addField(sorino.getName(),
                        "HEALTH: " + sorino.getHealth(Profile.getProfile(user, event).getLevel()) +
                                "\nENERGY: " + sorino.getEnergy(Profile.getProfile(user, event).getLevel()),
                        true);

            event.getChannel().sendMessage(message.build()).queue();
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
    };

    public static void fightPhase1(GuildMessageReceivedEvent event, Fight fight, String idSum){
        try {
            fight.usersID.add(event.getAuthor().getId());

            fight.usersID.add(event.getMessage().getMentionedUsers().get(0).getId());
            chooseSorino.action(event.getAuthor(), event);

            fight.saveFight(event.getGuild().getId(), idSum);
        } catch(IndexOutOfBoundsException e){
            EmbedBuilder message = new EmbedBuilder();
            message.setColor(0x000dff);

            message.setTitle("You did not tell me who you wish to fight!");
            event.getChannel().sendMessage(message.build()).queue();
        }catch (IOException e) {
            Logger logger = new Logger("Error in creating fight + \n" +
                    Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "There was a server error in creating your fight, please try again."
            ).queue(message ->
                    message.delete().queueAfter(25, TimeUnit.SECONDS)
            );
            System.out.println(Logger.exceptionAsString(e));
            try {
                logger.logError();
            } catch (IOException exc){
                exc.printStackTrace();
            }
        }
    }

    public static void fightPhase2(GuildMessageReceivedEvent event, Sorino sorino, Fight fight, String idSum){
        switch (fight.phase2Calls) {
            case 0:
                fight.fighters.add(sorino);

                event.getJDA().retrieveUserById(fight.usersID.get(1)).queue(user -> chooseSorino.action(user, event));

                fight.phase2Calls++;
                fight.opponents.add(new Opponent(sorino, event));
                try {
                    fight.saveFight(event.getGuild().getId(), idSum);
                } catch (IOException e) {
                    Logger logger = new Logger("Error in saving fight" +
                            Logger.exceptionAsString(e));
                    event.getChannel().sendMessage("There was a server error in saving the " +
                            "fight! Please end it!").queue(message ->
                            message.delete().queueAfter(25, TimeUnit.SECONDS)
                    );
                    try {
                        logger.logError();
                    } catch (IOException ex){
                        ex.printStackTrace();
                    }
                }
                break;
            case 1:
                fight.fighters.add(sorino);
                fight.phase2Calls = 0;
                fight.opponents.add(new Opponent(sorino, event));
                try {
                    fight.saveFight(event.getGuild().getId(), idSum);
                } catch (IOException e) {
                    Logger logger = new Logger("Error in saving fight" +
                            Logger.exceptionAsString(e));
                    event.getChannel().sendMessage("There was a server error in saving the " +
                            "fight! Please end it!").queue(message ->
                            message.delete().queueAfter(25, TimeUnit.SECONDS)
                    );
                    try {
                        logger.logError();
                    } catch (IOException ex){
                        ex.printStackTrace();
                    }
                }
                event.getJDA().retrieveUserById(fight.usersID.get(0)).queue(user ->
                        chooseMove.action(user, event, fight.fighters.get(0)));

                break;
        }
    }

    public static Optional<Fight.GameInfo> fightPhase3(Move nextMove, GuildMessageReceivedEvent event,
                                                       Fight fight) {
        if(fight.opponents.get(0).hasConceded())
            return Optional.of(new Fight.GameInfo(fight.usersID.get(1), fight.usersID.get(0)));
        else if(fight.opponents.get(1).hasConceded())
            return Optional.of(new Fight.GameInfo(fight.usersID.get(0), fight.usersID.get(1)));
        UserAction info = event1 -> {
            EmbedBuilder embed  = new EmbedBuilder();
            embed.setTitle("Health of the Fighters!");
            embed.setColor(0x000dff);

            event.getJDA().retrieveUserById(fight.usersID.get(0)).queue(user -> {
                embed.addField(user.getName() + "'s Health \t\t\t ",
                        "HEALTH: " + fight.opponents.get(0).getHealth() + "\n" +
                                "ENERGY: " + fight.opponents.get(0).getEnergy() + "\n" +
                                "DEFENCE: " + fight.opponents.get(0).getDecrease() + " drop-off",
                        true);
                event.getJDA().retrieveUserById(fight.usersID.get(1)).queue(user1 -> {
                    embed.addField(user1.getName() + "'s Health \t\t\t ",
                            "HEALTH: " + fight.opponents.get(1).getHealth() + "\n" +
                                    "ENERGY: " + fight.opponents.get(1).getEnergy() + "\n" +
                                    "DEFENCE: " + fight.opponents.get(1).getDecrease() + " drop-off",
                            true);

                    event1.getChannel().sendMessage(embed.build()).queue();
                });
            });

        };

        if(fight.currFighter == 0){
            Optional<String> noUsage;
            if(nextMove.isDefensive()) {
                noUsage = fight.opponents.get(fight.currFighter).defenseUp(nextMove, event);
                noUsage.ifPresentOrElse(s ->
                                event.getChannel().sendMessage(s).queue(),
                        () -> {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setColor(0x000dff);
                            event.getJDA().retrieveUserById(fight.usersID.get(fight.currFighter)).queue(user -> {
                                embedBuilder.setImage(nextMove.getUrl());
                                embedBuilder.setFooter(" gained " + nextMove.getEffect() + " defence",
                                        user.getAvatarUrl());
                                event.getChannel().sendMessage(embedBuilder.build()).queue();
                            });
                        });
            } else {
                noUsage = fight.opponents.get(fight.currFighter + 1).takeDamage(
                        nextMove, event);
                noUsage.ifPresentOrElse(s ->
                                event.getChannel().sendMessage(s).queue(),
                        () -> {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setColor(0x000dff);
                            event.getJDA().retrieveUserById(fight.usersID.get(fight.currFighter + 1)).queue(user -> {
                                embedBuilder.setImage(nextMove.getUrl());
                                embedBuilder.setFooter("was hit with " + nextMove.getEffect() + " damage!"
                                        , user.getAvatarUrl());
                                event.getChannel().sendMessage(embedBuilder.build()).queue();
                            });
                        });
            }
            fight.opponents.get(fight.currFighter).dropEnergy(nextMove);
            info.action(event);
            fight.currFighter = 1;
        } else if(fight.currFighter == 1){
            Optional<String> noUsage;
            if(nextMove.isDefensive()) {
                noUsage = fight.opponents.get(fight.currFighter).defenseUp(nextMove, event);
                noUsage.ifPresentOrElse(s ->
                                event.getChannel().sendMessage(s).queue(),
                        () -> {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setColor(0x000dff);
                            event.getJDA().retrieveUserById(fight.usersID.get(fight.currFighter)).queue(user -> {
                                embedBuilder.setImage(nextMove.getUrl());
                                embedBuilder.setFooter(" gained " + nextMove.getEffect() + " defence",
                                        user.getAvatarUrl());

                                event.getChannel().sendMessage(embedBuilder.build()).queue();
                            });
                        });
            } else {
                noUsage = fight.opponents.get(fight.currFighter - 1).takeDamage(
                        nextMove, event);
                noUsage.ifPresentOrElse(s ->
                                event.getChannel().sendMessage(s).queue(),
                        () -> {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setColor(0x000dff);
                            event.getJDA().retrieveUserById(fight.usersID.get(fight.currFighter - 1)).queue(user -> {
                                embedBuilder.setImage(nextMove.getUrl());
                                embedBuilder.setFooter("was hit with " + nextMove.getEffect() + " damage!"
                                        , user.getAvatarUrl());
                                event.getChannel().sendMessage(embedBuilder.build()).queue();
                            });
                        });
            }
            fight.opponents.get(fight.currFighter).dropEnergy(nextMove);
            info.action(event);
            fight.currFighter = 0;
        }
        if(fight.opponents.get(0).hasConceded())
            return Optional.of(new Fight.GameInfo(fight.usersID.get(1), fight.usersID.get(0)));
        else if(fight.opponents.get(1).hasConceded())
            return Optional.of(new Fight.GameInfo(fight.usersID.get(0), fight.usersID.get(1)));
        event.getJDA().retrieveUserById(fight.usersID.get(fight.currFighter)).queue(user ->
            chooseMove.action(user, event, fight.fighters.get(fight.currFighter))
        );
        return Optional.empty();
    }
    public static int[] fightPhase4(GuildMessageReceivedEvent event,
                                    Fight.GameInfo gameInfo){
        event.getJDA().retrieveUserById(gameInfo.getWinner()).queue(user ->
                        event.getJDA().retrieveUserById(gameInfo.getLoser()).queue(user1 -> {
            try {
                Profile winnerProfile = Profile.getProfile(user, event);
                Profile loserProfile = Profile.getProfile(user1, event);
                double coinMultiplier = Math.sqrt((double) (winnerProfile.getLevel() +
                        loserProfile.getLevel()) / 2);

                winnerProfile.setCoins((int) Math.floor((600) * coinMultiplier));
                winnerProfile.incrementWin();

                winnerProfile.incrementXP(600, event);
                loserProfile.incrementLoss();
                loserProfile.setCoins((int) Math.floor((-150) * coinMultiplier));

                winnerProfile.recreateProfile();
                loserProfile.recreateProfile();
            } catch (IOException | ClassNotFoundException e) {
                try{
                    Logger logger1 =
                            new Logger("Error in finding Profile due to IO and Classes \n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                            "Could not find profile due to IO and Classes "
                    ).queue();

                    logger1.logError();
                } catch (IOException excI){
                    event.getChannel().sendMessage(
                            "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                    Logger.exceptionAsString(excI)
                    ).queue();
                }
            } catch (ProfileNotFoundException e) {
                try{
                    Logger logger =
                            new Logger("Error in finding Profile \n" +
                                    Logger.exceptionAsString(e));
                    event.getChannel().sendMessage(
                            "Could not find profile!"
                    ).queue();
                    logger.logError();
                } catch (IOException excI){
                    event.getChannel().sendMessage(
                            "Error in logging, mention a dev to get it fixed! @Developers\n" +
                                    Logger.exceptionAsString(excI)
                    ).queue();
                }
            }
        })
        );


        try {
            Profile p1 = Profile.getProfile(event);
            Profile p2 = Profile.getProfile(event.getMessage().getMentionedUsers().get(0), event);

            double coinMultiplier = Math.sqrt((double) (p1.getLevel() + p2.getLevel()) /2);

            return new int[] {
                    (int) Math.floor((600) *  coinMultiplier), (int) Math.floor((-150) *  coinMultiplier)
            };
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
            return new int[0];
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
            return new int[0];
        }
    }
}
