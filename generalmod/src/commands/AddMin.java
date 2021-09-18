package commands;

import mainlib.CollectionManager;
import models.Ticket;

import java.util.ArrayList;

public class AddMin extends AbstractCommand {
    private final CollectionManager collectionManager;

    public AddMin(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> addMinCommand = new ArrayList<>();
        if (collectionManager.isEqualId(id)) {
            collectionManager.addMin(ticket);
            addMinCommand.add("min item added\n");
        } else addMinCommand.add("such an ID already exists, alas");
        return addMinCommand;
    }

    @Override
    public String getDescription() {
        return " add a new item to a collection if its value is less than the smallest item in this collection\n";
    }
}
