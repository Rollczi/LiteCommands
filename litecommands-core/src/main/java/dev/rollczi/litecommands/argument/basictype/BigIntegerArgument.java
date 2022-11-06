package dev.rollczi.litecommands.argument.basictype;

import java.math.BigInteger;

public class BigIntegerArgument extends AbstractBasicTypeArgument<BigInteger> {

    public BigIntegerArgument() {
        super(BigInteger::new, () -> TypeUtils.NUMBER_SUGGESTION);
    }

}

