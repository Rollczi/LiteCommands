package dev.rollczi.litecommands;

public final class AssertException extends RuntimeException {

    public AssertException(Object expected, Object actual) {
        super("Expected: " + expected + System.lineSeparator() + "Actual: " + actual);
    }

}
