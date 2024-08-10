package dev.rollczi.litecommands.input.raw;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface RawInputView {

    RawInputView sub(int startInclusive, int endExclusive);

    RawInputView sub(int startInclusive);

    String content();

    int countOf(char character);

    String sourceContent();

    String claim();

    String claim(int startInclusive, int endExclusive);

    static RawInputView of(String content) {
        return new RawInputViewFastImpl(content);
    }

    default int indexOf(String string) {
        return content().indexOf(string);
    }

    default int indexOf(String string, int fromIndex) {
        return content().indexOf(string, fromIndex);
    }

    default int length() {
        return content().length();
    }

}
