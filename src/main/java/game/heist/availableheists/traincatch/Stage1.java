package game.heist.availableheists.traincatch;

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

        embedBuilder.setTitle("You and your team parachute out of the helicopter");
        embedBuilder.setDescription("There are 3 main carriages on the train");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to land on the Front Cab", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to land on the Main Cargo hold", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to land on the Secondary Cargo hold", false);

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
        embedBuilder.setTitle("You landed on the Main Cargo Hold!");
        embedBuilder.setDescription("**Jessica** Fabulous!");

        return embedBuilder.build();
    }
}
