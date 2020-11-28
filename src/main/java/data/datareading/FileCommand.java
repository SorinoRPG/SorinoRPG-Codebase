package data.datareading;

import main.userinterface.UserAction;

enum FileCommand{
    CMD(() -> {

    });

    FileAction action;
    FileCommand(FileAction action){
        this.action = action;
    }

    static FileCommand getCmd(String command){

        return null;
    }
}
