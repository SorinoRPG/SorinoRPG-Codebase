package main.tasks;

import main.MainBot;
import main.Task;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Random;

public class StatusChanger implements Task {
    JDA jda;
    private Activity activity;



    static Activity[] phrases = {
            Activity.watching("Thanos destroy half the universe"),
            Activity.listening("ear-exploder 3000"),
            Activity.playing("with fire"),
            Activity.watching("people gambling away their lives"),
            Activity.competing("a tournament for low-lives"),
            Activity.watching("this server die"),
            Activity.watching("Phineas & Ferb"),
            Activity.watching("a PIRATED MOVIE?!"),
            Activity.watching("2020 on repeat"),
            Activity.listening("the sweet screams of children"),
            Activity.watching("Discord servers die"),
            Activity.watching("You. Always."),
            Activity.listening("All I wanna do is gunshot gunshot cash register"),
            Activity.playing("Monke punch Lizard"),
            Activity.watching("someone special in this server")
    };

    public StatusChanger(JDA jda){
        this.jda = jda;
    }

    @Override
    public void doTask() {

        activity = phrases[new Random().nextInt(phrases.length)];
        jda.getPresence().setActivity(activity);
    }

    @Override
    public void printTaskStatus() {
        MainBot.logger.info("Changed status to: " + activity.getName());
    }
}
