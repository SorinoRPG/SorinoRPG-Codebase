package game.heist.availableheists.bluechip;

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

        embedBuilder.setTitle("You and your team walk up to the computer");
        embedBuilder.setDescription("You are stopped by 5 Guards! They know you are frauds!");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to run", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to keep trying to convince them they are wrong", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to use the military grade weapons on them", false);

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
        embedBuilder.setDescription("**Marcus** No! Your cover was blown already!");
        embedBuilder.setFooter("You will be paying 5000 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You took all the guards out!");
        embedBuilder.setDescription("**Marcus** Great, you took all the guards out! Now log into this guys computer!");

        return embedBuilder.build();
    }
}
