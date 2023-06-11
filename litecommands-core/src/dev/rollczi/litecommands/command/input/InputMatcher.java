package dev.rollczi.litecommands.command.input;

public interface InputMatcher {

    boolean hasNextRoute();

    String nextRoute();

    String showNextRoute();

}
