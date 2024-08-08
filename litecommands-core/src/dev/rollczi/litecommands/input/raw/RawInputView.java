package dev.rollczi.litecommands.input.raw;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public interface RawInputView {

    RawInputView sub(int startInclusive, int endExclusive);

    RawInputView sub(int startInclusive);

    String content();

    @ApiStatus.Experimental
    int countOf(char character);

    String sourceContent();

    String claim();

    String claim(int startInclusive, int endExclusive);

    static RawInputView of(String content) {
        return new RawInputViewImpl(content);
    }

    default int indexOf(String string) {
        return content().indexOf(string);
    }

    default int indexOf(String string, int fromIndex) {
        return content().indexOf(string, fromIndex);
    }

    default int indexOfWithSkip(String space, int countToSkip) {
        String content = content();
        int index = -1;

        for (int i = 0; i < countToSkip; i++) {
            int indexOf = content.indexOf(space, index + 1);

            if (indexOf == -1) {
                return -1;
            }

            index = indexOf;
        }

        return index;
    }

    default int length() {
        return content().length();
    }

}
