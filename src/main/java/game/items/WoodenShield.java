package game.items;

import game.items.type.Capability;
import game.items.type.Item;
import org.bson.Document;

import java.util.ArrayList;

public class WoodenShield implements Item {
    String name = "Wooden Shield";
    int duplicity = 1;
    Capability capability = Capability.LOW_WEAPON;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDuplication() {
        return duplicity;
    }

    @Override
    public void remove() {
        this.duplicity--;
    }

    @Override
    public ArrayList<Item> getListOfItem() {
        ArrayList<Item> items = new ArrayList<>();

        for(int i = 0; i < duplicity; i++)
            items.add(this);

        return items;
    }

    @Override
    public void add() {
        this.duplicity++;
    }

    @Override
    public void initialize(int duplication) {
        this.duplicity = duplication;
    }

    @Override
    public Document toDocument() {
        return new Document("name", this.getName())
                .append("duplication", this.duplicity);
    }

    @Override
    public Capability getCapability() {
        return capability;
    }


    @Override
    public String toString() {
        return name;
    }
}
