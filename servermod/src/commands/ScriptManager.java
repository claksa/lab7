package commands;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static mainlib.Reader.PrintErr;


public class ScriptManager {
    private Scanner scriptReader;


    public ScriptManager(String path){
        try {
            scriptReader = new Scanner(new FileReader(path));
        } catch (FileNotFoundException e) {
            PrintErr("file doesn't found:" + path);
        }
    }


    public String nextLine(){
        try {
            return scriptReader.nextLine().trim();
        } catch (NullPointerException | NoSuchElementException e){
            return "exit";
        }
    }

    public Scanner getScriptReader() {
        return scriptReader;
    }
}
