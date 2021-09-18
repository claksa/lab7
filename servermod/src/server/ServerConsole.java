package server;

import commands.Commandable;
import exceptions.NoSuchCommandException;
import lib.ServerCommands;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConsole implements Runnable {
    List<Commandable> serverList;
    private boolean isSuchCommand = false;
    private static final Logger log = Logger.getLogger(ServerConsole.class.getName());

    public ServerConsole() {
        ServerCommands commands = new ServerCommands();
        serverList = commands.getServerCommands();
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            log.info("server console started");
            String message = "if you want to know about the commands available for execution on the server, enter 'help'\n" +
                    "or you can just wait for client connection";
            System.out.println(message);
            while (Server.running) {
                try {
                    String enteredCommand = scanner.nextLine();

                    if (enteredCommand.equals("exit")){
                        System.exit(0);
                    }

                    executeServer(enteredCommand);
                } catch (NoSuchCommandException e) {
                    log.log(Level.SEVERE, "it is not a command");
//                    mainlib.Reader.PrintMsg("it is not a command.");
                    Server.stop();
                }
            }
        }
    }

    public void executeServer(String command) throws NoSuchCommandException {
        StringBuilder res = new StringBuilder(" ");
        if (command.trim().equals("help")) {
            res.append("\nsave - save collection to file\n");
            res.append("exit - end the server\n");
            res.append("help - get list about available server commands\n");
            System.out.println(res);
            isSuchCommand = true;
        }
        for (Commandable each : serverList) {
            if (command.equals(each.getName())) {
                each.execute(command,null, null);
                isSuchCommand = true;
            }
        }
        if (!isSuchCommand) {
            throw new NoSuchCommandException();
        }
    }
}
