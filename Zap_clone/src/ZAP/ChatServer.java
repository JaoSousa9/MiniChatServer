package ZAP;

import ZAP.Strategy.Command;
import ZAP.Strategy.CommandEnum;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private final List<Client> clientList = new ArrayList<>();
    public static final int DEFAULT_PORT = 8085;
    private ServerSocket serverSocket = null;

    // Allocate a Pool of an undefined number of threads
    private final ExecutorService cachedPool = Executors.newCachedThreadPool();


    public static void main(String[] args) {
        //Creates Chat Server
        ChatServer chatServer = new ChatServer();

        try {
            chatServer.serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("Server was Initialized");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        chatServer.listen();

    }

    private void listen() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A User connected to the Server");
                Client client = new Client(socket);

                //In case a client connects and leaves the server
                client.setName("No attributed Name");
                
                ClientHandler clientHandler = new ClientHandler(client, this);
                cachedPool.submit(clientHandler);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getName(Socket socket) {
        try {
            DataOutputStream nameRequest = new DataOutputStream(socket.getOutputStream());;
            nameRequest.writeBytes("What's your name? \n");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return in.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void nameAlreadyExists(Socket socket) {
        DataOutputStream nameAlreadyExists = null;
        try {
            nameAlreadyExists = new DataOutputStream(socket.getOutputStream());
            nameAlreadyExists.writeBytes("Name already exists, please choose another one! \n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(Client sender) {

        String name = getName(sender.getClientSocket());
        sender.setName(name);

        if (checkNameExists(name)) {
            nameAlreadyExists(sender.getClientSocket());
            sender.setName(getName(sender.getClientSocket()));
        }

        clientList.add(sender);

        try {
            while (!sender.getClientSocket().isClosed()) {

                BufferedReader input = new BufferedReader(new InputStreamReader(sender.getClientSocket().getInputStream()));
                String line = input.readLine();
                System.out.println(line);

                if (line == null) {
                    System.out.println("Client " + sender.getName() + " closed, exiting...");
                    input.close();
                    sender.getClientSocket().close();

                } else if (!line.isEmpty()) {
                    String[] parsed = line.split(" ");
                    Command command = CommandEnum.choseCommand(parsed[0]);
                    command.sendMessage(sender, parsed, this);
                }

            }
            clientList.remove(sender);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(BufferedWriter out, String message, Client sender) {
        try {
            out.write(sender.getName() + ": " + message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkNameExists(String name) {
        for (Client client : clientList) {
            if (client.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public List<Client> getClientList() {
        return clientList;
    }
}
