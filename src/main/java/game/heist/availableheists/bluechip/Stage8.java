package game.heist.availableheists.bluechip;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage8 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("You and your attempt to leave the building!");
        embedBuilder.setDescription("Oh no! The entire Guard force is blocking you from leaving!");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to break the window and parachute out", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to attempt to fight 40 Guards", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to surrender", false);

        return embedBuilder.build();
    }

    @Override
    public int processChoice(char choice) {
        switch (choice){
            case 'A': return 0xC;
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
        embedBuilder.setDescription("**Marcus** Why did you not try to escape with the parachutes I gave!");
        embedBuilder.setFooter("You will be paying 5000 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You beat the heist!");
        embedBuilder.setDescription("**Marcus** AMAZING! You and I are going to be rich men! Find a place to land " +
                "and give me the data, I will wire you the money ASAP!");
        embedBuilder.setFooter("You will receive 100,000 coins for beating this heist");

        return embedBuilder.build();
    }
}
