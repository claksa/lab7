
import mainlib.Answer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.PortUnreachableException;
import java.util.Scanner;

import static mainlib.Reader.PrintErr;
import static mainlib.Reader.PrintMsg;

public class ResponseThread implements Runnable{
    Scanner scanner = new Scanner(System.in);
    boolean isEstablishedConnectionWithServer;


    @Override
    public void run() {
        while (true) {
            try {
                byte[] receivedMessage = new byte[65536];
                DatagramPacket receivedPacket = new DatagramPacket(receivedMessage, receivedMessage.length);
                Client.socket.receive(receivedPacket);
                byte[] received = receivedPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(received);
                ObjectInputStream ois = new ObjectInputStream(in);
                Answer answer = (Answer) ois.readObject();
                if (answer.getAnswer().get(0).equals("connected")) {
                    isEstablishedConnectionWithServer = true;
                    PrintMsg("the program was completed at the request of the user");
                    if (Client.commandNetNext != null) {
                        System.out.println(" i try to send command [ " + Client.commandNetNext.getEnteredCommand()[0] + " ] again!");
                        Client.send(Client.commandNetNext);
                    }
                    answer.printAnswer();
                    PrintMsg("We have successfully connected to the server (and database as well)!");
                }
                if (!Client.isSentUserCommand){
                    UserConsole.startWorkWithUser();
                }
            } catch (PortUnreachableException e) {
                try {
                    PrintMsg("failed to connect to the server :( try after 3 seconds");
                    Thread.sleep(1000);
                    System.out.println(".");
                    Thread.sleep(1000);
                    System.out.println("..");
                    Thread.sleep(1000);
                    System.out.println("...");
                    Client.connectServer();
                } catch (InterruptedException interruptedException) {
                    PrintMsg(" i want to sleep! Don't interrupt me pls");
                    return;
                }
            } catch (ClassNotFoundException e) {
                PrintErr("serialization");
                break;
            } catch (IOException e) {
                PrintErr("serialization/data receiving");
            }
            System.out.println("LISTENING: ");
        }
    }

}
