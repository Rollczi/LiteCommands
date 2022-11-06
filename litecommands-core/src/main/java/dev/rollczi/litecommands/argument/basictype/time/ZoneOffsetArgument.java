package dev.rollczi.litecommands.argument.basictype.time;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ZoneOffsetArgument extends TemporalAccessorArgument<ZoneOffset> {

    private static final List<ZoneOffset> ZONE_OFFSETS = IntStream.range(-18, 19)
        .mapToObj(ZoneOffset::ofHours)
        .collect(Collectors.toList());

    public ZoneOffsetArgument() {
        super("xxx",
            ZoneOffset::from,
            () -> ZONE_OFFSETS
        );
    }

}
