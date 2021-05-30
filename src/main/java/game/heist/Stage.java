package game.heist;

import game.items.type.Item;
import net.dv8tion.jda.api.entities.MessageEmbed;

public interface Stage {
    MessageEmbed option();

    Result result(Item item);

    class Result {
        public MessageEmbed embed;
        public int resultInteger;

        Result(MessageEmbed embed, int resultInteger){
            this.embed = embed;
            this.resultInteger = resultInteger;
        }
    }
}
