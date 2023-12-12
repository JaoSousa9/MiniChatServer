package ZAP.Strategy;

import ZAP.ChatServer;
import ZAP.Client;

public class InvalidCommand implements Command {
    @Override
    public void sendMessage(Client sender, String[] parsed, ChatServer chatServer) {


    }
}
