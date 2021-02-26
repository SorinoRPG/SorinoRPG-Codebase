package main.springboot.endpoint;

import data.Profile;
import data.ProfileStore;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import main.MainBot;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

@RestController
public class DBLHandler {
    @SuppressWarnings("unused")
    @RequestMapping(value = "webhook/dbl", consumes = "application/json", produces = "application/json",
                        method = RequestMethod.POST)
    public void onVote(@RequestHeader(value = "Authorization") String token, @RequestBody String payload){
        System.out.println(payload);

        if(!token.equals(MainBot.dbl_webhook)) return;

        JSONObject data = new JSONObject(payload);
        MainBot.getJda().retrieveUserById(data.getString("user")).queue(user ->{
            user.openPrivateChannel().queue(channel -> {
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setColor(0x000dff);

                        embedBuilder.setTitle("You have received 7,000 coins!");
                        embedBuilder.setDescription("Remember to vote in 12 hours again!");
                        channel.sendMessage(embedBuilder.build()).queue();
                    },
                    e -> System.out.println("Cannot contact: " + user.getId()));
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(
                        new FileInputStream(new File("/db/USERLIST.txt"))
                );
                @SuppressWarnings("unchecked")
                HashMap<String, ProfileStore> userList =
                        (HashMap<String, ProfileStore>) objectInputStream.readObject();
                objectInputStream.close();

                if (userList.containsKey(user.getId())) {
                    ProfileStore ps = userList.get(user.getId());
                    Profile p = Profile.storeToProfile(ps);

                    p.setCoins(7000);
                    p.recreateProfile();
                    return;
                }
            } catch (Exception fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            File[] directories = new File("/db").listFiles((current, name) ->
                    new File(current, name).isDirectory());
            for(File dir : directories){
                File potUserFile =
                        new File("/db/" + dir.getName() + "/@@" + user.getId() + ".txt");
                if(Arrays.asList(dir.listFiles()).contains(potUserFile)){
                    try {
                        Profile profile = Profile.readFromFile(potUserFile);
                        profile.setCoins(7000);
                        profile.baseRecreate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
