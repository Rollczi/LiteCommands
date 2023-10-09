package dev.rollczi.litecommands.command;

@FunctionalInterface
public interface CommandProvider {

    <SENDER> CommandRoute<SENDER> provide(CommandRoute<SENDER> root);

}
