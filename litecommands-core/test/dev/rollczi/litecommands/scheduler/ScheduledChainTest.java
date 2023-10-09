package dev.rollczi.litecommands.scheduler;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduledChainTest {

    @Test
    void test() {
        Scheduler scheduler = new SchedulerExecutorPoolImpl("test", 4);

        ScheduledChain<Argument, String, String> scheduledChain = ScheduledChain.<Argument, String>builder()
            .link(new Argument("1", SchedulerPoll.MAIN))
            .link(new Argument("2", SchedulerPoll.MAIN))
            .link(new Argument("3", SchedulerPoll.ASYNCHRONOUS))
            .link(new Argument("4", SchedulerPoll.MAIN))
            .link(new Argument("5", SchedulerPoll.ASYNCHRONOUS))
            .link(new Argument("6", SchedulerPoll.MAIN))
            .build(text -> text + " -> " + Thread.currentThread().getName());

        List<String> list = scheduledChain.collectChain(scheduler)
            .join()
            .getSuccess();

        assertEquals(6, list.size());
        assertEquals("1 -> scheduler-test-main", list.get(0));
        assertEquals("2 -> scheduler-test-main", list.get(1));
        assertEquals("3 -> scheduler-test-async-0", list.get(2));
        assertEquals("4 -> scheduler-test-main", list.get(3));
        assertEquals("5 -> scheduler-test-async-1", list.get(4));
        assertEquals("6 -> scheduler-test-main", list.get(5));
    }

    static class Argument implements ScheduledChainLink<String> {

        private final String value;
        private final SchedulerPoll type;

        Argument(String value, SchedulerPoll type) {
            this.value = value;
            this.type = type;
        }

        @Override
        public String call() {
            return value;
        }

        @Override
        public SchedulerPoll type() {
            return type;
        }
    }

}