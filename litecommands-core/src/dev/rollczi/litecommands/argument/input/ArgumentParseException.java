package dev.rollczi.litecommands.argument.input;

public class ArgumentParseException extends RuntimeException {

    public ArgumentParseException() {
    }

    public ArgumentParseException(String message) {
        super(message);
    }

    public ArgumentParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentParseException(Throwable cause) {
        super(cause);
    }

}
