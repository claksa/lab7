import db.UserManager;
import db.UserState;
import exceptions.LackOfAccessException;

import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

public class BasicDelivery implements Runnable{
    private final Scanner scanner = new Scanner(System.in);


    @Override
    public void run() {
        while(ClientResponse.isStartedBasicDelivery){
            try {
                    String command = scanner.nextLine();
                    if (command.equals("exit")){
                        System.exit(0);
                    }
                    Client.sendCommand(command);
            } catch (IOException | LackOfAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
