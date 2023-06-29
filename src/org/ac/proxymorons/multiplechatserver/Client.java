package org.ac.proxymorons.multiplechatserver;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    private String nickname = null;
    private Scanner sc;
    private PrintWriter cout;
    private Socket clientSocket;


    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }


    public void start() {
        sc = new Scanner(System.in);

        System.out.print("Please enter your nickname to enter the chat: ");
        nickname = sc.nextLine();

        System.out.print("Host: ");
        String host = sc.nextLine();

        System.out.print("Port: ");
        int portNum = sc.nextInt();


        connect(host, portNum);

    }

    public void connect(String host, int portNum) {

        try {

            clientSocket = new Socket(host, portNum);
            cout = new PrintWriter(clientSocket.getOutputStream(), true);

            ExecutorService singleExecutor = Executors.newSingleThreadExecutor();
            singleExecutor.submit(new ServerToClient(clientSocket));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cout.println(nickname + " has entered the chat!");

        clientToServer();

    }

    public void clientToServer() {
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        // message = sc.nextLine();

        if (message != null) {
            while (!message.equals("/quit")) {
                if (message.equals("/name")) {
                    System.out.print("Enter your new nickname: ");
                    nickname = scanner.nextLine();
                    message = scanner.nextLine();


                } else {
                    cout.println(nickname + ": " + message);
                    message = scanner.nextLine();
                }
            }

            if (message.equals("/quit")) {
                try {
                    cout.println(nickname + ": " + message);
                    clientSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }


    public class ServerToClient implements Runnable {

        private Socket clientSocket;
        private BufferedReader bReader;


        private ServerToClient(Socket clientSocket) {
            this.clientSocket = clientSocket;

        }

        @Override
        public void run() {

            try {
                this.bReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (true) {
                    String messageReceived = bReader.readLine();
                    if (messageReceived == null) {
                        break;
                    }
                    System.out.println(messageReceived);
                }
            } catch (IOException e) {
                System.out.println("Connection ended!");
                throw new RuntimeException(e);
            }

        }
    }

}
