package dev.rollczi.litecommands.input.raw;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.Nullable;

public class RawInputViewFastImpl implements RawInputView {

    protected final NavigableMap<Integer, Integer> claimedIndexes = new TreeMap<>();

    protected final char[] sourceContentChars;
    protected final String sourceContent;

    protected @Nullable String notClaimedCache;
    protected final AtomicInteger subIdSequence = new AtomicInteger();
    protected final Map<Integer, String> notClaimedSubsCache = new HashMap<>();

    public RawInputViewFastImpl(String content) {
        this.sourceContentChars = content.toCharArray();
        this.sourceContent = content;
        this.notClaimedCache = content;
    }

    @Override
    public String claim() {
        return claim(0, sourceContentChars.length);
    }

    @Override
    public String claim(int startInclusive, int endExclusive) {
        return this.sub(startInclusive, endExclusive).claim();
    }

    protected String internalClaim(int startInclusive, int endExclusive) {
        List<Entry<Integer, Integer>> newClaimedIndexes = new ArrayList<>();
        int toClaim = endExclusive - startInclusive;
        int lastBorderEnd = startInclusive;

        for (Entry<Integer, Integer> entry : claimedIndexes.entrySet()) {
            int borderStart = entry.getKey();
            int borderEnd = entry.getValue();

            if (borderEnd <= startInclusive) {
                continue;
            }

            if (borderStart >= endExclusive) {
                break;
            }

            if (toClaim > 0) {
                toClaim -= borderStart - lastBorderEnd;

                if (toClaim > 0) {
                    if (lastBorderEnd < borderStart) {
                        newClaimedIndexes.add(new SimpleEntry<>(lastBorderEnd, borderStart));
                    }
                }
                else {
                    newClaimedIndexes.add(new SimpleEntry<>(lastBorderEnd, borderStart + toClaim));
                }
            }

            toClaim -= borderEnd - borderStart;
            lastBorderEnd = borderEnd;
        }

        if (toClaim > 0) {
            newClaimedIndexes.add(new SimpleEntry<>(lastBorderEnd, lastBorderEnd + toClaim));
        }

        for (Entry<Integer, Integer> entry : newClaimedIndexes) {
            int start = entry.getKey();
            int end = entry.getValue();

            Entry<Integer, Integer> lowerEntry = claimedIndexes.lowerEntry(start + 1);
            if (lowerEntry != null && lowerEntry.getValue() >= start) {
                start = lowerEntry.getKey();

                claimedIndexes.remove(lowerEntry.getKey());
            }

            Entry<Integer, Integer> higherEntry = claimedIndexes.higherEntry(end - 1);
            if (higherEntry != null && higherEntry.getKey() <= end) {
                end = higherEntry.getValue();

                claimedIndexes.remove(higherEntry.getKey());
            }

            claimedIndexes.put(start, end);
        }

        notClaimedSubsCache.clear();
        notClaimedCache = null;

        StringBuilder builder = new StringBuilder();

        for (Entry<Integer, Integer> entry : newClaimedIndexes) {
            builder.append(sourceContent, entry.getKey(), entry.getValue());
        }

        return builder.toString();
    }

    @Override
    public String content() {
        if (notClaimedCache != null) {
            return notClaimedCache;
        }

        notClaimedCache = rerenderContent();
        return notClaimedCache;
    }

    private String rerenderContent() {
        if (claimedIndexes.isEmpty()) {
            return sourceContent;
        }

        char[] result = new char[sourceContent.length()];
        int lastSaved = 0;

        int lastNotClaimed = 0;
        for (Entry<Integer, Integer> entry : claimedIndexes.entrySet()) {
            int start = entry.getKey();

            System.arraycopy(sourceContentChars, lastNotClaimed, result, lastSaved, start - lastNotClaimed);
            lastSaved += start - lastNotClaimed;
            lastNotClaimed = entry.getValue();
        }

        System.arraycopy(sourceContentChars, lastNotClaimed, result, lastSaved, sourceContentChars.length - lastNotClaimed);
        lastSaved += sourceContentChars.length - lastNotClaimed;

        if (lastSaved < result.length) {
            result = Arrays.copyOf(result, lastSaved);
        }

        return new String(result);
    }

