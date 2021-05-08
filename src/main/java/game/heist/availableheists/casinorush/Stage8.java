package game.heist.availableheists.casinorush;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage8 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You hear police sirens!");
        embedBuilder.setDescription("The road is split into 3");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to take the tunnel to the ground", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to take the Freeway", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to take the Highway", false);

        return embedBuilder.build();
    }

    @Override
    public int processChoice(char choice) {
        switch(choice) {
            case 'A': return 0xC;
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
        embedBuilder.setDescription("**Marcus** The police arrested you!");
        embedBuilder.setFooter("You will lose 10000 coins for this failure");

        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("You beat the heist!");
        embedBuilder.setDescription("**Marcus** We are going to be rich men!");
        embedBuilder.setFooter("You will receive 200000 coins for this success!");

        return embedBuilder.build();
    }
}
