package main.userinterface.parser;

import game.SorinoNotFoundException;
import game.characters.Sorino;
import game.items.type.Item;
import game.items.type.ItemNotFound;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MarketParser {
    public static ArrayList<Object> bid(String text, String p) throws StringParseException {
        ArrayList<Object> objects = new ArrayList<>();

        if(!text.toUpperCase().startsWith("BID ")) throw new StringParseException(p + "MBID <ID> <Bid Price>");
        text = text.substring(4);

        String bidID = text.substring(0, text.indexOf(" "));
        if(!text.startsWith(bidID + " ")) throw new StringParseException(p + "MBID <ID> <Bid Price>");
        text = text.replace(bidID + " ", "");

        int bidPrice;
        try {
            bidPrice = Integer.parseInt(text);
        } catch (NumberFormatException e){
            throw new StringParseException(p + "MBID <ID> <Bid Price>");
        }

        objects.add(bidID);
        objects.add(bidPrice);

        return objects;
    }

    public static ArrayList<Object> sell(String text, String p) throws StringParseException{
        ArrayList<Object> objects = new ArrayList<>();
        text = text.toUpperCase();

        if(!text.startsWith("SELL ")) throw new StringParseException(p + "MSELL <Item/Sorino> <Time in hours> <Initial Price>");
        text = text.replace("SELL ", "");

        Object item;
        String itemName = text.substring(0, text.indexOf(" "));
        try {
            item = Sorino.AllSorino.getSorino(itemName);
        } catch (SorinoNotFoundException e) {
            try{
                if(!itemName.contains("("))
                    item = Item.AllItems.getItem(itemName);
                else {
                    String itemName1 = itemName.substring(0, itemName.indexOf("("));
                    int duplicity = Integer.parseInt(itemName.replace(itemName1 + "(", "")
                            .replace(")", ""));
                    Item item1 = Item.AllItems.getItem(itemName1);
                    item1.initialize(duplicity);
                    item = item1;
                }
            } catch (ItemNotFound | NumberFormatException itemNotFound) {
                throw new StringParseException(p + "MSELL <Item/Sorino> <Time in hours> <Initial Price>");
            }
        }
        if(!text.startsWith(itemName + " ")) throw new StringParseException(p + "MSELL <Item/Sorino> <Time in hours> <Initial Price>");
        text = text.replace(itemName + " ", "");
        long timeMillis;
        try {
            int bidTimeInt = Integer.parseInt(text.substring(0, text.indexOf(" ")));
            timeMillis = TimeUnit.HOURS.toMillis(bidTimeInt);
        } catch (NumberFormatException e){
            throw new StringParseException(p + "MSELL <Item/Sorino> <Time in hours> <Initial Price>");
        }
        if(!text.startsWith(text.substring(0, text.indexOf(" "))))
            throw new StringParseException(p + "MSELL <Item/Sorino> <Time in hours> <Initial Price>");
        text = text.replace(text.substring(0, text.indexOf(" ")) + " ", "");

        int bidPrice;
        try {
            bidPrice = Integer.parseInt(text);
        } catch (NumberFormatException e){
            throw new StringParseException(p + "MSELL <Item/Sorino> <Time in hours> <Initial Price>");
        }

        objects.add(item);
        objects.add(bidPrice);
        objects.add(timeMillis);

        return objects;
    }

    public static String oneArg(String text, String arg, String format) throws StringParseException {
        if(!text.toUpperCase().startsWith(arg.toUpperCase())) throw new StringParseException(format);
        return text.substring(arg.length());
    }
}
