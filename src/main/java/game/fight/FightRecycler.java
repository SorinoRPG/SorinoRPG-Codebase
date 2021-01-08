package game.fight;

import data.logging.Logger;
import main.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
public class FightRecycler implements Task {
    int filesDeleted = 0;
    long space = 0;
    @Override
    public void doTask() {
        File[] directories = new File("/db")
                .listFiles((current, name) -> new File(current, name).isDirectory());
        for (File dir : directories){
            File[] fights = new File(dir.toPath() + "/fights").listFiles();
            for (File fightFile : fights){
                try {
                    if((System.currentTimeMillis() - fightTime(fightFile) > 600000)){
                        space += Files.size(fightFile.toPath());
                        if(fightFile.delete()) filesDeleted++;
                    }
                } catch (IOException ignored){}
                catch (ClassNotFoundException e){
                    System.out.println("Error in deserialization\n\n" + Logger.exceptionAsString(e));
                }
            }
        }
    }

    @Override
    public void printTaskStatus() {
        System.out.println("Finished recycling.\n" +
                "Files deleted: " + filesDeleted + "\n" +
                "Space saved: " + space / 1024 +"KB");
        space = 0;
        filesDeleted = 0;
    }

    private long fightTime(File fight) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream =
                new ObjectInputStream(
                        new FileInputStream(
                                fight
                        )
                );
        Fight mainFight = (Fight) objectInputStream.readObject();
        objectInputStream.close();
        return mainFight.fightTime;
    }
}
