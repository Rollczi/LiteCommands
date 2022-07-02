package dev.rollczi.litecommands.sugesstion;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.command.LiteInvocation;

public class SuggestionMerger {

    private final int argumentLevel;
    private SuggestionStack root = SuggestionStack.empty();

    private SuggestionMerger(int argumentLevel) {
        this.argumentLevel = argumentLevel;
    }

    public SuggestionMerger append(int route, Suggestion suggestion) {
        if (route == argumentLevel) {
            this.root = root.with(suggestion);
            return this;
        }

        if (!suggestion.isMultilevel() || route > argumentLevel || route + (suggestion.lengthMultilevel() - 1) < argumentLevel) {
            return this;
        }

        this.append(route + 1, suggestion.slashLevel(1));
        return this;
    }

    public SuggestionMerger appendRoot(SuggestionStack suggestions) {
        for (Suggestion suggestion : suggestions.suggestions) {
            this.root = this.root.with(suggestion);
        }

        return this;
    }

    public SuggestionMerger append(int route, SuggestionStack suggestions) {
        if (route > argumentLevel) {
            return this;
        }

        for (Suggestion suggestion : suggestions.suggestions()) {
            this.append(route, suggestion);
        }

        return this;
    }

    public SuggestionMerger append(int route, UniformSuggestionStack suggestions) {
        if (route == argumentLevel) {
            this.root = root.with(suggestions.suggestions());
            return this;
        }

        if (!suggestions.isMultilevel() || route > argumentLevel || route + (suggestions.lengthMultilevel() - 1) < argumentLevel) {
            return this;
        }

        this.append(route + 1, suggestions.slashLevel(1));
        return this;
    }

    public SuggestionStack merge() {
        return this.root;
    }

    public static SuggestionMerger empty(Invocation<?> context) {
        return new SuggestionMerger(context.arguments().length);
    }

    public static SuggestionMerger empty(LiteInvocation context) {
        return new SuggestionMerger(context.arguments().length);
    }

}
