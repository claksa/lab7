package commands;

import models.Ticket;

import java.util.ArrayList;

public class Connect extends AbstractCommand {

    @Override
    public String getDescription() {
        return "connect client with server";
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> connectCommand = new ArrayList<>();
        connectCommand.add("connected");
        return connectCommand;
    }

    public boolean execute(){
        return true;
    }
}
