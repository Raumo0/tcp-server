package com.bsuir.oboi.stod.server;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread {
    private DataInputStream is = null;
    private PrintStream os = null;
    private Socket clientSocket = null;
    private final ClientThread[] threads;
    private int maxClientsCount;

    public ClientThread(Socket clientSocket, ClientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        ClientThread[] threads = this.threads;

        try {
      /*
       * Create input and output streams for this client.
       */
            is = new DataInputStream(clientSocket.getInputStream());
            os = new PrintStream(clientSocket.getOutputStream());
            os.println("Enter your name.");
            String name = is.readLine().trim();
            os.println("Hello " + name + clientSocket.getRemoteSocketAddress()
                    + " to our chat room.\nTo leave enter /quit in a new line");
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("*** A new user " + name + clientSocket.getRemoteSocketAddress()
                            + " entered the chat room !!! ***");
                }
            }
            while (true) {
                String line = is.readLine();
                if (line.startsWith("/quit")) {
                    break;
                }
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null) {
                        threads[i].os.println("<" + name + clientSocket.getRemoteSocketAddress() + ">: " + line);
                    }
                }
            }
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("*** The user " + name
                            + " is leaving the chat room !!! ***");
                }
            }
            os.println("*** Bye " + name + " ***");

      /*
       * Clean up. Set the current thread variable to null so that a new client
       * could be accepted by the server.
       */
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }

      /*
       * Close the output stream, close the input stream, close the socket.
       */
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {
        }
    }
}
