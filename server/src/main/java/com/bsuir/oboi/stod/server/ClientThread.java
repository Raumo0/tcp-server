package com.bsuir.oboi.stod.server;

import com.bsuir.oboi.stod.server.commands.Command;
import com.bsuir.oboi.stod.server.commands.CommandFactory;
import com.bsuir.oboi.stod.server.exceptions.CommandException;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientThread extends Thread {
    private DataInputStream input = null;
    private PrintStream output = null;
    private Socket clientSocket = null;
    private final ClientThread[] threads;
    private int maxClientsCount;
    private String name;
    private SocketAddress address;

    public ClientThread(Socket clientSocket, ClientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        ClientThread[] threads = this.threads;
        try {
            initializeUser();
            sendNewUserEnteredMessage(maxClientsCount, threads);
            commandCatching(maxClientsCount, threads);
            sendLeavingNotification(maxClientsCount, threads);
            cleanUpCurrentThread(maxClientsCount, threads);
            closeSocketAndStreams();
        } catch (IOException e) {
        }
    }

    private void commandCatching(int maxClientsCount, ClientThread[] threads) throws IOException {
        while (true) {
            String line = input.readLine();
            if(startWithCommand(line)) {
                parseCommand(line);
            }
            if (line.startsWith("/quit"))
                break;
            for (int i = 0; i < maxClientsCount; i++)
                if (threads[i] != null)
                    threads[i].output.println("<" + name + clientSocket.getRemoteSocketAddress() + ">: " + line);
        }
    }

    private void parseCommand(String line) {
        Command command;
        try {
            command = CommandFactory.getInstance().defineCommand(getCommandFromLine(line));
            command.execute("", this, threads);
        } catch (CommandException | IllegalArgumentException e) {
            e.printStackTrace();//unknown command message needed
        }
    }

    private String getCommandFromLine(String line) {
        return line.split(" ", 1)[0];
    }

    private boolean startWithCommand(String line) {
        return !line.isEmpty() && line.substring(0, 1).equals("-");
    }

    private void sendLeavingNotification(int maxClientsCount, ClientThread[] threads) {
        for (int i = 0; i < maxClientsCount; i++)
            if (threads[i] != null && threads[i] != this)
                threads[i].output.println("The user " + name + " is leaving.");
        output.println("Goodbye " + name + ".");
    }

    private void cleanUpCurrentThread(int maxClientsCount, ClientThread[] threads) {
        for (int i = 0; i < maxClientsCount; i++)
            if (threads[i] == this)
                threads[i] = null;
    }

    private void initializeUser() throws IOException {
        createStreams();
        initializeIPAndUsername();
    }

    private void initializeIPAndUsername() throws IOException {
        output.println("Enter your name.");
        name = input.readLine().trim();
        address = clientSocket.getRemoteSocketAddress();
    }

    private void createStreams() throws IOException {
        input = new DataInputStream(clientSocket.getInputStream());
        output = new PrintStream(clientSocket.getOutputStream());
    }

    private void sendNewUserEnteredMessage(int maxClientsCount, ClientThread[] threads) {
        output.println("Hello " + name + address
                + " to the chat room.\nTo leave enter /quit in a new line");
        for (int i = 0; i < maxClientsCount; i++)
            if (threads[i] != null && threads[i] != this)
                threads[i].output.println("A new user " + name + clientSocket.getRemoteSocketAddress()
                        + " entered the chat room.");
    }

    private void closeSocketAndStreams() throws IOException {
        input.close();
        output.close();
        clientSocket.close();
    }

    public PrintStream getOutput() {
        return output;
    }

    public ClientThread[] getThreads() {
        return threads;
    }
}
