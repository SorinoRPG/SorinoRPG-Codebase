package main.tasks;

import com.mongodb.client.MongoCollection;
import data.Mongo;
import game.characters.Sorino;
import game.items.type.Item;
import game.value.transfer.ItemListing;
import game.value.transfer.Market;
import game.value.transfer.SorinoListing;
import main.MainBot;
import main.Task;
import net.dv8tion.jda.api.JDA;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MarketPoster implements Task {
    int sorino;
    int numOfItems;
    ArrayList<Sorino> sorinoArrayList;
    ArrayList<Item> itemArrayList;
    JDA jda;
    MongoCollection<Document> collection;


    public MarketPoster(int numOfSorino, int numOfItems, JDA jda, MongoCollection<Document> collection){
        this.sorino = numOfSorino;
        this.jda = jda;
        this.collection = collection;
        this.numOfItems = numOfItems;
    }


    @Override
    public void doTask() {
        try {
            ArrayList<Sorino> sorinoArrayList = new ArrayList<>();
            for (int i = 0; i < sorino; i++) {
                Sorino sorinoToBeAdded = Sorino.AllSorino.getRandom();
                while (this.contains(sorinoToBeAdded, sorinoArrayList))
                    sorinoToBeAdded = Sorino.AllSorino.getRandom();
                sorinoArrayList.add(sorinoToBeAdded);
            }

            for (Sorino sorino : sorinoArrayList) {
                SorinoListing sorinoListing = new SorinoListing(jda.getSelfUser(), sorino,
                        this.calculatePrice(sorino), TimeUnit.HOURS.toMillis(2));
                this.collection.insertOne(sorinoListing.toDocument());
            }
            this.sorinoArrayList = sorinoArrayList;

            ArrayList<Item> itemArrayList = new ArrayList<>();
            for(int i = 0; i < numOfItems; i++){
                Item itemToBeAdded = Item.AllItems.randomItem();
                while(this.contains(itemToBeAdded, itemArrayList))
                    itemToBeAdded = Item.AllItems.randomItem();
                itemArrayList.add(itemToBeAdded);
            }

            for(Item item : itemArrayList){
                ItemListing itemListing = new ItemListing(jda.getSelfUser(), item, this.calculatePrice(item),
                        TimeUnit.HOURS.toMillis(2));
                this.collection.insertOne(itemListing.toDocument());
            }
            this.itemArrayList = itemArrayList;

        } catch(Exception ignored){}
    }

    private int calculatePrice(Sorino sorino){
        int basePrice = 100 - sorino.getRarity();
        int additional = (sorino.getEnergy(0) + sorino.getHealth(0) ) * (20 * sorino.getMoves().size());

        return basePrice + additional;
    }

    private int calculatePrice(Item item){
        int basePrice = (350 * item.getCapability().getEffect()) * item.getDuplication();
        int additional = new Random().nextInt(basePrice / 2);

        return basePrice + additional;
    }

    private <T> boolean contains(T item, List<T> itemList){
        for(T item1 : itemList)
            if(item1.toString().equals(item.toString())) return true;
        return false;
    }


    @Override
    public void printTaskStatus() {
        MainBot.logger.info(this.sorino +
                " new Sorino added: " +
                this.sorinoArrayList.toString());
        MainBot.logger.info(this.numOfItems +
                " new Item added: " +
                this.itemArrayList.toString());
    }
}
