package dev.rollczi.litecommands.input.raw;

import java.util.Arrays;

public class RawInputViewImpl implements RawInputView {

    private final String sourceContent;
    private final char[] content;
    private final boolean[] claimedIndexes;
    private final StringBuilder notClaimedContent;

    public RawInputViewImpl(String content) {
        this.sourceContent = content;
        this.content = content.toCharArray();
        this.notClaimedContent = new StringBuilder(content);
        this.claimedIndexes = new boolean[content.length()];
    }

    @Override
    public RawInputView sub(int startInclusive, int endExclusive) {
        return internalSub(0, this.content.length, startInclusive, endExclusive);
    }

    @Override
    public RawInputView sub(int startInclusive) {
        return this.sub(startInclusive, this.notClaimedContent.length());
    }

    RawInputView internalSub(int baseStart, int baseEnd, int startInclusive, int endExclusive) {
        if (startInclusive >= endExclusive) {
            throw new IllegalArgumentException("Invalid 'start' and 'end', expected: [start < end] but got: [" + startInclusive + " < " + endExclusive + "]");
        }

        if (startInclusive < 0) {
            throw new IllegalArgumentException("Invalid 'start', expected: [start >= 0] but got: [" + startInclusive + "]");
        }

        int internalFrom = -1;
        int internalTo = -1;

        int realIndex = 0;
        for (int i = baseStart; i < baseEnd; i++) {
            if (realIndex + 1 == endExclusive) {
                internalTo = i + 1;
            }

            if (claimedIndexes[i]) {
                continue;
            }

            if (realIndex == startInclusive) {
                internalFrom = i;
            }

            realIndex++;
        }

        if (internalFrom == -1) {
            throw new IllegalArgumentException("Invalid 'start', expected: [start < end <= length] but got: [" + startInclusive + " < " + endExclusive + " <= " + realIndex + "]");
        }

        if (internalTo == -1) {
            throw new IllegalArgumentException("Invalid 'end', expected: [start < end <= length] but got: [" + startInclusive + " < " + endExclusive + " <= " + realIndex + "]");
        }

        return new RawInputSubView(internalFrom, internalTo);
    }

    @Override
    public String content() {
        return this.notClaimedContent.toString();
    }

    public String sourceContent() {
        return this.sourceContent;
    }

    @Override
    public String claim() {
        String claimedContent = this.notClaimedContent.toString();
        this.notClaimedContent.delete(0, this.notClaimedContent.length());
        Arrays.fill(this.claimedIndexes, true);

        return claimedContent;
    }

    @Override
    public String claim(int startInclusive, int endExclusive) {
        return internalClaim(0, this.content.length, startInclusive, endExclusive);
    }

    String internalClaim(int baseStart, int baseEnd, int startInclusive, int endExclusive) {
        if (startInclusive < 0) {
            throw new IllegalArgumentException("Invalid 'start', expected: [start >= 0] but got: [" + startInclusive + "]");
        }

        if (startInclusive > endExclusive) {
            throw new IllegalArgumentException("Invalid 'start' and 'end', expected: [start <= end] but got: [" + startInclusive + " <= " + endExclusive + "]");
        }

        if (endExclusive > this.notClaimedContent.length()) {
            throw new IllegalArgumentException("Invalid 'end', expected: [start <= end <= length] but got: [" + startInclusive + " <= " + endExclusive + " <= " + this.notClaimedContent.length() + "]");
        }

        StringBuilder builder = new StringBuilder();

        int realIndex = 0;
        for (int i = baseStart; i < baseEnd; i++) {
            boolean isClaimed = this.claimedIndexes[i];

            if (isClaimed) {
                continue;
            }

            if (realIndex < startInclusive) {
                realIndex++;
                continue;
            }

            if (realIndex >= endExclusive) {
                break;
            }

            builder.append(this.content[i]);
            this.claimedIndexes[i] = true;
            this.notClaimedContent.deleteCharAt(baseStart + startInclusive);
            realIndex++;
        }

        return builder.toString();
    }

    private class RawInputSubView implements RawInputView {

        private final int internalFrom;
        private final int internalTo;

        public RawInputSubView(int internalFrom, int internalTo) {
            this.internalFrom = internalFrom;
            this.internalTo = internalTo;
        }

        @Override
        public RawInputView sub(int startInclusive, int endExclusive) {
            return internalSub(this.internalFrom, this.internalTo, startInclusive, endExclusive);
        }

        @Override
        public RawInputView sub(int startInclusive) {
            return this.sub(startInclusive, this.content().length());
        }

        @Override
        public String content() {
            StringBuilder builder = new StringBuilder();

            for (int i = this.internalFrom; i < this.internalTo; i++) {
                if (claimedIndexes[i]) {
                    continue;
                }

                builder.append(content[i]);
            }

            return builder.toString();
        }

        @Override
        public String claim() {
            return internalClaim(this.internalFrom, this.internalTo, 0, this.content().length());
        }

        @Override
        public String claim(int startInclusive, int endExclusive) {
            return internalClaim(this.internalFrom, this.internalTo, startInclusive, endExclusive);
        }

    }
}
