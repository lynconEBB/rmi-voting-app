package unioeste.sd.common.exceptions;

public class InvalidDataException extends RuntimeException{

    public InvalidDataException() {
        super("Invalid data!");
    }

    public InvalidDataException(String message) {
        super(message);
    }
}
