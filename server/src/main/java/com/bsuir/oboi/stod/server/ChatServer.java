package com.bsuir.oboi.stod.server;

import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class ChatServer {
    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private static final int maxClientsCount = 10;
    private static final ClientThread[] threads = new ClientThread[maxClientsCount];

    public static void main(String args[]) {
        int portNumber = 2222;//not less than 1023 if not root
        if (args.length < 1)
            printWelcomeMessage(portNumber);
        else
            portNumber = getPortNumberFromArgs(args);
        openSocket(portNumber);
        clientCreation();
    }

    private static void clientCreation() {
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                int i;
                for (i = 0; i < maxClientsCount; i++)
                    if (threads[i] == null) {
                        (threads[i] = new ClientThread(clientSocket, threads)).start();
                        break;
                    }
                if (i == maxClientsCount)
                    serverOverloadedBehavior();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private static void serverOverloadedBehavior() throws IOException {
        PrintStream os = new PrintStream(clientSocket.getOutputStream());
        os.println("Server too busy. Try later.");
        os.close();
        clientSocket.close();
    }

    private static void openSocket(int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static int getPortNumberFromArgs(String[] args) {
        return Integer.valueOf(args[0]).intValue();
    }

    private static void printWelcomeMessage(int portNumber) {
        System.out.println("Usage: java ChatServer <portNumber>\n"
                        + "Now using port number=" + portNumber);
    }
}
