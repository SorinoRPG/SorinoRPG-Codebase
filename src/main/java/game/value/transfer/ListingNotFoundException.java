package game.value.transfer;

public class ListingNotFoundException extends Exception{
    String id;

    ListingNotFoundException(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
