package commands;


import lib.CollectionManager;
import models.Ticket;

import java.util.ArrayList;

public class Exit extends AbstractCommand {
    private final CollectionManager collectionManager;

    public Exit(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * execute exit command and save collection to the file
     */

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id, String username) {
        ArrayList<String> exitCommand = new ArrayList<>();
//        collectionManager.save();
        System.exit(0);
        exitCommand.add("I am water!");
        return exitCommand;
    }

    @Override
    public String getDescription() {
        return " end the program (without saving to file)\n";
    }
}
