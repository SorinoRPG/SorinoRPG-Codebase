package game.heist.availableheists.artilleryessential;

import game.heist.Stage;
import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage2 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You and your team go deeper into the Hideout");
        embedBuilder.setDescription("There is another guard, who looks much stronger, " +
                "in front, but there is also a vent to the " +
                "left of him.");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to take out the guards with the knives", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to shoot the guards with the guns", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to go into the vent", false);

        return embedBuilder.build();
    }

    @Override
    public int processChoice(char choice) {
        switch (choice){
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

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You failed the heist!");
        embedBuilder.setDescription("**Antonio** You tried to take out that huge guy?" +
                " You deserve to fail! Maybe **Igor** will let you off if you pay him");
        embedBuilder.setFooter("You will be paying 300 coins due to this failure");
        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You entered the vent!");
        embedBuilder.setDescription("**Antonio** Lovely stuff! Now go to the right and enter the " +
                "main building through the next opening.");

        return embedBuilder.build();
    }
}
