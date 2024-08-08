package dev.rollczi.litecommands.util;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class StringUtilTest {

    @Test
    void spilt() {
        assertThat(StringUtil.spilt("a,b,c,d", ","))
            .containsExactly("a", "b", "c", "d");

        assertThat(StringUtil.spilt("a,b,c,d,", ","))
            .containsExactly("a", "b", "c", "d", "");
    }

    @Test
    void spiltWithSpace() {
        assertThat(StringUtil.spilt("a b c d", " "))
            .containsExactly("a", "b", "c", "d");

        assertThat(StringUtil.spilt("a b c d ", " "))
            .containsExactly("a", "b", "c", "d", "");
    }

}