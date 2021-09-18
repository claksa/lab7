package mainlib;

import commands.Commandable;

import java.util.List;
import java.util.Scanner;

public class CommanderHolder {
    private static final Commander commander = new Commander();
    private static final List<Commandable> cmdList = commander.getCommandsList(new FileManager(),new Scanner(System.in));

    public static Commander getCommander() {
        return commander;
    }

    public static List<Commandable> getCmdList() {
        return cmdList;
    }


}
