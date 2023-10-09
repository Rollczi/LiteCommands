package dev.rollczi.litecommands.jda;

import dev.rollczi.litecommands.input.Input;
import dev.rollczi.litecommands.input.InputMatcher;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class AbstractJDAInput<MATCHER extends AbstractJDAInput<MATCHER>.AbstractJDAMatcher> implements Input<MATCHER> {

    protected final List<String> routes = new ArrayList<>();
    protected final Map<String, OptionMapping> arguments = new HashMap<>();

    protected AbstractJDAInput(List<String> routes, Map<String, OptionMapping> arguments) {
        this.routes.addAll(routes);
        this.arguments.putAll(arguments);
    }

    @Override
    public List<String> asList() {
        List<String> list = new ArrayList<>(routes);

        for (Map.Entry<String, OptionMapping> entry : arguments.entrySet()) {
            list.add(entry.getValue().getAsString());
        }

        return list;
    }

    abstract class AbstractJDAMatcher implements InputMatcher {

        protected int routePosition = 0;

        AbstractJDAMatcher() {}

        AbstractJDAMatcher(int routePosition) {
            this.routePosition = routePosition;
        }

        @Override
        public boolean hasNextRoute() {
            return routePosition < routes.size();
        }

        @Override
        public String showNextRoute() {
            return routes.get(routePosition);
        }

        @Override
        public String nextRoute() {
            return routes.get(routePosition++);
        }

    }

}
