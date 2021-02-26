package game.heist.availableheists.artilleryessential;

import game.heist.Stage;
import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage6 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);
        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You and your team enter the weapons vault. ");
        embedBuilder.setDescription("There are 5 guards in there and they all see you!");
        embedBuilder.addField("`" + p + "HA`",
                "Enter this to try and hit the guards with the knives", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to take cover shoot the guards with the guns", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to try and knock the guards out by hand", false);
        return embedBuilder.build();
    }

    @Override
    public int processChoice(char choice) {
        switch (choice){
            case 'A': return 0xB;
            case 'C': return 0xB;
            case 'B': return 0xA;
            default: return 0;
        }
    }

    @Override
    public MessageEmbed failure(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x000dff);

            embedBuilder.setAuthor(user.getName() + "'s turn");
            embedBuilder.setTitle("You failed the heist!");
            embedBuilder.setDescription("**Antonio** Am I hearing this right? You used something other than guns " +
                    "against highly trained Russian guards!?");
            embedBuilder.setFooter("You will be paying 300 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x000dff);

            embedBuilder.setAuthor(user.getName() + "'s turn");
            embedBuilder.setTitle("You took down the guards!");
            embedBuilder.setDescription("**Antonio** Hahaha, we are pulling this off! Pack up the guns " +
                    "and exit as quickly as possible!");


        return embedBuilder.build();
    }

}
