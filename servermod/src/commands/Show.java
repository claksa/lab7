package commands;

import lib.CollectionManager;
import models.Ticket;

import java.util.ArrayList;

public class Show extends AbstractCommand {
    CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id, String username) {
        ArrayList<String> showCommand = new ArrayList<>();
        showCommand.add(collectionManager.show());
        showCommand.add("\nshowed\n");
        return showCommand;
    }

    @Override
    public String getDescription() {
        return " print to stdout all elements of the collection in string representation\n";
    }
}
