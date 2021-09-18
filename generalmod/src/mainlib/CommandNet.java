package mainlib;

import models.Ticket;

import java.io.Serializable;
import java.util.Scanner;

public class CommandNet implements Serializable {
    String[] enteredCommand;
    Ticket ticket;
    Integer id;

    public CommandNet(String[] enteredCommand){
        this.enteredCommand = enteredCommand;
        ConsoleManager consoleManager = new ConsoleManager(new Scanner(System.in));
        if (enteredCommand[0].equals("addmin") | enteredCommand[0].equals("remove") | enteredCommand[0].equals("removelower")) {
            this.id = consoleManager.readInteger("Enter an id");
        }
        if (enteredCommand[0].equals("add") | enteredCommand[0].equals("addmin") | enteredCommand[0].equals("update") | enteredCommand[0].equals("removelower")){
            this.ticket = consoleManager.getTicketObj(); //работает
        }
    }

    public String[] getEnteredCommand() {
        return enteredCommand;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Integer getId() {
        return id;
    }


}
