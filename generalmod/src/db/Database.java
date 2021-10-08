package db;

import exceptions.EmptyIOException;
import mainlib.Reader;
import models.*;
import server.Server;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
        String statement = "INSERT INTO tickets(ticket, coordinate1, coordinate2, creation, price, valuation, venue, place, street, zip, coordinate3, coordinate4, coordinate5, town,capacity) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            if (isValid()) {
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setString(1, UserManager.getName());
                preparedStatement.setDouble(2, ticket.getCoordinates().getX());
                preparedStatement.setInt(3, ticket.getCoordinates().getY());
                preparedStatement.setString(4, String.valueOf(ticket.getCreationDate()));
                preparedStatement.setInt(5, ticket.getPrice());
                preparedStatement.setString(6, String.valueOf(ticket.getType()));
                preparedStatement.setString(7, ticket.getVenue().getName());
                preparedStatement.setString(8, String.valueOf(ticket.getVenue().getType()));
                preparedStatement.setString(9, ticket.getVenue().getAddress().getStreet());
                preparedStatement.setString(10, ticket.getVenue().getAddress().getZipCode());
                preparedStatement.setFloat(11, ticket.getVenue().getAddress().getTown().getX());
                preparedStatement.setInt(12, ticket.getVenue().getAddress().getTown().getY());
                preparedStatement.setInt(13, ticket.getVenue().getAddress().getTown().getZ());
                preparedStatement.setString(14, ticket.getVenue().getAddress().getTown().getName());
                preparedStatement.setInt(15, ticket.getVenue().getCapacity());
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

    public boolean addIfMin(Ticket ticket) {
        boolean isAdded = false;
        Integer min = getTickets().stream().map(Ticket::getId).reduce(Integer::compareTo).orElse(-1);
        if (ticket.getId() < min) {
            isAdded = addToDatabase(ticket);
        }
        return isAdded;
    }

    public boolean checkId(Integer id){
        String statement = "SELECT FROM tickets WHERE id=?";
        ResultSet resultSet = null;
        int count = 0;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                count++;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count > 0;
    }

    public boolean removeById(Integer id, Ticket ticket) {
        String statement = "DELETE FROM tickets WHERE (id = ?) AND (ticket = ?)";
        try {
            if (isValid()) {
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, ticket.getName());
                if (preparedStatement.executeUpdate() != 0) {
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean removeByLowerId(Ticket ticket, Integer id) {
        String statement = "DELETE FROM tickets WHERE (id < ?) AND (ticket=?)";
        if (isValid()) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, ticket.getName());
                if (preparedStatement.executeUpdate() != 0) {
                    return true;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    public boolean updateCollection(Ticket update){
        boolean isUpdated = false;
        List<Ticket> tickets = getTickets().stream().map(ticket -> ticket.getId().equals(update.getId()) ? update : ticket).collect(Collectors.toCollection(Vector::new));
        for (Ticket t: tickets){
            isUpdated = addToDatabase(t);
        }
        return isUpdated;
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
//                    String name = resultSet.getString("ticket");
                    double coordX = resultSet.getDouble("coordinate1");
                    int coordY = resultSet.getInt("coordinate2");
                    int price = resultSet.getInt("price");
                    TicketType ticketType = TicketType.valueOf(resultSet.getString("valuation"));
                    long venueId = resultSet.getLong("id2");
                    String venue = resultSet.getString("venue");
                    int capacity = resultSet.getInt("capacity");
                    VenueType venueType = VenueType.valueOf(resultSet.getString("place"));
                    String street = resultSet.getString("street");
                    String zipCode = resultSet.getString("zip");
                    float venueX = resultSet.getFloat("coordinate3");
                    int venueY = resultSet.getInt("coordinate4");
                    int venueZ = resultSet.getInt("coordinate5");
                    String town = resultSet.getString("town");
                    String date = resultSet.getString("creation");
                    Location location = new Location(venueX, venueY, venueZ, town);
                    Address address = new Address(street, zipCode, location);
                    Venue venue1 = new Venue(venue, capacity, venueType, address);
                    venue1.setId(venueId);
                    Coordinates coordinates = new Coordinates(coordX, coordY);
                    Ticket ticket = new Ticket(UserManager.getName(),coordinates, price, ticketType, venue1);
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
