package game.value;

import data.Profile;
import game.characters.Sorino;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

interface WrapAction {
    void action(Profile profile, GuildMessageReceivedEvent event);
}
public enum Wrap implements WrapAction{
    STANDARD(){
        @Override
        public void action(Profile profile, GuildMessageReceivedEvent event) {
            if(profile.spend(5000)){
                event.getChannel().sendMessage("You have insufficient funds!").queue(
                        message -> message.delete().queueAfter(5, TimeUnit.SECONDS)
                );
                return;
            }
            event.getChannel().sendMessage("Opening Standard Wrap...").queue(
                    message -> message.delete().queueAfter(5, TimeUnit.SECONDS)
            );
            ArrayList<Sorino> sorino = new ArrayList<>();

            while (sorino.size() < 2){
                Sorino potSorino = Sorino.AllSorino.getRandom();

                if(potSorino.getName().contains("Uncommon") ||
                    potSorino.getName().contains("Common"))
                    sorino.add(potSorino);
            }
            for (Sorino sor: sorino)
                profile.addSorino(sor);
            EmbedBuilder message = new EmbedBuilder();
            message.setTitle(event.getAuthor().getName() + " caught...");

            for (Sorino sorino1 : sorino)
                message.addField(sorino1.getName(),
                          "HEALTH: " + sorino1.getHealth(profile.getLevel()) + "\n" +
                                "ENERGY: " + sorino1.getEnergy(profile.getLevel()),
                        true);

            event.getChannel().sendMessage(message.build()).queue(
                    message1 -> message1.delete().queueAfter(5, TimeUnit.SECONDS)
            );
        }
    },
    PREMIUM(){
        @Override
        public void action(Profile profile, GuildMessageReceivedEvent event) {
            if(profile.spend(15000)){
                event.getChannel().sendMessage("You have insufficient funds!").queue(
                        message -> message.delete().queueAfter(5, TimeUnit.SECONDS)
                );
                return;
            }
            event.getChannel().sendMessage("Opening Premium Wrap...").queue(
                    message -> message.delete().queueAfter(5, TimeUnit.SECONDS)
            );
            ArrayList<Sorino> sorino = new ArrayList<>();

            while (sorino.size() < 3){
                Sorino potSorino = Sorino.AllSorino.getRandom();

                if(potSorino.getName().contains("Uncommon") ||
                        potSorino.getName().contains("Rare"))
                    sorino.add(potSorino);
            }
            for (Sorino sor: sorino)
                profile.addSorino(sor);
            EmbedBuilder message = new EmbedBuilder();
            message.setTitle(event.getAuthor().getName() + " caught...");

            for (Sorino sorino1 : sorino)
                message.addField(sorino1.getName(),
                        "HEALTH: " + sorino1.getHealth(profile.getLevel()) + "\n" +
                                "ENERGY: " + sorino1.getEnergy(profile.getLevel()),
                        true);

            event.getChannel().sendMessage(message.build()).queue(
                    message1 -> message1.delete().queueAfter(5, TimeUnit.SECONDS)
            );
        }
    },
    CHAMPIONS(){
        @Override
        public void action(Profile profile, GuildMessageReceivedEvent event) {
            if(profile.spend(50000)){
                event.getChannel().sendMessage("You have insufficient funds!").queue(
                        message -> message.delete().queueAfter(5, TimeUnit.SECONDS)
                );
                return;
            }
            event.getChannel().sendMessage("Opening Champions Wrap...").queue(
                    message -> message.delete().queueAfter(5, TimeUnit.SECONDS)
            );
            ArrayList<Sorino> sorino = new ArrayList<>();

            while (sorino.size() < 3){
                Sorino potSorino = Sorino.AllSorino.getRandom();

                if(potSorino.getName().contains("Hidden") ||
                        potSorino.getName().contains("Lost"))
                    sorino.add(potSorino);
            }
            for (Sorino sor: sorino)
                profile.addSorino(sor);
            EmbedBuilder message = new EmbedBuilder();
            message.setTitle(event.getAuthor().getName() + " caught...");

            for (Sorino sorino1 : sorino)
                message.addField(sorino1.getName(),
                        "HEALTH: " + sorino1.getHealth(profile.getLevel()) + "\n" +
                                "ENERGY: " + sorino1.getEnergy(profile.getLevel()),
                        true);

            event.getChannel().sendMessage(message.build()).queue(
                    message1 -> message1.delete().queueAfter(5, TimeUnit.SECONDS)
            );
        }
    }
}
