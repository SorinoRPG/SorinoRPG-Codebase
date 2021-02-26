package game.heist.availableheists.traincatch;

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

        embedBuilder.setTitle("You and your team arrive at the front cab");
        embedBuilder.setDescription("You see the Train Conductor");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to shoot him with the silent weapons", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to smoke him out", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to try and set the bombs without him seeing", false);

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
        embedBuilder.setDescription("**Jessica** This was meant to be a stealth mission!");
        embedBuilder.setFooter("You will be paying 600 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You took out the Conductor!");
        embedBuilder.setDescription("**Jessica** Great! Now set that bomb and get out!");

        return embedBuilder.build();
    }
}
