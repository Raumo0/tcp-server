package com.bsuir.oboi.stod.server.commands.impl;

import com.bsuir.oboi.stod.server.ClientThread;
import com.bsuir.oboi.stod.server.commands.Command;
import com.bsuir.oboi.stod.server.commands.CommandType;
import com.bsuir.oboi.stod.server.exceptions.CommandException;

import java.util.HashMap;
import java.util.Map;

public class HelpCommand implements Command {
    @Override
    public void execute(String data, ClientThread currentThread, ClientThread[] AllThreads) {
        Map<String, String> result = new HashMap<>();
        for (CommandType commandType:CommandType.values()) {
            try {
                Command command = commandType.getCurrentCommand();
                if(data.isEmpty() || commandType.getCurrentCommand().getName().equals(data))
                    result.put(command.getName(), command.getDescription());
            } catch (CommandException e) {
                e.printStackTrace();
            }
        }
        currentThread.getOutput().println(result);
    }

    @Override
    public String getDescription() {
        return "Show full list of commands with description.";
    }

    @Override
    public String getName() {
        return "help";
    }
}
