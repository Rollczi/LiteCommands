package dev.rollczi.litecommands.annotations.argument.resolver.collector;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.Vector;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.jupiter.api.Test;

class CollectionArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute(name = "array")
        String[] testArray(@Arg String[] test) {
            return test;
        }

        @Execute(name = "collection")
        Collection<String> testCollection(@Arg Collection<String> test) {
            return test;
        }

        @Execute(name = "list")
        List<String> testList(@Arg List<String> test) {
            return test;
        }

        @Execute(name = "linkedList")
        LinkedList<String> testLinkedList(@Arg LinkedList<String> test) {
            return test;
        }

        @Execute(name = "vector")
        Vector<String> testVector(@Arg Vector<String> test) {
            return test;
        }

        @Execute(name = "arrayList")
        ArrayList<String> testArrayList(@Arg ArrayList<String> test) {
            return test;
        }

        @Execute(name = "stack")
        Stack<String> testStack(@Arg Stack<String> test) {
            return test;
        }

        @Execute(name = "queue")
        Queue<String> testQueue(@Arg Queue<String> test) {
            return test;
        }

        @Execute(name = "set")
        Set<String> testSet(@Arg Set<String> test) {
            return test;
        }

        @Execute(name = "sortedSet")
        SortedSet<String> testSortedSet(@Arg SortedSet<String> test) {
            return test;
        }

        @Execute(name = "linkedHashSet")
        LinkedHashSet<String> testLinkedHashSet(@Arg LinkedHashSet<String> test) {
            return test;
        }

        @Execute(name = "hashSet")
        HashSet<String> testHashSet(@Arg HashSet<String> test) {
            return test;
        }

    }

    @Test
    void testArray() {
        platform.execute("test array text1 text2 text3 text4")
            .assertSuccess(new String[]{"text1", "text2", "text3", "text4"});
    }

    @Test
    void testCollection() {
        platform.execute("test collection text1 text2 text3 text4")
            .assertSuccess(Arrays.asList("text1", "text2", "text3", "text4"))
            .assertSuccessAs(ArrayList.class);
    }

    @Test
    void testList() {
        platform.execute("test list text1 text2 text3 text4")
            .assertSuccess(Arrays.asList("text1", "text2", "text3", "text4"))
            .assertSuccessAs(ArrayList.class);
    }

    @Test
    void testLinkedList() {
        platform.execute("test linkedList text1 text2 text3 text4")
            .assertSuccess(Arrays.asList("text1", "text2", "text3", "text4"))
            .assertSuccessAs(LinkedList.class);
    }

    @Test
    void testVector() {
        platform.execute("test vector text1 text2 text3 text4")
            .assertSuccess(Arrays.asList("text1", "text2", "text3", "text4"))
            .assertSuccessAs(Vector.class);
    }

    @Test
    void testArrayList() {
        platform.execute("test arrayList text1 text2 text3 text4")
            .assertSuccess(Arrays.asList("text1", "text2", "text3", "text4"))
            .assertSuccessAs(ArrayList.class);
    }

    @Test
    void testStack() {
        platform.execute("test stack text1 text2 text3 text4")
            .assertSuccess(Arrays.asList("text1", "text2", "text3", "text4"))
            .assertSuccessAs(Stack.class);
    }

    @Test
    void testQueue() {
        platform.execute("test queue text1 text2 text3 text4")
            .assertSuccess(Arrays.asList("text1", "text2", "text3", "text4"))
            .assertSuccessAs(LinkedList.class);
    }

    @Test
    void testSet() {
        HashSet hashSet = platform.execute("test set text1 text2 text1 text4")
            .assertSuccessAs(HashSet.class);

        assertThat(hashSet).containsExactlyInAnyOrder("text1", "text2", "text4");
    }


}
