package data.datareading;


import data.Profile;
import game.SorinoNotFoundException;
import game.characters.Sorino;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

enum FileCommand{
    CMD(ignored -> {
        System.out.println(
                "GUILDS: returns the number of guilds\n" +
                "USERS: returns the number of users\n" +
                "AWARD_COINS guildID userID coins: awards (username)  coins\n" +
                "AWARD_SORINO guildID userID sorino: awards (sorino) to (userID)\n" +
                "UPDATE: updates every account to the latest version\n" +
                "SEE_USER: guildID userID: displays the users info\n" +
                        "Enter in correct syntax."
        );
    }),
    GUILDS(ignored -> {
        System.out.println("Counting guilds...");

        String[] directories = new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                "/src/main/java/data/files").list((current, name) -> new File(current, name).isDirectory());
        System.out.println("GUILDS: " + directories.length);
    }),
    USERS(ignored -> {
        int users = 0;
        System.out.println("Counting users....");

        File[] directories = new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                "/src/main/java/data/files").listFiles((current, name) -> new File(current, name).isDirectory());
        for (File directory : directories) users += directory.listFiles((dir, name) -> name.endsWith(".txt")).length;
        System.out.println("USERS: " + users);
    }),
    AWARD_COINS(input -> {
        try {
            String guildID = input.substring(0, input.indexOf(" "));
            String userID = input.substring(input.indexOf(" ")+1, input.lastIndexOf(" "));
            int coins = Integer.parseInt(input.substring(input.lastIndexOf(" ")+1));

            Profile profile = Profile.readFromFile(new File(
                    "/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase/src/main/java/data/files/" +
                            guildID + "/@@" + userID + ".txt"));
            profile.setCoins(coins);
            profile.recreateProfile();
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println("Could not find profile!");
            ioException.printStackTrace();
        }
    }),
    AWARD_SORINO(input -> {
        try {
            String guildID = input.substring(0, input.indexOf(" "));
            String userID = input.substring(input.indexOf(" ")+1, input.lastIndexOf(" "));
            String sorino = input.substring(input.lastIndexOf(" ")+1);
            Profile profile = Profile.readFromFile(new File(
                    "/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase/src/main/java/data/files/" +
                            guildID + "/@@" + userID + ".txt"));
            profile.addSorino(Sorino.AllSorino.getSorino(sorino));
            profile.recreateProfile();
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println("Could not find profile!");
        } catch (SorinoNotFoundException e) {
            System.out.println("Sorino does not exist!");
        }
    }),
    //TODO
    UPDATE((input) -> {
        System.out.println("Mass game update... Approximate time: 1 minute\n");
    }),
    SEE_USER((input) -> {
        System.out.println("Displaying user...\n");
        System.out.println(input);

        String guildID = input.substring(0, input.indexOf(" "));
        String userID = input.substring(input.lastIndexOf(" ")+1);
        try {
            Profile profile = Profile.readFromFile(
                    new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                    "/src/main/java/data/files/" +
                    guildID + "/@@" + userID + ".txt"));
            System.out.println(profile.toString());
        } catch (ClassNotFoundException | IOException ioException) {
            System.out.println("File was not found!");
        }
    }),
    ERROR((input) -> {
        System.out.println(input + "\n was an invalid command, assuming process end...");
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
                add(FileCommand.SEE_USER);
                add(FileCommand.UPDATE);
            }
        };
        for (FileCommand fc : cmdList) {
            if (command.contains(fc.toString()))
                return fc;
        }

        return FileCommand.ERROR;
    }
}
