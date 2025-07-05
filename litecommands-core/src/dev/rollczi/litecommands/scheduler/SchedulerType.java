package dev.rollczi.litecommands.scheduler;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class SchedulerType {

    /**
     * The main thread - used as default
     */
    public static final SchedulerType MAIN = new SchedulerType("main");

    /**
     * The async thread - used for async tasks
     * If not specified, the main thread will be used
     */
    public static final SchedulerType ASYNCHRONOUS = new SchedulerType("async", MAIN);

    /**
     * The completable future thread - used for completable future tasks
     * If not specified, the async thread will be used
     */
    public static final SchedulerType COMPLETABLE_FUTURE = new SchedulerType("completable_future", ASYNCHRONOUS);

    /**
     * The executor thread - used for executor tasks
     * If not specified, the main thread will be used
     */
    public static final SchedulerType EXECUTOR = new SchedulerType("executor", MAIN);

    /**
     * The suggester thread - used for generating suggestions
     * If not specified, the async thread will be used
     */
    public static final SchedulerType SUGGESTER = new SchedulerType("suggester", ASYNCHRONOUS);

    private final String name;
    private final Set<SchedulerType> replaceable = new HashSet<>();
    private final boolean logging; //TODO logger level

    protected SchedulerType(String name, SchedulerType... replaceable) {
        this(name, false, replaceable);
    }

    protected SchedulerType(String name, boolean logging, SchedulerType... replaceable) {
        this.name = name;
        this.replaceable.addAll(Arrays.asList(replaceable));
        this.logging = logging;
    }

    public String getName() {
        return name;
    }

    public boolean isLogging() {
        return logging;
    }

    public SchedulerType logging() {
        return new SchedulerType(name, true, replaceable.toArray(new SchedulerType[0]));
    }

    public Optional<SchedulerType> resolve(SchedulerType... available) {
        return resolve(Arrays.asList(available));
    }

    public  Optional<SchedulerType> resolve(Collection<SchedulerType> available) {
        if (available.isEmpty()) {
            throw new IllegalStateException("Cannot resolve the thread type");
        }

        if (available.contains(this)) {
            return Optional.of(this);
        }

        for (SchedulerType poll : replaceable) {
            if (available.contains(poll)) {
                return Optional.of(poll);
            }
        }

        for (SchedulerType poll : replaceable) {
            Optional<SchedulerType> resolve = poll.resolve(available);
            if (resolve.isPresent()) {
                return resolve;
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulerType that = (SchedulerType) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "SchedulerType{" +
            "name='" + name + '\'' +
            ", logging=" + logging +
            '}';
    }

}
