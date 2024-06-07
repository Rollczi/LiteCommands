package dev.rollczi.litecommands.input.raw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

class RawInputImpl implements RawInput {

    private final List<String> rawArgumentsToConsume;
    private int consumed = 0;

    RawInputImpl(List<String> rawArgumentsToConsume) {
        this.rawArgumentsToConsume = rawArgumentsToConsume;
    }

    @Override
    public boolean hasNext() {
        return !rawArgumentsToConsume.isEmpty();
    }

    @Override
    public String next() {
        if (rawArgumentsToConsume.isEmpty()) {
            throw new NoSuchElementException("No more arguments to consume, consumed: " + consumed);
        }

        consumed++;
        return rawArgumentsToConsume.remove(0);
    }

    @Override
    public List<String> next(int count) {
        if (count > rawArgumentsToConsume.size()) {
            throw new IllegalArgumentException("Cannot consume next " + count + " arguments, only " + rawArgumentsToConsume.size() + " left");
        }

        if (count == 0) {
            return Collections.emptyList();
        }

        consumed += count;
        List<String> sourceView = rawArgumentsToConsume.subList(0, count);
        List<String> copyView = new ArrayList<>(sourceView);

        sourceView.clear();
        return copyView;
    }

    @Override
    public List<String> nextAll() {
        List<String> copy = new ArrayList<>(rawArgumentsToConsume);
        rawArgumentsToConsume.clear();
        consumed += copy.size();
        return copy;
    }

    @Override
    public String seeNext() {
        if (rawArgumentsToConsume.isEmpty()) {
            throw new NoSuchElementException("No more arguments to consume, consumed: " + consumed);
        }

        return rawArgumentsToConsume.get(0);
    }

    @Override
    public List<String> seeNext(int count) {
        if (count > rawArgumentsToConsume.size()) {
            throw new IllegalArgumentException("Cannot see next " + count + " arguments, only " + rawArgumentsToConsume.size() + " left");
        }

        return rawArgumentsToConsume.subList(0, count);
    }

    @Override
    public List<String> seeAll() {
        return seeNext(rawArgumentsToConsume.size());
    }

    @Override
    public int size() {
        return rawArgumentsToConsume.size();
    }

    @Override
    public int consumedCount() {
        return this.consumed;
    }


}
