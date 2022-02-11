package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.annotations.Arg;
import dev.rollczi.litecommands.annotations.Between;
import dev.rollczi.litecommands.annotations.Execute;
import dev.rollczi.litecommands.annotations.Handler;
import dev.rollczi.litecommands.annotations.IgnoreClass;
import dev.rollczi.litecommands.annotations.Section;
import panda.std.Option;

@Section(route = "test")
public class TestCommand {

    public Result resultOption = new Result();
    public Result resultValueOption = new Result();
    public Result resultOptionOption = new Result();
    public ResultCustom resultCustom1 = new ResultCustom();
    public ResultCustom resultCustom2 = new ResultCustom();

    @Execute(route = "option", required = 1)
    public void execute(@Arg(0) @Handler(StringArg.class) Option<String> first) {
        resultOption.update(first.isEmpty());
    }

    @Execute(route = "value_option") @Between(min = 1, max = 2)
    public void execute(@Arg(0) String first, @Arg(1) @Handler(StringArg.class) Option<String> second) {
        resultValueOption.update(first == null, second.isEmpty());
    }

    @Execute(route = "option_option") @Between(min = 0, max = 2)
    public void execute(@Arg(0) @Handler(DoubleArg.class) Option<Double> first, @Arg(1) @Handler(StringArg.class) Option<String> second) {
        resultOptionOption.update(first == null, second.isEmpty());
    }

    @Execute(route = "customstring_string") @Between(min = 0, max = 2)
    public void executeCustom1(@Arg(0) @Handler(CustomStringArg.class) String custom, @Arg(1) @Handler(StringArg.class) String string) {
        resultCustom1.setString(string);
        resultCustom1.setCustomString(custom);
    }

    @Execute(route = "string_customstring") @Between(min = 0, max = 2)
    public void executeCustom2(@Arg(0) @Handler(StringArg.class) String string, @Arg(1) @Handler(CustomStringArg.class) String custom) {
        resultCustom2.setString(string);
        resultCustom2.setCustomString(custom);
    }

    @IgnoreClass
    public static class ResultCustom {
        private String string = null;
        private String customString = null;

        public void setString(String string) {
            this.string = string;
        }

        public void setCustomString(String customString) {
            this.customString = customString;
        }

        public String getString() {
            if (string == null) {
                throw new NullPointerException();
            }

            return string;
        }

        public String getCustomString() {
            if (customString == null) {
                throw new NullPointerException();
            }

            return customString;
        }

    }


    @IgnoreClass
    public static class Result {
        private int executions = 0;
        private int firstIsNull = 0;
        private int secondIsNull = 0;

        public void update(boolean firstIsNull, boolean secondIsNull) {
            executions++;

            if (firstIsNull) {
                this.firstIsNull++;
            }

            if (secondIsNull) {
                this.secondIsNull++;
            }
        }

        public void update(boolean firstIsNull) {
            this.update(firstIsNull, false);
            this.secondIsNull = -1;
        }

        public int getExecutions() {
            return executions;
        }

        public int getFirstIsNull() {
            return firstIsNull;
        }

        public int getSecondIsNull() {
            return secondIsNull;
        }
    }

}


