package game.fight;


import data.Profile;
import data.ProfileNotFoundException;

import data.files.Logger;

import game.characters.Sorino;

import main.userinterface.UserAction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.util.Optional;

public class FightManager {
    interface ChooseSorino {
        void action(User user, GuildMessageReceivedEvent event);
    }
    interface ChooseMove {
        void action(User user, GuildMessageReceivedEvent event, Sorino sorino);
    }

    private static final ChooseMove chooseMove = (user, event, sorino) -> {
        EmbedBuilder message = new EmbedBuilder();

        message.setTitle("Enter your move " + user.getName());
        message.setFooter("Needs to enter their move", user.getAvatarUrl());
        message.addField("Moves: ", sorino.getMoves().toString(), false);
        event.getChannel().sendMessage(message.build())
                .queue();  
    };
    private static final ChooseSorino chooseSorino = (user, event) ->{
        EmbedBuilder message = new EmbedBuilder();

        message.setTitle("Specify your Sorino " + user.getName());
        message.setFooter("is choosing their Sorino", user.getAvatarUrl());
        message.setDescription("Choose one of your Sorino");

        try {
            for (Sorino sorino : Profile.getProfile(user, event).getSorinoAsList())
                message.addField(sorino.getName(),
                        "HEALTH: " + sorino.getHealth(Profile.getProfile(user, event).getLevel()) +
                                "\nENERGY: " + sorino.getHealth(Profile.getProfile(user, event).getLevel()),
                        true);

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
    };

    public static void fightPhase1(GuildMessageReceivedEvent event, Fight fight, String idSum){
        try {
            fight.users.add(event.getAuthor());
            fight.users.add(event.getMessage().getMentionedUsers().get(0));
            chooseSorino.action(event.getAuthor(), event);

            fight.saveFight(event.getGuild().getId(), idSum);
        } catch(IndexOutOfBoundsException e){
            EmbedBuilder message = new EmbedBuilder();

            message.setTitle("You did not tell me who you wish to fight!");
            event.getChannel().sendMessage(message.build()).queue();
        }catch (IOException e) {
            Logger logger = new Logger("Error in creating fight + \n" +
                    Logger.exceptionAsString(e));
            event.getChannel().sendMessage(
                    "There was a server error in creating your fight, please try again."
            ).queue();
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
                chooseSorino.action(fight.users.get(1), event);
                fight.phase2Calls++;
                fight.opponents.add(new Opponent(sorino, event));
                try {
                    fight.saveFight(event.getGuild().getId(), idSum);
                } catch (IOException e) {
                    Logger logger = new Logger("Error in saving fight" +
                            Logger.exceptionAsString(e));
                    event.getChannel().sendMessage("There was a server error in saving the " +
                            "fight! Please end it!").queue();
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
                            "fight! Please end it!").queue();
                    try {
                        logger.logError();
                    } catch (IOException ex){
                        ex.printStackTrace();
                    }
                }

                chooseMove.action(fight.users.get(0), event, fight.fighters.get(0));
                break;
        }
    }

    public static Optional<Fight.GameInfo> fightPhase3(Move nextMove, GuildMessageReceivedEvent event,
                                                       Fight fight) {
        UserAction info = event1 -> {
            EmbedBuilder embed  = new EmbedBuilder();
            embed.setTitle("Health of the Fighters!");

            embed.addField(fight.users.get(0).getName() + "'s Health \t\t\t ",
                    "HEALTH: " + fight.opponents.get(0).getHealth() + "\n" +
                            "ENERGY: " + fight.opponents.get(0).getEnergy() + "\n" +
                            "DEFENCE: " + fight.opponents.get(0).getDecrease() + " drop-off",
                    true);
            embed.addField(fight.users.get(1).getName() + "'s Health",
                    "HEALTH: " + fight.opponents.get(1).getHealth() + "\n" +
                            "ENERGY: " + fight.opponents.get(1).getEnergy() + "\n" +
                            "DEFENCE: " + fight.opponents.get(1).getDecrease() + " drop-off",
                    true);

            event1.getChannel().sendMessage(embed.build()).queue();
        };
        return Optional.empty();
    }
    public static void fightPhase4(TextChannel channel, Fight.GameInfo gameInfo){

    }
}
