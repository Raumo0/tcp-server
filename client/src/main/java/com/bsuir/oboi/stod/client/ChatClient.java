package com.bsuir.oboi.stod.client;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient implements Runnable  {
    private static Socket clientSocket = null;
    private static PrintStream output = null;
    private static DataInputStream input = null;
    private static BufferedReader inputLine = null;
    private static boolean closed = false;

    public static void main(String[] args) {
        int portNumber = 2222;
        String host = "localhost";
        if (args.length < 2) {
            printWelcomeMessage(portNumber, host);
        } else {
            host = args[0];
            portNumber = Integer.valueOf(args[1]).intValue();
        }
        initializeSocketAndStreams(portNumber, host);
        workWithSocket();
    }

    private static void workWithSocket() {
        if (clientSocket != null && output != null && input != null) {
            try {
                new Thread(new ChatClient()).start();
                while (!closed) {
                    output.println(inputLine.readLine().trim());
                }
                closeSocketAndStreams();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }

    private static void closeSocketAndStreams() throws IOException {
        output.close();
        input.close();
        clientSocket.close();
    }

    private static void initializeSocketAndStreams(int portNumber, String host) {
        try {
            clientSocket = new Socket(host, portNumber);
            inputLine = new BufferedReader(new InputStreamReader(System.in));
            output = new PrintStream(clientSocket.getOutputStream());
            input = new DataInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to the host "
                    + host);
        }
    }

    private static void printWelcomeMessage(int portNumber, String host) {
        System.out
                .println("Usage: java ChatClient <host> <portNumber>\n"
                        + "Now using host=" + host + ", portNumber=" + portNumber);
    }

    public void run() {
        try {
            keepReadingUntilMessage("*** Bye");
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
