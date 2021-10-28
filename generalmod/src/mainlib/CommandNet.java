package mainlib;

import models.Ticket;

import java.io.Serializable;
import java.util.Scanner;

public class CommandNet  implements Serializable {
    String[] enteredCommand;
    String username;
    Ticket ticket;
    Integer id;

    public CommandNet(String[] enteredCommand){
        this.enteredCommand = enteredCommand;
        TicketFactory ticketFactory = new TicketFactory(new Scanner(System.in));
        if (enteredCommand[0].equals("addmin") | enteredCommand[0].equals("remove") | enteredCommand[0].equals("removelower")) {
            this.id = ticketFactory.readInteger("Enter an id: ");
            this.username = ticketFactory.readString("Please, enter your username again for additional verification: ");
        }
        if (enteredCommand[0].equals("add") |enteredCommand[0].equals("addmin") | enteredCommand[0].equals("update") ){
            this.ticket = ticketFactory.getTicketObj();
            this.username = ticketFactory.readString("Please, enter your username again for correct work: ");
            this.ticket.setName(username);
        }
    }

    public String[] getEnteredCommand() {
        return enteredCommand;
    }

    public String getUsername() {
        return username;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Integer getId() {
        return id;
    }




}
