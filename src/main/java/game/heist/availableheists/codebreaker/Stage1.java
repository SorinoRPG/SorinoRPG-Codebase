package game.heist.availableheists.codebreaker;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage1 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You leave the van dressed as Government people");
        embedBuilder.setDescription("There are 2 Guards outside of the entrance");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to take them out with the Guns", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to tell them you are new", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to run back into the van", false);

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
        embedBuilder.setDescription("**Jonah** This mission was meant to be undercover! Dimwit!");
        embedBuilder.setFooter("You will be paying 2000 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You fooled the Guard!");
        embedBuilder.setDescription("**Jonah** He did not suspect a thing!");

        return embedBuilder.build();
    }
}
