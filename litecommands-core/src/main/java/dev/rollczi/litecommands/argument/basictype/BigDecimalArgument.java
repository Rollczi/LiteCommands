package dev.rollczi.litecommands.argument.basictype;

import java.math.BigDecimal;

public class BigDecimalArgument extends AbstractBasicTypeArgument<BigDecimal> {

    public BigDecimalArgument() {
        super(BigDecimal::new, () -> TypeUtils.DECIMAL_SUGGESTION);
    }

}
