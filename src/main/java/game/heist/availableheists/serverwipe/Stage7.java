package game.heist.availableheists.serverwipe;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage7 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("You and your team prepare for the blast");
        embedBuilder.setDescription("There are 7 Guards that come out of nowhere");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to go use the guns", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to use the grenade", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to throw the knives", false);

        return embedBuilder.build();
    }

    @Override
    public int processChoice(char choice) {
        switch (choice){
            case 'A': return 0xA;
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
        embedBuilder.setDescription("**Antonio** Why did you not use the better weapons!");
        embedBuilder.setFooter("You will be paying 1000 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You took out the Guards!");
        embedBuilder.setDescription("**Antonio** They are a nuisance! But do what we came here for quick!");

        return embedBuilder.build();
    }
}
