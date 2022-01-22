package dev.rollczi.litecommands.scope;

import dev.rollczi.litecommands.annotations.Section;
import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.valid.ValidationInfo;
import org.jetbrains.annotations.Nullable;
import panda.std.Option;
import panda.utilities.text.Joiner;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Section(route = "test", priority = 1, aliases = {""})
public final class ScopeUtils {

    private ScopeUtils() {}

    @Nullable
    public static String getLastMessage(ValidationInfo info, LiteComponent.ContextOfResolving context) {
        List<LiteComponent> resolvers = context.getTracesOfResolvers();
        ListIterator<LiteComponent> iterator = resolvers.listIterator(resolvers.size());
        Option<String> message = Option.none();

        while (iterator.hasPrevious() && !message.isPresent()) {
            message = iterator.previous().getScope().getMessage(info);
        }

        return message.getOrNull();
    }


    public static String annotationFormat(ScopeMetaData scope) {
        ArrayList<String> list = new ArrayList<>();

        list.add("route = \"" + scope.getName() + "\"");

        if (scope.getPriority() != - 1) {
            list.add("priority = " + scope.getPriority());
        }

        return "@Section(" + Joiner.on(", ").join(list) + ")";
    }


}
