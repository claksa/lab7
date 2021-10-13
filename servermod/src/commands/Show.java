package commands;

import lib.CollectionManager;
import models.Ticket;
import server.Server;

import java.util.ArrayList;

public class Show extends AbstractCommand {
    CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> showCommand = new ArrayList<>();
        showCommand.add(String.valueOf(Server.getDatabase().getTickets()));
        showCommand.add("\nshowed\n");
        return showCommand;
    }

    @Override
    public String getDescription() {
        return " print to stdout all elements of the collection in string representation\n";
    }
}
