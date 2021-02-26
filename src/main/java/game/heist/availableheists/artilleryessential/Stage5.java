package game.heist.availableheists.artilleryessential;

import game.heist.Stage;
import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage5 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You and your team walk up the stairs");
        embedBuilder.setDescription("There are 2 guards on the final level of the staircase");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to take out the guards with the knives", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to shoot the guards with the guns", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to knock the guards out by hand", false);

        return embedBuilder.build();
    }

    @Override
    public int processChoice(char choice) {
        switch (choice){
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

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You failed the heist!");
        embedBuilder.setDescription("**Antonio** AGH! You failed!" +
                " Why didn't you use the knives again?!");
        embedBuilder.setFooter("You will be paying 300 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You took down the guards!");
        embedBuilder.setDescription("**Antonio** Perfect! Now enter the weapons vault");


        return embedBuilder.build();
    }
}
