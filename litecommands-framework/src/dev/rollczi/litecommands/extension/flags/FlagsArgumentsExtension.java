package dev.rollczi.litecommands.extension.flags;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsInternal;
import dev.rollczi.litecommands.configurator.LiteConfigurator;
import dev.rollczi.litecommands.extension.LiteExtension;
import dev.rollczi.litecommands.flag.experimental.FlagsArgumentsExperimentalResolver;
import dev.rollczi.litecommands.reflect.type.TypeRange;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class FlagsArgumentsExtension<SENDER> implements LiteExtension<SENDER, Void> {

    @Override
    public void configure(LiteConfigurator<Void> configurer) {
        configurer.configure(null);
    }

    @Override
    public void extend(LiteCommandsBuilder<SENDER, ?, ?> builder, LiteCommandsInternal<SENDER, ?> internal) {
        builder.advanced()
            .argument(TypeRange.upwards(Object.class), new FlagsArgumentsExperimentalResolver<>());
    }

}
