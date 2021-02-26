package game.heist.availableheists.artilleryessential;

import game.heist.Stage;
import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage7 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x000dff);

            embedBuilder.setAuthor(user.getName() + "'s turn");
            embedBuilder.setTitle("You and your team pack up the guns and leave.");
            embedBuilder.setDescription("All the guards are waiting for you outside!");

            embedBuilder.addField("`" + p + "HA`",
                    "Enter this to use the Bazooka", false);
            embedBuilder.addField("`" + p + "HB`",
                    "Enter this to take cover shoot the guards with the guns", false);
            embedBuilder.addField("`" + p + "HC`",
                    "Enter this to try and escape on foot", false);


        return embedBuilder.build();
    }

    @Override
    public int processChoice(char choice) {
        switch (choice){
            case 'A': return 0xA;
            case 'B': return 0xB;
            case 'C': return 0xB;
            default: return 0;
        }
    }

    @Override
    public MessageEmbed failure(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x000dff);

            embedBuilder.setAuthor(user.getName() + "'s turn");
            embedBuilder.setTitle("You failed the heist!");
            embedBuilder.setDescription("**Antonio** Fools! We were so close! You could have easily used " +
                    "the Bazooka!");
            embedBuilder.setFooter("You will be paying 300 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x000dff);

            embedBuilder.setAuthor(user.getName() + "'s turn");
            embedBuilder.setTitle("You took down the guards!");
            embedBuilder.setDescription("**Antonio** Amazing! Enter that car and we are home free!");


        return embedBuilder.build();
    }

}
