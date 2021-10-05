package models;

import java.io.Serializable;

public class Venue implements Serializable {
    private  long id;
    private final String name;
    private final Integer capacity;
    private final VenueType type;
    private final Address address;
    private static long lastVenueId = 0;

    public Venue(String name, Integer capacity, VenueType type, Address address) {
        this.name = name;
        this.capacity = capacity;
        this.type = type;
        this.address = address;
        lastVenueId++;
        this.id = lastVenueId;
    }

    public long getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public VenueType getType() {
        return type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", type=" + type +
                ", address=" + address.toString() +
                '}';
    }
}
