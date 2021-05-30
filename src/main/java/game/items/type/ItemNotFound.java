package game.items.type;

public class ItemNotFound extends Exception{
    String item;

    public ItemNotFound(String item){
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "ItemNotFound{" +
                "item='" + item + '\'' +
                '}';
    }
}
