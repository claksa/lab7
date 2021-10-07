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
        while(Client.isStartedBasicDelivery){
            try {
                if (!UserManager.getUserState().equals(UserState.NOT_REGISTERED)) {
                    String command = scanner.nextLine();
                    Client.sendCommand(command);
                } else {
                    throw new LackOfAccessException();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LackOfAccessException e) {
                System.out.println("problems with registration/authorization :) ");
            }
        }
    }
}
