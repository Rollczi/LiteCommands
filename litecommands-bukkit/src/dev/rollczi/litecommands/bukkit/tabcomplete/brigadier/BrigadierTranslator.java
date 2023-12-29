package dev.rollczi.litecommands.bukkit.tabcomplete.brigadier;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.argument.suggester.SuggesterNone;
import dev.rollczi.litecommands.argument.suggester.SuggesterRegistry;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.command.executor.CommandExecutor;

import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.range.Rangeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class BrigadierTranslator<SENDER> {

    private static final SuggestionProvider<?> DUMMY_SUGGESTION_PROVIDER = (context, builder) -> builder.buildFuture();

    private final SuggesterRegistry<SENDER> suggesterRegistry;
    private final ParserRegistry<SENDER> parserRegistry;

    public BrigadierTranslator(SuggesterRegistry<SENDER> suggesterRegistry, ParserRegistry<SENDER> parserRegistry) {
        this.suggesterRegistry = suggesterRegistry;
        this.parserRegistry = parserRegistry;
    }

    public List<LiteralArgumentBuilder<SENDER>> translate(CommandRoute<SENDER> route) {
        List<LiteralArgumentBuilder<SENDER>> argumentBuilders = new ArrayList<>();

        for (String name : route.names()) {
            LiteralArgumentBuilder<SENDER> builder = literal(name);

            for (CommandRoute<SENDER> child : route.getChildren()) {
                List<LiteralArgumentBuilder<SENDER>> translated = translate(child);

                for (LiteralArgumentBuilder<SENDER> literalArgumentBuilder : translated) {
                    builder.then(literalArgumentBuilder);
                }
            }

            argumentBuilders.add(this.translateExecutor(builder, route));
        }

        return argumentBuilders;
    }

    private LiteralArgumentBuilder<SENDER> translateExecutor(LiteralArgumentBuilder<SENDER> builder, CommandRoute<SENDER> commandRoute) {
        Map<Integer, CommandNode<SENDER>> indexed = new HashMap<>();

        for (CommandExecutor<SENDER> executor : commandRoute.getExecutors()) {

            int index = 0;
            for (Argument<?> argument : executor.getArguments()) {
                Parser<SENDER, ?, ?> parser = getParser(argument);
                Suggester<SENDER, ?> suggester = getSuggester(argument);
                Range range = getRange(parser, argument);
                boolean isSuggester = !(suggester instanceof SuggesterNone);

                if (range.getMax() == Integer.MAX_VALUE) {
                    ArgumentCommandNode<SENDER, ?> infiniteArgument = translateInfiniteArgument(argument)
                        .suggests(isSuggester ? dummySuggestion() : null)
                        .build();

                    indexed.put(index++, infiniteArgument);
                    continue;
                }

                List<CommandNode<SENDER>> commandNodes = this.translateArgument(argument, range, isSuggester);

                for (CommandNode<SENDER> commandNode : commandNodes) {
                    CommandNode<SENDER> last = indexed.get(index);

                    if (last == null) {
                        indexed.put(index++, commandNode);
                        continue;
                    }

                    ArgumentCommandNode<SENDER, ?> argumentNode = (ArgumentCommandNode<SENDER, ?>) last;
                    StringArgumentType type = (StringArgumentType) argumentNode.getType();

                    String first = last.getName();
                    String second = commandNode.getName();
                    String newCommand = first + "/" + second;

                    CommandNode<SENDER> newNode = RequiredArgumentBuilder.<SENDER, String>argument(newCommand, type)
                        .suggests(dummySuggestion())
                        .build();

                    indexed.put(index++, newNode);
                }
            }
        }

        AtomicReference<CommandNode<SENDER>> last = new AtomicReference<>();
        indexed.forEach((index, node) -> {
            if (last.get() == null) {
                builder.then(node);
                last.set(node);
                return;
            }

            CommandNode<SENDER> lastArgument = last.get();
            lastArgument.addChild(node);
            last.set(node);
        });

        return builder;
    }

    private Parser<SENDER, RawInput, ?> getParser(Argument<?> argument) {
        return parserRegistry.getParserSet(argument.getWrapperFormat().getParsedType(), argument.getKey()).getParser(
            RawInput.class
        ).orElseThrow(() -> new RuntimeException("Cannot find parser for argument: " + argument.getKey()));
    }

    private Suggester<SENDER, ?> getSuggester(Argument<?> argument) {
        return suggesterRegistry.getSuggester(argument.getWrapperFormat().getParsedType(), argument.getKey());
    }

    private Range getRange(Parser<SENDER, ?, ?> parser, Argument<?> argument) {
        return parser instanceof Rangeable
            ? ((Rangeable) parser).getRange(argument)
            : Range.of(1);
    }

    private List<CommandNode<SENDER>> translateArgument(Argument<?> argument, Range range, boolean isSuggester) {
        List<CommandNode<SENDER>> list = new ArrayList<>();

        for (int i = 0; i < range.getMax(); i++) {
            CommandNode<SENDER> builder = RequiredArgumentBuilder.<SENDER, String>argument(argument.getName() + i, StringArgumentType.word())
                .suggests(isSuggester ? dummySuggestion() : null)
                .build();

            list.add(builder);
        }

        return list;
    }

    private RequiredArgumentBuilder<SENDER, ?> translateInfiniteArgument(Argument<?> argument) {
        return RequiredArgumentBuilder.argument(argument.getName(), StringArgumentType.greedyString());
    }

    private RequiredArgumentBuilder<SENDER, ?> translateArgument(Argument<?> argument) {
        return RequiredArgumentBuilder.argument(argument.getName(), StringArgumentType.string());
    }

    private static <SENDER> SuggestionProvider<SENDER> dummySuggestion() {
        return (context, builder) -> builder.buildFuture();
    }

}
