package com.bsuir.oboi.stod.server.commands.impl;

import com.bsuir.oboi.stod.server.ClientThread;
import com.bsuir.oboi.stod.server.commands.Command;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ListCommand implements Command {
    private Gson gson = new Gson();

    @Override
    public void execute(String data, ClientThread currentThread, ClientThread[] AllThreads) {
        List<String> result = new ArrayList<>();
        for (ClientThread clientThread : AllThreads) {
            if(clientThread != null)
                result.add(clientThread.getUsername() + clientThread.getClientSocket().getRemoteSocketAddress());
        }
        currentThread.getOutput().println(gson.toJson(result));
    }

    @Override
    public String getDescription() {
        return getName() + ": Print full list of users.";
    }

    @Override
    public String getName() {
        return "list";
    }
}
