package game.heist.availableheists.artilleryessential;

import game.heist.Stage;
import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Stage8 implements Stage {
    @Override
    public MessageEmbed ask(String p, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x000dff);

            embedBuilder.setAuthor(user.getName() + "'s turn");
            embedBuilder.setTitle("You and your team enter the car.");
            embedBuilder.setDescription("The car has a lock! Enter the correct password!");

            embedBuilder.addField("`" + p + "HA`",
                    "Enter this to input 'SUPFAST69'", false);
            embedBuilder.addField("`" + p + "HB`",
                    "Enter this to input 'F&F31'", false);
            embedBuilder.addField("`" + p + "HC`",
                    "Enter this to input 'YCSM22'", false);
            embedBuilder.setFooter("password: SUPFAST69");


        return embedBuilder.build();
    }

    @Override
    public int processChoice(char choice) {
        switch (choice){
            case 'A': return 0xC;
            case 'C': return 0xB;
            case 'B': return 0xB;
            default: return 0;
        }
    }

    @Override
    public MessageEmbed failure(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x000dff);

            embedBuilder.setAuthor(user.getName() + "'s turn");
            embedBuilder.setTitle("You failed the heist!");
            embedBuilder.setDescription("**Antonio** No! We were so close! I remembered to leave " +
                        "the password in the car for you!");
            embedBuilder.setFooter("You will be paying 300 coins due to this failure");


        return embedBuilder.build();
    }

    @Override
    public MessageEmbed success(User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x000dff);

            embedBuilder.setAuthor(user.getName() + "'s turn");
            embedBuilder.setTitle("You passed the heist!");
            embedBuilder.setDescription("**Antonio** FANTASTIC! I'll wire you all the money " +
                    "as soon as possible. It would be great to do this again! So long, for now!");
            embedBuilder.setFooter("You will receive 10,000 coins each");


        return embedBuilder.build();
    }
}
