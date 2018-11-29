package com.bsuir.oboi.stod.server;

import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class ChatServer {
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private final int maxClientsCount = 10;
    private final ClientThread[] threads = new ClientThread[maxClientsCount];

    public ChatServer(int portNumber) {
        printWelcomeMessage(portNumber);
        openSocket(portNumber);
        start();
    }

    private void start() {
        while (true) {
            try {
                clientProcessing();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private void clientProcessing() throws IOException {
        clientSocket = serverSocket.accept();
        int i;
        for (i = 0; i < maxClientsCount; i++)
            if (threads[i] == null) {
                threads[i] = clientCreation();
                break;
            }
        if (i == maxClientsCount)
            serverOverloadedBehavior();
    }

    private ClientThread clientCreation() {
        ClientThread clientThread = new ClientThread(clientSocket, threads);
        clientThread.start();
        return clientThread;
    }

    private void serverOverloadedBehavior() throws IOException {
        PrintStream os = new PrintStream(clientSocket.getOutputStream());
        os.println("Server is overloaded, please try again or later.");
        os.close();
        clientSocket.close();
    }

    private void openSocket(int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void printWelcomeMessage(int portNumber) {
        System.out.println("Server now using port number: " + portNumber);
    }
}
