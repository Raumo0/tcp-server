package com.bsuir.oboi.stod.server.commands;

import com.bsuir.oboi.stod.server.exceptions.CommandException;

public class CommandFactory {
    private CommandFactory() {}

    public static CommandFactory getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public Command defineCommand(CommandType type) throws CommandException {
        return type.getCurrentCommand();
    }

    public Command defineCommand(String commandName) throws CommandException {
        CommandType type = CommandType.valueOf(commandName.toUpperCase());
        return defineCommand(type);
    }

    private static class SingletonHolder{
        private static final CommandFactory INSTANCE = new CommandFactory();
    }
}
