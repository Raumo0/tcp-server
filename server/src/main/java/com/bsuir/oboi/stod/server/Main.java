package com.bsuir.oboi.stod.server;

public class Main {
    public static void main(String[] args) {
        int portNumber = 2222;//not less than 1023 if not root
        if (args.length > 0)
            portNumber = getPortNumberFromArgs(args);
        new ChatServer(portNumber);
    }

    private static int getPortNumberFromArgs(String[] args) {
        return Integer.valueOf(args[0]).intValue();
    }
}
