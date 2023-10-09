package dev.rollczi.litecommands.input;

public interface InputMatcher {

    boolean hasNextRoute();

    String nextRoute();

    String showNextRoute();

}
