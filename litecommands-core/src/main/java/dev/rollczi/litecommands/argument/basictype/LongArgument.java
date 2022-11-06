package dev.rollczi.litecommands.argument.basictype;

public class LongArgument extends AbstractBasicTypeArgument<Long> {

    public LongArgument() {
        super(Long::parseLong, () -> TypeUtils.NUMBER_SUGGESTION);
    }

}
