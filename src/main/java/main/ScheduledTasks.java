package main;

import java.awt.*;
import java.util.TimerTask;

public class ScheduledTasks extends TimerTask {
    private final Task[] tasks;

    ScheduledTasks(Task ... tasks){
        this.tasks = tasks;
    }

    @Override
    public void run() {
        for(Task task : tasks){
            task.doTask();
            task.printTaskStatus();
        }
        System.out.println("Tasks completed: " + tasks.length  + "\n" +
                "Current time: ");

//        try {
//            SystemTray tray = SystemTray.getSystemTray();
//
//            TrayIcon trayIcon = new TrayIcon("Tray Demo");
//            trayIcon.setImageAutoSize(true);
//            trayIcon.setToolTip("System tray icon demo");
//            tray.add(trayIcon);
//
//            trayIcon.displayMessage("A data reset for SorinoRPG occurred!",
//                    "Please open in the command line", TrayIcon.MessageType.INFO);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
    }
}
