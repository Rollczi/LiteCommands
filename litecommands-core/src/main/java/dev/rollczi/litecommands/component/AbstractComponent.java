package dev.rollczi.litecommands.component;

public abstract class AbstractComponent implements LiteComponent {

    protected final ScopeMetaData scope;

    AbstractComponent(ScopeMetaData scope) {
        this.scope = scope;
    }

    @Override
    public ScopeMetaData getScope() {
        return scope;
    }

}
