package org.ac.proxymorons.multiplechatserver;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    ArrayList<Socket> clients;
    HashMap<Socket, String> clientsList;
    ServerWorker serverWorker;
    PrintWriter out;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();

    }

    public void start() {
        clients = new ArrayList<>();
        clientsList = new HashMap<>();
        ExecutorService cacheExecutor = Executors.newCachedThreadPool();
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println("Server is starting....");

            while (true) {

                Socket clientSocket = serverSocket.accept();
                clients.add(clientSocket);

                cacheExecutor.submit(serverWorker = new ServerWorker(clientSocket, this));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void sendAll(String reply) {

        for (int i = 0; i < clients.size(); i++) {
            Socket client = clients.get(i);
            try {
                out = new PrintWriter(client.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            out.println(reply);
        }
    }

    public void sendListToClient(Socket clientSocket) {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Socket client : clientsList.keySet()) {
            String clientPrint = clientsList.get(client);
            out.println(clientPrint);
        }
    }

    public void whisperToClient(String destSocket, String whisperMess, Socket originSocket) {
        for (Socket client : clientsList.keySet()) {
            if (clientsList.get(client).equals(destSocket)) {
                try {
                    out = new PrintWriter(client.getOutputStream(), true);
                    out.println("(whisper) " + destSocket + ": " + whisperMess);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            try {
                out = new PrintWriter(originSocket.getOutputStream(), true);
                out.println("That client doesn't exist!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }


    public class ServerWorker implements Runnable {

        private Socket clientSocket;
        private Server server;


        public ServerWorker(Socket clientSocket, Server server) {
            this.clientSocket = clientSocket;
            this.server = server;
        }

        @Override
        public void run() {

            String message = "";
            String reply = "";

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String enterChat = in.readLine();
                System.out.println(enterChat);
                String[] clientInput = enterChat.split(" ");
                String clientNickname = clientInput[0];

                if (!clientsList.containsKey(clientSocket)) {
                    clientsList.put(clientSocket, clientNickname);
                }
                while (true) {
                    try {
                        message = in.readLine();
                        message = message.substring(message.indexOf(" ") + 1);

                        if (message == null) {
                            break;
                        }
                        if (message != null) {


                            if (message.equals("/list")) {
                                server.sendListToClient(clientSocket);
                            }

                            if (message.equals("/quit")) {
                                reply = clientNickname + " left the chat!";
                                server.sendAll(reply);
                                clientsList.remove(clientSocket);

                                if(message.equals("/name")) {

                                }

                            } else if (message.contains("/whisper")) {
                                String[] getClient = message.split(" ");
                                String client = getClient[1];

                                String middleSplit = message.substring(message.indexOf(" ") + 1);
                                String whisperMess = middleSplit.substring(middleSplit.indexOf(" ") + 1);

                                server.whisperToClient(client, whisperMess, clientSocket);


                            } else {
                                reply = clientNickname + ": " + message;
                                server.sendAll(reply);
                                System.out.println(reply);
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }
}

