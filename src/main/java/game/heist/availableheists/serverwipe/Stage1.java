package game.heist.availableheists.serverwipe;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage1 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("You and your team exit the van with the explosives and weapons");
        embedBuilder.setDescription("There is a password to blow up the front door");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to input 'SRVER33'", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to input 'BLOWUP31'", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to input 'XPLOD3'", false);
        embedBuilder.setFooter("password: BLOWUP31");

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
        embedBuilder.setDescription("**Antonio** Idiots! You should have seen the password!");
        embedBuilder.setFooter("You will be paying 1000 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You entered the correct password!");
        embedBuilder.setDescription("**Antonio** You blew that door to smithereens!");

        return embedBuilder.build();
    }
}
