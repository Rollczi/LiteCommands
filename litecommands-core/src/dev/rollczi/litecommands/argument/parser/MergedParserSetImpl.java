package dev.rollczi.litecommands.argument.parser;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.shared.IterableReferences;
import java.util.List;
import org.jetbrains.annotations.Nullable;

class MergedParserSetImpl<SENDER, PARSED> implements ParserSet<SENDER, PARSED> {

    private final Iterable<ParserRegistryImpl<SENDER>.BucketByArgument<?>> bucketByArguments;


    public MergedParserSetImpl(Iterable<ParserRegistryImpl<SENDER>.BucketByArgument<?>> bucketByArguments) {
        this.bucketByArguments = bucketByArguments;
    }

    @Override
    public @Nullable <INPUT> Parser<SENDER, INPUT, PARSED> getValidParser(Class<INPUT> input, Invocation<SENDER> invocation, Argument<PARSED> argument) {
        for (ParserRegistryImpl<SENDER>.BucketByArgument<?> bucketByArgument : bucketByArguments) {
            ParserRegistryImpl<SENDER>.BucketByArgument<PARSED> bucket = (ParserRegistryImpl<SENDER>.BucketByArgument<PARSED>) bucketByArgument;
            ParserSet<SENDER, PARSED> parserSet = bucket.getParserSet(argument.getKey());

            if (parserSet == null) {
                continue;
            }

            Parser<SENDER, INPUT, PARSED> validParser = parserSet.getValidParser(input, invocation, argument);

            if (validParser != null) {
                return validParser;
            }
        }

        return null;
    }

}
