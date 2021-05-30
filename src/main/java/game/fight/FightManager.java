package game.fight;


import data.Profile;
import data.ProfileNotFoundException;

import game.GameSaver;
import game.characters.Sorino;

import main.Paginator;
import main.userinterface.Prefix;
import main.userinterface.UserAction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
        message.setFooter("To choose a move enter: " +
                 Prefix.guildPrefix(event.getGuild().getId()) +
                "F<MOVE>", user.getAvatarUrl());
        message.addField("Moves: ", sorino.getMoves().toString(), false);
        event.getChannel().sendMessage(message.build())
                .queue();  
    };
    private static final ChooseSorino chooseSorino = (user, event) ->{
        EmbedBuilder message = new EmbedBuilder();
        message.setColor(0x000dff);

        message.setTitle("Specify your Sorino " + user.getName());
        message.setFooter(user.getName() + " is choosing their Sorino");
        message.setDescription("Choose one of your Sorino");

        Paginator paginator = new Paginator(user.getName() + " is choosing their Sorino", 0x000dff);
        try {
            for (Sorino sorino : Profile.getProfile(user).getSorinoAsList()) {
                if(message.getFields().size() % 6 == 0 && message.getFields().size() != 0){
                    paginator.addPage(message);
                    message = new EmbedBuilder();
                    message.setTitle("Specify your Sorino " + user.getName());
                    message.setDescription("Choose one of your Sorino");
                }
                message.addField(sorino.getName(),
                        "HEALTH: " + sorino.getHealth(Profile.getProfile(user).getLevel()) +
                                "\nENERGY: " + sorino.getEnergy(Profile.getProfile(user).getLevel()) +
                                "\nTo choose this Sorino, enter `" + Prefix.guildPrefix(event.getGuild().getId())
                                + "F" + sorino.getName().substring(0, sorino.getName().indexOf(":")),
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
        }catch (ProfileNotFoundException e) {
            event.getChannel().sendMessage("You do not have a profile! Enter: `" +
                    Prefix.guildPrefix(event.getGuild().getId()) + "C`").queue();
        }
    };

    public static void fightPhase1(GuildMessageReceivedEvent event, Fight fight, String idSum){
        try {
            fight.usersID.add(event.getAuthor().getId());

            fight.usersID.add(event.getMessage().getMentionedUsers().get(0).getId());
            event.getChannel().sendMessage("To reject this battle, enter `" +
                    Prefix.guildPrefix(event.getGuild().getId()) + "FEND`").queue();
            chooseSorino.action(event.getAuthor(), event);

            fight.saveFight(idSum);
        } catch(IndexOutOfBoundsException e){
            EmbedBuilder message = new EmbedBuilder();
            message.setColor(0x000dff);

            message.setTitle("You did not tell me who you wish to fight!");
            event.getChannel().sendMessage(message.build()).queue();
        }
    }

    public static void fightPhase2(GuildMessageReceivedEvent event, Sorino sorino, Fight fight, String idSum){
        switch (fight.phase2Calls) {
            case 0:
                fight.fighters.add(sorino);

                event.getChannel().sendMessage("To reject this battle, enter `" +
                        Prefix.guildPrefix(event.getGuild().getId()) + "FEND`").queue();
                event.getJDA().retrieveUserById(fight.usersID.get(1)).queue(user -> chooseSorino.action(user, event));

                fight.phase2Calls++;
                fight.opponents.add(new Opponent(sorino, event));
                fight.saveFight(idSum);
                break;
            case 1:
                fight.fighters.add(sorino);
                fight.phase2Calls = 0;
                fight.opponents.add(new Opponent(sorino, event));
                fight.saveFight(idSum);
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
        EmbedBuilder infoEmbed = new EmbedBuilder();
        infoEmbed.setColor(0x000dff);

        UserAction info = event1 -> {
            event.getJDA().retrieveUserById(fight.usersID.get(0)).queue(user ->
                    infoEmbed.addField(user.getName() + "'s Health \t\t\t ",
                    "HEALTH: " + fight.opponents.get(0).getHealth() + "\n" +
                            "ENERGY: " + fight.opponents.get(0).getEnergy() + "\n" +
                            "DEFENCE: " + fight.opponents.get(0).getDecrease() + " drop-off",
                    true));

            event.getJDA().retrieveUserById(fight.usersID.get(1)).queue(user1 ->
                    infoEmbed.addField(user1.getName() + "'s Health \t\t\t ",
                    "HEALTH: " + fight.opponents.get(1).getHealth() + "\n" +
                            "ENERGY: " + fight.opponents.get(1).getEnergy() + "\n" +
                            "DEFENCE: " + fight.opponents.get(1).getDecrease() + " drop-off",
                    true));
        };

        if(fight.currFighter == 0){
            if(nextMove.isDefensive()) {
                fight.opponents.get(fight.currFighter).defenseUp(nextMove, event);
                event.getJDA().retrieveUserById(fight.usersID.get(fight.currFighter)).queue(user -> {
                    infoEmbed.setThumbnail(nextMove.getUrl());
                    infoEmbed.setAuthor(user.getName() + " gained " + nextMove.getEffect() + " defence");
                });
            } else {
                fight.opponents.get(fight.currFighter + 1).takeDamage(
                        nextMove, event);
                event.getJDA().retrieveUserById(fight.usersID.get(fight.currFighter + 1)).queue(user -> {
                    infoEmbed.setThumbnail(nextMove.getUrl());
                    infoEmbed.setAuthor(user.getName() + " was hit with " + nextMove.getEffect() + " damage!");
                });
            }
            fight.opponents.get(fight.currFighter).dropEnergy(nextMove);
            info.action(event);
            fight.currFighter = 1;
        } else if(fight.currFighter == 1){
            if(nextMove.isDefensive()) {
                fight.opponents.get(fight.currFighter).defenseUp(nextMove, event);
                event.getJDA().retrieveUserById(fight.usersID.get(fight.currFighter)).queue(user -> {
                    infoEmbed.setThumbnail(nextMove.getUrl());
                    infoEmbed.setAuthor(user.getName() + " gained " + nextMove.getEffect() + " defence");
                });

            } else {
                fight.opponents.get(fight.currFighter - 1).takeDamage(
                        nextMove, event);
                event.getJDA().retrieveUserById(fight.usersID.get(fight.currFighter - 1)).queue(user -> {
                    infoEmbed.setThumbnail(nextMove.getUrl());
                    infoEmbed.setAuthor(user.getName() + " was hit with " + nextMove.getEffect() + " damage!");
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
        event.getJDA().retrieveUserById(fight.usersID.get(fight.currFighter)).queue(user ->{
                infoEmbed.addField("Enter your move " + user.getName(),
                        "Moves: " +  fight.fighters.get(fight.currFighter).getMoves().toString(),
                        false);
                infoEmbed.setFooter("To choose a move enter: " +
                        Prefix.guildPrefix(event.getGuild().getId()) +
                        "F<MOVE>", user.getAvatarUrl());
                event.getChannel().sendMessage(infoEmbed.build())
                        .queue();
        });
        return Optional.empty();
    }
    public static double fightPhase4(GuildMessageReceivedEvent event,
                                    Fight.GameInfo gameInfo){
        AtomicReference<Double> returnMultiplier = new AtomicReference<>((double) 0);
        event.getJDA().retrieveUserById(gameInfo.getWinner()).queue(user ->
                        event.getJDA().retrieveUserById(gameInfo.getLoser()).queue(user1 -> {
                            try {
                                Profile winnerProfile = Profile.getProfile(user);
                                Profile loserProfile = Profile.getProfile(user1);
                                double coinMultiplier = Math.sqrt((double) (winnerProfile.getLevel() +
                                        loserProfile.getLevel()) / 2);

                                winnerProfile.setCoins((int) Math.floor((600) * coinMultiplier));
                                winnerProfile.incrementWin();

                                winnerProfile.incrementXP(600, event);
                                loserProfile.incrementLoss();
                                loserProfile.setCoins((int) Math.floor((-150) * coinMultiplier));

                                winnerProfile.recreate();
                                loserProfile.recreate();

                                returnMultiplier.set(coinMultiplier);
                            } catch (Exception e) {
                                event.getChannel().sendMessage("There was an error in finding a users profile!").queue();
                            }

                        }));

        return returnMultiplier.get();
    }
}
