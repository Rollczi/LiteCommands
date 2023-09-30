package dev.rollczi.litecommands.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SchedulerPoll {

    /**
     * The main thread - used as default
     */
    public static final SchedulerPoll MAIN = new SchedulerPoll("main");

    /**
     * The async thread - used for async tasks
     * If not specified, the main thread will be used
     */
    public static final SchedulerPoll ASYNCHRONOUS = new SchedulerPoll("async", MAIN);

    /**
     * The completable future thread - used for completable future tasks
     * If not specified, the async thread will be used
     */
    public static final SchedulerPoll COMPLETABLE_FUTURE = new SchedulerPoll("completable_future", ASYNCHRONOUS);

    /**
     * The executor thread - used for executor tasks
     * If not specified, the main thread will be used
     */
    public static final SchedulerPoll EXECUTOR = new SchedulerPoll("executor", MAIN);

    private final String name;
    private final Set<SchedulerPoll> replaceable = new HashSet<>();

    private SchedulerPoll(String name, SchedulerPoll... replaceable) {
        this.name = name;
        this.replaceable.addAll(Arrays.asList(replaceable));
    }

    public SchedulerPoll resolve(SchedulerPoll... available) {
        return resolve(Arrays.asList(available));
    }

    public SchedulerPoll resolve(Collection<SchedulerPoll> available) {
        if (available.isEmpty()) {
            throw new IllegalStateException("Cannot resolve the thread type");
        }

        if (available.contains(this)) {
            return this;
        }

        for (SchedulerPoll poll : replaceable) {
            if (available.contains(poll)) {
                return poll;
            }
        }

        throw new IllegalStateException("Cannot resolve the thread type");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulerPoll that = (SchedulerPoll) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
