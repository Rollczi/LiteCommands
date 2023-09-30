package dev.rollczi.litecommands.chatgpt;

import dev.rollczi.litecommands.annotations.LiteCommandsAnnotations;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.chatgpt.annotation.ChatGpt;
import dev.rollczi.litecommands.programmatic.LiteCommand;
import dev.rollczi.litecommands.programmatic.LiteCommandsProgrammatic;
import dev.rollczi.litecommands.unit.AssertSuggest;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;

import java.time.Duration;
import java.time.Instant;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {
        String[] alphabet = "abcdefghijklmnopqrstuvwxyz".split("");
        NavigableMap<String, Boolean> map = new TreeMap<>();

        for (String s1 : alphabet) {
            for (String s2 : alphabet) {
                for (String s3 : alphabet) {
                    for (String s4 : alphabet) {
                        for (String s5 : alphabet) {
                            map.put(s1 + s2 + s3 + s4 + s5, true);
                        }
                    }
                }
            }
        }

        System.out.println(map.size());

        long now = System.nanoTime();
        SortedMap<String, Boolean> sortedMap = map.subMap("siem", "siem" + Character.MAX_VALUE);
        long end = System.nanoTime();

        System.out.println((end - now) / 1000000.0 + "ms");

        System.out.println(sortedMap.size());

//        sortedMap.forEach((s, aBoolean) -> {
//            System.out.println(s);
//        });


//        TestPlatform testPlatform = LiteCommandsTestFactory.startPlatform(builder -> builder
//            .extension(new LiteChatGptExtension<>())
//            .commands(LiteCommandsAnnotations.of(new TestGptCommand()))
//        );
//
//        AssertSuggest assertSuggest = testPlatform.suggest("test-gpt Najlepsza partia w historii Polski to");
//
//        System.out.println(assertSuggest);
    }

    @Command(name = "test-gpt")
    public static class TestGptCommand {

        @Execute
        void execute(@Join @ChatGpt("ban reason") String message) {
            System.out.println(message);
        }

    }

}
