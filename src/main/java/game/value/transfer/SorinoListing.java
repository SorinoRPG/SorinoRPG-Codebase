package game.value.transfer;

import data.Profile;
import data.ProfileNotFoundException;
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
    String highestBidderName;
    ArrayList<String> losingBidders;

    public SorinoListing(User seller, Sorino sorino, int price, long timeAllocated){
        this.nameOfSeller = seller.getAsTag();
        this.sorino = sorino;
        this.price = price;
        this.timeAllocated = timeAllocated;
        this.timeInserted = System.currentTimeMillis();
        this.sellerID = seller.getId();
        this.listingID = Listing.createListingID();
        this.highestBidderID = seller.getId();
        this.highestBidderName = seller.getAsTag();

        this.losingBidders = new ArrayList<>();
        if(seller.isBot()) return;
        this.losingBidders.add(seller.getId());
    }

    private SorinoListing(String nameOfSeller, Sorino sorino, int price, long timeAllocated, long timeInserted,
                          String sellerID, String listingID, String highestBidderID, String highestBidderName,
                          ArrayList<String> losingBidders){
        this.nameOfSeller = nameOfSeller;
        this.sorino = sorino;
        this.price = price;
        this.timeAllocated = timeAllocated;
        this.timeInserted = timeInserted;
        this.sellerID = sellerID;
        this.listingID = listingID;
        this.highestBidderID = highestBidderID;
        this.highestBidderName = highestBidderName;
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
        return "CURRENT HIGHEST BIDDER: " + this.highestBidderName + "\n" +
                "SELLER: " + nameOfSeller() + "\n" +
                "PRICE: " + this.price + "\n" +
                "LISTING ID: " + this.listingID + "\n" +
                "BASE HEALTH: " + sorino.getHealth(0) + "\n" +
                "BASE ENERGY: " + sorino.getEnergy(0) ;
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
    public boolean checkHasItem(User user) throws ProfileNotFoundException {
        if(user.isBot()) return true;
        Profile profile = Profile.getProfile(user);
        for(Sorino sorino : profile.getSorinoAsList())
            if(sorino.getName().equals(this.sorino.getName())) return true;
        return false;
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
        return this.highestBidderID;
    }

    @Override
    public String highestBidderName() {
        return this.highestBidderName;
    }

    @Override
    public void setHighestBidderID(User user) {
        this.losingBidders.add(highestBidderID());
        this.highestBidderID = user.getId();
        this.highestBidderName = user.getAsTag();
    }

    @Override
    public void awardItem(Profile profile) {
        profile.addSorino(this.sorino);
    }

    @Override
    public void removeItem(Profile profile) {
        profile.removeSorino(sorino);
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
                .append("highestBidderName", this.highestBidderName)
                .append("losingBidders", this.losingBidders);
    }


    public static SorinoListing toListing(Document document) {
        Sorino sorino = null;
        try {
            String sorinoStr = document.getString("sorino");
            sorinoStr = sorinoStr.substring(0, sorinoStr.indexOf(":"));
            sorino = Sorino.AllSorino.getSorino(sorinoStr);
        } catch (Exception ignored){}
        return new SorinoListing(document.getString("nameOfSeller"), sorino, document.getInteger("price"),
                document.getLong("timeAllocated"), document.getLong("timeInserted"),
                document.getString("sellerID"), document.getString("listingID"),
                document.getString("highestBidderID"), document.getString("highestBidderName"),
                new ArrayList<>(document.getList("losingBidders", String.class)));
    }

    @Override
    public String toString() {
        return this.toDocument().toJson();
    }
}
