package ZAP.Strategy;

import ZAP.ChatServer;
import ZAP.Client;

public interface Command {
    void sendMessage(Client sender, String[] parsed, ChatServer chatServer);
}
