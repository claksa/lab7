package commands;


import lib.ListHolder;
import mainlib.Answer;
import models.Ticket;

import java.io.Serializable;
import java.util.ArrayList;

import static mainlib.Reader.PrintErr;
import static mainlib.Reader.PrintMsg;


public class Executor implements Serializable {


    public Answer execute(String receivedCommand, String argument, Ticket ticket, Integer id) {
        boolean isRightCommand = false;
        Commandable commandToExecute = null;
        Answer answer = null;
        if (receivedCommand != null) {
            for (Commandable listCommand : ListHolder.getCmdList()) {
                if (receivedCommand.equals(listCommand.getName())) {
                    PrintMsg("it is a right command");
                    commandToExecute = listCommand;
                    isRightCommand = true;
                    break;
                }
            }
            if (isRightCommand) {
                answer = new Answer(commandToExecute.execute(argument,ticket, id));
                PrintMsg("command was successfully executed!\n");
            }
        } else {
            ArrayList<String> list = new ArrayList<>();
            String error = "it isn't a right command. Theres is no way to execute this command";
            PrintErr(error);
            list.add(error);
            list.add("You can enter 'help' to get list about available commands!");
            answer = new Answer(list);
        }
        return answer;
    }

}
