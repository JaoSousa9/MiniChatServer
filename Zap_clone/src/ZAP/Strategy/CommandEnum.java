package ZAP.Strategy;

import java.util.Objects;

public enum CommandEnum {

    LIST("/list", new ListCommand()),

    WHISPER("/whisper", new WhisperCommand()),

    ALL("", new AllChat()),

    CHAT_ROOM("/chatroom", new ChatRoom()),

    INVALID("/", new InvalidCommand());

    private String name;
    private Command command;

    //all commands will implement the command Interface
    CommandEnum(String name, Command command) {
        this.name = name;
        this.command = command;
    }

    public static Command choseCommand(String command){
        Command chosenCommand = INVALID.command;

        String[] letters = command.split("");
        if(!Objects.equals(letters[0], "/")){
            return ALL.command;
        }

        for (CommandEnum command1: values()){
            if(command.equals(command1.name)){
                return command1.command;
            }
        }
        return chosenCommand;
    }
}
