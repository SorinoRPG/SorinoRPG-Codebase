package main.springboot.endpoint;

import data.Profile;
import data.ProfileNotFoundException;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import main.MainBot;

@SuppressWarnings("unused")
@RestController
public class DBLHandler {
    @RequestMapping(value = "webhook/dbl", consumes = "application/json", produces = "application/json",
                        method = RequestMethod.POST)
    public void onVote(@RequestHeader(value = "Authorization") String token, @RequestBody String payload){
        MainBot.logger.info(payload);

        if(!token.equals(MainBot.dbl_webhook)) return;

        JSONObject data = new JSONObject(payload);
        MainBot.getJda().retrieveUserById(data.getString("user")).queue(user ->{

            try{
                Profile userProfile = Profile.getProfile(user);
                userProfile.setCoins(7000);
                user.openPrivateChannel().queue(channel -> {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setColor(0x000dff);

                            embedBuilder.setTitle("Thank you for voting!");
                            embedBuilder.setDescription("You have received 7,000 coins");
                            embedBuilder.setFooter("Remember to vote again in 12 hours!");
                            channel.sendMessage(embedBuilder.build()).queue();
                        },
                        e -> System.out.println("Cannot contact: " + user.getId()));
            } catch(ProfileNotFoundException e){
                user.openPrivateChannel().queue(channel -> {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setColor(0x000dff);

                            embedBuilder.setTitle("Thank you for voting!");
                            embedBuilder.setDescription("You have not received your coins as you do not have a profile!");
                            embedBuilder.setFooter("Remember to make a profile and vote in 12 hours!");
                            channel.sendMessage(embedBuilder.build()).queue();
                        },
                        e1 -> System.out.println("Cannot contact: " + user.getId()));
            }
        });
    }
}
