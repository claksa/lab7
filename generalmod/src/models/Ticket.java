package models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Ticket implements Serializable {
    private  int id;
    private final String name;
    private final Coordinates coordinates;
    private final LocalDateTime creationDate;
    private final int price;
    private final TicketType type; //Поле может быть null
    private final Venue venue; //Поле может быть null
    private static Integer lastId = 0;

    public Ticket(String name,Coordinates coordinates, int price, TicketType type, Venue venue) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.price = price;
        this.type = type;
        this.venue = venue;
        lastId++;
        this.id = lastId;
    }


    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public TicketType getType() {
        return type;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public int getPrice() {
        return price;
    }

    public Venue getVenue() {
        return venue;
    }

    @Override
    public String toString() {
        return "\n"+"Ticket"+ "\n" +
                " { id =" + id + "\n" +
                "name='" + name + '\'' + "\n" +
                "coordinates=" + coordinates.toString() + "\n" +
                "price=" + price + "\n" +
                "type=" + type + "\n" +
                "venue=" + venue + "\n" +
                '}';
    }
}
