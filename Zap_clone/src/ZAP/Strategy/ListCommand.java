package ZAP.Strategy;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ListCommand implements Command{

    @Override
    public void sendMessage(Client sender) {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sender.get.getOutputStream()));
        out.write(("Numero de users: " + clientList.size()));
    }
}
