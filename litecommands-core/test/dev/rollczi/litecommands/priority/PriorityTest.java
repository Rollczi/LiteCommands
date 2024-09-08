package dev.rollczi.litecommands.priority;

import java.util.Iterator;
import java.util.TreeSet;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class PriorityTest {

    static class TestEntry implements Prioritized {
        private final String name;
        private final PriorityLevel level;

        TestEntry(String name, PriorityLevel level) {
            this.name = name;
            this.level = level;
        }

        @Override
        public PriorityLevel getPriority() {
            return level;
        }
    }

    @Test
    void test() {
        TreeSet<TestEntry> set = new TreeSet<>();
        set.add(new TestEntry("4", PriorityLevel.HIGH));
        set.add(new TestEntry("0", PriorityLevel.NONE));
        set.add(new TestEntry("1", PriorityLevel.LOWEST));
        set.add(new TestEntry("5", PriorityLevel.HIGHEST));
        set.add(new TestEntry("2", PriorityLevel.LOW));
        set.add(new TestEntry("6", PriorityLevel.MAX));
        set.add(new TestEntry("3", PriorityLevel.NORMAL));

        Iterator<TestEntry> iterator = set.iterator();

        assertPriority(iterator, "0");
        assertPriority(iterator, "1");
        assertPriority(iterator, "2");
        assertPriority(iterator, "3");
        assertPriority(iterator, "4");
        assertPriority(iterator, "5");
        assertPriority(iterator, "6");
    }

    private void assertPriority(Iterator<TestEntry> iterator, String name) {
        TestEntry entry = iterator.next();
        assertEquals(entry.name, name);
    }

}
