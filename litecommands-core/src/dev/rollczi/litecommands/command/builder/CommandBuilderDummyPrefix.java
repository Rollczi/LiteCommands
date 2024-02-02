package dev.rollczi.litecommands.command.builder;

import java.util.List;
import java.util.function.UnaryOperator;

@Deprecated //TODO Remove this implementation and replace with "current context of route"
class CommandBuilderDummyPrefix<SENDER> extends CommandBuilderBase<SENDER> implements CommandBuilder<SENDER> {

    protected CommandBuilder<SENDER> nativeDummyChildren;

    public CommandBuilderDummyPrefix(CommandBuilder<SENDER> nativeDummyChildren) {
        this.nativeDummyChildren = nativeDummyChildren;
        this.appendChild(nativeDummyChildren);
    }

    CommandBuilder<SENDER> dummyName(String name) {
        return super.routeName(name);
    }

    CommandBuilder<SENDER> dummyAliases(List<String> aliases) {
        return super.routeAliases(aliases);
    }

    @Override
    public CommandBuilder<SENDER> routeName(String name) {
        return this.nativeDummyChildren.routeName(name);
    }

    @Override
    public CommandBuilder<SENDER> routeAliases(List<String> aliases) {
        return this.nativeDummyChildren.routeAliases(aliases);
    }

    @Override
    public CommandBuilder<SENDER> applyOnRoute(UnaryOperator<CommandBuilder<SENDER>> apply) {
        this.nativeDummyChildren = this.nativeDummyChildren.applyOnRoute(apply);
        return this;
    }

    @Override
    public CommandBuilder<SENDER> getRealRoute() {
        return this.nativeDummyChildren.getRealRoute();
    }

}
