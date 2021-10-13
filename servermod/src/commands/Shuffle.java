package commands;


import lib.CollectionManager;
import models.Ticket;
import server.Server;

import java.util.ArrayList;
import java.util.Collections;

public class Shuffle extends AbstractCommand {
    private final CollectionManager collectionManager;

    public Shuffle(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }


    @Override
    public String getDescription() {
        return " shuffle the elements of the collection at random\n";
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> shuffleCommand = new ArrayList<>();
        collectionManager.shuffle();
        shuffleCommand.add("shuffled\n");
        return shuffleCommand;
    }
}
