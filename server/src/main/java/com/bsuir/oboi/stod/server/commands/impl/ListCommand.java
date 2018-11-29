package com.bsuir.oboi.stod.server.commands.impl;

import com.bsuir.oboi.stod.server.ClientThread;
import com.bsuir.oboi.stod.server.commands.Command;

public class ListCommand implements Command {
    @Override
    public void execute(String data, ClientThread currentThread, ClientThread[] AllThreads) {

    }

    @Override
    public String getDescription() {
        return getName() + " Print full list of users.";
    }

    @Override
    public String getName() {
        return "-list";
    }
}
