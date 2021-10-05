import exceptions.NoSuchCommandException;
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
    Scanner scanner;
    boolean isStarted = false;
    static boolean isSentUserCommand;
    boolean isCommand = false;

    public Client() {
        System.out.println("client started");
        scanner = new Scanner(System.in);
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            System.out.println("unknown host");
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        try {
            while (true) {
                if (!isStarted) {
                    startClient();
                    continue;
                }
                String line = scanner.nextLine();

                if (line.equals("exit")) {
                    System.exit(0);
                }

                String[] message = (line.trim() + " ").split(" ", 2);
//                checkCommand(message);
                CommandNet cmd = new CommandNet(message);
                send(cmd);
                isSentUserCommand = true;
            }
//        } catch (NoSuchCommandException e) {
//            System.out.println("Error: you entered incorrect command");
//            PrintMsg("Please, enter 'help' to get list about available commands!");
//            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void startClient() {
        if (isStarted) {
            PrintMsg("the client has already connected to server");
        } else {
            connectServer();
            Thread response = new Thread(new ResponseThread());
            response.start();
            isStarted = true;
        }
    }

    public static void connectServer() {
        try {
            PrintMsg("client connected to socket " + socket.toString());
            socket.connect(serverAdr);
            String[] connect = {"connect", " "};
            CommandNet commandNet = new CommandNet(connect);
            System.out.println("the client starts sending 'connect' command to the server");
            send(commandNet);
            isSentUserCommand = false;
        } catch (SocketException e) {
           PrintErr("socket connection");
        } catch (IOException e) {
            PrintErr("sending connect command");
        }
    }

    public static void send(CommandNet command) throws IOException {
        if (!command.getEnteredCommand()[0].equals("connect")) {
            commandNetNext = command;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(out);) {
            oos.writeObject(command);
            byte[] sendMessage = out.toByteArray();
            DatagramPacket packet = new DatagramPacket(sendMessage, sendMessage.length, address, 9000);
            socket.send(packet);
        }
        System.out.println("client send command to server");
    }



//    public void checkCommand(String[] cmd) throws NoSuchCommandException {
//        if (!cmd[0].trim().equals(" ")) {
//            for (Commandable eachCommand : ListHolder.getCmdList()) {
//                if (cmd[0].trim().equals(eachCommand.getName())) {
//                    PrintMsg("you entered a right command");
//                    isCommand = true;
//                }
//            }
//        }
//        if (!isCommand) {
//            throw new NoSuchCommandException();
//        }
//    }

}