    @Override
    public RawInputView sub(int startInclusive, int endExclusive) {
        int claimedBefore = claimedCount(0, startInclusive);
        int claimedInside = claimedCount(startInclusive, endExclusive);
        return new RawInputSubView(claimedBefore + startInclusive, claimedBefore + claimedInside + endExclusive);
    }

    private int claimedCount(int startInclusive, int endExclusive) {
        int count = 0;

        for (Entry<Integer, Integer> entry : claimedIndexes.entrySet()) {
            if (entry.getValue() <= startInclusive) {
                continue;
            }

            if (entry.getKey() >= endExclusive) {
                break;
            }

            count +=  (entry.getValue() - entry.getKey()) - Math.max(0, startInclusive - entry.getKey());
        }

        return count;
    }

    @Override
    public RawInputView sub(int startInclusive) {
        return sub(startInclusive, sourceContentChars.length);
    }

    @Override
    public int countOf(char character) {
        String content = content();
        int count = 0;

        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == character) {
                count++;
            }
        }

        return count;
    }

    @Override
    public String sourceContent() {
        return sourceContent;
    }

    private class RawInputSubView implements RawInputView {

        private final Integer id = subIdSequence.getAndIncrement();
        private final int internalFrom;
        private final int internalTo;
        private final String sourceContent;

        public RawInputSubView(int internalFrom, int internalTo) {
            this.internalFrom = internalFrom;
            this.internalTo = internalTo;
            this.sourceContent = RawInputViewFastImpl.this.sourceContent.substring(internalFrom, internalTo);
        }

        @Override
        public String content() {
            String subContent = RawInputViewFastImpl.this.notClaimedSubsCache.get(id);

            if (subContent != null) {
                return subContent;
            }

            String content = this.renderContent();
            RawInputViewFastImpl.this.notClaimedSubsCache.put(id, content);

            return content;
        }

        private String renderContent() {
            if (claimedIndexes.isEmpty()) {
                return this.sourceContent();
            }

            char[] result = new char[internalTo - internalFrom];
            int lastSaved = 0;

            int skiped = 0;
            int lastBorderEnd = this.internalFrom;
            for (Entry<Integer, Integer> entry : claimedIndexes.entrySet()) {
                int start = Math.max(entry.getKey(), internalFrom);
                int end = entry.getValue();

                if (end <= internalFrom) {
                    lastBorderEnd = end;
                    continue;
                }

                if (start >= internalTo) {
                    break;
                }

                int length = start - lastBorderEnd;

                System.arraycopy(sourceContentChars, lastBorderEnd, result, lastBorderEnd - internalFrom, length);
                lastSaved += length;
                skiped = end - start;
                lastBorderEnd = end;
            }

            int lengthCopy = internalTo - internalFrom - lastSaved - skiped;
            System.arraycopy(sourceContentChars, lastBorderEnd, result, lastSaved, lengthCopy);
            lastSaved += lengthCopy;

            if (lastSaved < result.length) {
                result = Arrays.copyOf(result, lastSaved);
            }

            return new String(result);
        }

        @Override
        public RawInputView sub(int startInclusive, int endExclusive) {
            int claimedCount0 = claimedCount(this.internalFrom, this.internalFrom + startInclusive);
            int claimedCount = claimedCount(this.internalFrom + startInclusive, this.internalFrom + endExclusive);
            return new RawInputSubView(this.internalFrom + claimedCount0 + startInclusive, this.internalFrom + claimedCount0 + claimedCount + endExclusive);
        }

        @Override
        public RawInputView sub(int startInclusive) {
            return sub(startInclusive, this.internalTo - this.internalFrom);
        }

        @Override
        public int countOf(char character) {
            String content = content();
            int count = 0;

            for (int i = 0; i < content.length(); i++) {
                if (content.charAt(i) == character) {
                    count++;
                }
            }

            return count;
        }

        @Override
        public String sourceContent() {
            return this.sourceContent;
        }

        @Override
        public String claim() {
            return internalClaim(this.internalFrom, this.internalTo);
        }

        @Override
        public String claim(int startInclusive, int endExclusive) {
            return this.sub(startInclusive, endExclusive).claim();
        }

    }

}
