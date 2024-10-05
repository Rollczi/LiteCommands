package dev.rollczi.litecommands.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class FutureUtil {
    
    private FutureUtil() {
    }
    
    public static <T> CompletableFuture<List<T>> asList(Collection<CompletableFuture<T>> futures) {
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(joined -> joined.join())
                .collect(Collectors.toList())
            );
    }

}
