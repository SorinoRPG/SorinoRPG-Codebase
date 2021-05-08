package game.value.transfer;

import data.Profile;
import game.characters.Sorino;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;

import java.util.ArrayList;

public class SorinoListing implements Listing{
    String nameOfSeller;
    Sorino sorino;
    int price;
    long timeAllocated;
    long timeInserted;
    String sellerID;
    String listingID;
    String highestBidderID;
    ArrayList<String> losingBidders;

    public SorinoListing(User seller, Sorino sorino, int price, long timeAllocated, String sellerID){
        this.nameOfSeller = seller.getAsTag();
        this.sorino = sorino;
        this.price = price;
        this.timeAllocated = timeAllocated;
        this.timeInserted = System.currentTimeMillis();
        this.sellerID = seller.getId();
        this.listingID = Listing.createListingID();
        this.highestBidderID = seller.getId();
        this.losingBidders = new ArrayList<>();

    }

    private SorinoListing(String nameOfSeller, Sorino sorino, int price, long timeAllocated, long timeInserted,
                          String sellerID, String listingID, String highestBidderID, ArrayList<String> losingBidders){
        this.nameOfSeller = nameOfSeller;
        this.sorino = sorino;
        this.price = price;
        this.timeAllocated = timeAllocated;
        this.timeInserted = timeInserted;
        this.sellerID = sellerID;
        this.listingID = listingID;
        this.highestBidderID = highestBidderID;
        this.losingBidders = losingBidders;
    }


    @Override
    public String nameOfSeller() {
        return this.nameOfSeller;
    }

    @Override
    public String nameOfItem() {
        return this.sorino.getName();
    }

    @Override
    public String descOfItem() {
        return "BASE HEALTH: " + sorino.getHealth(0) + "\n" +
                "BASE ENERGY: " + sorino.getEnergy(0);
    }

    @Override
    public int price() {
        return this.price;
    }

    @Override
    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean checkHasItem(Profile profile) {
        return profile.getSorinoAsList().contains(this.sorino);
    }

    @Override
    public long timeAllocated() {
        return this.timeAllocated;
    }

    @Override
    public long timePutOnMarket() {
        return this.timeInserted;
    }

    @Override
    public String sellerID() {
        return this.sellerID;
    }

    @Override
    public String listingID() {
        return this.listingID;
    }

    @Override
    public String highestBidderID() {
        return this.listingID;
    }

    @Override
    public void setHighestBidderID(String newID) {
        this.losingBidders.add(highestBidderID());
        this.highestBidderID = newID;
    }

    @Override
    public void awardItem(Profile profile) {
        profile.addSorino(this.sorino);
    }

    @Override
    public ArrayList<String> lostBidderIds() {
        return this.losingBidders;
    }

    @Override
    public Document toDocument() {
        return new Document("nameOfSeller", this.nameOfSeller)
                .append("sorino", this.sorino.getName())
                .append("price", this.price)
                .append("timeAllocated", this.timeAllocated)
                .append("timeInserted", this.timeInserted)
                .append("sellerID", this.sellerID)
                .append("listingID", this.listingID)
                .append("highestBidderID", this.highestBidderID)
                .append("losingBidders", this.losingBidders);
    }

    @Override
    public Listing toListing(Document document) {
        Sorino sorino;
        try {
            sorino = Sorino.AllSorino.getSorino(document.getString("sorino"));
        } catch (Exception ignored){return null;}
        return new SorinoListing(document.getString("nameOfSeller"), sorino, document.getInteger("price"),
                document.getLong("timeAllocated"), document.getLong("timeInserted"),
                document.getString("sellerID"), document.getString("listingID"),
                document.getString("highestBidderID"),
                new ArrayList<>(document.getList("losingBidders", String.class)));
    }
}
