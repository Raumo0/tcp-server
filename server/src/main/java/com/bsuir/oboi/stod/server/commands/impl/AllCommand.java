package com.bsuir.oboi.stod.server.commands.impl;

import com.bsuir.oboi.stod.server.ClientThread;
import com.bsuir.oboi.stod.server.commands.Command;
import com.google.gson.Gson;

import java.util.AbstractMap;

public class AllCommand implements Command {
    private Gson gson = new Gson();

    @Override
    public void execute(String data, ClientThread currentThread, ClientThread[] AllThreads) {
        for (ClientThread clientThread : AllThreads)
            if (clientThread != null)
                clientThread.getOutput().println(gson.toJson(new AbstractMap.SimpleEntry<>(
                        clientThread.getUsername() + clientThread.getClientSocket().getRemoteSocketAddress(),
                        data
                )));
    }

    @Override
    public String getDescription() {
        return "Provide send message for all users.";
    }

    @Override
    public String getName() {
        return "all";
    }
}
