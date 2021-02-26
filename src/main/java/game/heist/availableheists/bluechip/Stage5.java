package game.heist.availableheists.bluechip;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage5 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("You and your team enter the main office!");
        embedBuilder.setDescription("There is a password at the door");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to input 'GME69'", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to input 'RCH4C'", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to input 'MASN64'", false);
        embedBuilder.setFooter("password: RCH4C");

        return embedBuilder.build();
    }

    @Override
    public int processChoice(char choice) {
        switch(choice){
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

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You failed the heist!");
        embedBuilder.setDescription("**Marcus** Idiots! You should have seen the password!");
        embedBuilder.setFooter("You will be paying 5000 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You entered the correct password!");
        embedBuilder.setDescription("**Marcus** Haha, get to this guys computer!");

        return embedBuilder.build();
    }
}
