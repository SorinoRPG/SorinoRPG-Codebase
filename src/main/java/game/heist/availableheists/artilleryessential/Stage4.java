package game.heist.availableheists.artilleryessential;

import game.heist.Stage;
import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage4 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You and your team advance through the passage way.");
        embedBuilder.setDescription("There is a lock, it will sound an alarm if you get the password " +
                "wrong");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to input 'IGOR21'", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to input 'KINGIGOR'", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to input '55IGOR55'", false);
        embedBuilder.setFooter("password: IGOR21");

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
        embedBuilder.setDescription("**Antonio** Oh come on! **Igor** foolishly writes notes to remind " +
                "him of the password! Don't tell me you didn't see it!");
        embedBuilder.setFooter("You will be paying 300 coins due to this failure");

        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You entered the correct password!");
        embedBuilder.setDescription("**Antonio** I know, what a stupid password. Go up these stairs");


        return embedBuilder.build();
    }
}
