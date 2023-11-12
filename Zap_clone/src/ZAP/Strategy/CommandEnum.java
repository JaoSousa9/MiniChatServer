package ZAP.Strategy;

public enum CommandEnum {

    LIST("/list", new ListCommand()),

    WHISPER("/whisper", new WhisperCommand());

    private String name;

    private Command command;
    CommandEnum(String name, Command command) {
        this.name = name;
        this.command = command;
    }
}
