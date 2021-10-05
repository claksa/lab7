package lib;

import commands.Commandable;
import db.Database;
import exceptions.EmptyIOException;
import mainlib.FileManager;
import mainlib.Reader;

import java.util.ArrayList;
import java.util.List;

public class ListHolder {
    private static CommandFactory commandFactory;
    private static List<Commandable> cmdList;

    public ListHolder(CommandFactory commands) throws EmptyIOException {
        commandFactory = commands;

        if (commandFactory != null) {
            cmdList = commandFactory.getCommandsList(new FileManager());
        }

    }


    public static void setCommandFactory(CommandFactory commandFactory) {
        ListHolder.commandFactory = commandFactory;
    }

    public static CommandFactory getCommander() {
        return commandFactory;
    }

    public static List<Commandable> getCmdList() {
        return cmdList;
    }


}
