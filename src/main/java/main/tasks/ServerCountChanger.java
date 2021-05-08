package main.tasks;

import main.MainBot;
import main.Task;
import net.dv8tion.jda.api.JDA;
import okhttp3.*;
import org.discordbots.api.client.DiscordBotListAPI;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpClient;

public class ServerCountChanger implements Task {
    DiscordBotListAPI botListAPI;
    JDA jda;

    public ServerCountChanger(DiscordBotListAPI botListAPI, JDA jda){
        this.botListAPI = botListAPI;
        this.jda = jda;
    }

    @Override
    public void doTask() {
        botListAPI.setStats(jda.getGuilds().size());

        try {
            OkHttpClient httpClient = new OkHttpClient().newBuilder().build();
            RequestBody requestBody = new FormBody.Builder()
                    .add("guildCount", "" + jda.getGuilds().size())
                    .build();
            Request request = new Request.Builder()
                    .url("https://discord.bots.gg/api/v1/bots/" +
                            "764566349543899149/stats")
                    .addHeader("Authorization", MainBot.dbots)
                    .post(requestBody)
                    .method("POST", requestBody)
                    .build();
            Response response = httpClient.newCall(request).execute();
            MainBot.logger.info("Response code from dbots: " + response.code());
            response.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void printTaskStatus() {
        MainBot.logger.info("Changed server number to: " + jda.getGuilds().size());
    }

}
