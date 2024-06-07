package dev.rollczi.litecommands.input.raw;

import dev.rollczi.litecommands.util.StringUtil;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;

class RawInputViewLegacyAdapterTest {

    @Test
    void testClaimChar() {
        RawInput rawInput = RawInput.of(StringUtil.splitBySpace("first second third fourth"));
        RawInputViewLegacyAdapter view = new RawInputViewLegacyAdapter(rawInput);

        assertThat(view.content())
            .isEqualTo("first second third fourth");

        assertThat(view.claim(3, 5))
            .isEqualTo("st");

        assertThat(view.content())
            .isEqualTo("fir second third fourth");

        view.applyChanges();
        assertThat(String.join(" ", rawInput.seeAll()))
            .isEqualTo("second third fourth");

        assertThat(view.claim(10, 11))
            .isEqualTo(" ");

        assertThat(view.content())
            .isEqualTo("fir secondthird fourth");

        view.applyChanges();
        assertThat(String.join(" ", rawInput.seeAll()))
            .isEqualTo("third fourth");

        assertThat(view.claim(16, 22))
            .isEqualTo("fourth");

        assertThat(view.content())
            .isEqualTo("fir secondthird ");

        view.applyChanges();
        assertThat(String.join(" ", rawInput.seeAll()))
            .isEqualTo("");
    }

}