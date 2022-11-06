package dev.rollczi.litecommands.argument.basictype;

public class ByteArgument extends AbstractBasicTypeArgument<Byte> {

    public ByteArgument() {
        super(Byte::parseByte, () -> TypeUtils.NUMBER_SHORT_SUGGESTION);
    }

}
