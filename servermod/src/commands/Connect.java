package commands;

import lib.CollectionManager;
import mainlib.Reader;
import models.Ticket;
import server.Server;

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
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
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
