package mainlib;

import models.*;

import java.util.Scanner;

public class ConsoleManager extends Reader {

    public ConsoleManager(Scanner scanner) {
        super(scanner);
    }

    /**
     * getter for the ticket object
     * @return ticket object
     */

    public Ticket getTicketObj() {
//        PrintMsg("Ticket ID: " + id );
        String name = readString("Enter your name for the ticket: ");

        double x;
        while (true) {
            x = readDouble("Enter a x-coordinate for ticket:");
            if (x > 518) {
                PrintMsg("The maximum possible value of the x-coordinate = 518");
            } else {
                break;
            }
        }

        Integer y;
        while (true) {
            y = readInteger("Enter a y-coordinate for ticket:");
            if (y > 332) {
                PrintMsg("The maximum possible value of the y-coordinate = 332");
            } else {
                break;
            }
        }

        Coordinates coordinates = new Coordinates(x, y);


//        LocalDateTime time = LocalDateTime.now();
//        PrintMsg("CreationDate " + time);

        Integer price;
        while (true) {
            price = readInteger("Enter the price of the ticket (integer format):");
            if (price < 0) {
                PrintMsg("Price of the ticket cannot be negative lmao");
            } else {
                break;
            }
        }

        TicketType ticketType = readEnumType(TicketType.class, "Enter a ticket type:");

        return new Ticket(name,coordinates, price, ticketType, getVenueObj());
    }

    /**
     * getter for the venue obj
     * @return venue obj
     */

    private Venue getVenueObj() {
        PrintMsg("Venue ID: " );

        String venueName = readString("Enter a venue name:");
        Integer capacity;
        while (true) {
            capacity = readInteger("Enter the capacity of the venue (integer format):");
            if (capacity < 0) {
                PrintMsg("Capacity cannot be negative lmao");
            } else {
                break;
            }
        }

        VenueType venueType = readEnumType(VenueType.class, "Enter a venue type:");

        String street = readString("Enter a venue street:");
        String zipCode = readString("Enter a venue zipCode:");

        Float x = readFloat("Enter an address location x-coordinate (float format):");
        Integer y = readInteger("Enter an address location y-coordinate (integer format):");
        Integer z = readInteger("Enter an address location z-coordinate (integer format):");
        String locationName = readString("Enter the name of the town:");
        Location town = new Location(x, y, z, locationName);

        Address address = new Address(street, zipCode, town);

        return new Venue(venueName, capacity, venueType, address);
    }


}
