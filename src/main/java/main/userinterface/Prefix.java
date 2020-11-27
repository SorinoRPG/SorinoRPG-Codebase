package main.userinterface;

import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;

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
     * @param command The command that the user has given, notice that it is not an
     *                {@link Command} object.
     * @return If the command contains any prefixes in a boolean value
     * @see Command
     */
    public static boolean assertPrefix(Message command){
        return command.getContentRaw().startsWith(PrefixString.FIGHT.prefix())
                || command.getContentRaw().startsWith(PrefixString.SEARCH.prefix())
                || command.getContentRaw().startsWith(PrefixString.AWARD_OP.prefix())
//                || command.getContentRaw().startsWith(PrefixString.RETRACT_OP.prefix())
                || command.getContentRaw().startsWith(PrefixString.SEE_PROFILE.prefix())
                || command.getContentRaw().startsWith(PrefixString.CREATE_PROFILE.prefix())
                || command.getContentRaw().startsWith(PrefixString.ADD_IGNATIAMON.prefix())
//                || command.getContentRaw().startsWith(PrefixString.REMOVE_IGNATIAMON.prefix())
                || command.getContentRaw().startsWith(PrefixString.UPDATE_PROFILE.prefix())
                || command.getContentRaw().startsWith(PrefixString.GET_LOGS.prefix())
                || command.getContentRaw().startsWith(PrefixString.ERASE_PROFILE.prefix())
                || command.getContentRaw().startsWith(PrefixString.SEE_RANK.prefix())
                || command.getContentRaw().startsWith(PrefixString.LEADERBOARD.prefix());
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
     * @param command What will be affected by the
     *                removing of the prefix
     * @return The new string without a prefix
     * @see PrefixString
     */
    public static String removeFightPrefix(String command){
        return command.replace("-=", "");
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
     * @param command The command that contains the prefix
     * @return The prefix as a string
     */
    public static String cutPrefix(String command){
        return command.substring(0, 2);
    }

    enum PrefixString implements PrefixGetter {
        LEADERBOARD() {
            @Override
            public String prefix() {
                return "-L";
            }
        },
        ERASE_PROFILE() {
            @Override
            public String prefix() {
                return "-$";
            }
        },
        SEE_RANK() {
            @Override
            public String prefix() {
                return "-R";
            }
        }
        ,
        GET_LOGS() {
            @Override
            public String prefix() {
                return "-^";
            }
        },
        FIGHT() {
            @Override
            public String prefix() {
                return "-=";
            }
        },
        UPDATE_PROFILE(){
            @Override
            public String prefix() {
                return "-*";
            }
        }
        ,
        SEARCH() {
            @Override
            public String prefix() {
                return "--";
            }
        },
        AWARD_OP() {
            @Override
            public String prefix() {
                return "-/";
            }
        },
        /*
        RETRACT_OP() {
            @Override
            public String prefix() {
                return "-\\";
            }
        },*/
        CREATE_PROFILE(){
            @Override
            public String prefix(){
                return "-(";
            }
        },
        SEE_PROFILE() {
            @Override
            public String prefix(){
                return "-)";
            }
        },
        ADD_IGNATIAMON(){
            @Override
            public String prefix() {
                return "-%";
            }
        };
        /*REMOVE_IGNATIAMON(){
            @Override
            public String prefix() {
                return "-$";
            }
        };*/

        /**
         * Used to convert a prefix in a {@link String} form to a {@link PrefixString}
         *
         * <p>
         *     Checks the prefix against all the
         *     {@link PrefixString} values, if it
         *     equals one, returns it as an Optional
         *     object, else, it returns an empty
         *     Optional object
         * </p>
         * @param prefix The prefix that will be determined
         * @return An optional value for a {@link PrefixString}
         */
        public static Optional<PrefixString> getPrefix(String prefix) {
            ArrayList<PrefixString> prefixes =
                    new ArrayList<>(EnumSet.allOf(PrefixString.class));
            for(PrefixString prefixString: prefixes){
                if(prefixString.prefix().equals(prefix))
                    return Optional.of(
                    prefixString);
            }
            return Optional.empty();
        }
    }
}
