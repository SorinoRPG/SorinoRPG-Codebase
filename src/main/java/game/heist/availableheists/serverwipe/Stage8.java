package game.heist.availableheists.serverwipe;

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

        embedBuilder.setTitle("You and your team get to a safe distance");
        embedBuilder.setDescription("There is a password to blow uop the bomb");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to input '$3XE'", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to input 'BOOM31'", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to input '%^DSTROY'", false);
        embedBuilder.setFooter("password: $3XE");

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
        embedBuilder.setDescription("**Antonio** Why are you so incompetent!");
        embedBuilder.setFooter("You will be paying 1000 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("BOOOOM!");
        embedBuilder.setDescription("**Antonio** Amazing! You will all be paid soon! Well done!");
        embedBuilder.setFooter("You will be paid 60000 coins due to this success");


        return embedBuilder.build();
    }
}
