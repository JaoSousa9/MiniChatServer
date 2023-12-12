package ZAP.Strategy;
import ZAP.ChatServer;
import ZAP.Client;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class AllChat implements Command {
    @Override
    public void sendMessage(Client sender, String[] parsed, ChatServer chatServer) {

        for (Client client : chatServer.getClientList()) {
            if (client.getClientSocket() != sender.getClientSocket()) {
                BufferedWriter out;
                try {
                    out = new BufferedWriter(new OutputStreamWriter(client.getClientSocket().getOutputStream()));
                    StringBuilder message = new StringBuilder();
                    for (String s : parsed) {
                        message.append(" ").append(s);
                    }
                    chatServer.write(out, message.toString(), sender);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
