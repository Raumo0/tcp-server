package com.bsuir.oboi.stod.server.commands;

import com.bsuir.oboi.stod.server.commands.impl.AllCommand;
import com.bsuir.oboi.stod.server.commands.impl.HelpCommand;
import com.bsuir.oboi.stod.server.commands.impl.IpCommand;
import com.bsuir.oboi.stod.server.commands.impl.ListCommand;
import com.bsuir.oboi.stod.server.commands.impl.NameCommand;
import com.bsuir.oboi.stod.server.exceptions.CommandException;

import java.util.Map;
import java.util.TreeMap;

public enum CommandType {
    HELP, ALL, NAME, IP, LIST;

    private static Map<CommandType, Class> commands;
    static {
        commands = new TreeMap<>();
        commands.put(CommandType.HELP, HelpCommand.class);
        commands.put(CommandType.ALL, AllCommand.class);
        commands.put(CommandType.NAME, NameCommand.class);
        commands.put(CommandType.IP, IpCommand.class);
        commands.put(CommandType.LIST, ListCommand.class);
    }

    public Command getCurrentCommand() throws CommandException {
        Class commandClass = commands.get(this);
        try {
            return (Command) commandClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CommandException(e);
        }
    }
}
