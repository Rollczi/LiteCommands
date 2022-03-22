package dev.rollczi.litecommands.scope;

import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.valid.ValidationInfo;
import org.jetbrains.annotations.Nullable;
import panda.std.Option;
import panda.utilities.text.Joiner;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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

        return message.orNull();
    }


    public static String annotationFormat(ScopeMetaData scope) {
        ArrayList<String> list = new ArrayList<>();

        list.add("route = \"" + scope.getName() + "\"");

        if (scope.getPriority() != - 1) {
            list.add("priority = " + scope.getPriority());
        }

        if (scope.isAutoPriority()) {
            list.add("autoPriority = true");
        }

        if (!scope.getAliases().isEmpty()) {
            list.add("aliases = { " + Joiner.on(", ").join(scope.getAliases(), alias -> '"' + alias + '"') + " }");
        }

        return "@Section(" + Joiner.on(", ").join(list) + ")";
    }


}
