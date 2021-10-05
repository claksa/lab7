package lib;

import exceptions.EmptyIOException;
import mainlib.FileManager;
import models.Ticket;
import models.TicketType;
import server.Server;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CollectionManager {
    private final FileManager fileManager;
    static List<Integer> ids  = new ArrayList<>();
    List<Ticket> tickets;


    public CollectionManager(FileManager fileManager) throws EmptyIOException {
        this.fileManager = fileManager;
        this.tickets = Server.getDatabase().getTickets();
//      tickets = fileManager.readData();
        setIdList();
        sortCollection();
        fileManager.checkData(tickets);
    }


    public void setIdList(){
        ids = tickets.stream().map(Ticket::getId).collect(Collectors.toList());
    }

    public static List<Integer> getIds() {
        return ids;
    }

    public String getInformation() {
        String dataSimpleName = tickets.getClass().getSimpleName();
        ZonedDateTime creationDate = ZonedDateTime.now();
        String size = String.valueOf(tickets.size());
        String res = "";
        res += "\n" + "collection type: " + dataSimpleName + "\n";
        res += "collection creation time: " + creationDate + "\n";
        res += "number of items in the collection: " + size + "\n";
        return res;
    }

    public String getStringElements() {
        return String.valueOf(tickets);
    }


    public void save() {
        fileManager.saveData(tickets);
    }


    public Integer getID() {
        int maxID = 0;
        for (Ticket t : tickets) {
            int id = t.getId();
            if (maxID < id) {
                maxID = id;
            }
        }
        return maxID + 1;
    }

    public void update(Ticket update) {
        tickets = tickets.stream().map(ticket -> ticket.getId().equals(update.getId()) ? update : ticket).collect(Collectors.toCollection(Vector::new));
    }

    public void addItem(Ticket ticket) {
          tickets.add(ticket);
    }

    public boolean addIfMin(Ticket ticket) {
        boolean isAdded = false;
        Integer min = tickets.stream().map(Ticket::getId).reduce(Integer::compareTo).orElse(-1);
        if (ticket.getId() < min) {
            isAdded = tickets.add(ticket);
        }
        return isAdded;
    }


    public boolean isEqualId(Integer ID) {
        for (Ticket t : tickets) {
            if (t.getId().equals(ID)) {
                return false;
            }
        }
        return true;
    }


    public boolean remove(Integer id) {
        return tickets.removeIf(t -> t.getId().equals(id));
    }

    public boolean removeIfLowerId(Ticket ticket) {
        return tickets.removeIf(ticket1 -> (ticket.getId() > ticket1.getId()));
    }


    public String startsWithSubstring(String substr) {
        StringBuilder list = new StringBuilder();
        tickets.stream().filter((s) -> s.getName().startsWith(substr.trim())).forEach(list::append);
        return list.toString();
    }

    public String containsSomeSubstring(String substr) {
        StringBuilder list = new StringBuilder();
        tickets.stream().filter((s) -> s.getName().contains(substr.trim())).forEach(list::append);
        return list.toString();
    }

    public void clear() {
        tickets.clear();
    }

    public ArrayList<String> groupCount() {
        ArrayList<String> out = new ArrayList<>();
        Map<TicketType, Long> ticketsByType = tickets.stream().collect(Collectors.groupingBy(Ticket::getType, Collectors.counting()));
        for (Map.Entry<TicketType, Long> item : ticketsByType.entrySet()) {
            out.add(item.getKey() + " - " + item.getValue());
        }
        return out;
    }

    public void sortCollection() {
        if (!tickets.isEmpty()) {
            tickets = tickets.stream().sorted((Comparator.comparing(o -> o.getVenue().getType()))).collect(Collectors.toList());
        }
    }

}
