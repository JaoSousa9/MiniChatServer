package ZAP;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerUser {

    Socket mySocket;
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        ServerUser serverUser = new ServerUser();
        serverUser.connect();

    }

    private void connect(){
        try {
            //Initializes the connection to the specific server
            mySocket = new Socket(InetAddress.getLocalHost(), ChatServer.DEFAULT_PORT );
            ClientHandler clientHandler = new ClientHandler();
            //Now, while this Client is connected to the server he will be able to write while other clients are writing
            executorService.submit(clientHandler);
            read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //This method receives the server messages and prints them to the client
    private void read(){
        while (mySocket.isConnected()){
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                String message = in.readLine();
                System.out.println(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Sends the messages to the server to then deal with them
    private void write(){
        while (mySocket.isBound()){
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String message = in.readLine();

                //DataOutputStream out = new DataOutputStream(mySocket.getOutputStream());
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));
                out.write(message);
                out.newLine();
                out.flush();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public class ClientHandler implements Runnable{
        @Override
        public void run() {
            write();
        }
    }

}


