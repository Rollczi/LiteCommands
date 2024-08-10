package dev.rollczi.litecommands.input.raw;

public class RawInputViewLegacyAdapter extends RawInputViewFastImpl {

    private final RawInput rawInput;

    public RawInputViewLegacyAdapter(RawInput rawInput) {
        super(String.join(" ", rawInput.seeAll()));
        this.rawInput = rawInput;
    }

    public RawInput applyChanges() {
        if (claimedIndexes.isEmpty()) {
            return rawInput;
        }

        int topIndex = claimedIndexes.lastEntry().getValue() - 1;

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
