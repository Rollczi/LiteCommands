package dev.rollczi.litecommands.literal;

import dev.rollczi.litecommands.argument.profile.ArgumentProfile;
import dev.rollczi.litecommands.argument.profile.ArgumentProfileNamespace;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.meta.MetaKey;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.shared.Preconditions;
import dev.rollczi.litecommands.util.LiteCommandsUtil;
import dev.rollczi.litecommands.util.StringUtil;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class LiteralProfile implements ArgumentProfile<LiteralProfile> {

    public static final ArgumentProfileNamespace<LiteralProfile> NAMESPACE = ArgumentProfileNamespace.of(MetaKey.of("profile:literal", LiteralProfile.class));

    private final Set<String> literals;
    private final Range expectedRange;

    public LiteralProfile(Collection<String> literals, boolean caseInsensitive) {
        Preconditions.notEmpty(literals, "literals");
        LiteCommandsUtil.checkAliases(literals);

        this.literals = createSet(literals, caseInsensitive);
        this.expectedRange = calculateExpectedRange(literals);
    }

    public LiteralProfile(String[] literals, boolean caseInsensitive) {
        this(Arrays.asList(literals), caseInsensitive);
    }

    public Set<String> getLiterals() {
        return literals;
    }

    public Range getExpectedRange() {
        return expectedRange;
    }

    @Override
    public ArgumentProfileNamespace<LiteralProfile> getNamespace() {
        return NAMESPACE;
    }

    public static LiteralProfile of(Collection<String> literals) {
        return new LiteralProfile(literals, false);
    }

    public static LiteralProfile of(String... literals) {
        return new LiteralProfile(literals, false);
    }

    public static LiteralProfile ofIgnoreCase(Collection<String> literals) {
        return new LiteralProfile(literals, true);
    }

    public static LiteralProfile ofIgnoreCase(String... literals) {
        return new LiteralProfile(literals, true);
    }

    private static Set<String> createSet(Collection<String> literals, boolean caseInsensitive) {
        TreeSet<String> set = caseInsensitive ? new TreeSet<>(String.CASE_INSENSITIVE_ORDER) : new TreeSet<>();
        set.addAll(literals);
        return Collections.unmodifiableSortedSet(set);
    }

    private static Range calculateExpectedRange(Collection<String> literals) {
        int min = -1;
        int max = -1;

        for (String literal : literals) {
            int spaces = StringUtil.countOf(literal, RawCommand.COMMAND_SEPARATOR_CHAR) + 1;

            if (min == -1 && max == -1) {
                min = spaces;
                max = spaces;
                continue;
            }

            min = Math.min(min, spaces);
            max = Math.max(max, spaces);
        }

        return Range.range(min, max);
    }

}
