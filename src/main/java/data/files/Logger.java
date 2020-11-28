package data.files;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    String info;

    public Logger(String loggingInfo){
        this.info = loggingInfo;
    }

    public static String exceptionAsString(Exception exc){
        StringWriter sw = new StringWriter();
        exc.printStackTrace(new PrintWriter(sw));

        return sw.toString();
    }
    public void logAction() throws IOException {
        FileWriter fileWriter = new FileWriter(
                new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase/" +
                        "src/main/java/data/files/ActionLogs.log")
        , true);
        fileWriter.write(this.info + ">>Happened at>> " +
                DateTimeFormatter
                        .ofPattern("dd/MM/yyyy HH:mm:ss")
                        .format(LocalDateTime.now()) + "\n\n");
        fileWriter.close();
    }
    public void logError() throws IOException {
        FileWriter fileWriter = new FileWriter(
                new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase/" +
                        "src/main/java/data/files/ErrorLogs.log")
        , true);
        fileWriter.write(this.info + ">>Happened at>> " +
                DateTimeFormatter
                        .ofPattern("dd/MM/yyyy HH:mm:ss")
                        .format(LocalDateTime.now()) + "\n\n");
        fileWriter.close();
    }
}
