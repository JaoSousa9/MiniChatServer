package ZAP;

import java.net.Socket;

public class Client {
    private final Socket clientSocket;
    private String name;
    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
