package dev.rollczi.litecommands.prettyprint;

import dev.rollczi.litecommands.util.StringUtil;

import java.util.StringJoiner;

import static dev.rollczi.litecommands.prettyprint.PrettyPrint.NEW_LINE;
import static dev.rollczi.litecommands.prettyprint.PrettyPrint.SPACE;

final class PrettyPrintIndent {

    static final String INDENT = "    ";

    static String indentedContent(String content, int additional) {
        StringJoiner joiner = new StringJoiner(NEW_LINE);
        String indent = additional == 0 ? INDENT : StringUtil.repeat(SPACE, additional);

        for (String line : content.split(NEW_LINE)) {
            joiner.add(indent + line);
        }

        return joiner.toString();
    }

    static String indentedContent(String content) {
        StringJoiner joiner = new StringJoiner(NEW_LINE);

        for (String line : content.split(NEW_LINE)) {
            joiner.add(INDENT + line);
        }

        return joiner.toString();
    }

}
