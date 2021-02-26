package game.heist.availableheists.codebreaker;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage6 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You open the first code.");
        embedBuilder.setDescription("The code contains the number 72 and ends in pSAL!");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to enter '72pSAi'", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to enter 'pSAL72'", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to enter '72pSAL'", false);

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
        embedBuilder.setDescription("**Jonah** I thought you were a professional!");
        embedBuilder.setFooter("You will be paying 2000 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You cracked the first code!");
        embedBuilder.setDescription("**Jonah** Great, one more code to crack and we are GOLDEN!");

        return embedBuilder.build();
    }
}
