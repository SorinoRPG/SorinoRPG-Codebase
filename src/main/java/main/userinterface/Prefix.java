package main.userinterface;

import com.mongodb.client.MongoCollection;
import data.Mongo;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * A static class used for prefixes of commands
 *
 * <h1>Prefix</h1>
 * <p>
 *     Used mainly for understanding prefixes of commands,
 *     contains the {@linkplain PrefixGetter} and {@linkplain PrefixString}
 * </p>
 *
 * @author Emmanuel Okafor
 * @see Command
 * @see PrefixString
 * @see PrefixGetter
 */

public class Prefix {

    interface PrefixGetter{
        String prefix();
    }

    /**
     * Used to assert if a message has a prefix or not
     *
     * <p>
     *     It will check the command if it starts with
     *     each prefix. It does not require a {@link Command}
     *     object because this is still used in the initialization
     *     of that object. Only 1 usage.
     * </p>
     *
     * @param event The command that the user has given, notice that it is not an
     *                {@link Command} object.
     * @return If the command contains any prefixes in a boolean value
     * @see Command
     */
    public static boolean assertPrefix(GuildMessageReceivedEvent event){
        return event.getMessage().getContentRaw().startsWith(Prefix.guildPrefix(event.getGuild().getId()));
    }

    /**
     * Used to remove the Fight prefix
     *
     * <p>
     *     Removes the fight prefix from the command,
     *     this makes it much simpler and easier to interact
     *     with what the user is actually asking to do.
     * </p>
     *
     * @param event What will be affected by the
     *                removing of the prefix
     * @return The new string without a prefix
     * @see PrefixString
     */
    public static String removeFightPrefix(GuildMessageReceivedEvent event){
        String message = event.getMessage()
                .getContentRaw();
        return message
                .substring(Prefix.guildPrefix(event.getGuild().getId()).length()+1);
    }

    /**
     * Cuts out the prefix from a command
     *
     * <p>
     *     Unlike the removeFightPrefix() method,
     *     this one actually just cuts out the prefix
     *     from the string, so the actual prefix can be
     *     utilized.
     * </p>
     *
     * @param guildID The command that contains the prefix
     * @return The prefix as a string
     */

    public static String guildPrefix(String guildID){
        MongoCollection<Document> mongoCollection = Mongo.mongoClient()
                .getDatabase("guild")
                .getCollection("prefix");
        if(mongoCollection.find(new Document("guildID", guildID)).iterator().hasNext()){
            Document doc = mongoCollection.find(new Document("guildID", guildID)).first();
            return  doc.getString("prefix");
        }
        return ".";
    }
    enum PrefixString implements PrefixGetter {
        HELP() {
            @Override
            public String prefix() {
                return "HELP";
            }
        },
        BRIBE() {
            @Override
            public String prefix() {
                return "BRIBE";
            }
        },
        BUG() {
            @Override
            public String prefix() {
                return "BUG";
            }
        },
        VOTE() {
            @Override
            public String prefix() {
                return "VOTE";
            }
        },
        UPDATE() {
            @Override
            public String prefix() {
                return "UPDATE";
            }
        },
        INFO() {
            @Override
            public String prefix() {
                return "INFO";
            }
        },
        INVITE(){
            @Override
            public String prefix() {
                return "INVITE";
            }
        },
        CHANGE() {
            @Override
            public String prefix() {
                return "SET";
            }
        },
        DONATE() {
            @Override
            public String prefix() {
                return "DONATE";
            }
        },
        FIGHT() {
            @Override
            public String prefix() {
                return "F";
            }
        },
        SEARCH() {
            @Override
            public String prefix() {
                return "S";
            }
        },
        SEE_PROFILE() {
            @Override
            public String prefix(){
                return "P";
            }
        },
        CREATE_PROFILE(){
            @Override
            public String prefix(){
                return "C";
            }
        },
        SEE_RANK() {
            @Override
            public String prefix() {
                return "R";
            }
        },
        WRAP() {
            @Override
            public String prefix() {
                return "W";
            }
        },
        SLOT() {
            @Override
            public String prefix() {
                return "G";
            }
        },
        HEISTS(){
            @Override
            public String prefix() {
                return "H";
            }
        },
        STREET_FIGHT(){
            @Override
            public String prefix() {
                return "B";
            }
        },
        MARKET() {
            @Override
            public String prefix() {
                return "M";
            }
        }
    }
}
