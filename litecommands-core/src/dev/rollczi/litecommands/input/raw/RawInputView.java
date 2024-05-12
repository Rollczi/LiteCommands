package dev.rollczi.litecommands.input.raw;

public interface RawInputView {

    RawInputView sub(int from, int to);

    RawInputView sub(int from);

    String content();

    String claim();

    String claim(int from, int index);

    static RawInputView of(String content) {
        return new RawInputViewImpl(content);
    }

}
