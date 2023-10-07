package dev.rollczi.litecommands.argument.joiner;

public class StringJoinerArgument<SENDER> extends JoinerArgument<SENDER, String> {

    public StringJoinerArgument() {
        super(String::valueOf);
    }

}
