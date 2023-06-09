package dev.rollczi.litecommands.meta;

import java.util.List;

public interface MetaCollector {

    <T> List<T> collect(CommandKey<T> key);

}
