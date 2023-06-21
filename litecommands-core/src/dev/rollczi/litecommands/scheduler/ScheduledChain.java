package dev.rollczi.litecommands.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class ScheduledChain<CHAIN extends ScheduledChainLink<? extends T>, T, R> {

    private final List<CHAIN> chain = new ArrayList<>();
    private final BiFunction<CHAIN, T, R> mapper;

    private ScheduledChain(Collection<CHAIN> chain, BiFunction<CHAIN, T, R> mapper) {
        this.chain.addAll(chain);
        this.mapper = mapper;
    }

    public CompletableFuture<ScheduledChainResult<R>> collectChain(Scheduler scheduler) {
        ChainCollector collector = new ChainCollector(scheduler);

        return collector.collect();
    }

    public static <CHAIN extends ScheduledChainLink<? extends T>, T> Builder<CHAIN, T> builder() {
        return new Builder<>();
    }

    public static class Builder<CHAIN extends ScheduledChainLink<? extends T>, T> {

        private final List<CHAIN> chain = new ArrayList<>();

        public Builder<CHAIN, T> link(CHAIN link) {
            chain.add(link);
            return this;
        }

        public Builder<CHAIN, T> links(Collection<CHAIN> links) {
            chain.addAll(links);
            return this;
        }

        public <R> ScheduledChain<CHAIN, T, R> build(Function<T, R> mapper) {
            return new ScheduledChain<>(chain, (chain1, t) -> mapper.apply(t));
        }

        public <R> ScheduledChain<CHAIN, T, R> build(BiFunction<CHAIN, T, R> mapper) {
            return new ScheduledChain<>(chain, mapper);
        }

        public ScheduledChain<CHAIN, T, T> build() {
            return build(t -> t);
        }

    }

    private class ChainCollector {

        private final Iterator<CHAIN> chainLinkIterator;
        private final Scheduler scheduler;

        ChainCollector(Scheduler scheduler) {
            this.scheduler = scheduler;
            this.chainLinkIterator = chain.iterator();
        }

        CompletableFuture<ScheduledChainResult<R>> collect() {
            return collect(new ArrayList<>());
        }

        private CompletableFuture<ScheduledChainResult<R>> collect(List<R> results) {
            if (!chainLinkIterator.hasNext()) {
                return completedFuture(ScheduledChainResult.success(results));
            }

            CHAIN chainLink = chainLinkIterator.next();

            return scheduler
                .supply(chainLink.type(), () -> {
                    try {
                        R returnValue = mapper.apply(chainLink, chainLink.call());
                        results.add(returnValue);

                        return this.collect(results);
                    }
                    catch (ScheduledChainException exception) {
                        return completedFuture(ScheduledChainResult.<R>failure(exception.getReason()));
                    }
                })
                .thenCompose(completableFuture -> completableFuture);
        }

    }

}
