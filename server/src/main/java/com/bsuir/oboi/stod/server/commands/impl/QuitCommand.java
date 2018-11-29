package com.bsuir.oboi.stod.server.commands.impl;

import com.bsuir.oboi.stod.server.ClientThread;
import com.bsuir.oboi.stod.server.commands.Command;

public class QuitCommand implements Command {
    @Override
    public void execute(String data, ClientThread currentThread, ClientThread[] AllThreads) {
        currentThread.setQuit(!currentThread.isQuit());
    }

    @Override
    public String getDescription() {
        return "Command provide exit function from program. <command name>";
    }

    @Override
    public String getName() {
        return "quit";
    }
}
