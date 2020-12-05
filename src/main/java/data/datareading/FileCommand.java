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
                "AWARD_COINS guildID userID: awards (username) 100,000 coins\n" +
                "AWARD_SORINO guildID userID sorino: awards (sorino) to (userID)\n" +
                "DELETE_ACCOUNT guildID userID: deletes account in a certain guild\n" +
                "DELETE_SPEC_ACCOUNT userID: deletes all instances of an account in SorinoRPG" +
                "DELETE_GUILD guildID: deletes the guild specified\n" +
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

        String[] directories = new File("/Users/Emman/IdeaProjects/SorinoRPG/SorinoRPG-Codebase" +
                "/src/main/java/data/files").list((current, name) -> new File(current, name).isDirectory());
        for (String directory : directories) {
            File guild = new File(directory);
            users += guild.listFiles((file) -> file.getName().startsWith("@@")).length;
        }

        System.out.println("USERS: " + users);
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
    DELETE_ACCOUNT((input) -> {
        System.out.println("Deleting account...\n");
    })
    ,
    DELETE_SPEC_ACCOUNT((input) -> {
        System.out.println("Deleting specific account...\n");
    })
    ,
    DELETE_GUILD((input) -> {
        System.out.println("Deleting guild...\n");
    })
    ,
    UPDATE((input) -> {
        System.out.println("Mass game update... Approximate time: 1 minute\n");
    })
    ,
    SEE_USER((input) -> {
        System.out.println("Displaying user...\n");
    })
    ,
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
                add(FileCommand.DELETE_ACCOUNT);
                add(FileCommand.DELETE_GUILD);
                add(FileCommand.DELETE_SPEC_ACCOUNT);
                add(FileCommand.SEE_USER);
                add(FileCommand.UPDATE);
            }
        };
        for (FileCommand fc : cmdList) {
            if (fc.toString().equals(command.trim()))
                return fc;
        }

        return FileCommand.ERROR;
    }
}
