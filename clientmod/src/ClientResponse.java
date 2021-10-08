

import db.UserManager;
import db.UserState;
import mainlib.Answer;
import mainlib.AnswerType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.PortUnreachableException;
import java.util.Scanner;

import static db.UserState.AUTHORIZED;
import static db.UserState.NOT_REGISTERED;
import static mainlib.AnswerType.ERROR;
import static mainlib.AnswerType.WIN;
import static mainlib.Reader.PrintErr;
import static mainlib.Reader.PrintMsg;

public class ClientResponse implements Runnable {
    static boolean isEstablishedConnection = false;
    static boolean isStartedBasicDelivery = false;
    AnswerType answerType;


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
                if (answer.getAnswer().get(0).equals("connected")) {
                    if (Client.commandNetNext != null) {
                        Client.send(Client.commandNetNext);
                    } else {
                        Client.sendUser();
                    }
                } else {
                    answer.printAnswer();
                }
                if (!answer.getAnswerType().equals(WIN)) {
                    stop();
                }
                if (answer.getAnswerType().equals(ERROR) && !UserManager.isAuthorized()) {
                    Client.sendUser();
                }
                boolean ded = answer.getAnswerType().equals(WIN) && answer.getUserState().equals(AUTHORIZED);
                System.out.println(ded);
                if (ded) {
                    System.out.println("i would like to send command!");
                    startCommandSender();
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

    public void startCommandSender() {
        isStartedBasicDelivery = true;
        System.out.println("IN CLIENT: ");
        Thread thr = new Thread(new BasicDelivery());
        thr.start();
    }

    public void stop() {
        isStartedBasicDelivery = false;
    }

}
