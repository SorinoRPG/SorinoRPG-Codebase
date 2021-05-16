package game.value.transfer;

import com.mongodb.client.MongoCollection;
import data.Mongo;
import data.Profile;
import data.ProfileNotFoundException;
import main.MainBot;
import main.Paginator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Market {
    private static ArrayList<Listing> getListings(){
        ArrayList<Listing> listing = new ArrayList<>();

        MongoCollection<Document> collection = Mongo.mongoClient().getDatabase("game")
                .getCollection("market");
        for (Document document : collection.find()) {
            if(document.containsKey("sorino"))
                listing.add(SorinoListing.toListing(document));
            else if(document.containsKey("item"))
                listing.add(ItemListing.toListing(document));
        }

        Collections.shuffle(listing);
        return listing;
    }

    public static void update(){
        for(Listing listing : getListings())
            if(System.currentTimeMillis() > listing.timePutOnMarket() + listing.timeAllocated())
                new SellTask(listing.listingID()).run();
    }

    public static Paginator top(int pages){
        ArrayList<Listing> listings = getListings();
        EmbedBuilder message = new EmbedBuilder();
        message.setColor(0x00dff);

        message.setTitle("Top posts on the Market");
        Paginator paginator = new Paginator("", 0x00dff);
        for(Listing listing : listings){
            if(message.getFields().size() % 6 == 0 && message.getFields().size() != 0){
                paginator.addPage(message);
                message = new EmbedBuilder();
                message.setTitle("Top posts on the Market");
            }
            long time = listing.timePutOnMarket() + listing.timeAllocated() - System.currentTimeMillis();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(time);

            message.addField(listing.nameOfItem() + "#" + listing.listingID(),
                    listing.descOfItem() + "\n" +
                            "Time until bidding ends `" + minutes + " minutes`",
                    true);

            if(paginator.pages >= pages) break;
        }

        if(paginator.embeds.size() == 0) {
            return new Paginator(message);
        }
        paginator.addPage(message);

        return paginator;
    }

    public static Paginator search(String searchTerm){
        ArrayList<Listing> match = new ArrayList<>();

        for(Listing listing : getListings()){
            if(listing.nameOfItem().toUpperCase().contains(searchTerm.toUpperCase()) ||
                    listing.descOfItem().contains(searchTerm)) match.add(listing);

            else if(listing.listingID().equals(searchTerm)||
                    (listing.nameOfItem() + "#" + listing.listingID()).equals(searchTerm)){
                EmbedBuilder listingEmbed = new EmbedBuilder();
                listingEmbed.setTitle(listing.nameOfItem() + "#" + listing.listingID());
                listingEmbed.setDescription(listing.descOfItem());

                long time = listing.timePutOnMarket() + listing.timeAllocated() - System.currentTimeMillis();
                long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
                listingEmbed.setFooter( "Time until bidding ends " + minutes + " minutes");

                return new Paginator(listingEmbed);
            }
        }

        EmbedBuilder message = new EmbedBuilder();
        message.setColor(0x00dff);

        message.setTitle("Search results for: " + searchTerm);
        Paginator paginator = new Paginator(searchTerm, 0x00dff);

        for(Listing listing : match){
            if(message.getFields().size() % 6 == 0 && message.getFields().size() != 0){
                paginator.addPage(message);
                message = new EmbedBuilder();
                message.setTitle("Search results for: " + searchTerm);
            }
            long time = listing.timePutOnMarket() + listing.timeAllocated() - System.currentTimeMillis();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(time);

            message.addField(listing.nameOfItem() + "#" + listing.listingID(),
                    listing.descOfItem() + "\n" +
                            "Time until bidding ends `" + minutes + " minutes`",
                    true);
        }

        if(paginator.embeds.size() == 0) {
            return new Paginator(message);
        }
        paginator.addPage(message);
        paginator.paginate();

        return paginator;
    }
    
    public static Paginator personalItems(User user){
        ArrayList<Listing> personalListing = new ArrayList<>();
        
        for(Listing listing : getListings())
            if(listing.sellerID().equals(user.getId())) personalListing.add(listing);

        EmbedBuilder message = new EmbedBuilder();
        message.setColor(0x00dff);

        message.setTitle(user.getAsTag() + " current market listings");
        message.setDescription("These are the current listing on the market that you have posted.");

        Paginator paginator = new Paginator("Your listings", 0x00dff);

        for (Listing listing : personalListing) {
            if(message.getFields().size() % 6 == 0 && message.getFields().size() != 0){
                paginator.addPage(message);
                message = new EmbedBuilder();
                message.setTitle(user.getAsTag() + " current market listings");
                message.setDescription("These are the current listing on the market that you have posted.");
            }
            long time = listing.timePutOnMarket() + listing.timeAllocated() - System.currentTimeMillis();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(time);

            message.addField(listing.nameOfItem() + "#" + listing.listingID(),
                    listing.descOfItem() + "\n" +
                            "Time until bidding ends `" + minutes + " minutes`",
                    true);
        }

        if(paginator.embeds.size() == 0) {
            return new Paginator(message);
        }
        paginator.addPage(message);
        paginator.paginate();

        return paginator;
    }

    public static MessageEmbed sell(Listing listing, User user) throws ProfileNotFoundException {
        if(!listing.checkHasItem(user)){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x00dff);
            embedBuilder.setThumbnail(user.getAvatarUrl());

            embedBuilder.setTitle("Error in selling.");
            embedBuilder.setDescription("Your attempt of selling has failed as you do not own the item you are " +
                    "trying to sell.");
            embedBuilder.setFooter("Please try again with an item you own.");

            return embedBuilder.build();
        }

        for(Listing listingItem : getListings()) {
            if (listingItem.sellerID().equals(user.getId())) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(0x00dff);
                embedBuilder.setThumbnail(user.getAvatarUrl());

                embedBuilder.setTitle("Error in selling.");
                embedBuilder.setDescription("Your attempt of selling has failed as you are already selling the item.");
                embedBuilder.setFooter("Please try again with an item you own.");

                return embedBuilder.build();
            }
        }

        MongoCollection<Document> collection = Mongo.mongoClient()
                .getDatabase("game")
                .getCollection("market");
        collection.insertOne(listing.toDocument());

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.schedule(new SellTask(listing.listingID()), listing.timeAllocated(), TimeUnit.MILLISECONDS);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(0x00dff);
        embed.setThumbnail(user.getAvatarUrl());

        embed.setTitle("You have successfully uploaded " + listing.nameOfItem() + "#" + listing.listingID() + " to " +
                "the market.");
        long time = listing.timePutOnMarket() + listing.timeAllocated() - System.currentTimeMillis();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        embed.setDescription(listing.descOfItem() + "\n" +
                "Time until bidding ends `" + minutes + " minutes`");

        embed.setFooter("Cross your fingers that someone bids on it!");

        return embed.build();
    }



    public static MessageEmbed bid(Listing listing, int price, User user, Profile profile){
        if(listing.price() >= price) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x00dff);
            embedBuilder.setThumbnail(user.getAvatarUrl());

            embedBuilder.setTitle("Error in bidding.");
            embedBuilder.setDescription("Your bid offer of " + price + " coins is not enough to outbid " +
                    listing.highestBidderName());
            embedBuilder.setFooter("Please try a higher offer.");

            return embedBuilder.build();
        }
        else if(profile.coins - price < 0){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x00dff);
            embedBuilder.setThumbnail(user.getAvatarUrl());

            embedBuilder.setTitle("Error in bidding.");
            embedBuilder.setDescription("Your bid offer of " + price + " coins is too high for you to pay.");
            embedBuilder.setFooter("Please try make more money, or a lower bid.");

            return embedBuilder.build();
        } else if(((profile.coins - profile.coinsToBeSpent) - price) < 0){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x00dff);
            embedBuilder.setThumbnail(user.getAvatarUrl());

            embedBuilder.setTitle("Error in bidding.");
            embedBuilder.setDescription("Your bid offer of " + price + " coins you are yet to pay are outstanding.");
            embedBuilder.setFooter("Please try make more money, or a lower bid.");

            return embedBuilder.build();
        }

        listing.setHighestBidderID(user);
        listing.setPrice(price);

        try {
            profile.addCoinsToBeSpent(price);
            profile.recreate();
        } catch(ProfileNotFoundException ignored) {}

        for(String userID : listing.lostBidderIds()){
            if(userID.equals(MainBot.getJda().getSelfUser().getId())) continue;
            MainBot.getJda().retrieveUserById(userID).queue(user1 ->
                user1.openPrivateChannel().queue(channel -> {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(0x00dff);
                    embedBuilder.setThumbnail(user1.getAvatarUrl());

                    if(user.getAsTag().equals(listing.highestBidderName()))
                        embedBuilder.setTitle(user.getAsTag() + " has increased their bid on " +
                                listing.nameOfItem() + "#" + listing.listingID());
                    else embedBuilder.setTitle(user.getAsTag() + " has become the new highest bidder on " +
                            listing.nameOfItem() + "#" + listing.listingID());
                    
                    embedBuilder.setDescription("The new price for the item is now " + price + " coins"+ "\n" +
                            listing.descOfItem());
                    long time = listing.timePutOnMarket() + listing.timeAllocated() - System.currentTimeMillis();
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
                    embedBuilder.setFooter("Time until bidding ends: `" + minutes + " minutes`");
                    channel.sendMessage(embedBuilder.build()).queue();
            }));
        }

        MongoCollection<Document> collection = Mongo.mongoClient().getDatabase("game")
                .getCollection("market");
        collection.replaceOne(new Document("listingID", listing.listingID()), listing.toDocument());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x00dff);
        embedBuilder.setThumbnail(user.getAvatarUrl());

        embedBuilder.setTitle(user.getAsTag() + " has become the new highest bidder on " +
                listing.nameOfItem() + "#" + listing.listingID());
        embedBuilder.setDescription("The new price for the item is now " + price + " coins"+ "\n" +
                listing.descOfItem());
        long time = listing.timePutOnMarket() + listing.timeAllocated() - System.currentTimeMillis();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        embedBuilder.setFooter("Time until bidding ends: `" + minutes + " minutes`");



        return embedBuilder.build();
    }
}
