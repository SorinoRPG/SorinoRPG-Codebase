package game.heist.availableheists.traincatch;

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

        embedBuilder.setTitle("You and your team go to the vault");
        embedBuilder.setDescription("There is a password to set the explosive again!");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to input 'BOOMBOOM22'", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to input 'BOMB41'", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to input 'TNT23'", false);
        embedBuilder.setFooter("password: 'TNT23'");

        return embedBuilder.build();
    }

    @Override
    public int processChoice(char choice) {
        switch (choice){
            case 'A': return 0xB;
            case 'B': return 0xB;
            case 'C': return 0xC;
            default: return 0;
        }
    }

    @Override
    public MessageEmbed failure(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You failed the heist!");
        embedBuilder.setDescription("**Jessica** You've wasted my time!");
        embedBuilder.setFooter("You will be paying 600 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("BOOOM! You passed the heist!");
        embedBuilder.setDescription("**Jessica** Amazing! I'll wire you the money ASAP, well done boys! " +
                "Adios!");
        embedBuilder.setFooter("You will be paid 60000 coins due to this success!");


        return embedBuilder.build();
    }
}
