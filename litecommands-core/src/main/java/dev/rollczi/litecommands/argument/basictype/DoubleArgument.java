package dev.rollczi.litecommands.argument.basictype;

public class DoubleArgument extends AbstractBasicTypeArgument<Double> {

    public DoubleArgument() {
        super(Double::parseDouble, () -> TypeUtils.DECIMAL_SUGGESTION);
    }

}
