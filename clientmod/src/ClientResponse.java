
import db.UserManager;
import mainlib.Answer;
import mainlib.AnswerType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.PortUnreachableException;

import static mainlib.Reader.PrintErr;
import static mainlib.Reader.PrintMsg;

public class ClientResponse implements Runnable {
    static boolean isEstablishedConnection = false;
    AnswerType answerType;
    boolean isAccessedToWork = false;


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
                answerType = answer.getAnswerType();
                if (answerType.equals(AnswerType.USER)){
                    isAccessedToWork = true;
                }
                if (answerType.equals(AnswerType.INT_COMMAND) && isAccessedToWork) {
                    if (answer.getAnswer().get(0).equals("connected")) {
                        if (Client.commandNetNext != null) {
                            System.out.println(" i try to send command [ " + Client.commandNetNext.getEnteredCommand()[0] + " ]");
                            Client.send(Client.commandNetNext);
                        }
                    } else {
                        Client.commandNetNext = null;
                    }
                }
                answer.printAnswer();
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
