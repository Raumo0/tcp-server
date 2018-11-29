package com.bsuir.oboi.stod.server.commands;

import com.bsuir.oboi.stod.server.ClientThread;

public interface Command {
    void execute(String data, ClientThread currentThread, ClientThread[] AllThreads);
    String getDescription();
    String getName();
}
