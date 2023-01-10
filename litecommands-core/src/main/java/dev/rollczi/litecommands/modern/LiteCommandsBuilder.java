package dev.rollczi.litecommands.modern;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.ArgumentKey;
import dev.rollczi.litecommands.modern.command.argument.invocation.ArgumentResolver;
import dev.rollczi.litecommands.modern.command.suggestion.SuggestionResolver;
import dev.rollczi.litecommands.modern.extension.LiteCommandsExtension;

public interface LiteCommandsBuilder<SENDER, B extends LiteCommandsBuilder<SENDER, B>> {

    <
        EXPECTED,
        ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContext<Object, EXPECTED>>
        > LiteCommandsBuilder<SENDER, B> argumentOnly(
            Class<EXPECTED> type,
            ARGUMENT argument
    );

    <
        EXPECTED,
        ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContext<Object, EXPECTED>>
        > LiteCommandsBuilder<SENDER, B> argumentOnly(
        Class<EXPECTED> type,
        ARGUMENT argument,
        ArgumentKey argumentKey
    );

    <
        EXPECTED,
        ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContext<Object, EXPECTED>> & SuggestionResolver<SENDER, Object, EXPECTED, ArgumentContext<Object, EXPECTED>>
        > LiteCommandsBuilder<SENDER, B> argument(
            Class<EXPECTED> type,
            ARGUMENT argument
    );

    <
        EXPECTED,
        ARGUMENT extends ArgumentResolver<SENDER, Object, EXPECTED, ArgumentContext<Object, EXPECTED>> & SuggestionResolver<SENDER, Object, EXPECTED, ArgumentContext<Object, EXPECTED>>
        > LiteCommandsBuilder<SENDER, B> argument(
        Class<EXPECTED> type,
        ARGUMENT argument,
        ArgumentKey argumentKey
    );

    <
        DETERMINANT,
        EXPECTED,
        CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>,
        ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>
        > LiteCommandsBuilder<SENDER, B> argumentOnly(
            Class<DETERMINANT> determinantType,
            Class<EXPECTED> expectedType,
            Class<CONTEXT> contextType,
            ARGUMENT argument
    );

    <
        DETERMINANT,
        EXPECTED,
        CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>,
        ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>
        > LiteCommandsBuilder<SENDER, B> argumentOnly(
        Class<DETERMINANT> determinantType,
        Class<EXPECTED> expectedType,
        Class<CONTEXT> contextType,
        ARGUMENT argument,
        ArgumentKey argumentKey
    );

    <
        DETERMINANT,
        EXPECTED,
        CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>,
        ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> & SuggestionResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>
        > LiteCommandsBuilder<SENDER, B> argument(
            Class<DETERMINANT> determinantType,
            Class<EXPECTED> expectedType,
            Class<CONTEXT> contextType,
            ARGUMENT argument
    );

    <
        DETERMINANT,
        EXPECTED,
        CONTEXT extends ArgumentContext<DETERMINANT, EXPECTED>,
        ARGUMENT extends ArgumentResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT> & SuggestionResolver<SENDER, DETERMINANT, EXPECTED, CONTEXT>
        > LiteCommandsBuilder<SENDER, B> argument(
        Class<DETERMINANT> determinantType,
        Class<EXPECTED> expectedType,
        Class<CONTEXT> contextType,
        ARGUMENT argument,
        ArgumentKey argumentKey
    );

    <NEW_BUILDER extends LiteCommandsBuilder<SENDER, NEW_BUILDER>> NEW_BUILDER withExtension(LiteCommandsExtension<SENDER, NEW_BUILDER> extension);

    LiteCommands<SENDER> register();

}
