package com.bsuir.oboi.stod.server.commands.impl;

import com.bsuir.oboi.stod.server.ClientThread;
import com.bsuir.oboi.stod.server.commands.Command;

public class AllCommand implements Command {
    @Override
    public void execute(String data, ClientThread currentThread, ClientThread[] AllThreads) {

    }

    @Override
    public String getDescription() {
        return getName() + ": Provide send message for all users.";
    }

    @Override
    public String getName() {
        return "all";
    }
}
