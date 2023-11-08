package org.didenko.command;

import lombok.Getter;

import java.util.Arrays;

public enum Commands {

    PORTFOLIO_LIST("/portfolios"),
    AUTHENTICATION("/authenticate");

    private final String commandValue;

    private Commands(String commandValue){
        this.commandValue = commandValue;
    }

    public String getCommandValue() {
        return commandValue;
    }

    public static Commands fromString(String commandValue){
        for (Commands c : Commands.values()){
            if(c.getCommandValue().equals(commandValue))
                return c;
        }
        throw new IllegalArgumentException();
    }
}
