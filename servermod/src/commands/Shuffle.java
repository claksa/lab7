package commands;


import lib.CollectionManager;
import models.Ticket;

import java.util.ArrayList;

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
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id, String username) {
        ArrayList<String> shuffleCommand = new ArrayList<>();
        collectionManager.shuffle();
        shuffleCommand.add("shuffled\n");
        return shuffleCommand;
    }
}
