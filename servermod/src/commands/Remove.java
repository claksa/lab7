package commands;

import exceptions.EmptyIOException;
import exceptions.NoSuchIdException;
import lib.CollectionManager;
import models.Ticket;
import server.Server;

import java.util.ArrayList;

public class Remove extends DBCommand {
    private final CollectionManager collectionManager;


    public Remove(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }


    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> removeCommand = new ArrayList<>();
        try {
            if (argument.trim().isEmpty()) throw new EmptyIOException();
            if (Server.getDatabase().checkId(id)) throw new NoSuchIdException();
        } catch (EmptyIOException e) {
            removeCommand.add("Error: you entered a null-argument");
        } catch (NoSuchIdException e) {
            removeCommand.add("Error: no element with such id in collection");
        }
        if (Server.getDatabase().removeById(id,ticket)) {
            collectionManager.remove(id);
            removeCommand.add("removed\n");
        } else {
            removeCommand.add("problems with removing");
        }
        return removeCommand;
    }

    @Override
    public String getDescription() {
        return " (id) remove an item from the collection by its id\n";
    }

    @Override
    void connectToDB() {

    }
}
