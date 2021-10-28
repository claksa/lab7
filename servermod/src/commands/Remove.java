package commands;

import exceptions.EmptyIOException;
import exceptions.NoSuchIdException;
import lib.CollectionManager;
import models.Ticket;
import server.Server;

import java.util.ArrayList;

public class Remove extends AbstractCommand {
    private final CollectionManager collectionManager;


    public Remove(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }


    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id, String username) {
        ArrayList<String> removeCommand = new ArrayList<>();
        try {
            if (argument.trim().isEmpty()) throw new EmptyIOException();
            if (!Server.getDatabase().checkId(id)) throw new NoSuchIdException();
            if (Server.getDatabase().removeById(id,username)){
                removeCommand.add("ticket with such ID ("+ id + ") successfully removed");
            } else {
                removeCommand.add("Error: difficulties with deleting a ticket with such ID ("+ id + ") in the database. Perhaps the ticket with this ID does not belong to you!");
            }
        } catch (EmptyIOException e) {
            removeCommand.add("Error: you entered a null-argument");
        } catch (NoSuchIdException e) {
            removeCommand.add("Error: no element with such id in collection");
        }
        return removeCommand;
    }

    @Override
    public String getDescription() {
        return " (id) remove an item from the collection by its id\n";
    }

}
