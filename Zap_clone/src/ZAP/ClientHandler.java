package ZAP;

import ZAP.ChatServer;
import ZAP.Client;

public class ClientHandler implements Runnable {

    private final ChatServer chatServer;
    private final Client client;

    public ClientHandler(Client client, ChatServer chatServer) {
        this.chatServer = chatServer;
        this.client = client;
    }

    public ChatServer getChatServer() {
        return chatServer;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void run() {
        chatServer.sendMessage(client);
    }
}
