package game.heist.availableheists.casinorush;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage5 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You enter into vault, and see 3 Sapphires!");
        embedBuilder.setDescription("We can only bring one!");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to get the blue Sapphire", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to take the red Sapphire", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to take the green Sapphire", false);

        return embedBuilder.build();
    }

    @Override
    public int processChoice(char choice) {
        switch(choice) {
            case 'A': return 0xB;
            case 'B': return 0xA;
            case 'C': return 0xB;
            default: return 0;
        }
    }

    @Override
    public MessageEmbed failure(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("You failed the heist!");
        embedBuilder.setDescription("**Marcus** Fools! I told you to get the red Sapphire!");
        embedBuilder.setFooter("You will lose 10000 coins for this failure");

        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("You collected the red Sapphire!");
        embedBuilder.setDescription("**Marcus** Fantastic! This Sapphire will make us millionaires! Well, " +
                "me a millionaire.");

        return embedBuilder.build();
    }
}
