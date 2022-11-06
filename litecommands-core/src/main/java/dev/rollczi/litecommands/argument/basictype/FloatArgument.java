package dev.rollczi.litecommands.argument.basictype;

public class FloatArgument extends AbstractBasicTypeArgument<Float> {

    public FloatArgument() {
        super(Float::parseFloat, () -> TypeUtils.DECIMAL_SUGGESTION);
    }

}
