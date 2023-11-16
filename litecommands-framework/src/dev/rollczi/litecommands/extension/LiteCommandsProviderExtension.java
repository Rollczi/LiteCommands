package dev.rollczi.litecommands.extension;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsInternal;
import dev.rollczi.litecommands.LiteCommandsProvider;

/**
 * A generic interface for configuring {@link LiteCommandsProvider}.
 *
 * @param <SENDER> the type of the sender
 */
public interface LiteCommandsProviderExtension<SENDER> extends LiteExtension<SENDER> {

    void extendCommandsProvider(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternal<SENDER, ?> internal, LiteCommandsProvider<SENDER> provider);

}
