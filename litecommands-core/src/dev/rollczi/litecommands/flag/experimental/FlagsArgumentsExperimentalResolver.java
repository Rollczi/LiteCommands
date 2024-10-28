package dev.rollczi.litecommands.flag.experimental;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.profile.ProfileNamespaces;
import dev.rollczi.litecommands.argument.profile.ProfiledMultipleArgumentResolver;
import dev.rollczi.litecommands.command.executor.CommandExecutor;
import dev.rollczi.litecommands.flag.FlagProfile;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.input.raw.RawInputView;
import dev.rollczi.litecommands.input.raw.UniversalViewRawInput;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.util.ArrayList;
import java.util.List;

public class FlagsArgumentsExperimentalResolver<SENDER> extends ProfiledMultipleArgumentResolver<SENDER, Object, FlagProfile> {

    private final ParserRegistry<SENDER> registry;

    public FlagsArgumentsExperimentalResolver(ParserRegistry<SENDER> registry) {
        super(FlagProfile.NAMESPACE);
        this.registry = registry;
    }

    @Override
    public ParseResult<Object> parse(Invocation<SENDER> invocation, Argument<Object> currnetArgument, RawInput rawInput, FlagProfile meta) {
        if (!(rawInput instanceof UniversalViewRawInput)) {
            throw new IllegalStateException("Flags arguments are supported only with Boolean! If you want to use flags with other types, please use enable the flags arguments extension.");
        }

        UniversalViewRawInput view = (UniversalViewRawInput) rawInput;
        CommandExecutor<SENDER> executor = (CommandExecutor<SENDER>) currnetArgument.getExecutor();

        List<Argument<?>> arguments = this.extractFlagsArguments(executor, currnetArgument);

        int indexOf = view.indexOf(meta.getValue());

        if (indexOf == -1) {
            return ParseResult.failure(InvalidUsage.Cause.MISSING_ARGUMENT);
        }

        String claimed = view.claim(indexOf, indexOf + meta.getValue().length() + 1);

        if (!claimed.equals(meta.getValue() + RawCommand.COMMAND_SEPARATOR)) {
            throw new IllegalStateException("Expected flag value: " + meta.getValue() + RawCommand.COMMAND_SEPARATOR + ", but got: " + claimed);
        }

        UniversalViewRawInput rawInputView = view.sub(indexOf);
        Argument<Object> argument = currnetArgument.child(currnetArgument.getType());
        Parser<SENDER, Object> parser = registry.getParser(argument);

        return parser.parse(invocation, argument, rawInputView);
    }

    private List<Argument<?>> extractFlagsArguments(CommandExecutor<SENDER> executor, Argument<?> currentArgument) {
        List<Argument<?>> arguments = new ArrayList<>();
        boolean found = false;

        for (Argument<?> argument : executor.getArguments()) {
            if (argument.equals(currentArgument)) {
                arguments.add(argument);
                found = true;
                continue;
            }

            boolean isFlag = argument.getProfile(ProfileNamespaces.FLAG).isPresent();

            if (isFlag) {
                arguments.add(argument);
                continue;
            }

            if (found) {
                return arguments;
            }

            arguments.clear();
        }

        if (arguments.isEmpty()) {
            throw new IllegalStateException("No flags arguments found!");
        }

        return arguments;
    }

    @Override
    public Range getRange(Argument<Object>  argument, FlagProfile meta) {
        return Range.range(0, 1);
    }

    @Override
    public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<Object>  argument, SuggestionContext context, FlagProfile meta) {
        return SuggestionResult.of(argument.getName());
    }

}

