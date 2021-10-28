package commands;

import lib.CollectionManager;
import models.Ticket;
import server.Server;

import java.util.ArrayList;

public class AddMin extends AbstractCommand {
    CollectionManager collectionManager;

    public AddMin(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id, String username) {
        ArrayList<String> addMinCommand = new ArrayList<>();
        if (Server.getDatabase().addIfMin(ticket,username)) {
            addMinCommand.add("the new item added to the collection\n");
        } else {
            addMinCommand.add("error with adding to the collection");
        }
        return addMinCommand;
    }

    @Override
    public String getDescription() {
        return " add a new item to a collection if its value is less than the smallest item in this collection\n";
    }

}
