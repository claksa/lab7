package commands;

import exceptions.EmptyIOException;
import exceptions.NoSuchIdException;
import lib.CollectionManager;
import models.Ticket;

import java.util.ArrayList;

public class Update extends AbstractCommand {
    private final CollectionManager collectionManager;


    public Update(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> updateCommand = new ArrayList<>();
        try {
            if (argument.trim().equals("")) throw new EmptyIOException();
            id = Integer.parseInt(argument.trim());
            if (collectionManager.isEqualId(id)) throw new NoSuchIdException();
        } catch (NumberFormatException e) {
            updateCommand.add("Error: you enter not number value");
        } catch (EmptyIOException e) {
            updateCommand.add("Error: you enter null-argument");
        } catch (NoSuchIdException e) {
            updateCommand.add("Error: No such ID in collection");
        }
        collectionManager.update(ticket);
        updateCommand.add("updated\n");
        return updateCommand;
    }

    @Override
    public String getDescription() {
        return " (id) update the value of the collection element whose id is equal to the given\n";
    }
}
