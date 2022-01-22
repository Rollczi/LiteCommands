package dev.rollczi.litecommands.platform;

public interface LiteSenderCreator<T> {

    LiteSender create(T originalSender);

}
