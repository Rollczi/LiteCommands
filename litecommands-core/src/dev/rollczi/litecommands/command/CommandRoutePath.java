package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.command.executor.CommandExecutor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandRoutePath {

    private final List<String> paths;

    private CommandRoutePath(List<String> paths) {
        this.paths = paths;
    }

    public <SENDER> CommandRoute<SENDER> createReference(CommandExecutor<SENDER> referenceTo) {
        String firstPath = paths.get(0);

        if (paths.size() == 1) {
            return new CommandRouteExecutorReferenceImpl<>(firstPath, referenceTo.getParent(), referenceTo);
        }

        CommandRoute<SENDER> first = new CommandRouteReference<>(firstPath, referenceTo.getParent());
        CommandRoute<SENDER> current = first;

        for (int i = 1; i < paths.size(); i++) {
            String path = paths.get(i);

            if (i == paths.size() - 1) {
                current.appendChildren(new CommandRouteExecutorReferenceImpl<>(path, current, referenceTo));
                break;
            }

            CommandRouteImpl<SENDER> route = new CommandRouteReference<>(path, current);
            current.appendChildren(route);
            current = route;
        }

        return first;
    }

    public static CommandRoutePath of(List<String> paths) {
        return new CommandRoutePath(paths);
    }

    public static CommandRoutePath from(String paths) {
        return new CommandRoutePath(Arrays.asList(paths.split(" ")));
    }

    private static class CommandRouteReference<S> extends CommandRouteImpl<S> {

        CommandRouteReference(String name, CommandRoute<S> parent) {
            super(name, Collections.emptyList(), parent);
        }

        @Override
        public boolean isReference() {
            return true;
        }
    }

}
