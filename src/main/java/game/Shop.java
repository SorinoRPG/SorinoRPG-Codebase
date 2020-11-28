package game;

import java.util.ArrayList;
import java.util.Arrays;

public class Shop {
    static ArrayList<Item> shop;

    public void createShop(){
        shop = new ArrayList<>(Arrays.asList(new Item("Revive",
                                                    1000, "Revives a specific Ignatiamon"),
                                             new Item("Ignaball",
                                                     200, "Adds five more slots to your Ignatiamon"),
                                             new Item("InstaPunch",
                                                     300, "Use an InstaPunch and make opponent lose 50HP!")));
    }

    public static String seeShop(){
        StringBuilder shopStr = new StringBuilder();

        for (Item item : shop) {
            shopStr.append(item.getName())
                    .append(", ")
                    .append(item.getPrice())
                    .append(", ").append(item.getDesc()).append("\n");
        }
        return String.valueOf(shopStr);
    }

    private static class Item{
        private final String name;
        private final int price;
        private final String desc;

        public String getName() {
            return name;
        }
        public String getDesc(){
            return desc;
        }

        public int getPrice() {
            return price;
        }

        Item(String name, int price, String desc){
            this.name = name;
            this.price = price;
            this.desc = desc;
        }

    }
}
