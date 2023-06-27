package dev.rollczi.litecommands.suggestion;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SuggestionStream<T> implements AutoCloseable {

    private final Stream<T> stream;

    private SuggestionStream(Stream<T> stream) {
        this.stream = stream;
    }

    public SuggestionStream<T> concat(SuggestionStream<T> other) {
        return new SuggestionStream<>(Stream.concat(this.stream, other.stream));
    }

    public SuggestionStream<T> concat(Collection<T> other) {
        return new SuggestionStream<>(Stream.concat(this.stream, other.stream()));
    }

    @SafeVarargs
    public final SuggestionStream<T> concat(T... other) {
        return new SuggestionStream<>(Stream.concat(this.stream, Stream.of(other)));
    }

    public SuggestionResult collect(Function<T, String> mapper) {
        return stream
            .map(mapper)
            .collect(SuggestionResult.collector());
    }

    public SuggestionStream<T> filter(Predicate<? super T> predicate) {
        return new SuggestionStream<>(stream.filter(predicate));
    }

    public <R> SuggestionStream<R> map(Function<? super T, ? extends R> mapper) {
        return new SuggestionStream<>(stream.map(mapper));
    }

    public <R> SuggestionStream<R> flatMapStream(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return new SuggestionStream<>(stream.flatMap(mapper));
    }

    public <R> SuggestionStream<R> flatMap(Function<? super T, ? extends Collection<? extends R>> mapper) {
        return new SuggestionStream<>(stream.flatMap(t -> mapper.apply(t).stream()));
    }

    public SuggestionStream<T> distinct() {
        return new SuggestionStream<>(stream.distinct());
    }

    public SuggestionStream<T> sorted() {
        return new SuggestionStream<>(stream.sorted());
    }

    public SuggestionStream<T> sorted(Comparator<? super T> comparator) {
        return new SuggestionStream<>(stream.sorted(comparator));
    }

    public SuggestionStream<T> peek(Consumer<? super T> action) {
        return new SuggestionStream<>(stream.peek(action));
    }

    public SuggestionStream<T> limit(long maxSize) {
        return new SuggestionStream<>(stream.limit(maxSize));
    }

    public SuggestionStream<T> skip(long n) {
        return new SuggestionStream<>(stream.skip(n));
    }

    @NotNull
    public SuggestionStream<T> unordered() {
        return new SuggestionStream<>(stream.unordered());
    }

    @NotNull
    public SuggestionStream<T> onClose(Runnable closeHandler) {
        return new SuggestionStream<>(stream.onClose(closeHandler));
    }

    @Override
    public void close() {
        stream.close();
    }

    public Stream<T> original() {
        return stream;
    }

    @SafeVarargs
    public static <T> SuggestionStream<T> of(T... suggestions) {
        return new SuggestionStream<>(Stream.of(suggestions));
    }

    public static <T> SuggestionStream<T> of(Collection<T> suggestions) {
        return new SuggestionStream<>(suggestions.stream());
    }

}
