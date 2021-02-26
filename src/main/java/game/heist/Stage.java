package game.heist;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.io.Serializable;

public interface Stage extends Serializable {
    MessageEmbed ask(String p, User user);
    int processChoice(char choice);
    MessageEmbed failure(User user);
    MessageEmbed success(User user);
}
