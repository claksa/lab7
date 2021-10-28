package commands;

import lib.CollectionManager;
import models.Ticket;

import java.util.ArrayList;

public class Connect extends AbstractCommand {
    CollectionManager collectionManager;

    public Connect(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }



    @Override
    public String getDescription() {
        return "connect client with server";
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id, String username) {
        ArrayList<String> connectCommand = new ArrayList<>();
//        if (Server.getDatabase().connect()) {
            connectCommand.add("connected");
//        } else {
//            Reader.PrintErr("database connection");
//        }
        return connectCommand;
    }

    public boolean execute(){
        return true;
    }
}
