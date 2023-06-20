package dev.rollczi.litecommands.argument.parser;

public class LiteParseException extends RuntimeException {

    public LiteParseException() {
    }

    public LiteParseException(String message) {
        super(message);
    }

    public LiteParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public LiteParseException(Throwable cause) {
        super(cause);
    }

}
