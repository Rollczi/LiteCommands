package dev.rollczi.litecommands.prettyprint;
import static dev.rollczi.litecommands.LiteCommandsVariables.BRANCH;
import static dev.rollczi.litecommands.LiteCommandsVariables.COMMIT;
import static dev.rollczi.litecommands.LiteCommandsVariables.VERSION;
import static dev.rollczi.litecommands.prettyprint.PrettyPrint.NEW_LINE;

public final class PrettyPrintLiteError {

    private final static String ERROR_PATTERN =
        "---------------------------------------------------------------------------------------------" + NEW_LINE +
        " LiteCommands " + VERSION + " - " + BRANCH + " - " + "(" + COMMIT + ")" + NEW_LINE +
        "---------------------------------------------------------------------------------------------" + NEW_LINE +
        "{content}" + NEW_LINE +
        "---------------------------------------------------------------------------------------------";

    public static String formatError(String content) {
        return ERROR_PATTERN.replace("{content}", content);
    }

}
