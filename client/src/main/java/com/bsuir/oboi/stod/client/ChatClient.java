package com.bsuir.oboi.stod.client;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient implements Runnable  {
    private Socket clientSocket = null;
    private PrintStream output = null;
    private DataInputStream input = null;
    private BufferedReader inputLine = null;
    private boolean closed = false;
    private final Gson gson = new Gson();

    public ChatClient(int portNumber, String host) {
        printWelcomeMessage(portNumber, host);
        initializeSocketAndStreams(portNumber, host);
        workWithSocket();
    }

    private void workWithSocket() {
        if (clientSocket != null && output != null && input != null) {
            try {
                new Thread(this).start();
                if (!closed)
                    output.println(inputLine.readLine().trim());
                Dto dto;
                while (!closed) {
                    dto = new Dto();
                    dto.setCommand(inputLine.readLine().trim());
                    dto.setArgument(inputLine.readLine().trim());
                    output.println(gson.toJson(dto));
                }
                closeSocketAndStreams();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }

    private void closeSocketAndStreams() throws IOException {
        output.close();
        input.close();
        clientSocket.close();
    }

    private void initializeSocketAndStreams(int portNumber, String host) {
        try {
            clientSocket = new Socket(host, portNumber);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            output = new PrintStream(clientSocket.getOutputStream());
            input = new DataInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Unknown Host: " + host);
        } catch (IOException e) {
            System.err.println("Can't connect to the host: "
                    + host);
        }
    }

    private void printWelcomeMessage(int portNumber, String host) {
        System.out
                .println("Now using host=" + host + ", portNumber=" + portNumber);
    }

    public void run() {
        try {
            keepReadingUntilMessage("Goodbye");
            closed = true;
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }

    private void keepReadingUntilMessage(String message) throws IOException {
        String responseLine;
        while ((responseLine = input.readLine()) != null) {
            System.out.println(responseLine);
            if (responseLine.indexOf(message) != -1)
                break;
        }
    }
}
