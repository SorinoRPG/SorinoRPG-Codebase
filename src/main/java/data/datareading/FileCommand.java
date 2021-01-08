package data.datareading;


import data.Profile;
import data.ProfileStore;
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
                "NAMES: displays all users with names\n" +
                "AWARD_COINS guildID userID coins: awards (username)  coins\n" +
                "AWARD_SORINO guildID userID sorino: awards (sorino) to (userID)\n" +
                "UPDATE: updates every account to the latest version\n" +
                "SEE_USER: guildID userID: displays the users info\n" +
                "INCREMENT_XP: guildID userID XP: increments user XP\n"
        );
    }),
    GUILDS(ignored -> {
        System.out.println("Counting guilds...");

        String[] directories = new File("/db").list((current, name) -> new File(current, name).isDirectory());
        System.out.println("GUILDS: " + directories.length);
    }),
    USERS(ignored -> {
        int users = 0;
        System.out.println("Counting users....");

        File[] directories = new File("/db").listFiles((current, name) -> new File(current, name).isDirectory());
        for (File directory : directories) users += directory.listFiles((dir, name) -> name.startsWith("@@")).length;
        System.out.println("USERS: " + users);
    }),
    AWARD_COINS(input -> {
        try {
            String guildID = input.substring(0, input.indexOf(" "));
            String userID = input.substring(input.indexOf(" ")+1, input.lastIndexOf(" "));
            int coins = Integer.parseInt(input.substring(input.lastIndexOf(" ")+1));

            Profile profile = Profile.readFromFile(new File(
                    "/db/" +
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
                    "/db/" +
                            guildID + "/@@" + userID + ".txt"));
            profile.addSorino(Sorino.AllSorino.getSorino(sorino));
            profile.recreateProfile();
        } catch (IOException | ClassNotFoundException ioException) {
            System.out.println("Could not find profile!");
            ioException.printStackTrace();
        } catch (SorinoNotFoundException e) {
            System.out.println("Sorino does not exist!");
        }
    }),
    UPDATE((input) -> {
        System.out.println("Mass game update...\n");
        long currTime = System.currentTimeMillis();

        File[] directories = new File("/db").listFiles((current, name) -> new File(current, name).isDirectory());

        for(File dir : directories){
            File updateDir = new File(dir.getPath() + "/UPDATE_STORE");
            File[] updateFiles = updateDir.listFiles((direc, name) -> name.startsWith("$"));
            for (File updateFile : updateFiles) {
                try {
                    ProfileStore profileStore = ProfileStore.readFromFile(updateFile);
                    Profile profile = Profile.storeToProfile(profileStore);
                    profile.recreateProfile();
                    System.out.println("Successfully updated: " + profile.getName());
                } catch (Exception exc) {
                    System.out.println("There was an error in updating...");
                    exc.printStackTrace();
                }
            }
        }
        System.out.println("Successfully ran mass update: " + (System.currentTimeMillis() - currTime) + " ms");
    }),
    SEE_USER((input) -> {
        System.out.println("Displaying user...\n");
        System.out.println(input);

        String guildID = input.substring(0, input.indexOf(" "));
        String userID = input.substring(input.lastIndexOf(" ")+1);
        try {
            Profile profile = Profile.readFromFile(
                    new File("/db/" +
                    guildID + "/@@" + userID + ".txt"));
            System.out.println("NAME: " + profile.getName() + "\n" + profile.toString());
        } catch (ClassNotFoundException | IOException ioException) {
            System.out.println("File was not found!");
            ioException.printStackTrace();
        }
    }),
    INCREMENT_XP(input -> {
        String guildID = input.substring(0, input.indexOf(" "));
        String userID = input.substring(input.indexOf(" ")+1, input.lastIndexOf(" "));
        int increment =
                Integer.parseInt(input.substring(input.lastIndexOf(" ")+1));

        try {
            Profile profile = Profile.readFromFile(
                    new File("/db/" +
                            guildID + "/@@" + userID + ".txt"));
            profile.silentXPIncrement(increment);
        } catch (ClassNotFoundException | IOException ioException) {
            System.out.println("File was not found!");
            ioException.printStackTrace();
        }
    }),
    NAMES(ignored -> {
        File[] directories = new File("/db").listFiles((current, name) -> new File(current, name).isDirectory());

        for(File dir : directories){
            File[] files = dir.listFiles((direc, name) -> name.startsWith("@@"));
            for (File file : files) {
                try {
                    Profile profile = Profile.readFromFile(file);
                    System.out.println(profile.getName());
                } catch (Exception exc) {
                    System.out.println("Reading");
                    exc.printStackTrace();
                }
            }
        }
    }),
    ERROR((input) -> {
        System.out.println(input + " BAD COMMAND");
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
                add(FileCommand.INCREMENT_XP);
                add(FileCommand.NAMES);
            }
        };
        for (FileCommand fc : cmdList) {
            if (command.contains(fc.toString()))
                return fc;
        }

        return FileCommand.ERROR;
    }
}
