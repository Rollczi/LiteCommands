package dev.rollczi.litecommands.scheme;

public class Brackets {

    public static final Brackets PARENTHESES = new Brackets("(", ")");
    public static final Brackets ANGLED = new Brackets("<", ">");
    public static final Brackets SQUARE = new Brackets("[", "]");
    public static final Brackets BRACES = new Brackets("{", "}");
    public static final Brackets NONE = new Brackets(null, null);

    private final String front;
    private final String back;

    private Brackets(String front, String back) {
        this.front = front;
        this.back = back;
    }

    public String close(String text) {
        if (this.front == null || this.back == null) {
            return text;
        }

        return this.front + text + this.back;
    }

}
