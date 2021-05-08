package main;

import net.dv8tion.jda.api.EmbedBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;

public class ScheduledTasks extends TimerTask {
    private final Task[] tasks;

    ScheduledTasks(Task ... tasks
    ){
        this.tasks = tasks;
    }

    @Override
    public void run() {
        for(Task task : tasks){
            task.doTask();
            task.printTaskStatus();
        }
        System.out.println("Tasks completed: " + tasks.length  + "\n" +
                "Current time: "  + DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm:ss")
                .format(LocalDateTime.now()));

        MainBot.getJda().retrieveUserById("672883208811184166").queue(user -> {
            user.openPrivateChannel().queue(channel -> {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(0x000dff);

                embedBuilder.setTitle("Database reset!");
                embedBuilder.setDescription("Please check to see what has happened");
                channel.sendMessage(embedBuilder.build()).queue();
            });
        });
    }
}
