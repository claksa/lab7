package commands;

import java.io.Serializable;


public  abstract class AbstractCommand implements Commandable, Serializable {
    private boolean isCommand;


    public boolean isCommand() {
        return isCommand;
    }

}
