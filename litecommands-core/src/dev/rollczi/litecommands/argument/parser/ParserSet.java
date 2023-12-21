package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.reflect.type.TypeRange;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.ApiStatus;

public interface ParserSet<SENDER, PARSED> {

    <INPUT> List<Parser<SENDER, INPUT, PARSED>> getParsers(Class<INPUT> inType);

    /**
     * @deprecated use {@link #getParsers(Class)} instead
     */
    @ApiStatus.ScheduledForRemoval(inVersion = "3.3.0")
    @Deprecated
    default <INPUT> Optional<Parser<SENDER, INPUT, PARSED>> getParser(Class<INPUT> inType) {
        return getParsers(inType).stream().findFirst();
    }

    /**
     * @deprecated use {@link #getParsers(Class)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "3.3.0")
    default Collection<Parser<SENDER, ?, PARSED>> getParsers() {
        return new ArrayList<>(this.getParsers(Object.class));
    }

    /**
     * @deprecated use {@link #of(TypeRange, Parser)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "3.3.0")
    static <SENDER, PARSED, IN> ParserSet<SENDER, PARSED> of(Class<PARSED> parsedClass, Parser<SENDER, IN, PARSED> parser) {
        return of(TypeRange.downwards(parsedClass), parser);
    }

    static <SENDER, PARSED, IN> ParserSet<SENDER, PARSED> of(TypeRange<PARSED> parsedRange, Parser<SENDER, IN, PARSED> parser) {
        ParserSetImpl<SENDER, PARSED> parserSet = new ParserSetImpl<>(parsedRange);

        parserSet.registerParser(parser);
        return parserSet;
    }

}
