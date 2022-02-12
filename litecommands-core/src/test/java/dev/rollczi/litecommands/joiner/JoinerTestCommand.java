package dev.rollczi.litecommands.joiner;

import dev.rollczi.litecommands.annotations.Arg;
import dev.rollczi.litecommands.annotations.Execute;
import dev.rollczi.litecommands.annotations.Joiner;
import dev.rollczi.litecommands.annotations.Section;

@Section(route = "test")
public class JoinerTestCommand {

    static String arg1 = "";
    static String arg2 = "";
    static String result = "";

    @Execute
    void execute(@Joiner String joinedText) {
        JoinerTestCommand.result = joinedText;
    }

    @Execute(route = "single")
    void execute(@Arg(0) String arg1, @Joiner String joinedText) {
        JoinerTestCommand.arg1 = arg1;
        JoinerTestCommand.result = joinedText;
    }

    @Execute(route = "double")
    void execute(@Arg(0) String arg1, @Arg(1) String arg2, @Joiner String joinedText) {
        JoinerTestCommand.arg1 = arg1;
        JoinerTestCommand.arg2 = arg2;
        JoinerTestCommand.result = joinedText;
    }

    @Section(route = "section")
    public static class TestSection {

        @Execute
        void execute(@Joiner String joinedText) {
            JoinerTestCommand.result = joinedText;
        }

        @Execute(route = "single")
        void execute(@Arg(0) String arg1, @Joiner String joinedText) {
            JoinerTestCommand.arg1 = arg1;
            JoinerTestCommand.result = joinedText;
        }

    }

}
