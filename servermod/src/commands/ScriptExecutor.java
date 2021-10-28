package commands;

import exceptions.NoSuchCommandException;
import lib.ListHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ScriptExecutor {
    Scanner scanner;
    List<String> script;

    public ScriptExecutor() {
        scanner = new Scanner(System.in);
        script = new ArrayList<>();
    }

    public void executeScript(String argument) throws NoSuchCommandException {
        ScriptManager scriptManager = new ScriptManager(argument.trim());
        script.add(argument.trim());
        while (true) {
            String nextLine = scriptManager.nextLine();
            if (nextLine == null)
                break;
            String[] cmd = (nextLine.trim() + " ").split(" ", 2);
            if (cmd[0].trim().equals("executescript")) {
                if (!cmd[1].equals(" ")) {
                    if (script.contains(cmd[1].trim())) {
                        ExecuteScript.getExecuteScriptCommand().add("Error: trying to recursively call the script");
                        break;
                    } else {
                        executeScript(cmd[1].trim());
                    }
                } else {
                    ExecuteScript.getExecuteScriptCommand().add("enter file name");
                }
            } else if (!cmd[0].trim().equals("")) {
                boolean isFindCommand = false;
                for (Commandable commandable : ListHolder.getCmdList()) {
                    if (cmd[0].trim().equals(commandable.getName())) {
                        if (commandable.getName().trim().equals("add")) {
                            changeScanner(scriptManager.getScriptReader());
                            try {
                                ExecuteScript.getExecuteScriptCommand().addAll(commandable.execute(cmd[1],null,null, null));
                                isFindCommand = true;
                                break;
                            } catch (NoSuchElementException e) {
                                ExecuteScript.getExecuteScriptCommand().add("Error: reading element from file");
                            }
                            changeScanner(new Scanner(System.in));
                        } else {
                            ExecuteScript.getExecuteScriptCommand().addAll(commandable.execute(cmd[1],null ,null, null));
                            isFindCommand = true;
                            break;
                        }
                    }
                }
                if (!isFindCommand) {
                    throw new NoSuchCommandException();
                }
            }
        }
    }
    public List<String> getScript() {
        return script;
    }

    public void changeScanner(Scanner sc) {
        scanner = sc;
    }
}
