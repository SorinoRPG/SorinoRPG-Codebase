package game.heist.availableheists.bluechip;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage4 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("You and your team reach the highest floor");
        embedBuilder.setDescription("There is another person who does not recognise you!");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to knock him out", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to tell him you are new", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to tell him you are from a different branch", false);

        return embedBuilder.build();
    }

    @Override
    public int processChoice(char choice) {
        switch (choice){
            case 'A': return 0xB;
            case 'B': return 0xA;
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
        embedBuilder.setDescription("**Marcus** Why in the hell would you try and knock out someone in front of " +
                "everyone?!");
        embedBuilder.setFooter("You will be paying 5000 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You fooled the person!");
        embedBuilder.setDescription("**Marcus** She did not suspect a thing! Go into the main office!");

        return embedBuilder.build();
    }
}
