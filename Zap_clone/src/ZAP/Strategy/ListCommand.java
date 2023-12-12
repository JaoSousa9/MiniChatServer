package ZAP.Strategy;

import ZAP.ChatServer;
import ZAP.Client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ListCommand implements Command{

    @Override
    public void sendMessage(Client sender, String[] parsed, ChatServer chatServer) {
        BufferedWriter out;
        try {
            out = new BufferedWriter(new OutputStreamWriter(sender.getClientSocket().getOutputStream()));
            out.write(("Numero de users: " + chatServer.getClientList().size()));
            out.newLine();
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
