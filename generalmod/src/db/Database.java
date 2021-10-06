package db;

import exceptions.EmptyIOException;
import mainlib.Reader;
import models.*;
import server.Server;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

public class Database {
    private static final Logger log = Logger.getLogger(Database.class.getName());
    private static final String URL = "jdbc:postgresql://localhost:5674/studs";
    private static final String LOGIN = "s312196";
    private static final String PASSWORD = "msw447";
    Connection connection;
    private boolean isValid;


    public boolean connect() {
        try {
            Class.forName("org.postgresql.Driver");
            log.info("JDBC Driver has been successfully loaded");
            connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            log.info("database connection successfully established");
            isValid = connection.isValid(2);
        } catch (SQLException throwables) {
            Reader.PrintErr("no ways to get connection with database");
        } catch (ClassNotFoundException e) {
            Reader.PrintErr("PostgreSQL JDBC Driver is not found. Include it in your library path");
        }
        return isValid;
    }

    public boolean addToDatabase(Ticket ticket) {
        boolean isAddedToDB = false;
        String statement = "INSERT INTO tickets(ticket, coordX, coordY, creationDate, price, ticketType, venue, capacity, venueType, street, zipCode, venueX, venueY, venueZ, town) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            if (isValid()) {
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setString(1, ticket.getName());
                preparedStatement.setDouble(2, ticket.getCoordinates().getX());
                preparedStatement.setInt(3, ticket.getCoordinates().getY());
                preparedStatement.setString(4, String.valueOf(ticket.getCreationDate()));
                preparedStatement.setInt(5, ticket.getPrice());
                preparedStatement.setString(6, String.valueOf(ticket.getType()));
                preparedStatement.setString(7, ticket.getVenue().getName());
                preparedStatement.setInt(8, ticket.getVenue().getCapacity());
                preparedStatement.setString(9, String.valueOf(ticket.getVenue().getType()));
                preparedStatement.setString(10, ticket.getVenue().getAddress().getStreet());
                preparedStatement.setString(11, ticket.getVenue().getAddress().getZipCode());
                preparedStatement.setFloat(12, ticket.getVenue().getAddress().getTown().getX());
                preparedStatement.setInt(13, ticket.getVenue().getAddress().getTown().getY());
                preparedStatement.setInt(14, ticket.getVenue().getAddress().getTown().getZ());
                preparedStatement.setString(15, ticket.getVenue().getAddress().getTown().getName());
                int i = preparedStatement.executeUpdate();
                log.info("add object: " + i);
                if (i != 0) {
                    isAddedToDB = true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return isAddedToDB;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public Vector<Ticket> getTickets() {
        String statement = "SELECT * from tickets";
        Vector<Ticket> tickets = new Vector<>();
        int lastId = 0;
        Statement stm;
        try {
            if (isValid()) {
                stm = Server.getDatabase().getConnection().createStatement();
                ResultSet resultSet = stm.executeQuery(statement);
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    lastId = id;
                    String name = resultSet.getString("ticket");
                    double coordX = resultSet.getDouble("coordX");
                    int coordY = resultSet.getInt("coordY");
                    String date = resultSet.getString("creationDate");
                    int price = resultSet.getInt("price");
                    TicketType ticketType = TicketType.valueOf(resultSet.getString("ticketType"));
                    long venueId = resultSet.getLong("venueId");
                    String venue = resultSet.getString("venue");
                    int capacity = resultSet.getInt("capacity");
                    VenueType venueType = VenueType.valueOf(resultSet.getString("venueType"));
                    String street = resultSet.getString("street");
                    String zipCode = resultSet.getString("zipcode");
                    float venueX = resultSet.getFloat("venueX");
                    int venueY = resultSet.getInt("venueY");
                    int venueZ = resultSet.getInt("venueZ");
                    String town = resultSet.getString("town");
                    Location location = new Location(venueX, venueY, venueZ, town);
                    Address address = new Address(street, zipCode, location);
                    Venue venue1 = new Venue(venue, capacity, venueType, address);
                    venue1.setId(venueId);
                    Coordinates coordinates = new Coordinates(coordX, coordY);
                    Ticket ticket = new Ticket(name, coordinates, price, ticketType, venue1);
                    ticket.setId(id);
                    ticket.setCreationDate(LocalDateTime.parse(date));
                    tickets.add(ticket);
                }
                Ticket.setLastId(lastId);
            } else {
                throw new EmptyIOException();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (EmptyIOException e) {
            Reader.PrintErr("cannot find jdbc driver");
        }
        return tickets;
    }

    public boolean isValid() {
        return isValid;
    }

    public Connection getConnection() {
        return connection;
    }
}
