package data.files;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    String info;
    FileWriter fileWriter;

    public Logger(String loggingInfo){
        this.info = loggingInfo;
    }

    public static String exceptionAsString(Exception exc){
        StringWriter sw = new StringWriter();
        exc.printStackTrace(new PrintWriter(sw));

        return sw.toString();
    }
    public void logAction() throws IOException {

        fileWriter = new FileWriter(
                new File("/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files/ActionLogs.log")
        , true);
        fileWriter.write(this.info + ">>Happened at>> " +
                DateTimeFormatter
                        .ofPattern("dd/MM/yyyy HH:mm:ss")
                        .format(LocalDateTime.now()) + "\n\n");
    }
    public void logError() throws IOException {

        fileWriter = new FileWriter(
                new File("/Users/Emman/IdeaProjects/Ignatiamon!/src/main/java/data/files/ErrorLogs.log")
        , true);
        fileWriter.write(this.info + ">>Happened at>> " +
                DateTimeFormatter
                        .ofPattern("dd/MM/yyyy HH:mm:ss")
                        .format(LocalDateTime.now()) + "\n\n");
    }
    public void closeLogger(){
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
