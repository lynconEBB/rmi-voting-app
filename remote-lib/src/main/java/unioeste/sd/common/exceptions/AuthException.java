package unioeste.sd.common.exceptions;

public class AuthException extends RuntimeException{
    public AuthException() {
        super("Username already used!");
    }

    public AuthException(String message) {
        super(message);
    }
}
