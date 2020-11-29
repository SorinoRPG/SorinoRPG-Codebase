package data.datareading;


import data.Profile;
import game.SorinoNotFoundException;
import game.characters.Sorino;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

enum FileCommand{
    CMD(ignored -> {
        System.out.println(
                "GUILDS: returns the number of guilds\n" +
                "USERS: returns the number of users\n" +
                "AWARD_COINS guildID-userID: awards (username) 100,000 coins\n" +
                "AWARD_SORINO guildID-userID sorino: awards (sorino) to (userID)"
        );
    }),
    GUILDS(ignored -> {
        List<File> files = new ArrayList<>(Arrays.asList(
                new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                        "/src/main/java/data/files").listFiles()));
        List<File> filteredList = files.parallelStream().filter((file) ->
                file.getPath().contains("&")).collect(Collectors.toList());

        System.out.println("GUILDS: " + filteredList.size());
    }),
    USERS(ignored -> {
        List<File> files = new ArrayList<>(Arrays.asList(
                new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                        "/src/main/java/data/files").listFiles()));
        List<File> filteredList = files.parallelStream().filter((file) ->
                file.getPath().contains("@@")).collect(Collectors.toList());

        System.out.println("USERS: " + filteredList.size());
    }),
    AWARD_COINS(input -> {
        try {
            Profile profile = Profile.readFromFile(new File(
                    "/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase/src/main/java/data/files/@@" +
                            input.substring(0, input.lastIndexOf(" ")) + ".txt"));
            profile.setCoins(Integer.parseInt(input.substring(input.lastIndexOf(" ")+1)));
            profile.recreateProfile();
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println("Could not find profile!");
        }
    }),
    AWARD_SORINO(input -> {
        try {
            Profile profile = Profile.readFromFile(new File(
                    "/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase/src/main/java/data/files/@@" +
                            input.substring(0, input.lastIndexOf(" ")) + ".txt"));
            profile.addSorino(Sorino.AllSorino.getSorino(input.substring(input.lastIndexOf(" ")+1)));
            profile.recreateProfile();
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println("Could not find profile!");
        } catch (SorinoNotFoundException e) {
            System.out.println("Sorino does not exist!");
        }
    }),
    ERROR((string) -> {
        System.out.println(string + "\n was an invalid command, assuming process end...");
        System.exit(0);
    });

    FileAction action;
    FileCommand(FileAction action){
        this.action = action;
    }

    static FileCommand getCmd(String command){
        ArrayList<FileCommand> cmdList = new ArrayList<>(){
            {
                add(FileCommand.CMD);
                add(FileCommand.GUILDS);
                add(FileCommand.USERS);
                add(FileCommand.AWARD_COINS);
                add(FileCommand.AWARD_SORINO);
            }
        };
        for (FileCommand fc : cmdList) {
            if (fc.toString().equals(command.trim()))
                return fc;
        }

        return FileCommand.ERROR;
    }
}
