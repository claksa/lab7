package commands;

import models.Ticket;

import java.util.ArrayList;
import java.util.List;

public class Help extends AbstractCommand {

    private final List<Commandable> commandsAvailable;


    public Help(List<Commandable> commandsAvailable) {
        this.commandsAvailable = commandsAvailable;
        commandsAvailable.add(0, this);
    }

    @Override
    public ArrayList<String> execute(String argument, Ticket ticket, Integer id) {
        ArrayList<String> helpCommand = new ArrayList<>();
        StringBuilder res = new StringBuilder();
        for (Commandable commandable : commandsAvailable) {
            res.append(commandable.getName()).append(" - ").append(commandable.getDescription()).append("\n");
        }
        helpCommand.add(res.toString());
        helpCommand.add("\ncommand help executed\n");
        return helpCommand;
    }

    @Override
    public String getDescription() {
        return " display help for available server.models.lib.commands\n";
    }

    public List<Commandable> getCommandsAvailable() {
        return commandsAvailable;
    }
}
