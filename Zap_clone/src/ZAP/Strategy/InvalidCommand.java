package ZAP.Strategy;

import ZAP.ChatServer;
import ZAP.Client;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class InvalidCommand implements Command {
    @Override
    public void sendMessage(Client sender, String[] parsed, ChatServer chatServer) {

        BufferedWriter out;
        try {
            out = new BufferedWriter(new OutputStreamWriter(sender.getClientSocket().getOutputStream()));
            out.write(("There isn't such command available" ));
            out.newLine();
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
