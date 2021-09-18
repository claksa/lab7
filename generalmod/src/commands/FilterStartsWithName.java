package commands;

import exceptions.EmptyIOException;
import mainlib.CollectionManager;
import models.Ticket;

import java.util.ArrayList;

public class FilterStartsWithName extends AbstractCommand {
    private final CollectionManager collectionManager;

    public FilterStartsWithName(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }


    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> filterStartsCommand = new ArrayList<>();
        if (argument.isEmpty()) {
            try {
                throw new EmptyIOException();
            } catch (EmptyIOException e) {
                filterStartsCommand.add("Error: you enter null-argument");
            }
        }
        String list = collectionManager.startsWithSubstring(argument);
        filterStartsCommand.add(list);
        if (list.isEmpty()) {
            filterStartsCommand.add("Error: no such element");
        }
        return filterStartsCommand;
    }

    @Override
    public String getDescription() {
        return " (name) display elements whose name field value begins with a given substring\n";
    }
}
