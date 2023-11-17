package dev.rollczi.litecommands.extension;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsInternal;

/**
 * A generic interface for configuring {@link LiteCommandsBuilder} and {@link LiteCommandsInternal}.
 *
 * @param <SENDER> the type of the sender
 */
public interface LiteExtension<SENDER> {

    void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternal<SENDER, ?> internal);

}
