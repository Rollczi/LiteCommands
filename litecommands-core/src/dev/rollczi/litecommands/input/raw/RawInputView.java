package dev.rollczi.litecommands.input.raw;

public interface RawInputView {

    RawInputView sub(int startInclusive, int endExclusive);

    RawInputView sub(int startInclusive);

    String content();

    String claim();

    String claim(int startInclusive, int endExclusive);

    static RawInputView of(String content) {
        return new RawInputViewImpl(content);
    }

}
