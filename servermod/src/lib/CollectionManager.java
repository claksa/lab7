package lib;

import exceptions.EmptyIOException;
import models.Ticket;
import models.TicketType;
import server.RequestDataHandler;
import server.Server;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CollectionManager {
    static List<Integer> ids  = new ArrayList<>();
    List<Ticket> tickets;


    public CollectionManager() throws EmptyIOException {
        this.tickets = Server.getDatabase().getTickets();
        setIdList();
    }

    public String show(){
        return String.valueOf(getTickets());
    }

    public void shuffle(){
        Collections.shuffle(getTickets());
    }


    public void setIdList(){
        ids = getTickets().stream().map(Ticket::getId).collect(Collectors.toList());
    }

    public static List<Integer> getIds() {
        return ids;
    }

    public String getInformation() {
        String dataSimpleName = getTickets().getClass().getSimpleName();
        ZonedDateTime creationDate = ZonedDateTime.now();
        String size = String.valueOf(getTickets().size());
        String res = "";
        res += "\n" + "collection type: " + dataSimpleName + "\n";
        res += "collection creation time: " + creationDate + "\n";
        res += "number of items in the collection: " + size + "\n";
        return res;
    }


    public String startsWithSubstring(String substr) {
        StringBuilder list = new StringBuilder();
        getTickets().stream().filter((s) -> s.getName().startsWith(substr.trim())).forEach(list::append);
        return list.toString();
    }

    public String containsSomeSubstring(String substr) {
        StringBuilder list = new StringBuilder();
        getTickets().stream().filter((s) -> s.getName().contains(substr.trim())).forEach(list::append);
        return list.toString();
    }


    public ArrayList<String> groupCount() {
        ArrayList<String> out = new ArrayList<>();
        Map<TicketType, Long> ticketsByType = getTickets().stream().collect(Collectors.groupingBy(Ticket::getType, Collectors.counting()));
        for (Map.Entry<TicketType, Long> item : ticketsByType.entrySet()) {
            out.add(item.getKey() + " - " + item.getValue());
        }
        return out;
    }


    public void sortCollection() {
        if (!getTickets().isEmpty()) {
            tickets = getTickets().stream().sorted((Comparator.comparing(o -> o.getVenue().getType()))).collect(Collectors.toList());
        }
    }


    public List<Ticket> getTickets() {
        return Server.getDatabase().getTickets();
    }
}
