package game.heist.availableheists.codebreaker;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage4 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You enter the main building");
        embedBuilder.setDescription("The floor is swarming with Guard!");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to throw a Gas Knockout grenade", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to try and shoot them all", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to throw a grenade", false);

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
        embedBuilder.setDescription("**Jonah** You should have used the knockout!");
        embedBuilder.setFooter("You will be paying 2000 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You knocked out all the Guards!");
        embedBuilder.setDescription("**Jonah** All gone!");

        return embedBuilder.build();
    }
}
