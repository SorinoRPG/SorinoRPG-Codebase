package game.heist;

import main.Task;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class HeistRecycler implements Task {
    int deletedHeists = 0;
    @Override
    public void doTask() {
        String[] directories = new File("/db").list((current, name) ->
                new File(current, name).isDirectory());

        for(String dir : directories){
            try {
                File[] heists = new File(dir + "/heist").listFiles();
                if(heists == null) continue;
                for(File heist : heists){
                    long time = time(heist);
                    if((System.currentTimeMillis() - time) > 600000){
                        FileUtils.forceDelete(heist);
                        deletedHeists++;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private long time(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(file)
        );
        Heist heist = (Heist) objectInputStream.readObject();
        objectInputStream.close();

        return heist.time();
    }

    @Override
    public void printTaskStatus() {
        System.out.println("HEISTS DELETED: " + deletedHeists);
    }
}
