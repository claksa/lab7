package commands;

import lib.CollectionManager;
import models.Ticket;
import server.Server;

import java.util.ArrayList;

public class Add extends DBCommand  {
    private final CollectionManager collectionManager;

    public Add(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> addCommand = new ArrayList<>();
        if(Server.getDatabase().isValid()) {
            if (Server.getDatabase().addToDatabase(ticket)) {
                collectionManager.addItem(ticket);
                addCommand.add("the new item added to the collection\n");
            } else {
                addCommand.add("error with adding to the collection");
            }
            return addCommand;
        } else {
            addCommand.add(" error in adding to DB");
        }
        return addCommand;
    }

    @Override
    public String getDescription() {
        return "add new element to collection\n";
    }

    @Override
    void connectToDB() {


    }
}
