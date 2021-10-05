package commands;

public abstract class DBCommand implements Commandable{
    abstract void connectToDB();

}
