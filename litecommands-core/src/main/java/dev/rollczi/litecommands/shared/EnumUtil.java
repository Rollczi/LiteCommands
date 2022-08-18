package dev.rollczi.litecommands.shared;

import panda.std.Result;

public final class EnumUtil {

    private EnumUtil() {}

    @SuppressWarnings("unchecked")
    public static Result<Enum<?>, Exception> parse(Class<?> type, String argument) {
        return Result.supplyThrowing(Exception.class, () -> Enum.valueOf((Class<? extends Enum>) type, argument));
    }

}
