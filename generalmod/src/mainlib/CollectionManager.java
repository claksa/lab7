package mainlib;

import models.Ticket;
import models.TicketType;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class CollectionManager {
    private List<Ticket> tickets;
    private final FileManager fileManager;


    public CollectionManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.tickets = fileManager.readData();
        sortCollection();
        fileManager.checkData(tickets);
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


    public List<Ticket> getTickets() {
        return tickets;
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
        Vector<Ticket> res = new Vector<>();
        for (Ticket t : tickets) {
            if (!t.getId().equals(update.getId())) {
                res.add(t);
            }
        }
        res.add(update);
        tickets = res;
    }

    public void addItem(Ticket ticket) {
        for (Ticket t : tickets) {
            tickets.add(ticket);
            System.out.println("added\n");
            return;
        }
    }

    public void addMin(Ticket ticket) {
        for (Ticket t : tickets) {
            if (ticket.getId() > t.getId()) {
                System.out.println("you cannot add\n");
                return;
            }
        }
        tickets.add(ticket);
    }


    public boolean isEqualId(Integer ID) {
        for (Ticket t : tickets) {
            if (t.getId().equals(ID)) {
                return false;
            }
        }
        return true;
    }


    public void remove(Integer id) {
        for (Ticket t : tickets) {
            if (t.getId().equals(id)) {
                tickets.remove(t);
                return;
            }
        }
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
            out.add(item.getKey() + " -- " + item.getValue());
        }
        return out;
    }

    public void sortCollection() {
        if (!tickets.isEmpty()) {
            tickets = tickets.stream().sorted((Comparator.comparing(o -> o.getVenue().getType()))).collect(Collectors.toList());
        }
    }
}
