package dev.rollczi.litecommands.input.raw;

import dev.rollczi.litecommands.util.StringUtil;
import java.util.ArrayList;
import java.util.List;

class ViewBasedRawInputImpl implements RawInput {

    private final RawInputView rawInputView;

    public ViewBasedRawInputImpl(RawInputView rawInputView) {
        this.rawInputView = rawInputView;
    }

    @Override
    public boolean hasNext() {
        return !this.rawInputView.content().isEmpty();
    }

    @Override
    public String next() {
        int index = this.rawInputView.indexOf(RawCommand.COMMAND_SEPARATOR);

        if (index == -1) {
            return this.rawInputView.claim();
        }

        return this.rawInputView.claim(0, index);
    }

    @Override
    public List<String> next(int count) {
        List<String> list = new ArrayList<>();

        String content = this.rawInputView.content();

        int spaceCount = 0;
        int indexToClaim = 0;
        for (String element : StringUtil.splitBySpace(content)) {
            if (spaceCount >= count) {
                break;
            }

            list.add(element);
            spaceCount++;
            indexToClaim += element.length() + RawCommand.COMMAND_SEPARATOR.length();
        }

        if (spaceCount < count) {
            throw new IllegalArgumentException("Cannot consume next " + count + " arguments, only " + spaceCount + " left");
        }

        rawInputView.claim(0, indexToClaim);
        return list;
    }

    @Override
    public List<String> nextAll() {
        return StringUtil.splitBySpace(this.rawInputView.claim());
    }

    @Override
    public String seeNext() {
        String content = this.rawInputView.content();
        int index = this.rawInputView.indexOf(RawCommand.COMMAND_SEPARATOR);

        if (index == -1) {
            return content;
        }

        return content.substring(0, index);
    }

    @Override
    public List<String> seeNext(int count) {
        List<String> list = new ArrayList<>();
        String content = this.rawInputView.content();

        int spaceCount = 0;
        for (String element : StringUtil.splitBySpace(content)) {
            if (spaceCount >= count) {
                break;
            }

            list.add(element);
            spaceCount++;
        }

        if (spaceCount < count) {
            throw new IllegalArgumentException("Cannot see next " + count + " arguments, only " + spaceCount + " left");
        }

        return list;
    }

    @Override
    public List<String> seeAll() {
        return StringUtil.splitBySpace(this.rawInputView.content());

    }

    @Override
    public int size() {
        return rawInputView.countOf(RawCommand.COMMAND_SEPARATOR_CHAR) - 1;
    }

    @Override
    public int consumedCount() {
        String sourceContent = rawInputView.sourceContent();
        String content = rawInputView.content();

        int count = StringUtil.countOf(sourceContent, RawCommand.COMMAND_SEPARATOR_CHAR);
        int countNow = StringUtil.countOf(content, RawCommand.COMMAND_SEPARATOR_CHAR);

        if (!sourceContent.isEmpty()) {
            count += 1;
        }

        if (!content.isEmpty()) {
            countNow += 1;
        }

        return count - countNow;
    }

}
