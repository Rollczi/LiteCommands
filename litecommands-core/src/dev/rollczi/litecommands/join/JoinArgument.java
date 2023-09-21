package dev.rollczi.litecommands.join;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.util.function.Supplier;

public class JoinArgument<EXPECTED> extends SimpleArgument<EXPECTED> {

    public static final String DEFAULT_SEPARATOR = " ";
    public static final int DEFAULT_LIMIT = -1;

    public static final ArgumentKey KEY = ArgumentKey.typed(JoinArgument.class);

    private final String separator;
    private final int limit;

    public JoinArgument(Supplier<String> name, WrapFormat<EXPECTED, ?> wrapperFormat, String separator, int limit) {
        super(name, wrapperFormat);
        this.separator = separator;
        this.limit = limit;
    }

    public JoinArgument(Supplier<String> name, WrapFormat<EXPECTED, ?> wrapperFormat) {
        this(name, wrapperFormat, DEFAULT_SEPARATOR, DEFAULT_LIMIT);
    }

    public String getSeparator() {
        return separator;
    }

    public int getLimit() {
        return limit;
    }

}
