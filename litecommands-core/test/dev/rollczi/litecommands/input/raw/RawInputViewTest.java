package dev.rollczi.litecommands.input.raw;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class RawInputViewTest {

    @Test
    void testSub() {
        RawInputView view = RawInputView.of("siema, co tam u, ciebie?");
        String content = view.content();

        assertEquals("siema, co tam u, ciebie?", content);
        int index = content.indexOf(",") + 1;
        int index2 = content.indexOf(",", index);

        {
            RawInputView sub = view.sub(index, index2);
            assertEquals(" co tam u", sub.content());

            String claimed = sub.claim(4, 7);
            assertEquals("tam", claimed);

            assertEquals("siema, co  u, ciebie?", view.content());
            assertEquals(" co  u", sub.content());
        }

        {
            RawInputView sub = view.sub(index, index2);
            assertEquals(" co  u, c", sub.content());

            RawInputView subSub = sub.sub(1, 3);
            assertEquals("co", subSub.content());

            assertEquals(" co  u, c", sub.claim());
        }

        assertEquals("siema,iebie?", view.content());
        assertEquals("iema", view.claim(1, 5));

        assertEquals("s,iebie?", view.content());
        assertEquals("", view.claim(0, 0));

        assertEquals("s,iebie?", view.content());
        assertEquals("s,iebie?", view.claim());

        assertEquals("", view.content());
    }

}