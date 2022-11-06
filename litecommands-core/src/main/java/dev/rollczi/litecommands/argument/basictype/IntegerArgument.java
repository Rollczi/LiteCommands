package dev.rollczi.litecommands.argument.basictype;

public class IntegerArgument extends AbstractBasicTypeArgument<Integer> {

    public IntegerArgument() {
        super(Integer::parseInt, () -> TypeUtils.NUMBER_SUGGESTION);
    }

}
