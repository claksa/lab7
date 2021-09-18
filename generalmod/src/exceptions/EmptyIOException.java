package exceptions;

public class EmptyIOException extends Exception{
    @Override
    public String getMessage() {
        return " Please, enter a non-null value";
    }
}
