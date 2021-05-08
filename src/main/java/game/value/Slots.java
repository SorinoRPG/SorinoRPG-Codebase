package game.value;

import data.Profile;
import game.characters.Sorino;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;

public class Slots {
    interface SlotInfo {
        String img();
        void action(Profile profile, int stake, TextChannel channel);
    }
    public enum Slot implements SlotInfo{
        CHERRIES(){
            @Override
            public String img() {
                return "https://cdn.discordapp.com/attachments/784891397584060459/793638200337760276/Cherries.jpg";
            }

            @Override
            public void action(Profile profile, int stake, TextChannel channel) {
                channel.sendMessage(profile.getName() + " got 3 Cherries and will receive " +
                        stake * 1.5 + " coins!").queue();
                    profile.setCoins((int) (stake * 1.5));
            }
        },
        BANANAS(){
            @Override
            public String img() {
                return "https://cdn.discordapp.com/attachments/784891397584060459/793638094393573376/Bananas.png";
            }

            @Override
            public void action(Profile profile, int stake, TextChannel channel) {
                channel.sendMessage(profile.getName() + " got 3 Bananas and will receive " +
                        stake * 1.25 + " coins!").queue();
                profile.setCoins((int) (stake * 1.25));
            }
        },
        BLUE_BEARS(){
            @Override
            public String img() {
                return "https://cdn.discordapp.com/attachments/784891397584060459/793637993767763988/Blue_Bear.jpg";
            }

            @Override
            public void action(Profile profile, int stake, TextChannel channel) {
                channel.sendMessage(profile.getName() + " got 3 Blue Bears and will receive " +
                        stake * 2 + " coins!").queue();
                profile.setCoins(stake * 2);
            }
        },
        BEARS(){
            @Override
            public String img() {
                return "https://cdn.discordapp.com/attachments/784891397584060459/793637909513240618/Bear.jpg";
            }

            @Override
            public void action(Profile profile, int stake, TextChannel channel) {
                channel.sendMessage(profile.getName() + " got 3 Bears and will receive " +
                        stake * 1.75 + " coins!").queue();
                profile.setCoins((int) (stake * 1.75));
            }
        },
        SAPPHIRES(){
            @Override
            public String img() {
                return "https://cdn.discordapp.com/attachments/784891397584060459/793638488918196264/Sapphire.jpg";
            }

            @Override
            public void action(Profile profile, int stake, TextChannel channel) {
                channel.sendMessage(profile.getName() + " got 3 Sapphires and will receive " +
                        stake * 4 + " coins!").queue();
                profile.setCoins(stake * 4);
            }

        },
        STARS(){
            @Override
            public String img() {
                return "https://cdn.discordapp.com/attachments/784891397584060459/793649794727804968/Star.jpg";
            }

            @Override
            public void action(Profile profile, int stake, TextChannel channel) {
                Sorino sorino = Sorino.AllSorino.getRandom();
                channel.sendMessage(profile.getName() + " received a Sorino from the Slot Machine!\n" +
                        "You got a: " + sorino.getName()).queue();
                profile.addSorino(sorino);
            }
        },
        PENCIL(){
            @Override
            public String img(){
                return "https://cdn.discordapp.com/attachments/784891397584060459/793819581386194954/Pencil.jpg";
            }

            @Override
            public void action(Profile profile, int stake, TextChannel channel){
                channel.sendMessage(profile.getName() + " gained 1000" +
                        " coins!").queue();
                profile.setCoins(1000);
            }
        },
        APPLE(){
            @Override
            public String img(){
                return "https://cdn.discordapp.com/attachments/784891397584060459/793819598465925130/Apple.png";
            }

            @Override
            public void action(Profile profile, int stake, TextChannel channel){
                channel.sendMessage(profile.getName() + " gained 1000" +
                        " coins!").queue();
                profile.setCoins(1000);
            }
        },
        WATERMELON(){
            @Override
            public String img(){
                return "https://cdn.discordapp.com/attachments/784891397584060459/793819603020152882/Watermelon.jpg";
            }

            @Override
            public void action(Profile profile, int stake, TextChannel channel){
                channel.sendMessage(profile.getName() + " gained 1000" +
                        " coins!").queue();
                profile.setCoins(1000);
            }
        }
    }

    String[] slotImages;

    public Slots(){
        ArrayList<String> slotImages = new ArrayList<>();
        slotImages.add(Slot.STARS.img());
        slotImages.add(Slot.SAPPHIRES.img());

        for(int i = 0; i != 10; i++)
            slotImages.add(Slot.BEARS.img());
        for(int i = 0; i != 60; i++)
            slotImages.add(Slot.CHERRIES.img());
        for(int i = 0; i != 79; i++)
            slotImages.add(Slot.BANANAS.img());
        for(int i = 0; i < 22; i++)
            slotImages.add(Slot.BLUE_BEARS.img());

        this.slotImages = slotImages.toArray(new String[0]);
    }

    public String[] random(){
        Random random = new Random();

        return new String[] {slotImages[random.nextInt(slotImages.length)],
                slotImages[random.nextInt(slotImages.length)],
                slotImages[random.nextInt(slotImages.length)]};
    }

    public static boolean checkSame(String[] slots){
        String img = slots[0];

        return img.equals(slots[1]) && img.equals(slots[2]);
    }

    public Slot getInfo(String img){
        ArrayList<Slot> list = new ArrayList<>(EnumSet.allOf(Slots.Slot.class));

        for(Slot slot : list){
            if(slot.img().equals(img)) return slot;
        }
        return null;
    }
}
