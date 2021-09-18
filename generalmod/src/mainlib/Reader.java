package mainlib;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Reader {
    Scanner scanner;


    public Reader(Scanner scanner) {
        this.scanner = scanner;
    }

    public static void PrintMsg(String msg) {
        System.out.println(msg);
    }

    public static void PrintErr(String msg) {
        System.out.println("Error: " + msg);
    }

    public String readString(String msg) {
        String out = null;
        while (out == null) {
            PrintMsg(msg);
            String buffer = scanner.nextLine().trim();
            if (!buffer.isEmpty()) {
                out = buffer;
            } else {
                PrintErr("Please, enter a non-null value\n" + msg);
            }
        }
        return out;
    }

    public Integer readInteger(String msg) {
        Integer out = null;
        while (out == null) {
            PrintMsg(msg);
            String buffer = scanner.nextLine().trim();
            if (!buffer.isEmpty()) {
                out = Integer.parseInt(buffer);
            } else PrintErr("Please, enter a non-null value\n");
        }
        return out;
    }

    public Double readDouble(String msg) {
        Double out = null;
        while (out == null) {
            PrintMsg(msg);
            String buffer = scanner.nextLine().trim();
            if (!buffer.isEmpty()) {
                out = Double.parseDouble(buffer);
            } else PrintErr("Please, enter a non-null value\n");
        }
        return out;
    }

    public Long readLong(String msg) {
        Long out = null;
        while (out == null) {
            PrintMsg(msg);
            String buffer = scanner.nextLine().trim();
            if (!buffer.isEmpty()) {
                out = Long.parseLong(buffer);
            } else PrintErr("Please, enter a non-null value\n");
        }
        return out;
    }

    public Float readFloat(String msg) {
        Float out = null;
        while (out == null) {
            PrintMsg(msg);
            String buffer = scanner.nextLine().trim();
            if (!buffer.isEmpty()) {
                out = Float.parseFloat(buffer);
            } else PrintErr("Please, enter a non-null value\n");
        }
        return out;
    }

//  it doesn't work...why...too much questions too less answers...

    public ZonedDateTime readDate(String msg) {
        ZonedDateTime out = null;
        String dateFormat = "yyyy-MM-dd HH:mm:ss z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

        while (out == null) {
            PrintMsg(msg);
            PrintMsg("Please, use correct date input format(" + dateFormat + ")");
            String buffer = scanner.nextLine().trim();
            if (!buffer.isEmpty()) {
                out = ZonedDateTime.parse(buffer, formatter);
            } else
                PrintErr("Please, enter a non-null value\n");

        }
        return out;
    }


    public <T extends Enum<T>> T readEnumType(Class<T> enumeration, String msg) {
        T out = null;
        while (out == null) {
            PrintMsg(msg);

            StringBuilder s = new StringBuilder();
            T[] enumConstants = enumeration.getEnumConstants();
            for (int i = 0; i < enumConstants.length; i++) {
                if (i > 0) {
                    s.append("| ");
                }
                s.append(enumConstants[i].toString());
            }

            PrintMsg("Values (" + s + "):");
            String buffer = scanner.nextLine().trim().toUpperCase();
            if (!buffer.isEmpty()) {
                try {
                    out = Enum.valueOf(enumeration, buffer);
                } catch (IllegalArgumentException e) {
                    PrintMsg("Enter one of the available values :/ (" + s + "):");
                }
            } else PrintErr("Please, enter a not empty or null value\n");
        }
        return out;
    }

}





