package dev.rollczi.litecommands.argument.basictype;

import panda.std.Option;

public class CharacterArgument extends AbstractBasicTypeArgument<Character> {

    public CharacterArgument() {
        super(CharacterArgument::parse, () -> TypeUtils.CHARACTER_SUGGESTION);
    }

    private static char parse(String argument) {
        return Option.of(argument)
                .filter(arg -> arg.length() == 1)
                .map(arg -> arg.charAt(0))
                .orThrow(() -> new IllegalArgumentException("Argument is not character"));
    }

}
