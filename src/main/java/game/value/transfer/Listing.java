package game.value.transfer;

import data.Mongo;
import data.Profile;
import data.ProfileNotFoundException;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Random;

public interface Listing {
    String nameOfSeller();
    String nameOfItem();
    String descOfItem();
    int price();
    void setPrice(int price);
    boolean checkHasItem(User user) throws ProfileNotFoundException;
    long timeAllocated();
    long timePutOnMarket();
    String sellerID();
    String listingID();
    String highestBidderID();
    String highestBidderName();
    void setHighestBidderID(User user);
    void awardItem(Profile profile);
    void removeItem(Profile profile);
    ArrayList<String> lostBidderIds();
    Document toDocument();

    static String createListingID(){
        char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                        'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
                        'm', 'n', 'o', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
                        '6', '7', '8', '9'};
        StringBuilder stringBuilder = new StringBuilder();
        while(stringBuilder.length() != 8){
            Random random = new Random();
            stringBuilder.append(chars[random.nextInt(chars.length)]);
        }
        return stringBuilder.toString();
    }
    
    static Listing getListingById(String id) throws ListingNotFoundException{
        if(!Mongo.mongoClient()
                .getDatabase("game")
                .getCollection("market")
                .find(new Document("listingID", id)).iterator().hasNext()) throw new ListingNotFoundException(id);

        Document document = Mongo.mongoClient()
                .getDatabase("game")
                .getCollection("market")
                .find(new Document("listingID", id)).first();

        if(document.containsKey("sorino"))
            return SorinoListing.toListing(document);
        else
            return ItemListing.toListing(document);
    }
}
