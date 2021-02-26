package game.heist.availableheists.codebreaker;

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
        embedBuilder.setTitle("You enter the Boss's room");
        embedBuilder.setDescription("His computer has a password!");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to enter 'LOG54'", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to enter '4T3'", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to enter 'C0D3'", false);
        embedBuilder.setFooter("password: 4T3");

        return embedBuilder.build();
    }

    @Override
    public int processChoice(char choice) {
        switch (choice){
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
        embedBuilder.setDescription("**Jonah** The password was obvious!");
        embedBuilder.setFooter("You will be paying 2000 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You entered the correct password!");
        embedBuilder.setDescription("**Jonah** Perfect, now find that crack that code and get out of there!");

        return embedBuilder.build();
    }
}
