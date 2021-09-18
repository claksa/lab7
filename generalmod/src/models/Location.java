package models;

import java.io.Serializable;

public class Location implements Serializable {
    private final Float x;
    private final Integer y;
    private final Integer z;
    private final String name;

    public Location (Float x, Integer y, Integer z, String name){
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public Float getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", name='" + name + '\'' +
                '}';
    }
}