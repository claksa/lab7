package commands;

import lib.CollectionManager;
import models.Ticket;

import java.util.ArrayList;

public class Clear extends AbstractCommand {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }



    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        collectionManager.clear();
        ArrayList<String> clearCommand = new ArrayList<>();
        clearCommand.add("collection cleaned successfully");
        return clearCommand;
    }

    @Override
    public String getDescription() {
        return " clear collection\n";
    }
}
