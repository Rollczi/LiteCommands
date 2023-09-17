package dev.rollczi.litecommands.annotation.processor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;

public interface SourceProcessor<SENDER, SOURCE> {

    CommandBuilder<SENDER> processBuilder(SOURCE source);

}
