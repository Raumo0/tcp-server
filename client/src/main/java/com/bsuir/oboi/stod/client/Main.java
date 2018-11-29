package com.bsuir.oboi.stod.client;

public class Main {
    public static void main(String[] args) {
        int portNumber = 2222;
        String host = "localhost";
        if (args.length > 1) {
            host = args[0];
            portNumber = Integer.valueOf(args[1]).intValue();
        }
        new ChatClient(portNumber, host);
    }
}
