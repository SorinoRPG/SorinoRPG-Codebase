package game.heist.availableheists.casinorush;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;


public class Stage3 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You enter into the main Casino floor, civilians flee the Casino.");
        embedBuilder.setDescription("12 Guards line up!");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to take them out with the Guns", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to try and take them out with a Grenade", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to try and run away", false);

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

        embedBuilder.setTitle("You failed the heist!");
        embedBuilder.setDescription("**Marcus** Oh no! They were able to take you out!");
        embedBuilder.setFooter("You will lose 10000 coins for this failure");

        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("The Guards are all knocked out!");
        embedBuilder.setDescription("**Marcus** All of them are down! Take the stairs down to the vault!");

        return embedBuilder.build();
    }
}
