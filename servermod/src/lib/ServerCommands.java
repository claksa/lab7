package lib;

import commands.Commandable;
import commands.Save;
import mainlib.CollectionManager;
import mainlib.CommanderHolder;

import java.util.ArrayList;
import java.util.List;

public class ServerCommands {
    List<Commandable> serverCommands;

    public ServerCommands(){
        serverCommands = new ArrayList<Commandable>();
    }

    public List<Commandable> getServerCommands() {
        CollectionManager collectionManager = CommanderHolder.getCommander().getCollectionManager();
        serverCommands.add(new Save(collectionManager));
//        serverCommands.add(new Exit(collectionManager));
        return serverCommands;
    }
}
