package game.heist.availableheists.traincatch;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage6 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("You and your team pack up the cargo");
        embedBuilder.setDescription("You prepare to head to the carriage you need to set the bomb");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to go to the Front Cab", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to go to the Main Cargo hold", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to go to the Secondary Cargo hold", false);

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
        embedBuilder.setDescription("**Jessica** Next time I will take my money to some people who can actually " +
                "listen!");
        embedBuilder.setFooter("You will be paying 600 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You went to the Front Cab!");
        embedBuilder.setDescription("**Jessica** Fabulous!");

        return embedBuilder.build();
    }
}
