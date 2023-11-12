package ZAP;

import ZAP.Strategy.ListCommand;

public enum Commands {
    LIST("/list"),
    WHISPER("/whisper"),
    DEFAULT("");

    private String name;
    Commands(String name){
        this.name = name;

    }




}
