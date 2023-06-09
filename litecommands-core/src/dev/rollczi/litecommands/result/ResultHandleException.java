package dev.rollczi.litecommands.result;

public class ResultHandleException extends RuntimeException {

    public ResultHandleException(String message) {
        super(message);
    }

    public ResultHandleException(String message, Throwable cause) {
        super(message, cause);
    }

}
