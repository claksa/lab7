package commands;

import lib.CollectionManager;
import models.Ticket;
import server.Server;

import java.util.ArrayList;

public class Clear extends AbstractCommand {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }



    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> clearCommand = new ArrayList<>();
        if (Server.getDatabase().clearCollection()) {
            clearCommand.add("collection cleaned successfully");
        } else {
            clearCommand.add("Error: there is no way to clear the collection. Check your database connection.");
        }
        return clearCommand;
    }

    @Override
    public String getDescription() {
        return " clear collection\n";
    }
}
