package mainlib;

import commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Commander {
    protected List<Commandable> commandsList;
    CollectionManager collectionManager;


    public Commander(){
        commandsList = new ArrayList<>();
    }

    public List<Commandable> getCommandsList(FileManager fileManager, Scanner scanner){
        collectionManager = new CollectionManager(fileManager);
        commandsList.add(new Add(collectionManager));
        commandsList.add(new AddMin(collectionManager));
        commandsList.add(new Clear(collectionManager));
        commandsList.add(new FilterStartsWithName(collectionManager));
        commandsList.add(new FilterContainsName(collectionManager));
        commandsList.add(new GroupCountByType(collectionManager));
        commandsList.add(new Help(commandsList));
        commandsList.add(new Info(collectionManager));
        commandsList.add(new Remove(collectionManager));
        commandsList.add(new RemoveLower(collectionManager));
        commandsList.add(new Show(collectionManager));
        commandsList.add(new Shuffle(collectionManager));
        commandsList.add(new Update(collectionManager));
        commandsList.add(new Connect());
        commandsList.add(new ExecuteScript());
        return commandsList;
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }
}
