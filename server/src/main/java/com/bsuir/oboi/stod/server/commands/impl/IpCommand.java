package com.bsuir.oboi.stod.server.commands.impl;

import com.bsuir.oboi.stod.server.ClientThread;
import com.bsuir.oboi.stod.server.commands.Command;

public class IpCommand implements Command {
    @Override
    public void execute(String data, ClientThread currentThread, ClientThread[] AllThreads) {

    }

    @Override
    public String getDescription() {
        return getName() + " Provide send private message to user by IP.";
    }

    @Override
    public String getName() {
        return "-ip";
    }
}
