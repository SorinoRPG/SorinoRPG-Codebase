package game.quest;

import data.Profile;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.Serializable;

public interface Quest extends Serializable {
    boolean requirement(Profile profile);
    boolean hasFinished(Profile profile);
    MessageEmbed status(Profile profile);
    void lore(GuildMessageReceivedEvent event);
    void award(Profile profile);
}
