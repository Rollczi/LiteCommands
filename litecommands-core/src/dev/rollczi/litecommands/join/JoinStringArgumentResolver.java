package dev.rollczi.litecommands.join;

import java.util.List;

public class JoinStringArgumentResolver<SENDER> extends JoinArgumentResolver<SENDER, String> {

    @Override
    protected String join(JoinArgument<String> argument, List<String> values) {
        return String.join(argument.getSeparator(), values);
    }

}
