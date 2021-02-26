package data.datareading;


import data.Profile;
import data.ProfileStore;
import data.logging.Logger;
import game.SorinoNotFoundException;
import game.characters.Sorino;
import game.characters.starter.Gray;
import game.value.Coins;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public enum FileCommand{
    CMD(ignored -> {
        System.out.println(
                "GUILDS: returns the number of guilds\n" +
                "USERS: returns the number of users\n" +
                "RATIO: returns server to user ratio\n" +
                "NAMES: displays all users with names\n" +
                "AWARD_COINS guildID userID coins: awards (username)  coins\n" +
                "AWARD_SORINO guildID userID sorino: awards (sorino) to (userID)\n" +
                "UPDATE: updates every account to the latest version\n" +
                "SEE_USER: guildID userID: displays the users info\n" +
                "INCREMENT_XP: guildID userID XP: increments user XP\n" +
                "CHECK_DEFECTS: searches for problems in DB\n" +
                "ACCOUNT_RESET guildID userID: Resets an account\n" +
                "DB_BACKUP: backs up the database"
        );
    }),
    GUILDS(ignored -> {
        System.out.println("Counting guilds...");

        String[] directories = new File("/db").list((current, name) -> new File(current, name).isDirectory());
        System.out.println("GUILDS: " + directories.length);
    }),
    RATIO(ignored -> {
        System.out.println("Calculating ratio...");
        int guilds = new File("/db").list((current, name) ->
                new File(current, name).isDirectory()).length;
        int users = 0;
        File[] directories = new File("/db").listFiles((current, name) ->
                new File(current, name).isDirectory());
        for (File directory : directories) users += directory.listFiles((dir, name) ->
                name.startsWith("@@")).length;

        System.out.printf("RATIO: %.2f %n", (float) users / guilds);
    }),
    USERS(ignored -> {
        int users = 0;
        System.out.println("Counting users....");

        File[] directories = new File("/db").listFiles((current, name) ->
                new File(current, name).isDirectory());
        for (File directory : directories)
            users += directory.listFiles((dir, name) -> name.startsWith("@@")).length;
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
        } catch (Exception ioException) {
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
        } catch (Exception ioException) {
            System.out.println("Could not find profile!");
            ioException.printStackTrace();
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
                    System.out.println("There was an error in updating: " + updateFile.getPath() + "\n");
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
                    //if(file.getName().startsWith("@@672883208811184166")) continue;
                    Profile profile = Profile.readFromFile(file);
                    System.out.println(profile.getName() + ": " +file.getPath());
                } catch (Exception exc) {
                    System.out.println("Reading");
                    exc.printStackTrace();
                }
            }
        }
    }),
    CHECK_DEFECTS(ignored -> {
        File[] directories = new File("/db").listFiles((current, name) ->
                new File(current, name).isDirectory());


        for(File dir : directories){
            File[] files = dir.listFiles((direc, name) -> name.startsWith("@@"));
            for (File file : files) {
                try {
                    Profile.readFromFile(file);
                } catch (Exception exc) {
                    System.out.println("Defect found: "  + file.getPath() + "\n" +
                            Logger.exceptionAsString(exc));
                }
            }
        }
    }),
    ACCOUNT_RESET(input -> {
        String userID = input.replace("ACCOUNT_RESET ", "");
        System.out.println(userID);


        File[] directories = new File("/db").listFiles((current, name) ->
                new File(current, name).isDirectory());

        try {
            for (File guild : directories) {
                File userFile = new File(guild.getPath() + "/@@" + userID + ".txt");
                System.out.println(userFile.getPath());
                if (Arrays.asList(guild.listFiles((current, name) ->
                        new File(current, name).isFile())).contains(
                        userFile
                )) {
                    System.out.println("found");
                    Profile newProfile =
                            new Profile(new ArrayList<>(Collections.singletonList(new Gray())),
                                    new Coins(50),
                                    userID, Profile.readFromFile(userFile).getName()
                                    , 0, 0, Profile.readFromFile(userFile).getImageUrl(),
                                    guild.getName());
                    newProfile.createProfile();
                }

            }

            ObjectInputStream objectInputStream = new ObjectInputStream(
                    new FileInputStream(new File("/db/USERLIST.txt"))
            );

            @SuppressWarnings("unchecked")
            HashMap<String, ProfileStore> userList =
                    (HashMap<String, ProfileStore>) objectInputStream.readObject();
            objectInputStream.close();

            if(userList.containsKey(userID)){
                Profile newKey = Profile.storeToProfile(userList.get(userID));

                userList.replace(userID,
                        new Profile(new ArrayList<>(Collections.singletonList(new Gray())),
                                new Coins(50),
                                userID, newKey.getName(),
                                0, 0,
                                newKey.getImageUrl(),
                                newKey.getGuildID()).profileToStore());

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                        new FileOutputStream(
                                new File("/db/USERLIST.txt")
                        )
                );
                objectOutputStream.flush();
                objectOutputStream.writeObject(userList);
                objectOutputStream.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }),
    DB_BACKUP(ignored -> {
        try {
            long time = System.currentTimeMillis();
            System.out.println("Backing up DB...");
            File dir = new File("/db_recycle/db_backup" + System.currentTimeMillis());
            assert dir.createNewFile();
            FileUtils.copyDirectory(new File("/db"), dir);
            System.out.println("Successfully backed up DB: " + (System.currentTimeMillis() - time) + " ms");
        } catch (Exception e){
            e.printStackTrace();
        }


    }),
    ERROR((input) -> {
        System.out.println(input + " BAD COMMAND");
    });

    public FileAction action;
    FileCommand(FileAction action){
        this.action = action;
    }

    public static FileCommand getCmd(String command){
        ArrayList<FileCommand> cmdList = new ArrayList<>(){
            {
                add(CMD);
                add(GUILDS);
                add(USERS);
                add(AWARD_COINS);
                add(AWARD_SORINO);
                add(CHECK_DEFECTS);
                add(SEE_USER);
                add(UPDATE);
                add(INCREMENT_XP);
                add(NAMES);
                add(RATIO);
                add(DB_BACKUP);
                add(ACCOUNT_RESET);
            }
        };
        for (FileCommand fc : cmdList) {
            if (command.contains(fc.toString()))
                return fc;
        }

        return FileCommand.ERROR;
    }
}
