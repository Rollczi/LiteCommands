package dev.rollczi.litecommands.command;

public interface CommandNode<SENDER> {

    CommandRoute<SENDER> getParent();

}
