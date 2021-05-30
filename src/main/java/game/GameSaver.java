package game;

import game.fight.Fight;
import game.heist.Heist;
import main.MainBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.jodah.expiringmap.ExpiringMap;

import java.util.concurrent.TimeUnit;

public class GameSaver {
    public static ExpiringMap<String, Fight> fightMap = ExpiringMap.builder()
            .maxSize(10000000)
            .expiration(1, TimeUnit.HOURS)
            .expirationListener((k, v) -> {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(0x00dff);
                embed.setTitle("This fight has gone on for 1 hour.");
                embed.setDescription("The fight in this channel has gone on too long, therefore it was deleted.");
                embed.setFooter("All user-data will stay as it was before the fight.");

                //noinspection ConstantConditions
                MainBot.getJda().getTextChannelById((String) k).sendMessage(embed.build()).queue();
            }).build();
    public static ExpiringMap<String, Heist> heistMap = ExpiringMap.builder()
            .maxSize(10000000)
            .expiration(1, TimeUnit.HOURS)
            .expirationListener((k, v) -> {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(0x00dff);
                embed.setTitle("This heist has gone on for 1 hour.");
                embed.setDescription("The heist in this channel has gone on too long, therefore it was deleted.");
                embed.setFooter("All user-data will stay as it was before the heist.");

                //noinspection ConstantConditions
                MainBot.getJda().getTextChannelById((String) k).sendMessage(embed.build()).queue();
            }).build();

    public static boolean exists(String id){
        int exists = 0;
        exists += (fightMap.containsKey(id)) ? 1 : 0;
        exists += (heistMap.containsKey(id)) ? 1 : 0;
        return exists > 0;
    }
}
