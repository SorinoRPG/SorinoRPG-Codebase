package main.tasks;

import data.logging.Logger;
import main.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;



public class LogRecycler implements Task {
    static java.util.logging.Logger logger = java.util.logging.Logger.getAnonymousLogger();

    long spaceSaved;
    String errors = "";

    @Override
    public void doTask() {
        try {
            spaceSaved += Files.size(Paths.get("/db/ActionLogs.log")) +
                Files.size(Paths.get("/db/ErrorLogs.log"));

            FileWriter actionLog = clearLogs("ActionLogs");
            clearLogs("ErrorLogs");

            Logger log = new Logger("Cleared Logs ");
            log.logAction(actionLog);
            actionLog.close();
        } catch (IOException e){
            errors += Logger.exceptionAsString(e) + "\n";
        }
    }

    private FileWriter clearLogs(String log) throws IOException {
        FileWriter fileWriter = new FileWriter(
                new File("/db/" + log + ".log"), false
        );
        fileWriter.write("");
        return fileWriter;
    }

    @Override
    public void printTaskStatus() {
        logger.info("Completed log clear | Approximate space saved " + spaceSaved + "B");
        System.err.println("EXCEPTIONS:\n" +
                errors);
        errors = "";
        spaceSaved = 0;
    }
}
