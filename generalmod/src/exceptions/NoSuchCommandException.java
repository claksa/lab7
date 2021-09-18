package exceptions;

public class NoSuchCommandException extends Exception{

    @Override
    public String getMessage() {
        return " No such command exists!";
    }
}
