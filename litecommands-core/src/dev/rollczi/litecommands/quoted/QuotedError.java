package dev.rollczi.litecommands.quoted;

public class QuotedError {

    private final String content;
    private final Cause cause;

    public QuotedError(String content, Cause cause) {
        this.content = content;
        this.cause = cause;
    }

    public String getContent() {
        return content;
    }

    public Cause getCause() {
        return cause;
    }

    public enum Cause {
        FIRST_QUOTE,
        LAST_QUOTE,
    }

    @Override
    public String toString() {
        return "QuotedError{" +
            "content='" + content + '\'' +
            ", cause=" + cause +
            '}';
    }

}
