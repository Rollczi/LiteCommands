package dev.rollczi.litecommands.input.raw;

public class RawInputViewLegacyAdapter extends RawInputViewImpl {

    private final RawInput rawInput;
    private int topIndex = -1;

    public RawInputViewLegacyAdapter(RawInput rawInput) {
        super(String.join(" ", rawInput.seeAll()));
        this.rawInput = rawInput;
    }

    @Override
    protected void claimChar(int index) {
        super.claimChar(index);

        if (topIndex < index) {
            topIndex = index;
        }
    }

    @Override
    protected void claimAllChars() {
        super.claimAllChars();
        this.topIndex = this.sourceContent().length();
    }

    public RawInput applyChanges() {
        if (topIndex == -1) {
            return rawInput;
        }

        String sourceContent = sourceContent();

        if (topIndex == sourceContent.length()) {
            rawInput.nextAll();
            return rawInput;
        }


        int toRemove = 1;
        for (char c : sourceContent.substring(0, topIndex).toCharArray()) {
            if (c == ' ') {
                toRemove++;
            }
        }

        toRemove -= this.rawInput.consumedCount();

        this.rawInput.next(toRemove);
        return rawInput;
    }

}
