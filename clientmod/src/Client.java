import exceptions.EmptyIOException;
import mainlib.User;
import db.UserAct;
import db.UserManager;
import db.UserState;
import exceptions.LackOfAccessException;
import mainlib.*;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import static mainlib.Reader.PrintErr;
import static mainlib.Reader.PrintMsg;


public class Client {
    static InetAddress address;
    static SocketAddress serverAdr = new InetSocketAddress("localhost", 9000);
    static DatagramSocket socket;
    static CommandNet commandNetNext = null;
    private static final Scanner scanner = new Scanner(System.in);
    boolean isStarted = false;

    public Client() {
        System.out.println("client started");
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            System.out.println("unknown host");
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    public static User createUser() {
        PrintMsg("register/log in");
        String option = scanner.nextLine();
        User user = null;
        try {
            String username = null;
            String password = null;
            if (option.equals("register") || option.equals("log in")) {
                PrintMsg("Enter a username:");
                username = scanner.nextLine();
                PrintMsg("Enter a password");
                password = scanner.nextLine();
            } else {
                throw new EmptyIOException();
            }
            if (option.equals("register")) {
                user = new User(username, password, UserAct.REGISTER);
            } else {
                user = new User(username, password, UserAct.LOG_IN);
            }
        } catch (EmptyIOException e) {
            PrintErr("you write nothing! Please, enter some data!");
        }
      return user;
    }

    public static void sendUser(){
        try {
            send(createUser());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public void run() {
//        try {
//            while (true) {
//                if (!isStarted) {
//                    startClient();
//                }
////                String line = scanner.nextLine();
////                if (line.equals("exit")) {
////                    System.exit(0);
////                }
////                sendCommand(line);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        catch (LackOfAccessException e) {
////            PrintErr("you cannot send commands if you are not logged in");
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//    }

    public void startClient() {
        if (isStarted) {
            PrintMsg("the client has already connected to server");
        } else {
            connectServer();
            Thread response = new Thread(new ClientResponse());
            response.start();
            isStarted = true;
        }
    }

    public static void sendCommand(String line) throws IOException, LackOfAccessException {
        String[] message = (line.trim() + " ").split(" ", 2);
        System.out.println("line in client: " + line);
        CommandNet cmd = new CommandNet(message);
        send(cmd);
    }

    public static void connectServer() {
        try {
            PrintMsg("client connected to socket " + socket.toString());
            socket.connect(serverAdr);
            String[] connect = {"connect", " "};
            CommandNet commandNet = new CommandNet(connect);
            System.out.println("the client starts sending 'connect' command to the server");
            send(commandNet);
            ClientResponse.isEstablishedConnection = true;
        } catch (SocketException e) {
            PrintErr("socket connection");
        } catch (IOException e) {
            PrintErr("sending connect command");
        }
    }

    public static void send(Object data) throws IOException {
        if (data instanceof CommandNet) {
            if (!((CommandNet) data).getEnteredCommand()[0].equals("connect")) {
                commandNetNext = (CommandNet) data;
            }
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(out);) {
            oos.writeObject(data);
            byte[] sendMessage = out.toByteArray();
            DatagramPacket packet = new DatagramPacket(sendMessage, sendMessage.length, address, 9000);
            socket.send(packet);
        }
        System.out.println("client send command to server");
    }


}
