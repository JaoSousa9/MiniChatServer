package ZAP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private final Vector<Client> clientList = new Vector<>();
    public static final int DEFAULT_PORT = 8085;
    private ServerSocket serverSocket = null;

    // Allocate a Pool of an undefined number of threads
    private final ExecutorService cachedPool = Executors.newCachedThreadPool();


    public static void main(String[] args) {

        //Creates Chat Server
        ChatServer chatServer = new ChatServer();

        try {
            chatServer.serverSocket = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        chatServer.listen();

    }

    private void listen() {

        try {
            while (serverSocket.isBound()) {
                Socket socket = serverSocket.accept();

                //Reads Name of the User
                DataOutputStream nameRequest = new DataOutputStream(socket.getOutputStream());
                nameRequest.writeBytes("What's your name? \n");
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String name = in.readLine();

                if (checkNameExists(name)) {
                    //Error message -> Name already exists;
                } else {
                    Client client = new Client(socket, name);
                    clientList.add(client);

                    ClientHandler clientHandler = new ClientHandler(client, this);
                    cachedPool.submit(clientHandler);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(Client sender) {

        try {
            while (sender.getClientSocket().isBound()) {

                BufferedReader input = new BufferedReader(new InputStreamReader(sender.getClientSocket().getInputStream()));
                String forParsing = getSenderMessage(input);

                String[] parsed = forParsing.split(" ");

                if (parsed[0].equals("/whisper")) {
                    String receiver = "";
                    String message = "";

                    if (parsed[1] != null && parsed[2] != null) {
                        receiver = parsed[1];
                        for (int i = 2; i < parsed.length; i++) {
                            message += parsed[i] + " ";
                        }
                    }

                    System.out.println(receiver + " " + message);
                    Socket receiverSocket = null;

                    for (Client client : clientList) {
                        if (client.getName().equals(receiver)) {
                            receiverSocket = client.getClientSocket();
                        }
                    }

                    if (receiverSocket != null) {
                        //DataOutputStream out = new DataOutputStream(receiverSocket.getOutputStream());
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(receiverSocket.getOutputStream()));
                        write(out, message, sender);
                    }
                } else if (parsed[0].equals("/list")) {
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sender.getClientSocket().getOutputStream()));
                    out.write(("Numero de users: " + clientList.size()));

                } else {
                    if (parsed[0] != null) {
                        for (Client client : clientList) {
                            if (client.getClientSocket() != sender.getClientSocket()) {
                                //DataOutputStream out = new DataOutputStream(client.getClientSocket().getOutputStream());
                                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getClientSocket().getOutputStream()));
                                String message = "";
                                for (int i = 0; i < parsed.length; i++) {
                                    message += parsed[i] + " ";
                                }
                                write(out, message, sender);
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private String getSenderMessage(BufferedReader in) throws IOException {
        return in.readLine();
    }

    private void write(BufferedWriter out, String message, Client sender) {
        try {
            out.write(sender.getName() + ": " + message);
            System.out.println(sender.getName() + ": " +message);
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


}
