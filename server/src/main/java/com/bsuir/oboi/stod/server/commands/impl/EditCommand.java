package com.bsuir.oboi.stod.server.commands.impl;

import com.bsuir.oboi.stod.server.ClientThread;
import com.bsuir.oboi.stod.server.commands.Command;

public class EditCommand implements Command {
    @Override
    public void execute(String data, ClientThread currentThread, ClientThread[] AllThreads) {
        String name = data.split(" ",1)[0];
        currentThread.setUsername(name);
        currentThread.getOutput().println("Your name is changed to: " + currentThread.getName());
    }

    @Override
    public String getDescription() {
        return getName() + ": Provide change user name to another one.";
    }

    @Override
    public String getName() {
        return "edit";
    }
}
