package dev.rollczi.litecommands.wrapper;

public interface Wrap<PARSED> {

    Object unwrap();

    Class<PARSED> getParsedType();

}
