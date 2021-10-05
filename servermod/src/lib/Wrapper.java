package lib;

import commands.Commandable;
import mainlib.CommandNet;
import models.Ticket;

import java.io.Serializable;


public class Wrapper implements Serializable {
    String command;
    String argument;


    public String getWrappedCommand(CommandNet cmd) {
        String[] command = cmd.getEnteredCommand();
        argument = command[1];
        for (Commandable each : ListHolder.getCmdList()) {
            if (!command[0].equals("")) {
                if (command[0].equals(each.getName())) {
                    this.command = command[0];
                }
            }
        }
        return this.command;
    }

    public Ticket getWrappedTicket(CommandNet cmd) {
        return cmd.getTicket();
    }

    public Integer getWrappedId(CommandNet cmd){
        return cmd.getId();
    }

    public String getArgument() {
        return argument;
    }

}
