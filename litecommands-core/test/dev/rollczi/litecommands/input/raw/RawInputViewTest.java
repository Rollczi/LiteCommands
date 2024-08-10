package dev.rollczi.litecommands.input.raw;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class RawInputViewTest {

    @Test
    void testSub() {
        RawInputView view = RawInputView.of("<L>, <test [inside]>, <last>");
        RawInputView last = view.sub(20);

        assertThat(last.content())
            .isEqualTo(", <last>");

        {
            String content = view.content();
            assertEquals("<L>, <test [inside]>, <last>", content);

            int startIndex = content.indexOf(",") + 1;
            int endIndex = content.indexOf(",", startIndex);

            RawInputView sub = view.sub(startIndex, endIndex);
            assertEquals(" <test [inside]>", sub.content());

            String claimed = sub.claim(7, 15);
            assertEquals("[inside]", claimed);

            assertEquals("<L>, <test >, <last>", view.content());
            assertEquals(" <test >", sub.content());
        }

        {
            RawInputView sub = view.sub(0, 20);
            String content = sub.content();
            assertEquals("<L>, <test >, <last>", content);

            int startIndex = content.indexOf(",") + 1;
            int endIndex = content.indexOf(",", startIndex);

            RawInputView subSub = sub.sub(startIndex, endIndex);

            assertEquals(" <test >", subSub.content());
            assertEquals(" <test >", subSub.claim());
        }

        {
            assertEquals("<L>,, <last>", view.content());
            assertEquals(",", view.claim(3, 4));
            assertEquals("<L>, <last>", view.content());
        }

        assertEquals("<L>, <last>", view.content());
        assertEquals("<L>", view.claim(0, 3));

        assertEquals(", <last>", view.content());
        assertEquals("", view.claim(0, 0));

        assertEquals(", <last>", last.content());
        assertEquals(", <last>", last.claim());

        assertEquals("", last.content());
        assertEquals("", view.content());
    }

    @Test
    void testSubWithOnlyStart() {
        RawInputView view = RawInputView.of("<L>, <test [inside]>, <last>");

        {
            RawInputView sub = view.sub(4);
            String content = sub.content();
            assertEquals(" <test [inside]>, <last>", content);

            int startIndex = content.indexOf(",") + 1;
            RawInputView subSub = sub.sub(startIndex);

            assertEquals(" <last>", subSub.content());
            assertEquals(" <last>", subSub.claim());

            assertEquals(" <test [inside]>,", sub.content());
            assertEquals(" <test [inside]>,", sub.claim());

            assertEquals("<L>,", view.content());
        }
    }

    @Test
    void testSubWithInvalidRange() {
        RawInputView view = RawInputView.of("0123456789");

        view.sub(0, 10);

        assertThatThrownBy(() -> view.sub(0, 11))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Invalid 'end', expected: [start <= end <= length] but got: [0 <= 11 <= 10]");

        assertThatThrownBy(() -> view.sub(9, 8))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Invalid 'start' and 'end', expected: [start <= end] but got: [9 <= 8]");

        assertThat(view.sub(10, 10));

        assertThatThrownBy(() -> view.sub(-1, 10))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Invalid 'start', expected: [start >= 0] but got: [-1]");
    }

    @Test
    void testClaimWithInvalidRange() {
        RawInputView view = RawInputView.of("0123456789");

        assertThatThrownBy(() -> view.claim(0, 11))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Invalid 'end', expected: [start <= end <= length] but got: [0 <= 11 <= 10]");

        assertThatThrownBy(() -> view.claim(9, 8))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Invalid 'start' and 'end', expected: [start <= end] but got: [9 <= 8]");

        assertThatThrownBy(() -> view.claim(-1, 10))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Invalid 'start', expected: [start >= 0] but got: [-1]");

        view.claim(0, 10);
    }

}