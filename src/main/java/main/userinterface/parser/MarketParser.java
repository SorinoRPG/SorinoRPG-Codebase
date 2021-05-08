package main.userinterface.parser;

import game.SorinoNotFoundException;
import game.characters.Sorino;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MarketParser {
    public static ArrayList<Object> bid(String text, String p) throws StringParseException {
        ArrayList<Object> objects = new ArrayList<>();

        if(!text.startsWith("MBID ")) throw new StringParseException(p + "MBID <ID> <Bid Price>");
        text = text.replace("MBID ", "");

        String bidID = text.substring(0, text.indexOf(" "));
        if(!text.startsWith(bidID + " ")) throw new StringParseException(p + "MBID <ID> <Bid Price>");
        text = text.replace(bidID + " ", "");

        int bidPrice;
        try {
            bidPrice = Integer.parseInt(text.substring(0, text.indexOf(" ")));
        } catch (NumberFormatException e){
            throw new StringParseException(p + "MBID <ID> <Bid Price>");
        }

        if(!text.startsWith(text.substring(0, text.indexOf(" "))))
            throw new StringParseException(p + "MBID <ID> <Bid Price>");

        objects.add(bidID);
        objects.add(bidPrice);

        return objects;
    }

    public static ArrayList<Object> sell(String text, String p) throws StringParseException{
        ArrayList<Object> objects = new ArrayList<>();

        if(!text.startsWith("MSELL ")) throw new StringParseException(p + "MSELL <Item/Sorino> <Time in hours> <Initial Price>");
        text = text.replace("MSELL ", " ");

        Sorino sorino;
        String itemName = text.substring(0, text.indexOf(" "));
        try {
            sorino = Sorino.AllSorino.getSorino(itemName);
        } catch (SorinoNotFoundException e) {
            throw new StringParseException(p + "MSELL <Item/Sorino> <Time in hours> <Initial Price>");
        }
        if(!text.startsWith(itemName + " ")) throw new StringParseException(p + "MSELL <Item/Sorino> <Time in hours> <Initial Price>");
        text = text.replace(itemName + " ", "");

        long timeHours;
        try {
            int bidTimeInt = Integer.parseInt(text.substring(0, text.indexOf(" ")));
            timeHours = TimeUnit.HOURS.toMillis(bidTimeInt);
        } catch (NumberFormatException e){
            throw new StringParseException(p + "MBID <ID> <Bid Price>");
        }
        if(!text.startsWith(text.substring(0, text.indexOf(" ")))) throw new StringParseException(p + "MBID <ID> <Bid Price>");
        text = text.replace(text.substring(0, text.indexOf(" ")) + " ", "");

        int bidPrice;
        try {
            bidPrice = Integer.parseInt(text.substring(0, text.indexOf(" ")));
        } catch (NumberFormatException e){
            throw new StringParseException(p + "MBID <ID> <Bid Price>");
        }

        if(!text.startsWith(text.substring(0, text.indexOf(" "))))
            throw new StringParseException(p + "MBID <ID> <Bid Price>");


        objects.add(sorino);
        objects.add(timeHours);
        objects.add(bidPrice);

        return objects;
    }
}
