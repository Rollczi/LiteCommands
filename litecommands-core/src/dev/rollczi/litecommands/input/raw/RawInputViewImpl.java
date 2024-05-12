package dev.rollczi.litecommands.input.raw;

import dev.rollczi.litecommands.util.StringUtil;

public class RawInputViewImpl implements RawInputView {

    private final String sourceContent;
    private final char[] content;
    private final boolean[] claimedIndexes;
    private String notClaimedContent;

    public RawInputViewImpl(String content) {
        this.sourceContent = content;
        this.content = content.toCharArray();
        this.notClaimedContent = content;
        this.claimedIndexes = new boolean[content.length()];
    }

    @Override
    public RawInputView sub(int from, int to) {
        int internalFrom = -1;
        int internalTo = -1;

        int realIndex = 0;
        for (int i = 0; i < this.content.length; i++) {
            if (this.claimedIndexes[i]) {
                continue;
            }

            if (realIndex == from) {
                internalFrom = i;
            }

            if (realIndex == to) {
                internalTo = i;
            }

            realIndex++;
        }

        if (internalFrom == -1 || internalTo == -1) {
            throw new IllegalArgumentException("Invalid range");
        }

        return new RawInputSubView(internalFrom, internalTo);
    }

    @Override
    public RawInputView sub(int from) {
        return this.sub(from, this.notClaimedContent.length());
    }

    @Override
    public String content() {
        return this.notClaimedContent;
    }

    public String sourceContent() {
        return this.sourceContent;
    }

    @Override
    public String claim() {
        String claimedContent = this.notClaimedContent;
        this.notClaimedContent = StringUtil.EMPTY;

        for (int i = 0; i < this.claimedIndexes.length; i++) {
            this.claimedIndexes[i] = true;
        }

        return claimedContent;
    }

    @Override
    public String claim(int from, int to) {
        StringBuilder builder = new StringBuilder();

        int realIndex = 0;
        for (int i = 0; i < this.content.length; i++) {
            boolean isClaimed = this.claimedIndexes[i];

            if (isClaimed) {
                continue;
            }

            if (realIndex < from) {
                realIndex++;
                continue;
            }

            if (realIndex >= to) {
                break;
            }

            builder.append(this.content[i]);
            this.claimedIndexes[i] = true;
            realIndex++;
        }

        this.notClaimedContent = this.notClaimedContent.replace(builder.toString(), StringUtil.EMPTY);
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
        public RawInputView sub(int from, int to) {
            int internalFrom = -1;
            int internalTo = -1;

            int realIndex = 0;
            for (int i = this.internalFrom; i < this.internalTo; i++) {
                if (claimedIndexes[i]) {
                    continue;
                }

                if (realIndex == from) {
                    internalFrom = i;
                }

                if (realIndex == to) {
                    internalTo = i;
                }

                realIndex++;
            }

            if (internalFrom == -1 || internalTo == -1) {
                throw new IllegalArgumentException("Invalid range");
            }

            return new RawInputSubView(internalFrom, internalTo);
        }

        @Override
        public RawInputView sub(int from) {
            return this.sub(from, this.content().length());
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
            StringBuilder builder = new StringBuilder();

            for (int i = this.internalFrom; i < this.internalTo; i++) {
                if (claimedIndexes[i]) {
                    continue;
                }

                builder.append(content[i]);
                claimedIndexes[i] = true;
            }

            notClaimedContent = notClaimedContent.replace(builder.toString(), StringUtil.EMPTY);
            return builder.toString();
        }

        @Override
        public String claim(int from, int index) {
            StringBuilder builder = new StringBuilder();

            int realIndex = 0;
            for (int i = this.internalFrom; i < this.internalTo; i++) {
                if (claimedIndexes[i]) {
                    continue;
                }

                if (realIndex < from) {
                    realIndex++;
                    continue;
                }

                if (realIndex >= index) {
                    break;
                }

                builder.append(content[i]);
                claimedIndexes[i] = true;
                realIndex++;
            }

            notClaimedContent = notClaimedContent.replace(builder.toString(), StringUtil.EMPTY);
            return builder.toString();
        }

    }
}
