package game.heist.availableheists.casinorush;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage6 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You exit the vault!");
        embedBuilder.setDescription("There are two Guards in front of you!");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to take them out with the Guns", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to try and take them out with knives", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to try and take them out by hand", false);

        return embedBuilder.build();
    }

    @Override
    public int processChoice(char choice) {
        switch(choice) {
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

        embedBuilder.setTitle("You failed the heist!");
        embedBuilder.setDescription("**Marcus** Fools! I gave you guns for a reason!");
        embedBuilder.setFooter("You will lose 10000 coins for this failure");

        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("You took out the Guards!");
        embedBuilder.setDescription("**Marcus** Perfect! I set up a van outside the door on your left!");

        return embedBuilder.build();
    }
}
