package dev.rollczi.litecommands.argument.basictype;

public class ShortArgument extends AbstractBasicTypeArgument<Short> {

    public ShortArgument() {
        super(Short::parseShort, () -> TypeUtils.NUMBER_SHORT_SUGGESTION);
    }

}
