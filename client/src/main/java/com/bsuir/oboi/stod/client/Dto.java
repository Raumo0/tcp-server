package com.bsuir.oboi.stod.client;

public class Dto {
    String command;
    String argument;

    public Dto() {
    }

    public Dto(String command, String argument) {

        this.command = command;
        this.argument = argument;
    }

    public String getCommand() {

        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }
}
