package commands;

import exceptions.EmptyIOException;
import exceptions.NoSuchIdException;
import lib.CollectionManager;
import models.Ticket;
import server.Server;

import java.util.ArrayList;

public class RemoveLower extends AbstractCommand {
    private final CollectionManager collectionManager;

    public RemoveLower(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> removeLowerCommand = new ArrayList<>();
        try {
            if (argument.trim().isEmpty()) throw new EmptyIOException();
            if (!Server.getDatabase().checkId(id)) throw new NoSuchIdException();
            if (Server.getDatabase().removeByLowerId(id)){
                removeLowerCommand.add("ticket with such ID ("+ id + ") successfully removed");
            } else {
                removeLowerCommand.add("Error: difficulties with deleting a ticket with such ID ("+ id + ") in the database. Perhaps the ticket with this ID does not belong to you!");
            }
        } catch (EmptyIOException e) {
            removeLowerCommand.add("Error: you entered a null-argument");
        } catch (NoSuchIdException e) {
            removeLowerCommand.add("Error: no element with such id in collection");
        }
        return removeLowerCommand;
    }


    @Override
    public String getDescription() {
        return " remove all elements from the collection that are less than the given one\n";
    }

}
