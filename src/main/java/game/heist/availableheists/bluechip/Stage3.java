package game.heist.availableheists.bluechip;

import game.heist.Stage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage3 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setColor(0x000dff);

        embedBuilder.setTitle("You and your team walk up the stairwell");
        embedBuilder.setDescription("There is a Guard who notices you are impostors!");

        embedBuilder.addField("`" + p + "HA`",
                "Enter this to take him out with the silenced weapons", false);
        embedBuilder.addField("`" + p + "HB`",
                "Enter this to tell him he has it all wrong", false);
        embedBuilder.addField("`" + p + "HC`",
                "Enter this to use a shotgun on him", false);

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
        embedBuilder.setDescription("**Marcus** You should have used the silenced weapons I gave you!");
        embedBuilder.setFooter("You will be paying 5000 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x000dff);

        embedBuilder.setAuthor(user.getName() + "'s turn");
        embedBuilder.setTitle("You took out the guard successfully!");
        embedBuilder.setDescription("**Marcus** Perfect! Now hide his body, and advance to the highest floor");

        return embedBuilder.build();
    }

}
