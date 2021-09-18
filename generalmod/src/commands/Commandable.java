package commands;

import models.Ticket;

import java.util.ArrayList;

/**
 * interface with methods that implement command classes
 */
public interface Commandable  {
    String getDescription();
    ArrayList<String> execute (String argument, Ticket ticket, Integer id);

    default String getName() {
        return getClass().getSimpleName().toLowerCase();
    }

}
