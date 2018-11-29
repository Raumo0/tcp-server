package com.bsuir.oboi.stod.server.commands.impl;

import com.bsuir.oboi.stod.server.ClientThread;
import com.bsuir.oboi.stod.server.commands.Command;
import com.google.gson.Gson;

import java.util.AbstractMap;

public class IpCommand implements Command {
    Gson gson = new Gson();

    @Override
    public void execute(String data, ClientThread currentThread, ClientThread[] AllThreads) {
        String ip = data.split(" ", 0)[0];
        for (ClientThread clientThread : AllThreads)
            if (clientThread != null && !ip.isEmpty() &&
                    clientThread.getClientSocket().getRemoteSocketAddress().toString().toUpperCase().equals(ip.toUpperCase()))
                clientThread.getOutput().println(gson.toJson(new AbstractMap.SimpleEntry<>(
                        clientThread.getUsername() + clientThread.getClientSocket().getRemoteSocketAddress(),
                        data.substring(ip.length())
                )));
    }

    @Override
    public String getDescription() {
        return "Provide send private message to user by IP. <command name> <enter> </host:port> <message>";
    }

    @Override
    public String getName() {
        return "ip";
    }
}
