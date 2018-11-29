package com.bsuir.oboi.stod.server.commands.impl;

import com.bsuir.oboi.stod.server.ClientThread;
import com.bsuir.oboi.stod.server.commands.Command;
import com.google.gson.Gson;

import java.util.AbstractMap;

public class NameCommand implements Command {
    Gson gson = new Gson();

    @Override
    public void execute(String data, ClientThread currentThread, ClientThread[] AllThreads) {
        String name = data.split(" ", 0)[0];
        for (ClientThread clientThread : AllThreads)
            if (clientThread != null && !name.isEmpty() &&
                    clientThread.getUsername().toUpperCase().equals(name.toUpperCase()))
                clientThread.getOutput().println(gson.toJson(new AbstractMap.SimpleEntry<>(
                        clientThread.getUsername() + clientThread.getClientSocket().getRemoteSocketAddress(),
                        data.substring(name.length())
                )));
    }

    @Override
    public String getDescription() {
        return "Provide send private message to user by name. <command name> <enter> <username> <message>";
    }

    @Override
    public String getName() {
        return "name";
    }
}
