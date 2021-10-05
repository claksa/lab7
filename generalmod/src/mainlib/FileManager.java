package mainlib;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import exceptions.EmptyIOException;
import exceptions.IncorrectValueException;
import exceptions.LackOfAccessException;
import mainlib.Reader;
import models.Location;
import models.Ticket;
import models.Venue;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

public class FileManager {
    public static final String PATH = "out.json";


    /**
     * the collection to be loaded into the file
     * @return vector
     */
    public Vector<Ticket> readData() {

        Gson gson = new Gson();
        try {
            if (!Files.isReadable(Paths.get(PATH))) {
                throw new LackOfAccessException();
            }
            BufferedReader reader = new BufferedReader(new FileReader(PATH));
            Type dataType = new TypeToken<Vector<Ticket>>() {
            }.getType();
            String json = reader.readLine();
            if (json == null) {
                return new Vector<>();
            }
            Vector<Ticket> data = gson.fromJson(json.trim(), dataType);
            mainlib.Reader.PrintMsg("Collection read from file");
            return data;
        } catch (FileNotFoundException e) {
            mainlib.Reader.PrintErr(" file not found");
        } catch (NoSuchElementException e) {
            mainlib.Reader.PrintErr("file is empty");
        } catch (JsonParseException e) {
            mainlib.Reader.PrintErr("Another collection in the file");
        } catch (LackOfAccessException e) {
            mainlib.Reader.PrintErr("no read rights");
        } catch (IOException e) {
            mainlib.Reader.PrintErr(" Houston, have problems. IOException...");
        }
        return new Vector<>();
    }

    /**
     * saves collection to file
     *
     * @param tickets is a collection to save
     */
    public void saveData(List<Ticket> tickets) {

        /*GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.setPrettyPrinting().create();*/

        Gson gson = new Gson();
        try {
            if (!Files.isWritable(Paths.get(PATH))) {
                throw new LackOfAccessException();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(PATH));
            writer.write(gson.toJson(tickets));
            mainlib.Reader.PrintMsg("Collection saved successfully\n");
            writer.close();
        } catch (LackOfAccessException e) {
            mainlib.Reader.PrintErr("no write rights");
        } catch (IOException e) {
            mainlib.Reader.PrintErr("there is no file to save");
        }
    }

    /**
     * additional check if the collection in the file was changed manually
     *
     * @param tickets collection<Ticket></Ticket>
     */

    public void checkData(List<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            int id = ticket.getId();
            String name = ticket.getName();
            LocalDateTime time = ticket.getCreationDate();
            int price = ticket.getPrice();

            double x = ticket.getCoordinates().getX();
            Integer y = ticket.getCoordinates().getY();

            Venue venue = ticket.getVenue();
            long idV = venue.getId();
            String nameV = venue.getName();
            Integer capacity = venue.getCapacity();

            String zipCode = venue.getAddress().getZipCode();

            Location location = venue.getAddress().getTown();
            Float xLoc = location.getX();
            Integer yLoc = location.getY();
            Integer zLoc = location.getZ();
            String nameLoc = location.getName();

            try {
                if (name.equals("") | nameV.equals("") | xLoc == null | yLoc == null | zLoc == null | nameLoc == null | zipCode == null | time == null | y == null) {
                    throw new EmptyIOException();
                }
                if (id < 0 | price < 0 | idV < 0 | capacity < 0) {
                    mainlib.Reader.PrintErr(" negative or zero value");
                    throw new IncorrectValueException();
                }
                if (x > 518 | y > 332) {
                    mainlib.Reader.PrintErr(" value is greater than the maximum (x_max=518; y_max=332)");
                    throw new IncorrectValueException();
                }
            } catch (EmptyIOException | IncorrectValueException e) {
                Reader.PrintMsg("Working with the collection will be incorrect.\n" +
                        "Exit the program.");
                System.exit(0);
            }
        }
    }
}
