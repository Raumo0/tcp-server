package com.bsuir.oboi.stod.server;

import com.bsuir.oboi.stod.server.commands.Command;
import com.bsuir.oboi.stod.server.commands.CommandFactory;
import com.bsuir.oboi.stod.server.exceptions.CommandException;
import com.google.gson.Gson;

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
    private String username;
    private SocketAddress address;
    private Gson gson = new Gson();

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
        Dto dto;
        while (true) {
            output.println("Enter a command and then enter an argument.");
            String line = input.readLine();
            dto = gson.fromJson(line, Dto.class);
            if(isCommandExists(dto.getCommand())) {
                parseRequest(dto.getCommand(), dto.getArgument());
            }
            if (line.startsWith("/quit"))
                break;
            for (int i = 0; i < maxClientsCount; i++)
                if (threads[i] != null)
                    threads[i].output.println("<" + username + clientSocket.getRemoteSocketAddress() + ">: " + line);
        }
    }

    private void parseRequest(String commandName, String arguments) {
        Command command;
        try {
            command = CommandFactory.getInstance().defineCommand(commandName);
            command.execute(arguments, this, threads);
        } catch (CommandException | IllegalArgumentException e) {
            e.printStackTrace();//unknown command message needed
        }
    }


    private boolean isCommandExists(String line) {
        return !line.isEmpty();
    }

    private void sendLeavingNotification(int maxClientsCount, ClientThread[] threads) {
        for (int i = 0; i < maxClientsCount; i++)
            if (threads[i] != null && threads[i] != this)
                threads[i].output.println("The user " + username + " is leaving.");
        output.println("Goodbye " + username + ".");
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
        output.println("Enter your username.");
        username = input.readLine().trim();
        address = clientSocket.getRemoteSocketAddress();
    }

    private void createStreams() throws IOException {
        input = new DataInputStream(clientSocket.getInputStream());
        output = new PrintStream(clientSocket.getOutputStream());
    }

    private void sendNewUserEnteredMessage(int maxClientsCount, ClientThread[] threads) {
        output.println("Hello " + username + address
                + " to the chat room.\nTo leave enter /quit in a new line");
        for (int i = 0; i < maxClientsCount; i++)
            if (threads[i] != null && threads[i] != this)
                threads[i].output.println("A new user " + username + clientSocket.getRemoteSocketAddress()
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
