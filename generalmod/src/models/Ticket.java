package models;


import java.io.Serializable;
import java.time.LocalDateTime;

public class Ticket implements Serializable {
    private  int id;
    private final String name;
    private final Coordinates coordinates;
    private LocalDateTime creationDate;
    private final int price;
    private final TicketType type;
    private final Venue venue;
    private static Integer lastId = 0;

    public Ticket(String name,Coordinates coordinates, int price, TicketType type, Venue venue) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.price = price;
        this.type = type;
        this.venue = venue;
        this.id = getIncLastId();
    }

//    private void generateId(){
//        for (Integer id: CollectionManager.getIds()) {
//            if (id.equals(this.id)){
//                this.id = getIncLastId();
//            }
//        }
//    }



    private static Integer getIncLastId(){
        return ++lastId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public static void setLastId(Integer lastId) {
        Ticket.lastId = lastId;
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
