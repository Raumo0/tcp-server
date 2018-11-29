package com.bsuir.oboi.stod.server.commands.impl;

import com.bsuir.oboi.stod.server.ClientThread;
import com.bsuir.oboi.stod.server.commands.Command;
import com.bsuir.oboi.stod.server.commands.CommandType;
import com.bsuir.oboi.stod.server.exceptions.CommandException;

public class HelpCommand implements Command {
    @Override
    public void execute(String data, ClientThread currentThread, ClientThread[] AllThreads) {
        String result = "";
        for (CommandType commandType:CommandType.values()) {
            try {
                result += commandType.getCurrentCommand().getDescription() + "\n";
            } catch (CommandException e) {
                e.printStackTrace();
            }
        }
        currentThread.getOutput().println(result);
    }

    @Override
    public String getDescription() {
        return getName() + " Show full list of commands with description.";
    }

    @Override
    public String getName() {
        return "-help";
    }
}
