package ZAP;

import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private final Socket clientSocket;
    private final String name;
    public Client(Socket clientSocket, String name) {
        this.clientSocket = clientSocket;
        this.name = name;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public String getName() {
        return name;
    }
}
