package game.items.type;

import game.items.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;

public interface Item {
    String getName();
    int getDuplication();
    void remove();
    ArrayList<Item> getListOfItem();
    void add();
    void initialize(int duplication);
    Document toDocument();
    Capability getCapability();
    static Item toItem(Document document) throws ItemNotFound {
        Item item = AllItems.getItem(document.getString("name"));
        item.initialize(document.getInteger("duplication"));

        return item;
    }

    interface GetItem {
        Item getItem();
    }

    @SuppressWarnings("unused")
    enum AllItems implements GetItem{
        GRENADE {
            @Override
            public Item getItem() {
                return new Grenade();
            }
        },
        ANDROMEDAN_SHIELD {
            @Override
            public Item getItem() {
                return new AndromedanShield();
            }
        },
        BAZOOKA {
            @Override
            public Item getItem() {
                return new Bazooka();
            }
        },
        CODE_CRACKER {
            @Override
            public Item getItem() {
                return new CodeCracker();
            }
        },
        DUMPTRUCK {
            @Override
            public Item getItem() {
                return new Dumptruck();
            }
        },
        ENERGY_FIELD {
            @Override
            public Item getItem() {
                return new EnergyField();
            }
        },
        FERRARI {
            @Override
            public Item getItem() {
                return new Ferrari();
            }
        },
        HANDGUN {
            @Override
            public Item getItem() {
                return new Handgun();
            }
        },
        HEAVY_SNIPER {
            @Override
            public Item getItem() {
                return new HeavySniper();
            }
        },
        LAPTOP {
            @Override
            public Item getItem() {
                return new Laptop();
            }
        },
        M4A1 {
            @Override
            public Item getItem() {
                return new M4A1();
            }
        },
        MAKESHIFT_DEVICE {
            @Override
            public Item getItem() {
                return new MakeshiftDevice();
            }
        },
        MOLOTOV_COCKTAIL {
            @Override
            public Item getItem() {
                return new MolotovCocktail();
            }
        },
        MOWV {
            @Override
            public Item getItem() {
                return new MOWV();
            }
        },
        WOODEN_SHIELD {
            @Override
            public Item getItem() {
                return new WoodenShield();
            }
        };

        public static Item getItem(String str) throws ItemNotFound {
            ArrayList<AllItems> allItems = new ArrayList<>(EnumSet.allOf(AllItems.class));
            for(AllItems allItems1 : allItems){
                Item item = allItems1.getItem();
                if(item.getName().equals(str)) return item;
            }
            throw new ItemNotFound(str);
        }

        public static Item randomItem() {
            ArrayList<Item.AllItems> Item = new ArrayList<>(EnumSet.allOf(Item.AllItems.class));
            ArrayList<Item> randomItem = new ArrayList<>();

            for (Item.AllItems allItem : Item) {
                Item currItem = allItem.getItem();
                for(int i = 0; i < currItem.getCapability().getRarity(); i++)
                    randomItem.add(currItem);
            }

            return randomItem.get(new Random().nextInt(randomItem.size()));
        }
    }

}
