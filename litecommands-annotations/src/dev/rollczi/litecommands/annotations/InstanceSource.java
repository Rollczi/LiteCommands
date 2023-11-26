package dev.rollczi.litecommands.annotations;

@Deprecated // TODO remove
class InstanceSource {

    private final Object instance;

    public InstanceSource(Object instance) {
        this.instance = instance;
    }

    public Object getInstance() {
        return instance;
    }

}
