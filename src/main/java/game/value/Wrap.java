package game.value;

import data.Profile;
import game.characters.Sorino;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

interface WrapAction {
    void action(Profile profile, GuildMessageReceivedEvent event);
}
public enum Wrap implements WrapAction{
    BASIC(){
        @Override
        public void action(Profile profile, GuildMessageReceivedEvent event) {
            if(profile.spend(1500)){
                event.getChannel().sendMessage("You have insufficient funds!").queue();
                return;
            }

            event.getChannel().sendMessage("Opening Basic Wrap...").queue();
            Sorino sorino;

            while (true){
                Sorino potSorino = Sorino.AllSorino.getRandom();

                if(potSorino.getName().contains("Uncommon") ||
                        potSorino.getName().contains("Common")) {
                    profile.addSorino(potSorino);
                    sorino = potSorino;
                    break;
                }
            }


            EmbedBuilder message = new EmbedBuilder();
            message.setColor(0x000dff);
            message.setTitle(event.getAuthor().getName() + " caught...");
            message.addField(sorino.getName(),
                    "HEALTH: " + sorino.getHealth(profile.getLevel()) + "\n" +
                            "ENERGY: " + sorino.getEnergy(profile.getLevel()),
                    true);
            event.getChannel().sendMessage(message.build()).queue();
        }
    },
    STANDARD(){
        @Override
        public void action(Profile profile, GuildMessageReceivedEvent event) {
            if(profile.spend(10000)){
                event.getChannel().sendMessage("You have insufficient funds!").queue();
                return;
            }
            event.getChannel().sendMessage("Opening Standard Wrap...").queue();
            ArrayList<Sorino> sorino = new ArrayList<>();

            while (sorino.size() < 2){
                Sorino potSorino = Sorino.AllSorino.getRandom();

                if(potSorino.getName().contains("Uncommon") ||
                    potSorino.getName().contains("Common") && !sorino.contains(potSorino))
                    sorino.add(potSorino);
            }
            for (Sorino sor: sorino)
                profile.addSorino(sor);
            EmbedBuilder message = new EmbedBuilder();
            message.setColor(0x000dff);
            message.setTitle(event.getAuthor().getName() + " caught...");

            for (Sorino sorino1 : sorino)
                message.addField(sorino1.getName(),
                          "HEALTH: " + sorino1.getHealth(profile.getLevel()) + "\n" +
                                "ENERGY: " + sorino1.getEnergy(profile.getLevel()),
                        true);

            event.getChannel().sendMessage(message.build()).queue();
        }
    },
    PREMIUM(){
        @Override
        public void action(Profile profile, GuildMessageReceivedEvent event) {
            if(profile.spend(30000)){
                event.getChannel().sendMessage("You have insufficient funds!").queue();
                return;
            }
            event.getChannel().sendMessage("Opening Premium Wrap...").queue();
            ArrayList<Sorino> sorino = new ArrayList<>();

            while (sorino.size() < 3){
                Sorino potSorino = Sorino.AllSorino.getRandom();

                if(potSorino.getName().contains("Uncommon") ||
                        potSorino.getName().contains("Rare")
                    && !sorino.contains(potSorino))

                    sorino.add(potSorino);
            }
            for (Sorino sor: sorino)
                profile.addSorino(sor);
            EmbedBuilder message = new EmbedBuilder();
            message.setColor(0x000dff);
            message.setTitle(event.getAuthor().getName() + " caught...");

            for (Sorino sorino1 : sorino)
                message.addField(sorino1.getName(),
                        "HEALTH: " + sorino1.getHealth(profile.getLevel()) + "\n" +
                                "ENERGY: " + sorino1.getEnergy(profile.getLevel()),
                        true);

            event.getChannel().sendMessage(message.build()).queue();
        }
    },
    CHAMPIONS(){
        @Override
        public void action(Profile profile, GuildMessageReceivedEvent event) {
            if(profile.spend(75000)){
                event.getChannel().sendMessage("You have insufficient funds!").queue();
                return;
            }
            event.getChannel().sendMessage("Opening Champions Wrap...").queue();
            ArrayList<Sorino> sorino = new ArrayList<>();

            while (sorino.size() < 3){
                Sorino potSorino = Sorino.AllSorino.getRandom();

                if(potSorino.getName().contains("Hidden") ||
                        potSorino.getName().contains("Lost") && !sorino.contains(potSorino))
                    sorino.add(potSorino);
            }
            for (Sorino sor: sorino)
                profile.addSorino(sor);
            EmbedBuilder message = new EmbedBuilder();
            message.setColor(0x000dff);
            message.setTitle(event.getAuthor().getName() + " caught...");

            for (Sorino sorino1 : sorino)
                message.addField(sorino1.getName(),
                        "HEALTH: " + sorino1.getHealth(profile.getLevel()) + "\n" +
                                "ENERGY: " + sorino1.getEnergy(profile.getLevel()),
                        true);

            event.getChannel().sendMessage(message.build()).queue();
        }
    }
}
