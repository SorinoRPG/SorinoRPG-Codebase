package game.heist.availableheists.codebreaker;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage8 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You open the code");
        embedBuilder.setDescription("The code only gives a capital city called Vientiane");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to send Jonah 'Laos, Vientiane'", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to send Jonah 'Italy, Vientiane'", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to enter 'Macedonia, Vientiane'", false);

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
        embedBuilder.setDescription("**Jonah** Useless");
        embedBuilder.setFooter("You will be paying 2000 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You sent Jonah the code!");
        embedBuilder.setDescription("**Jonah** Amazing! Get out of there and you'll be rich men!");
        embedBuilder.setFooter("You will receive 80000 coins due to this success");

        return embedBuilder.build();
    }
}
