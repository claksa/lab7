package commands;

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
        if (Server.getDatabase().checkId(id)) {
            if(Server.getDatabase().removeByLowerId(id)) {
                removeLowerCommand.add("removed\n");
            } else {
                removeLowerCommand.add("error in removing");
            }
        } else {
            removeLowerCommand.add("This id already exists! Enter less");
        }
        return removeLowerCommand;
    }


    @Override
    public String getDescription() {
        return " remove all elements from the collection that are less than the given one\n";
    }

}
