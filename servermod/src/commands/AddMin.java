package commands;

import lib.CollectionManager;
import models.Ticket;
import server.Server;

import java.util.ArrayList;

public class AddMin extends AbstractCommand {
    CollectionManager collectionManager;

    public AddMin(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> addMinCommand = new ArrayList<>();
            if (Server.getDatabase().checkId(id)) {
                if (Server.getDatabase().addIfMin(ticket)) {
                    addMinCommand.add("an element smaller than the entered ID (" + id + ")does not exist --> " + "your ticket successfully added");
                } else {
                    addMinCommand.add("Error adding ticket. For execution, you must enter the ID strictly less than the minimum");
                }
            }
        return addMinCommand;
    }

    @Override
    public String getDescription() {
        return " add a new item to a collection if its value is less than the smallest item in this collection\n";
    }

}
