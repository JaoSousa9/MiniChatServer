package ZAP.Strategy;

import ZAP.ChatServer;
import ZAP.Client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class WhisperCommand implements Command{
    @Override
    public void sendMessage(Client sender, String[] parsed, ChatServer chatServer) {

        String receiver = "";
        String message = "";

        if (parsed[1] != null && parsed[2] != null) {
            receiver = parsed[1];
            message = getMessage(parsed);
        }

        Socket receiverSocket = null;

        for (Client client : chatServer.getClientList()) {
            if (client.getName().equals(receiver)) {
                receiverSocket = client.getClientSocket();
            }
        }

        if (receiverSocket != null) {
            BufferedWriter out;
            try {
                out = new BufferedWriter(new OutputStreamWriter(receiverSocket.getOutputStream()));
                chatServer.write(out, message, sender);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getMessage(String[] parsed){
        String message = "";
        for (int i = 2; i < parsed.length; i++) {
            message += parsed[i] + " ";
        }
        return message;
    }


}
