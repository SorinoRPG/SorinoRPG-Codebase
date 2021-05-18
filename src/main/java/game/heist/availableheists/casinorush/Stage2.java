package game.heist.availableheists.casinorush;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage2 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You and your team set the explosives");
        embedBuilder.setDescription("There is a password to set the explosive");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to input 'BOOMBOOM22'", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to input 'BOMB41'", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to input 'TNT23'", false);

        embedBuilder.setFooter("password: TNT23");

        return embedBuilder.build();
    }

    @Override
    public int processChoice(char choice) {
        switch(choice) {
            case 'A': return 0xB;
            case 'B': return 0xB;
            case 'C': return 0xA;
            default: return 0;
        }
    }

    @Override
    public MessageEmbed failure(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("You failed the heist!");
        embedBuilder.setDescription("**Marcus** Fools! I told you the password to set the bombs!");
        embedBuilder.setFooter("You will lose 10000 coins for this failure");

        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("BOOOOOM!!");
        embedBuilder.setDescription("**Marcus** We are in! Now we just have to push in through into the vault!");

        return embedBuilder.build();
    }
}